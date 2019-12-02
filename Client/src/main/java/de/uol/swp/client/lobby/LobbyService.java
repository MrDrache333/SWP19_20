package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.request.LeaveAllLobbiesOnLogoutRequest;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;

public class LobbyService implements de.uol.swp.common.lobby.LobbyService {

    private static final Logger LOG = LogManager.getLogger(LobbyService.class);
    private final EventBus bus;

    @Inject
    public LobbyService(EventBus bus) {
        this.bus = bus;
    }

    /**
     * erstellt ein RetrieveAllOnlineLobbiesRequest und postet es auf den Eventbus
     *
     * @author Julia
     * @since Sprint2
     */
    @Override
    public List<Lobby> retrieveAllLobbies() {
        RetrieveAllOnlineLobbiesRequest request = new RetrieveAllOnlineLobbiesRequest();
        bus.post(request);
        return null;
    }


    @Override
    public void joinLobby(String name, User user, UUID lobbyID) {
        LobbyJoinUserRequest request = new LobbyJoinUserRequest(name, user, lobbyID);
        bus.post(request);
    }

    @Override
    public void leaveLobby(String name, User user, UUID lobbyID) {
        LobbyLeaveUserRequest request = new LobbyLeaveUserRequest(name, user, lobbyID);
        bus.post(request);
    }

    @Override
    public void leaveAllLobbiesOnLogout(User user) {
        LeaveAllLobbiesOnLogoutRequest request = new LeaveAllLobbiesOnLogoutRequest(user);
        bus.post(request);


    }
}