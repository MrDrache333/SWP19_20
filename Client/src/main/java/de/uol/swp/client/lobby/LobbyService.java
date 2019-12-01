package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.lobby.message.UpdateLobbyReadyStatusRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineUsersInLobbyRequest;
import de.uol.swp.common.message.RequestMessage;
import de.uol.swp.common.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Lobby service.
 */
public class LobbyService implements de.uol.swp.common.lobby.LobbyService {

    private static final Logger LOG = LogManager.getLogger(LobbyService.class);
    private final EventBus bus;

    /**
     * Instantiates a new Lobby service.
     *
     * @param bus the bus
     */

    @Inject
    public LobbyService(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public List<Lobby> retrieveAllLobbies() {
        RetrieveAllOnlineLobbiesRequest msg = new RetrieveAllOnlineLobbiesRequest();
        bus.post(msg);
        return null;
    }

    @Override
    public ArrayList<LobbyUser> retrieveAllUsersInLobby(String lobbyName) {
        RequestMessage msg = new RetrieveAllOnlineUsersInLobbyRequest(lobbyName);
        bus.post(msg);
        return null;
    }

    @Override
    public void setLobbyUserStatus(String LobbyName, User user, boolean Status) {
        RequestMessage msg = new UpdateLobbyReadyStatusRequest(LobbyName, user, Status);
        bus.post(msg);
    }
}
