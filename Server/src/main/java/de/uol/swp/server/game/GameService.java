package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.exception.GameManagementException;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.exception.NotEnoughMoneyException;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.game.request.GameGiveUpRequest;
import de.uol.swp.common.game.request.PlayCardRequest;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.lobby.request.UpdateInGameRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.player.Player;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Der GameService. Verarbeitet alle Anfragen, die über den Bus gesendet werden.
 */
@SuppressWarnings("UnstableApiUsage")
public class GameService extends AbstractService {

    private static final Logger LOG = LogManager.getLogger(GameService.class);
    private final GameManagement gameManagement;
    private final AuthenticationService authenticationService;
    private final UserDTO infoUser = new UserDTO("infoUser", "", "");

    /**
     * Erstellt einen neuen GameService
     *
     * @param eventBus              Der zu nutzende EventBus
     * @param gameManagement        Das GameManagement
     * @param authenticationService der Authentication-Service
     * @author KenoO
     * @since Sprint 5
     */
    @Inject
    public GameService(EventBus eventBus, GameManagement gameManagement, AuthenticationService authenticationService) {
        super(eventBus);
        this.gameManagement = gameManagement;
        this.authenticationService = authenticationService;
        gameManagement.setGameService(this);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * Eine Methode um einem bestimmten Spieler eine Message zu schicken.
     * Bisherige Verwendung um einem Spieler die aktuelle Hand zu schicken.
     *
     * @param thePlayer Der Spieler, welcher die spezifizierte Nachricht bekommen soll.
     * @param msg   Momentan wird hierrüber die DrawHandMessage verschickt.
     * @author Ferit
     * @since Sprint 5
     */
    public void sendToSpecificPlayer(Player thePlayer, ServerMessage msg) {
        Set<User> playerToUserSet = new HashSet<>(1);
        playerToUserSet.add(thePlayer.getTheUserInThePlayer());

        msg.setReceiver(authenticationService.getSessions(playerToUserSet));

        // TODO: BotSession holen und message.SetReciever setzen.

        post(msg);
    }

    /**
     * Sendet die letzte Karte an den Game Service
     *
     * @param gameID die Game-ID
     * @param cardID die Karten-ID
     * @param user   der User
     * @author Fenja
     * @since Sprint6
     */
    public void sendLastCardOfDiscardPile(UUID gameID, short cardID, User user) {
        sendToAllPlayers(gameID, new DiscardPileLastCardMessage(gameID, cardID, user));
    }

    /**
     * Sendet eine Nachricht an alle Player eines Games
     *
     * @param gameID  die ID des Games
     * @param msg die Nachricht
     * @author Julia
     * @since Sprint5
     */
    public void sendToAllPlayers(UUID gameID, ServerMessage msg) {
        Optional<Game> game = gameManagement.getGame(gameID);
        if (game.isPresent()) {
            List<Player> players = game.get().getPlayground().getPlayers();
            Set<User> users = new HashSet<>();
            players.forEach(player -> users.add(player.getTheUserInThePlayer()));
            msg.setReceiver(authenticationService.getSessions(users));
            post(msg);
        } else {
            LOG.error("Es existiert kein Spiel mit der ID " + gameID);
        }
    }

    public void dropFinishedGame(UUID lobbyID) {
        gameManagement.deleteGame(lobbyID);
        post(new UpdateInGameRequest(lobbyID));
    }

    /**
     * Das Kartfeld wird geschickt.
     *
     * @param gameID    die ID des Spiels
     * @param cardField das Kartenfeld
     * @author Anna, Fenja
     * @since Sprint 7
     */
    public void sendCardField(UUID gameID, Map<Short, Integer> cardField) {
        sendToAllPlayers(gameID, new SendCardFieldMessage(gameID, cardField));
    }

    /**
     * Startet das Spiel wenn die StartGameInternalMessage ankommt.
     * Sendet außerdem eine Nachricht mit dem ersten Spieler in den Chat.
     *
     * @param msg Interne Message mit der LobbyId um das Game zu starten.
     * @author Ferit, Julia, Marvin
     * @since Sprint5
     */
    @Subscribe
    void startGame(StartGameInternalMessage msg) {
        try {
            gameManagement.createGame(msg.getLobbyID());
            Game game = gameManagement.getGame(msg.getLobbyID()).get();
            game.getPlayground().newTurn();
        } catch (GameManagementException e) {
            LOG.error("Es wurde eine GameManagementException geworfen: " + e.getMessage());
            // TODO: In späteren Sprints hier ggf. weiteres Handling?
        }
    }

    /**
     * Versucht die aktuelle Phase zu überspringen; falls dies fehlschlägt, wird eine Nachricht
     * mit entsprechender Fehlermeldung gesendet
     *
     * @param req SkipPhaseRequest
     * @author Julia
     * @since Sprint5
     */
    @Subscribe
    public void onSkipPhaseRequest(SkipPhaseRequest req) {
        Optional<Game> game = gameManagement.getGame(req.getGameID());
        if (game.isPresent()) {
            Playground playground = game.get().getPlayground();
            if (playground.getActualPlayer().getTheUserInThePlayer().getUsername().equals(req.getUser().getUsername())) {
                try {
                    playground.nextPhase();
                } catch (GamePhaseException e) {
                    sendToSpecificPlayer(playground.getActualPlayer(), new GameExceptionMessage(req.getGameID(), req.getUser(), e.getMessage()));
                }
            }
        } else {
            LOG.error("Es existiert kein Spiel mit der ID " + req.getGameID());
        }
    }

    /**
     * Handling, dass der User aufgegeben hat und aus dem Playground entfernt wird. Ggf später auf null gesetzt wird o.ä.
     *
     * @param req Request zum Aufgeben
     * @author Haschem, Ferit
     * @since Sprint5
     */
    @Subscribe
    void userGivesUp(GameGiveUpRequest req) {
        Boolean userRemovedSuccessfully = gameManagement.getGame(req.getLobbyID()).get().getPlayground().playerGaveUp(req.getLobbyID(), req.getGivingUpUser(), req.getGivingUp());
        if (userRemovedSuccessfully && !(gameManagement.lobbyIsNotPresent(req.getLobbyID()))) {
            sendToAllPlayers(req.getLobbyID(), new UserGaveUpMessage(req.getLobbyID(), req.getGivingUpUser(), true));
            sendToAllPlayers(req.getLobbyID(), new NewChatMessage(req.getLobbyID().toString(), new ChatMessage(infoUser, req.getGivingUpUser().getUsername() + " gab auf!")));
        } else {
            LOG.error("User " + req.getGivingUpUser().getUsername() + "konnte nicht aufgeben");
            post(new AllOnlineLobbiesResponse(gameManagement.getAllLobies()));
            // TODO: Implementierung: Was passiert wenn der User nicht entfernt werden kann? Welche Fälle gibt es?
        }
    }

    public void userGavesUpLeavesLobby(UUID gameID, UserDTO user) {
        LobbyLeaveUserRequest leaveUserRequest = new LobbyLeaveUserRequest(gameID, user);
        post(leaveUserRequest);

    }

    /**
     * Versuch eine Karte zu kaufen
     *
     * @param req BuyCardRequest wird hier vom Client empfangen
     * @author Paula, Rike
     * @since Sprint6
     */
    @Subscribe
    public void onBuyCardRequest(BuyCardRequest req) {
        Optional<Game> game = gameManagement.getGame(req.getLobbyID());
        if (game.isPresent()) {
            Playground playground = game.get().getPlayground();
            if (req.getCurrentUser().equals(playground.getActualPlayer().getTheUserInThePlayer()) && playground.getActualPhase() == Phase.Type.Buyphase) {
                try {
                    Card card = playground.getCardsPackField().getCards().getCardForId(req.getCardID());
                    int moneyValuePlayer = playground.getActualPlayer().getPlayerDeck().actualMoneyFromPlayer();
                    int additionalMoney = playground.getActualPlayer().getAdditionalMoney();
                    if (card.getCosts() <= moneyValuePlayer + additionalMoney) {
                        ChatMessage infoMessage = new ChatMessage(infoUser, req.getCurrentUser().getUsername() + " kauft Karte " + card.getName() + "!");
                        post(new NewChatMessageRequest(req.getLobbyID().toString(), infoMessage));
                        int count = playground.getCompositePhase().executeBuyPhase(playground.getActualPlayer(), req.getCardID());
                        Short costCard = playground.getCompositePhase().getCardFromId(playground.getCardsPackField().getCards(), req.getCardID()).getCosts();
                        sendToAllPlayers(req.getLobbyID(), new BuyCardMessage(req.getLobbyID(), req.getCurrentUser(), req.getCardID(), count, costCard));
                    } else {
                        throw new NotEnoughMoneyException("Dafür hast du nicht genug Geld! ");
                    }
                } catch (NotEnoughMoneyException notEnoughMoney) {
                    sendToSpecificPlayer(playground.getActualPlayer(), new GameExceptionMessage(req.getLobbyID(), req.getCurrentUser(), notEnoughMoney.getMessage()));
                }
            } else {
                LOG.error("Du bist nicht dran. " + playground.getActualPlayer().getPlayerName() + "ist an der Reihe.");
            }
        } else {
            LOG.error("Es existiert kein Spiel mit der ID " + req.getCardID());
        }
    }

    /**
     * Versuch eine Karte zu spielen
     *
     * @param req PlayCardRequest wird hier vom Client empfangen
     * @author Devin, Rike
     * @since Sprint6
     */
    @Subscribe
    public void onPlayCardRequest(PlayCardRequest req) {
        Optional<Game> game = gameManagement.getGame(req.getGameID());
        UUID gameID = req.getGameID();
        User player = req.getCurrentUser();
        Short cardID = req.getHandCardID();
        if (game.isPresent()) {
            Playground playground = game.get().getPlayground();
            if (playground.getActualPlayer().getTheUserInThePlayer().getUsername().equals(player.getUsername()) && playground.getActualPhase() == Phase.Type.ActionPhase) {
                try {
                    playground.endTimer();
                    // Karte wird an die ActionPhase zum Handling übergeben.
                    playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), cardID);
                    //sendToSpecificPlayer(playground.getActualPlayer(), new PlayCardMessage(gameID, player, cardID, true));
                    playground.getPlayers().forEach(n -> {
                        PlayCardMessage msg = new PlayCardMessage(gameID, playground.getActualPlayer().getTheUserInThePlayer(), cardID, true,
                                playground.getIndexOfPlayer(n), playground.getIndexOfPlayer(playground.getActualPlayer()), playground.getCompositePhase().getExecuteAction().isRemoveCardAfter());
                        sendToSpecificPlayer(n, msg);
                    });
                    Card card = playground.getCardsPackField().getCards().getCardForId(cardID);
                    ChatMessage infoMessage = new ChatMessage(infoUser, playground.getActualPlayer().getTheUserInThePlayer().getUsername() + " spielt Karte " + (card != null ? card.getName() : "Undefiniert") + "!");
                    post(new NewChatMessageRequest(gameID.toString(), infoMessage));
                } catch (IllegalArgumentException e) {
                    sendToSpecificPlayer(playground.getActualPlayer(), new GameExceptionMessage(gameID, req.getCurrentUser(), e.getMessage()));
                }
            }
        } else {
            LOG.error("Irgendwas ist bei der onSelectCardRequest im GameService falsch gelaufen. Folgende ID: " + gameID);
        }
    }

    public GameManagement getGameManagement() {
        return gameManagement;
    }
}
