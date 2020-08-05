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
import de.uol.swp.common.game.request.*;
import de.uol.swp.common.lobby.exception.LobbyExceptionMessage;
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
@SuppressWarnings("UnstableApiUsage, unused")
public class GameService extends AbstractService {

    private static final Logger LOG = LogManager.getLogger(GameService.class);
    private final GameManagement gameManagement;
    private final AuthenticationService authenticationService;
    private final UserDTO infoUser = new UserDTO("infoUser", "", "");
    private final Timer poopTimer = new Timer();
    private final Map<User, Boolean> poopMap = new HashMap<>();
    List<Player> actualPlayers = new ArrayList<>();
    private User poopInitiator = null;
    private Timer timer;
    private int interval;
    private boolean timerStarted;
    private static final int DELAY = 1000;
    private static final int PERIOD = 1000;

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
        timerStarted = false;
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * Eine Methode um einem bestimmten Spieler eine Message zu schicken.
     * Bisherige Verwendung um einem Spieler die aktuelle Hand zu schicken.
     *
     * @param thePlayer Der Spieler, welcher die spezifizierte Nachricht bekommen soll.
     * @param msg       Momentan wird hierrüber die DrawHandMessage verschickt.
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
     * @param gameID die ID des Games
     * @param msg    die Nachricht
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
    public void startGame(StartGameInternalMessage msg) {
        try {
            gameManagement.createGame(msg.getLobbyID());
            if (gameManagement.getGame(msg.getLobbyID()).isPresent()) {
                Game game = gameManagement.getGame(msg.getLobbyID()).get();
                game.getPlayground().newTurn();
            }
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
    public void userGivesUp(GameGiveUpRequest req) {
        if (gameManagement.getGame(req.getLobbyID()).isPresent()) {
            Boolean userRemovedSuccessfully = gameManagement.getGame(req.getLobbyID()).get().getPlayground().playerGaveUp(req.getLobbyID(), req.getGivingUpUser(), req.getGivingUp());
            if (userRemovedSuccessfully && !(gameManagement.lobbyIsNotPresent(req.getLobbyID()))) {
                sendToAllPlayers(req.getLobbyID(), new UserGaveUpMessage(req.getLobbyID(), req.getGivingUpUser(), true));
                sendToAllPlayers(req.getLobbyID(), new NewChatMessage(req.getLobbyID().toString(), new ChatMessage(infoUser, req.getGivingUpUser().getUsername() + " gab auf!")));
            } else {
                LOG.error("User " + req.getGivingUpUser().getUsername() + "konnte nicht aufgeben");
                post(new AllOnlineLobbiesResponse(gameManagement.getAllLobbies()));
                // TODO: Implementierung: Was passiert wenn der User nicht entfernt werden kann? Welche Fälle gibt es?
            }
        }
        else
            sendToAllPlayers(req.getLobbyID(), new LobbyExceptionMessage(req.getLobbyID(), "Der User konnte nicht entfernt werden!"));
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
            if (req.getCurrentUser().equals(playground.getActualPlayer().getTheUserInThePlayer()) && playground.getActualPhase() == Phase.Type.BuyPhase) {
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
                    playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), cardID);
                    PlayCardMessage msg = new PlayCardMessage(gameID, playground.getActualPlayer().getTheUserInThePlayer(),
                            cardID, true, playground.getCompositePhase().getExecuteAction().isRemoveCardAfter());
                    sendToAllPlayers(req.getGameID(), msg);
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

    /**
     * Startet eine Klopause, wenn schon nicht eine läuft.
     *
     * @param req Der PoopBreakRequest
     * @author Keno S.
     * @since Sprint 10
     */
    @Subscribe
    public void onPoopBreakRequest(PoopBreakRequest req) {
        Optional<Game> game = gameManagement.getGame(req.getGameID());
        if (game.isPresent()) {
            actualPlayers.clear();
            game.get().getPlayground().getPlayers().forEach(player -> {
                if (!player.isBot() && !actualPlayers.contains(player))
                    actualPlayers.add(player);
            });
            if (poopInitiator == null && req.getPoopInitiator() != null)
                poopInitiator = req.getPoopInitiator();
            if (!poopMap.containsKey(req.getUser())) {
                poopMap.put(req.getUser(), req.getPoopDecision());
                sendToAllPlayers(req.getGameID(), new PoopBreakMessage(poopInitiator, req.getUser(), req.getGameID(), poopMap));
            }
            if  (actualPlayers.size() < 2 || poopMap.values().stream().filter(d -> d).count() >= (actualPlayers.size() == 4 ? 3 : 2)) {
                sendToAllPlayers(req.getGameID(), new StartPoopBreakMessage(poopInitiator, req.getGameID()));
                poopMap.clear();
                clock(req.getGameID());
            }
            else if (poopMap.values().stream().filter(d -> !d).count() >= (actualPlayers.size() > 2 ? 2 : 1)) {
                sendToAllPlayers(req.getGameID(), new CancelPoopBreakMessage(poopInitiator, req.getGameID(), new ArrayList<>(poopMap.values())));
                poopMap.clear();
                poopInitiator = null;
            }
        }
    }

    /**
     * Beendet eine Klopause.
     *
     * @param req Der PoopBreakRequest
     * @author Keno S.
     * @since Sprint 10
     */
    @Subscribe
    public void onCancelPoopBreakRequest(CancelPoopBreakRequest req) {
        Optional<Game> game = gameManagement.getGame(req.getGameID());
        if (game.isPresent()) {
            if (!req.getUser().equals(poopInitiator) && interval > 0) {
                game.get().getPlayground().getPlayers().forEach(player -> {
                    if (player.getTheUserInThePlayer().equals(req.getUser()))
                        sendToSpecificPlayer(player, new CancelPoopBreakMessage(poopInitiator, req.getGameID()));
                });
            }
            else {
                if (timerStarted) {
                    timer.cancel();
                    timerStarted = false;
                }
                sendToAllPlayers(req.getGameID(), new CancelPoopBreakMessage(poopInitiator, req.getGameID()));
                interval = 0;
                poopInitiator = null;
                poopMap.clear();
            }
        }
    }

    /**
     * Hilfsmethode, die eine Clock erzeugt, damit der Countdown bei allen Clients sychron ist.
     *
     * @param gameID Die GameID
     * @author Keno S.
     * @since Sprint 10
     */
    private void clock(UUID gameID) {
        timer = new Timer();
        interval = 60;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!timerStarted)
                    timerStarted = true;
                sendToAllPlayers(gameID, new CountdownRefreshMessage(gameID, countdownTimer()));
            }
        }, DELAY, PERIOD);
    }

    /**
     * Hilfsmethode, um den Countdown herunterzuzählen.
     *
     * @return Den Countdown - 1
     */
    private int countdownTimer() {
        if (interval <= 0) {
            timer.cancel();
            timerStarted = false;
        }
        return --interval;
    }
    public boolean isTimerStarted() {
        return timerStarted;
    }

    public void killTimer() {
        interval = 0;
    }

    public GameManagement getGameManagement() {
        return gameManagement;
    }
}
