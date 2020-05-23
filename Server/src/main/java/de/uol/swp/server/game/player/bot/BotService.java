package de.uol.swp.server.game.player.bot;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.ServerInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;

public class BotService extends AbstractService {

    private final LobbyManagement lobbyManagement;
    private final AuthenticationService authenticationService;

    /**
     * Erstellt einen neuen GameService
     *
     * @param eventBus              Der zu nutzende EventBus
     * @param lobbyManagement       Das GameManagement
     * @param authenticationService
     * @author KenoO
     * @since Sprint 5
     */
    @Inject
    public BotService(EventBus eventBus, LobbyManagement lobbyManagement, AuthenticationService authenticationService) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    public void post(ServerInternalMessage msg) {

    }
}
