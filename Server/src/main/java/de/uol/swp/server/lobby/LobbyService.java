package de.uol.swp.server.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.exception.LobbyNotFoundExceptionMessage;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.lobby.request.UpdateAllOnlineLobbiesRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.lobby.response.UpdateAllOnlineLobbiesResponse;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LobbyService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(LobbyService.class);

    private final LobbyManagement lobbyManagement;
    private final AuthenticationService authenticationService;

    @Inject
    public LobbyService(LobbyManagement lobbyManagement, AuthenticationService authenticationService, EventBus eventBus) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
    }

    /**
     * lobbyManagment auf dem Server wird aufgerufen und übergibt LobbyNamen und den Besitzer.
     * Wenn dies erfolgt ist, folgt eine returnMessage an den Client die LobbyView anzuzeigen.
     *
     * @param msg enthält die Message vom Client mit den benötigten Daten um die Lobby zu erstellen.
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * @since Sprint2
     */


    @Subscribe
    public void onCreateLobbyRequest(CreateLobbyRequest msg) {
        lobbyManagement.createLobby(msg.getName(), msg.getOwner());
        ServerMessage returnMessage = new CreateLobbyMessage(msg.getName(), msg.getUser());
        post(returnMessage);
        LOG.info("onCreateLobbyRequest wird auf dem Server aufgerufen.");
    }

    /**
     * Lobby wird rausgesucht und falls vorhanden wird der user gejoined, andernfalls wird
     * eine ExceptionMessage zurückgegeben.
     *
     * @param msg enthält die Message vom Client mit Lobbynamen und User.
     * @author Marvin
     * @version 0.1
     * @since Sprint2
     */

    @Subscribe
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest msg) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getName());
        LOG.info("LobbyJoinUserRequest empfangen");
        ServerMessage returnMessage;
        if (lobby.isPresent()) {
            lobby.get().joinUser(msg.getUser());
            returnMessage = new UserJoinedLobbyMessage(msg.getName(), msg.getUser());
            LOG.info("Lobby vorhanden, User gejoined, UserJoinedLobbyMessage gesendet");
        } else {
            returnMessage = new LobbyNotFoundExceptionMessage("Lobby " + msg.getName() + " not Found!", msg.getUser());
            LOG.info("Lobby nicht vorhanden; LobbyNotFoundExceptionMessage gesendet");
        }
        post(returnMessage);
    }


    @Subscribe
    public void onLobbyLeaveUserRequest(LobbyLeaveUserRequest lobbyLeaveUserRequest) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyLeaveUserRequest.getName());

        if (lobby.isPresent()) {
            lobby.get().leaveUser(lobbyLeaveUserRequest.getUser());
            sendToAll(lobbyLeaveUserRequest.getName(), new UserLeftLobbyMessage(lobbyLeaveUserRequest.getName(), lobbyLeaveUserRequest.getUser()));
        }
        // TODO: error handling not existing lobby
    }


    public void sendToAll(String lobbyName, ServerMessage message) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyName);

        if (lobby.isPresent()) {
            message.setReceiver(authenticationService.getSessions(lobby.get().getUsers()));
            post(message);
        }

        // TODO: error handling not existing lobby
    }

    @Subscribe
    public void onRetrieveAllOnlineLobbiesRequest(RetrieveAllOnlineLobbiesRequest msg) {
        AllOnlineLobbiesResponse response = new AllOnlineLobbiesResponse(lobbyManagement.getLobbies());
        response.initWithMessage(msg);
        post(response);
    }

    @Subscribe
    public void onUpdateAllOnlineLobbiesRequest(UpdateAllOnlineLobbiesRequest msg) {
        UpdateAllOnlineLobbiesResponse response = new UpdateAllOnlineLobbiesResponse(lobbyManagement.getLobbies(), msg.getName(), msg.getValue());
        response.initWithMessage(msg);
        post(response);
    }

}
