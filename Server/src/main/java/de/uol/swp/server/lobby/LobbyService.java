package de.uol.swp.server.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;

public class LobbyService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(LobbyService.class);

    private final LobbyManagement lobbyManagement;
    private final ChatManagement chatManagement;
    private final AuthenticationService authenticationService;

    @Inject
    public LobbyService(LobbyManagement lobbyManagement, AuthenticationService authenticationService, ChatManagement chatManagement, EventBus eventBus) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
        this.chatManagement = chatManagement;
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

        UUID chatID = lobbyManagement.createLobby(msg.getName(), msg.getOwner());

        chatManagement.createChat(chatID.toString());
        LOG.info("Der Chat mir der UUID " + chatID + " wurde erfolgreich erstellt");

        ServerMessage returnMessage = new CreateLobbyMessage(msg.getName(), msg.getUser(), chatID);
        post(returnMessage);
        LOG.info("onCreateLobbyRequest wird auf dem Server aufgerufen.");
    }


    @Subscribe
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest msg) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getName());
        if (lobby.isPresent()) {
            if(LOG.isDebugEnabled()) {
                LOG.debug("User " + msg.getUser().getUsername() + " is joining lobby " + msg.getName());
            }
            lobby.get().joinUser(msg.getUser());
            ServerMessage returnMessage = new UserJoinedLobbyMessage(msg.getName(), msg.getUser(), msg.getLobbyID());
            sendToAll(msg.getName(), returnMessage);
        }
        // TODO: error handling not existing lobby
    }

    @Subscribe
    public void onLobbyLeaveUserRequest(LobbyLeaveUserRequest msg) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getName());
        if (lobby.isPresent()) {
            if(LOG.isDebugEnabled()) {
                LOG.debug("User " + msg.getUser().getUsername() + " is leaving lobby " + msg.getName());
            }
            lobby.get().leaveUser(msg.getUser());
            ServerMessage returnMessage = new UserLeftLobbyMessage(msg.getName(), msg.getUser(), msg.getLobbyID());
            post(returnMessage);
            //sendToAll(msg.getName(), returnMessage); //TODO fix?
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

    /**
     * erstellt eine Response-Message und schickt diese ab
     *
     * @author Julia
     */
    @Subscribe
    public void onRetrieveAllOnlineLobbiesRequest(RetrieveAllOnlineLobbiesRequest msg) {
        AllOnlineLobbiesResponse response = new AllOnlineLobbiesResponse(lobbyManagement.getLobbies());
        response.initWithMessage(msg);
        post(response);
    }
}
