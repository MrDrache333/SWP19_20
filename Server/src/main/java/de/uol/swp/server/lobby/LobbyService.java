package de.uol.swp.server.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.request.*;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.message.ResponseMessage;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The type Lobby service.
 */
public class LobbyService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(LobbyService.class);

    private final LobbyManagement lobbyManagement;
    private final ChatManagement chatManagement;
    private final AuthenticationService authenticationService;

    /**
     * Instantiates a new Lobby service.
     *
     * @param lobbyManagement       the lobby management
     * @param authenticationService the authentication service
     * @param chatManagement        the chat management
     * @param eventBus              the event bus
     */
    @Inject
    public LobbyService(LobbyManagement lobbyManagement, AuthenticationService authenticationService, ChatManagement chatManagement, EventBus eventBus) {
        super(eventBus);
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
        this.chatManagement = chatManagement;
    }


    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

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
        UUID chatID = lobbyManagement.createLobby(msg.getLobbyName(), new LobbyUser(msg.getOwner()));

        chatManagement.createChat(chatID.toString());
        LOG.info("Der Chat mir der UUID " + chatID + " wurde erfolgreich erstellt");
        Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getLobbyName());
        LobbyDTO lobbyDTO = new LobbyDTO(lobby.get().getName(), lobby.get().getOwner(),  lobby.get().getLobbyID(), lobby.get().getUsers(), lobby.get().getPlayers());
        ServerMessage returnMessage = new CreateLobbyMessage(msg.getLobbyName(), msg.getUser(), chatID, lobbyDTO);
        post(returnMessage);
        LOG.info("onCreateLobbyRequest wird auf dem Server aufgerufen.");
    }

    /**
     * LobbyManagment auf dem Server wird aufgerufen und übergibt den Namen des Nutzers.
     * Wenn dies erfolgt ist, folgt eine UserJoinedLobbyMessage an den Client, um den User zur Lobby hinzuzufügen
     *
     * @param msg the msg
     * @author Paula, Julia
     * @since Sprint3
     */
    @Subscribe
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest msg) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getLobbyName());
        if (lobby.isPresent() && !lobby.get().getUsers().contains(msg.getUser()) && lobby.get().getPlayers() < 4) {
            LOG.info("User " + msg.getUser().getUsername() + " is joining lobby " + msg.getLobbyName());
            lobby.get().joinUser(new LobbyUser(msg.getUser()));
            LobbyDTO lobbyDTO = new LobbyDTO(lobby.get().getName(), lobby.get().getOwner(),  lobby.get().getLobbyID(), lobby.get().getUsers(), lobby.get().getPlayers());
            ServerMessage returnMessage = new UserJoinedLobbyMessage(msg.getLobbyName(), msg.getUser(), msg.getLobbyID(), lobbyDTO);
            post(returnMessage);
        }
        else {
            LOG.error("Joining lobby " + msg.getLobbyName() + " failed");
        }
    }

    /**
     * lobbyManagment wird aufgerufen und übergibt Namen der Lobby und User.
     * UserLeftLobbyMessage wird an Client gesendet
     *
     * @param msg the msg
     * @author Julia, Paula
     * @since Sprint3
     */
    @Subscribe
    public void onLobbyLeaveUserRequest(LobbyLeaveUserRequest msg) {
        if (lobbyManagement.leaveLobby(msg.getLobbyName(), msg.getUser())) {
            LOG.info("User " + msg.getUser().getUsername() + " is leaving lobby " + msg.getLobbyName());
            ServerMessage returnMessage;
            Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getLobbyName());
            if(lobby.isPresent()) {
                LobbyDTO lobbyDTO = new LobbyDTO(lobby.get().getName(), lobby.get().getOwner(),  lobby.get().getLobbyID(), lobby.get().getUsers(), lobby.get().getPlayers());
                returnMessage = new UserLeftLobbyMessage(msg.getLobbyName(), msg.getUser(), msg.getLobbyID(), lobbyDTO);
            }
            else {
                returnMessage = new UserLeftLobbyMessage(msg.getLobbyName(), msg.getUser(), msg.getLobbyID(), null);
            }
            post(returnMessage);

        } else {
            LOG.error("Leaving lobby " + msg.getLobbyName() + " failed");
        }
    }

    /**
     * Lobbys, in denen User drinnen ist, werden verlassen
     *
     * @param msg the msg
     * @author Julia, Paula
     * @since Sprint3
     */
    @Subscribe
    public void onLeaveAllLobbiesOnLogoutRequest(LeaveAllLobbiesOnLogoutRequest msg) {
        List<Lobby> toLeave = new ArrayList<>();
        lobbyManagement.getLobbies().forEach(lobby -> {
            List<User> users = new ArrayList<>(lobby.getUsers());
            if (users.contains(msg.getUser())) {
                toLeave.add(lobby);
            }
        });
        LOG.info("User " + msg.getUser().getUsername() + " is leaving all lobbies");
        toLeave.forEach(lobby -> lobbyManagement.leaveLobby(lobby.getName(), msg.getUser()));
    }

    /**
     * On update lobby ready status reqest.
     *
     * @param request the request
     * @author Keno Oelrichs Garcia
     * @since Sprint3
     */
    @Subscribe
    public void onUpdateLobbyReadyStatusRequest(UpdateLobbyReadyStatusRequest request) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(request.getLobbyName());

        if (lobby.isPresent()) {
            lobby.get().setReadyStatus(request.getUser(), request.isReady());
            ServerMessage msg = new UpdatedLobbyReadyStatusMessage(lobby.get().getLobbyID(), lobby.get().getName(), request.getUser(), lobby.get().getReadyStatus(request.getUser()));
            sendToAll(lobby.get().getName(), msg);
            LOG.debug("Sending Updated Status of User " + request.getUser().getUsername() + " to " + request.isReady() + " in Lobby: " + lobby.get().getLobbyID());
            allPlayersReady(lobby.get());
        } else
            LOG.debug("Lobby " + request.getLobbyName() + " NOT FOUND!");
    }

    /**
     * Auf ID umgestellt
     *
     * @param request the request
     * @author Marvin
     * @since Sprint3
     */
    @Subscribe
    public void onRetrieveAllOnlineUsersInLobbyRequest(RetrieveAllOnlineUsersInLobbyRequest request) {
        Optional<String> lobbyName = lobbyManagement.getName(request.getLobbyId());
        if (lobbyName.isPresent()) {
            Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyName.get());

            if (lobby.isPresent()) {
                ResponseMessage msg = new AllOnlineUsersInLobbyResponse(lobby.get().getLobbyID(), lobby.get().getUsers(), lobby.get().getEveryReadyStatus());
                msg.initWithMessage(request);
                post(msg);
            } else {
                LOG.debug("LobbyID in Map but Name " + lobbyName + "NOT FOUND!");
            }
        } else {
            LOG.debug("LobbyID " + request.getLobbyId() + " NOT FOUND!");
        }
    }

    /**
     * erstellt eine AllOnlineLobbiesResponse mit allen Lobbies im LobbyManagement und schickt diese ab
     *
     * @param msg the msg
     * @author Julia
     * @since Sprint2
     */
    @Subscribe
    public void onRetrieveAllOnlineLobbiesRequest(RetrieveAllOnlineLobbiesRequest msg) {
        AllOnlineLobbiesResponse response = new AllOnlineLobbiesResponse(lobbyManagement.getLobbies());
        response.initWithMessage(msg);
        post(response);
    }

    //--------------------------------------
    // Help Methods
    //--------------------------------------


    /**
     * Send to all.
     *
     * @param lobbyName the lobby name
     * @param message   the message
     */
    public void sendToAll(String lobbyName, ServerMessage message) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyName);
        if (lobby.isPresent()) {
            message.setReceiver(authenticationService.getSessions(lobby.get().getUsers()));
            post(message);
        }
        // TODO: error handling not existing lobby
    }


    /**
     * überprüft ob alle Spieler bereit sind
     * Spiel startet wenn 4 Spieler Bereit sind
     *
     * @param lobby the lobby
     * @author Darian, Keno
     * @since Sprint 4
     */
    private void allPlayersReady(Lobby lobby) {
        if (lobby.getPlayers() == 1) return;
        //Prüfen, ob jeder Spieler in der Lobby fertig ist
        for (User user : lobby.getUsers()) {
            if (!lobby.getReadyStatus(user)) return;
        }
        //Lobby starten
        LOG.debug("Game starts in Lobby: " + lobby.getName());
        StartGameMessage msg = new StartGameMessage(lobby.getName(), lobby.getLobbyID());
        sendToAll(lobby.getName(), msg);
    }
}
