package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.player.Player;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;


public class PlaygroundService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(PlaygroundService.class);
    private final AuthenticationService authenticationService;
    private final GameService gameService;

    @Inject
    public PlaygroundService(EventBus eventBus, AuthenticationService authenticationService, GameService gameService) {
        super(eventBus);
        this.authenticationService = authenticationService;
        this.gameService = gameService;
    }

    /**
     * Eine Methode um einem bestimmten Spieler eine Message zu schicken.
     * Bisherige Verwendung um einem Spieler die aktuelle Hand zu schicken.
     *
     * @param thePlayer Der Spieler, welcher die spezifizierte Nachricht bekommen soll.
     * @param message   Momentan wird hierrüber die DrawHandMessage verschickt.
     * @author Ferit
     * @since Sprint DUNNO
     */

    public void sendToSpecificPlayer(Player thePlayer, ServerMessage message) {
        Set<User> playerToUserSet = null;
        playerToUserSet.add((User) thePlayer);
        message.setReceiver(authenticationService.getSessions(playerToUserSet));
        post(message);
    }
}
