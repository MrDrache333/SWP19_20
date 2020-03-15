package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.game.exception.GameManagementException;
import de.uol.swp.common.game.messages.UserGivedUpMessage;
import de.uol.swp.common.game.request.GameGiveUpRequest;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.player.Player;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

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
     * Startet das Spiel wenn die StartGameInternalMessage ankommt.
     *
     * @param msg InterneMessage mit der LobbyId um das Game zu starten.
     * @author Ferit
     * @since Sprint5
     */
    @Subscribe
    void startGame(StartGameInternalMessage msg) throws GameManagementException {
        try {
            gameManagement.createGame(msg.getLobbyID());
            // Manueller Test, wird beim Mergen entfernt.
            gameManagement.getGame(msg.getLobbyID()).get().getPlayground().sendPlayersHand();
            LOG.debug("StartGame Methode funktioniert ------------------------------->");
        } catch (GameManagementException e) {
            LOG.error("Es wurde eine GameManagementException geworfen: " + e.getMessage());
            // TODO: In späteren Sprints hier ggf. weiteres Handling?
        }
    }

    /**
     * Handling das der User aufgegeben hat und aus dem Playgrpund entfernt wird. Ggf später auf null gesetzt wird o.ä.
     *
     * @param msg Request zum Aufgeben
     * @author Haschem, Ferit
     * @since Sprint5
     */
    @Subscribe
    void userGivesUp(GameGiveUpRequest msg) {
        Boolean userRemovedSuccesfully = gameManagement.getGame(msg.getTheSpecificLobbyID()).get().getPlayground().playerGivedUp(msg.getTheSpecificLobbyID(), msg.getGivingUpUSer(), msg.getWantsToGiveUP());
        if (userRemovedSuccesfully) {
            UserGivedUpMessage givedUp = new UserGivedUpMessage(msg.getTheSpecificLobbyID(), msg.getGivingUpUSer(), true);
            Player givedUpPlayer = gameManagement.getGame(msg.getTheSpecificLobbyID()).get().getPlayground().getLatestGivedUpPlayer();
            sendToSpecificPlayer(givedUpPlayer, givedUp);
        } else {
            // TODO: Implementierung: Was passiert wenn der User nicht entfernt werden kann? Welche Fälle gibt es?
        }
    }
}
