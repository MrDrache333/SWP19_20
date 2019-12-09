package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineUsersInLobbyRequest;
import de.uol.swp.common.lobby.request.SetMaxPlayerRequest;
import de.uol.swp.common.lobby.request.UpdateLobbyReadyStatusRequest;
import de.uol.swp.common.message.RequestMessage;
import de.uol.swp.common.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    /**
     * Alternative Requesterstellung über lobbyID statt Name.
     *
     * @param lobbyID Lobby ID
     * @author Marvin
     * @since Sprint3
     */
    public ArrayList<LobbyUser> retrieveAllUsersInLobby(UUID lobbyID) {
        RequestMessage msg = new RetrieveAllOnlineUsersInLobbyRequest(lobbyID);
        bus.post(msg);
        return null;
    }

    @Override
    public void setLobbyUserStatus(String LobbyName, User user, boolean Status) {
        RequestMessage msg = new UpdateLobbyReadyStatusRequest(LobbyName, user, Status);
        bus.post(msg);
    }

    @Override
    /**
     * @author Timo, Rike
     * @since Sprint 3
     * @implNote Erstellt einen SetMaxPlayerRequest
     */
    public void setMaxPlayer(Integer maxPlayer, UUID lobbyID)
    {
        SetMaxPlayerRequest cmd = new SetMaxPlayerRequest(maxPlayer, lobbyID);
        bus.post(cmd);
    }
}
