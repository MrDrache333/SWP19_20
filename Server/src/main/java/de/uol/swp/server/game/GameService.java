package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.game.exception.GameManagementException;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.messages.GameExceptionMessage;
import de.uol.swp.common.game.messages.UserGaveUpMessage;
import de.uol.swp.common.game.request.GameGiveUpRequest;
import de.uol.swp.common.game.request.SelectCardRequest;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.lobby.request.UpdateInGameRequest;
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
public class GameService extends AbstractService {

    private static final Logger LOG = LogManager.getLogger(GameService.class);

    private final GameManagement gameManagement;
    private final AuthenticationService authenticationService;

    /**
     * Erstellt einen neuen GameService
     *
     * @param eventBus              Der zu nutzende EventBus
     * @param gameManagement        Das GameManagement
     * @param authenticationService
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
     * @param message   Momentan wird hierrüber die DrawHandMessage verschickt.
     * @author Ferit
     * @since Sprint 5
     */
    // TODO: Wenn PlaygroundService implementiert ist, dann verschieben der Methode dorthin.
    public void sendToSpecificPlayer(Player thePlayer, ServerMessage message) {
        Set<User> playerToUserSet = new HashSet<User>(1);
        playerToUserSet.add(thePlayer.getTheUserInThePlayer());
        message.setReceiver(authenticationService.getSessions(playerToUserSet));
        post(message);
    }

    /**
     * Sendet eine Nachricht an alle Player eines Games
     *
     * @param gameID  die ID des Games
     * @param message die Nachricht
     * @author Julia
     * @since Sprint5
     */
    public void sendToAllPlayers(UUID gameID, ServerMessage message) {
        Optional<Game> game = gameManagement.getGame(gameID);
        if (game.isPresent()) {
            List<Player> players = game.get().getPlayground().getPlayers();
            Set<User> users = new HashSet<>();
            players.forEach(player -> users.add(player.getTheUserInThePlayer()));
            message.setReceiver(authenticationService.getSessions(users));
            post(message);
        } else {
            LOG.error("Es existiert kein Spiel mit der ID " + gameID);
        }
    }

    public void dropFinishedGame(UUID lobbyID) {
        gameManagement.deleteGame(lobbyID);
        post(new UpdateInGameRequest(lobbyID));
    }

    /**
     * Startet das Spiel wenn die StartGameInternalMessage ankommt.
     *
     * @param msg InterneMessage mit der LobbyId um das Game zu starten.
     * @author Ferit, Julia
     * @since Sprint5
     */
    @Subscribe
    void startGame(StartGameInternalMessage msg) {
        try {
            gameManagement.createGame(msg.getLobbyID());
            gameManagement.getGame(msg.getLobbyID()).get().getPlayground().newTurn();
        } catch (GameManagementException e) {
            LOG.error("Es wurde eine GameManagementException geworfen: " + e.getMessage());
            // TODO: In späteren Sprints hier ggf. weiteres Handling?
        }
    }

    /**
     * Versucht die aktuelle Phase zu überspringen; falls dies fehlschlägt, wird eine Nachricht
     * mit entsprechender Fehlermeldung gesendet
     *
     * @param msg SkipPhaseRequest
     * @author Julia
     * @since Sprint5
     */
    @Subscribe
    public void onSkipPhaseRequest(SkipPhaseRequest msg) {
        Optional<Game> game = gameManagement.getGame(msg.getGameID());
        if (game.isPresent()) {
            Playground playground = game.get().getPlayground();
            if (playground.getActualPlayer().getTheUserInThePlayer().equals(msg.getUser())) {
                try {
                    playground.skipCurrentPhase();
                } catch (GamePhaseException e) {
                    sendToSpecificPlayer(playground.getActualPlayer(), new GameExceptionMessage(msg.getGameID(), e.getMessage()));
                }
            }
        } else {
            LOG.error("Es existiert kein Spiel mit der ID " + msg.getGameID());
        }
    }

    /**
     * Handling das der User aufgegeben hat und aus dem Playground entfernt wird. Ggf später auf null gesetzt wird o.ä.
     *
     * @param msg Request zum Aufgeben
     * @author Haschem, Ferit
     * @since Sprint5
     */
    @Subscribe
    void userGivesUp(GameGiveUpRequest msg) {
        Boolean userRemovedSuccesfully = gameManagement.getGame(msg.getTheSpecificLobbyID()).get().getPlayground().playerGaveUp(msg.getTheSpecificLobbyID(), msg.getGivingUpUSer(), msg.getWantsToGiveUP());
        if (userRemovedSuccesfully) {
            UserGaveUpMessage gaveUp = new UserGaveUpMessage(msg.getTheSpecificLobbyID(), msg.getGivingUpUSer(), true);
            sendToAllPlayers(msg.getTheSpecificLobbyID(), gaveUp);
        } else {
            // TODO: Implementierung: Was passiert wenn der User nicht entfernt werden kann? Welche Fälle gibt es?
        }
    }


    public void userGavesUpLeavesLobby(UUID gameID, UserDTO user) {
        LobbyLeaveUserRequest leaveUserRequest = new LobbyLeaveUserRequest(gameID, user);
        post(leaveUserRequest);

    }

    /**
     * Die Methode cancelt aktuell den Timer der AktionPhase.
     *
     * @param request SelectCardRequest vom Client an Server wird hier empfangen.
     */
    @Subscribe
    public void onSelectCardRequest(SelectCardRequest request) {
        Optional<Game> game = gameManagement.getGame(request.getMessage().getGameID());
        if (game.isPresent()) {
            Playground playground = game.get().getPlayground();
            // TODO: Timestamp Handling wenn die SelectCardRequest Clientseitig implementiert worden ist.
            if (playground.getActualPlayer().getTheUserInThePlayer().getUsername().equals(request.getMessage().getPlayer().getUsername())) {
                try {
                    playground.endTimer();
                    // Karte wird an die ActionPhase zum Handling übergeben. TODO: Weitere Implementierung in der ActionPhase.
                    playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), request.getMessage().getCardID());
                } catch (GamePhaseException e) {
                    sendToSpecificPlayer(playground.getActualPlayer(), new GameExceptionMessage(request.getMessage().getGameID(), e.getMessage()));
                }
            }
        } else {
            LOG.error("Irgendwas ist bei der onSelectCardRequest im GameService falsch gelaufen..Folgende ID: " + request.getMessage().getGameID());
        }
    }
}
