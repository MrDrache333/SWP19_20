package de.uol.swp.server.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.exception.*;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.request.*;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.lobby.response.SetChosenCardsResponse;
import de.uol.swp.common.message.ResponseMessage;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.player.bot.BotPlayer;
import de.uol.swp.server.game.player.bot.internal.messages.AuthBotInternalRequest;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Die Klasse LobbyService, welche eine Lobby erstellt
 *
 * @author KenoO
 * @since Sprint 2
 */
@SuppressWarnings("UnstableApiUsage, unused")
public class LobbyService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(LobbyService.class);

    private final LobbyManagement lobbyManagement;
    private final ChatManagement chatManagement;
    private final AuthenticationService authenticationService;
    private final EventBus eventBus;

    /**
     * Instanziiert einen neuen Lobby Service
     *
     * @param lobbyManagement       Das LobbyManagement
     * @param authenticationService Den AuthenticationService
     * @param chatManagement        Den ChatManagement
     * @param eventBus              Den Eventbus
     * @author KenoO
     * @since Sprint 2
     */
    @Inject
    public LobbyService(LobbyManagement lobbyManagement, AuthenticationService authenticationService, ChatManagement chatManagement, EventBus eventBus) {
        super(eventBus);
        this.eventBus = eventBus;
        this.lobbyManagement = lobbyManagement;
        this.authenticationService = authenticationService;
        this.chatManagement = chatManagement;
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------


    /**
     * LobbyManagement auf dem Server wird aufgerufen und übergibt LobbyNamen, LobbyPassword und den Besitzer.
     * Wenn dies erfolgt ist, folgt eine returnMessage an den Client die LobbyView anzuzeigen.
     *
     * @param req Enthält den Request vom Client mit den benötigten Daten, um die Lobby zu erstellen.
     * @author Haschem, Ferit, Rike, Marvin, Paula
     * @version 0.2
     * @since Sprint 2
     */
    @Subscribe
    public void onCreateLobbyRequest(CreateLobbyRequest req) {
        if (containsLobbyName(req.getLobbyName())) {
            LOG.info("Lobby wurde nicht erstellt");
            ServerMessage returnMessage = new CreateLobbyMessage(null, null, req.getUser(), null, null);
            authenticationService.sendToLoggedInPlayers(returnMessage);
        } else {
            UUID chatID = lobbyManagement.createLobby(req.getLobbyName(), req.getLobbyPassword(), new LobbyUser(req.getOwner()));
            chatManagement.createChat(chatID.toString());
            LOG.info("Der Chat mit der UUID " + chatID + " wurde erfolgreich erstellt!");
            Optional<Lobby> lobby = lobbyManagement.getLobby(chatID);
            ServerMessage returnMessage = new CreateLobbyMessage(req.getLobbyName(), req.getLobbyPassword(), req.getUser(), chatID, (LobbyDTO) lobby.get());
            authenticationService.sendToLoggedInPlayers(returnMessage);
            LOG.info("onCreateLobbyRequest wird auf dem Server aufgerufen.");
        }

    }

    /**
     * LobbyManagement auf dem Server wird aufgerufen und übergibt den Namen des Nutzers.
     * Wenn dies erfolgt ist, folgt eine UserJoinedLobbyMessage an den Client, um den User zur Lobby hinzuzufügen
     *
     * @param req Der LobbyJoinUserRequest
     * @author Paula, Julia, Marvin
     * @since Sprint 3
     */
    @Subscribe
    public void onLobbyJoinUserRequest(LobbyJoinUserRequest req) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(req.getLobbyID());
        if (lobby.isPresent() && !lobby.get().getUsers().contains(req.getUser()) && lobby.get().getPlayers() < lobby.get().getMaxPlayer() && !lobby.get().getInGame()) {
            LOG.info("User " + req.getUser().getUsername() + " tritt der Lobby " + lobby.get().getName() + " bei.");
            LobbyUser theLobbyUser = new LobbyUser(req.getUser());
            lobby.get().joinUser(theLobbyUser);
            ServerMessage returnMessage = new UserJoinedLobbyMessage(req.getLobbyID(), req.getUser(), (UserDTO) lobby.get().getOwner(), (LobbyDTO) lobby.get());
            authenticationService.sendToLoggedInPlayers(returnMessage);
            if (req.getBot()) {
                lobby.get().setReadyStatus(req.getUser(), true);
                ServerMessage msg2 = new UpdatedLobbyReadyStatusMessage(lobby.get().getLobbyID(), req.getUser(), lobby.get().getReadyStatus(req.getUser()));
                LOG.debug("Ändere den Update-Status vom Bot " + req.getUser().getUsername() + " auf true in Lobby " + lobby.get().getName());
                authenticationService.sendToLoggedInPlayers(msg2);
                allPlayersReady(lobby.get());
            }
        } else {
            LOG.error("Beitreten der Lobby mit der ID " + req.getLobbyID() + " fehlgeschlagen");
            authenticationService.sendToUser(new JoinLobbyExceptionMessage("Beitreten der Lobby ist fehlgeschlagen"), req.getUser());
        }
    }

    /**
     * LobbyManagement wird aufgerufen und übergibt Namen der Lobby und User.
     * UserLeftLobbyMessage wird an Client gesendet
     *
     * @param msg Der LobbyLeaveUserRequest
     * @author Julia, Paula, Darian, Marvin
     * @since Sprint 3
     */
    @Subscribe
    public void onLobbyLeaveUserRequest(LobbyLeaveUserRequest msg) {
        try {
            //Falls der Besitzer der Lobby aus der Lobby geht wird dieser aktualisiert
            boolean leavedLobbySuccessful = lobbyManagement.leaveLobby(msg.getLobbyID(), msg.getUser());
            if (leavedLobbySuccessful && lobbyManagement.getLobby(msg.getLobbyID()).isPresent() && !(lobbyManagement.getLobby(msg.getLobbyID()).get().onlyBotsLeft(msg.getLobbyID()))) {
                Optional<Lobby> lobby = lobbyManagement.getLobby(msg.getLobbyID());
                LOG.info("User " + msg.getUser().getUsername() + " verlässt die Lobby " + msg.getLobbyID());
                ServerMessage returnMessage;
                if (lobby.isPresent()) {
                    returnMessage = new UserLeftLobbyMessage(msg.getLobbyID(), msg.getUser(), (UserDTO) lobby.get().getOwner(), (LobbyDTO) lobby.get());
                } else {
                    returnMessage = new UserLeftLobbyMessage(msg.getLobbyID(), msg.getUser(), null, null);
                }
                authenticationService.sendToLoggedInPlayers(returnMessage);
            } else if (leavedLobbySuccessful && lobbyManagement.getLobby(msg.getLobbyID()).isPresent() &&
                    (lobbyManagement.getLobby(msg.getLobbyID()).get().onlyBotsLeft(msg.getLobbyID()))) {
                lobbyManagement.dropLobby(msg.getLobbyID());
                ServerMessage returnMessage;
                returnMessage = new UserLeftLobbyMessage(msg.getLobbyID(), msg.getUser(), null, null);
                authenticationService.sendToLoggedInPlayers(returnMessage);
            } else {
                ServerMessage returnMessage;
                returnMessage = new UserLeftLobbyMessage(msg.getLobbyID(), msg.getUser(), null, null);
                authenticationService.sendToLoggedInPlayers(returnMessage);
            }
        } catch (LeaveLobbyException e) {
            authenticationService.sendToUser(new LobbyExceptionMessage(msg.getLobbyID(), e.getMessage()), msg.getUser());
            LOG.error(e.getMessage());
        }
    }

    /**
     * Lobbys, in denen User drinnen ist, werden verlassen
     *
     * @param req Der LeaveAllLobbiesOnLogoutRequest
     * @author Paula, Julia, Marvin, Darian
     * @since Sprint 3
     */
    @Subscribe
    public void onLeaveAllLobbiesOnLogoutRequest(LeaveAllLobbiesOnLogoutRequest req) {
        List<LobbyDTO> toLeave = new ArrayList<>();
        lobbyManagement.getLobbies().forEach(lobby -> {
            List<User> users = new ArrayList<>(lobby.getUsers());
            if (users.contains(req.getUser()) && !lobby.getInGame()) {
                toLeave.add((LobbyDTO) lobby);
            }
        });
        LOG.info("User " + req.getUser().getUsername() + " verlässt alle Lobbys");
        toLeave.forEach(lobby -> lobbyManagement.leaveLobby(lobby.getLobbyID(), req.getUser()));
        toLeave.clear();
        lobbyManagement.getLobbies().forEach(lobby -> toLeave.add((LobbyDTO) lobby));
        ServerMessage returnMessage = new UserLeftAllLobbiesMessage(req.getUser(), toLeave);
        authenticationService.sendToLoggedInPlayers(returnMessage);
    }

    /**
     * Der Status eines Spielers wird abgefragt, ob der geupdatet wurde
     *
     * @param req Der geupdatetete Status des Users
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Subscribe
    public void onUpdateLobbyReadyStatusRequest(UpdateLobbyReadyStatusRequest req) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(req.getLobbyID());
        if (lobby.isPresent()) {
            lobby.get().setReadyStatus(req.getUser(), req.isReady());
            ServerMessage msg = new UpdatedLobbyReadyStatusMessage(lobby.get().getLobbyID(), req.getUser(), lobby.get().getReadyStatus(req.getUser()));
            sendToAll(lobby.get().getLobbyID(), msg);
            LOG.debug("Sending Updated Status of User " + req.getUser().getUsername() + " to " + req.isReady() + " in Lobby: " + lobby.get().getLobbyID());
            allPlayersReady(lobby.get());
        } else
            LOG.debug("Lobby nicht gefunden! ID: " + req.getLobbyID());
    }

    /**
     * Setzt den inGame-Status der Lobby bei Spielende wieder auf false
     *
     * @param req Der UpdateInGameRequest
     * @author Julia
     * @since Sprint 6
     */
    @Subscribe
    public void onGameEnd(UpdateInGameRequest req) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(req.getLobbyID());
        if (lobby.isPresent() && !(onlyBotsLeft(req.getLobbyID()))) {
            LOG.info("Spiel in Lobby " + req.getLobbyID() + " beendet.");
            lobby.get().setInGame(false);
            authenticationService.sendToLoggedInPlayers(new UpdatedInGameMessage(req.getLobbyID()));

        } else if (lobby.isPresent() && onlyBotsLeft(req.getLobbyID())) {
            lobbyManagement.dropLobby(req.getLobbyID());
        } else
            LOG.debug("Lobby nicht gefunden! ID: " + req.getLobbyID());
    }

    /**
     * Hilfmethode, ob nur noch Bots in der Lobby sind.
     *
     * @param lobbyID Die LobbyID
     * @return Wahrheitswert
     */
    public Boolean onlyBotsLeft(UUID lobbyID) {
        for (User user : lobbyManagement.getLobby(lobbyID).get().getUsers()) {
            if (!user.getIsBot()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ruft alle Online User in der Lobby ab
     *
     * @param req die RetrieveAllOnlineUsersInLobbyRequest
     * @author Marvin
     * @since Sprint 3
     */
    @Subscribe
    public void onRetrieveAllOnlineUsersInLobbyRequest(RetrieveAllOnlineUsersInLobbyRequest req) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(req.getLobbyID());
        if (lobby.isPresent()) {
            ResponseMessage msg = new AllOnlineUsersInLobbyResponse(lobby.get().getLobbyID(), lobby.get().getUsers(), lobby.get().getEveryReadyStatus());
            msg.initWithMessage(req);
            post(msg);
        } else {
            LOG.debug("Lobby nicht gefunden! ID: " + req.getLobbyID());
        }
    }

    /**
     * Erstellt eine AllOnlineLobbiesResponse mit allen Lobbies im LobbyManagement und schickt diese ab
     *
     * @param req Der RetrieveAllOnlineLobbiesRequest
     * @author Julia
     * @since Sprint 2
     */
    @Subscribe
    public void onRetrieveAllOnlineLobbiesRequest(RetrieveAllOnlineLobbiesRequest req) {
        AllOnlineLobbiesResponse response = new AllOnlineLobbiesResponse(lobbyManagement.getLobbies());
        response.initWithMessage(req);
        post(response);
    }

    /**
     * Lobbies werden aktualisiert nachdem ein User seine Daten geändert hat
     *
     * @param req Der UpdateLobbiesRequest
     * @author Julis
     * @since Sprint 4
     */
    @Subscribe
    public void onUpdateLobbiesRequest(UpdateLobbiesRequest req) {
        lobbyManagement.updateLobbies(req.getUpdatedUser(), req.getOldUser());
    }

    /**
     * Bei der Anfrage einen Spieler aus einer Lobby zu entfernen, wird dieser entfernt.
     *
     * @param req Der KickUserRequest
     * @author Marvin, Darian
     */
    @Subscribe
    public void onKickUserRequest(KickUserRequest req) {
        try {
            lobbyManagement.kickUser(req.getLobbyID(), req.getUserToKick(), req.getUser());
            LOG.info("User " + req.getUser().getUsername() + " wurde von der Lobby mit folgender ID " + req.getLobbyID() + " gekickt!");
            ServerMessage returnMessage = new KickUserMessage(req.getLobbyID(), req.getUserToKick(), (LobbyDTO) lobbyManagement.getLobby(req.getLobbyID()).get());
            authenticationService.sendToLoggedInPlayers(returnMessage);
        } catch (KickPlayerException e) {
            authenticationService.sendToUser(new LobbyExceptionMessage(req.getLobbyID(), e.getMessage()), req.getUser());
            LOG.error(e.getMessage());
        }
    }

    /**
     * Abarbeitung des Requests.
     *
     * @param req Der SetMaxPlayerRequest
     * @author Timo, Rike, Marvin, Ferit, Darian
     * @since Sprint 3
     */
    @Subscribe
    public void onSetMaxPlayerRequest(SetMaxPlayerRequest req) {
        LobbyDTO lobby = (LobbyDTO) lobbyManagement.getLobby(req.getLobbyID()).get();
        try {
            lobbyManagement.setMaxPlayer(req.getLobbyID(), req.getUser(), req.getMaxPlayerValue());
            ServerMessage returnMessage = new SetMaxPlayerMessage(req.getMaxPlayerValue(), req.getLobbyID(), lobbyManagement.getLobbyOwner(req.getLobbyID()).get(), lobby);
            authenticationService.sendToLoggedInPlayers(returnMessage);
        } catch (SetMaxPlayerException e) {
            ServerMessage returnMessage2 = new LobbyExceptionMessage(req.getLobbyID(), e.getMessage());
            authenticationService.sendToUser(returnMessage2, lobby.getOwner());
            LOG.debug(e.getMessage());
        }
    }

    /**
     * Sende die Anfrage, der ausgewählten Karten
     *
     * @param req Die SendChosenCardsRequest
     * @author Fenja, Anna
     * @since Sprint 7
     */
    @Subscribe
    public void onSendChosenCardsRequest(SendChosenCardsRequest req) {
        LOG.debug("Ausgewählte Karten erhalten");
        LobbyDTO lobby = (LobbyDTO) lobbyManagement.getLobby(req.getLobbyID()).get();
        lobby.setChosenCards(req.getChosenCards());

        SetChosenCardsResponse response = new SetChosenCardsResponse(req.getLobbyID(), true);
        response.initWithMessage(req);
        post(response);
        sendToAll(req.getLobbyID(), new NewChatMessage(req.getLobbyID().toString(), new ChatMessage(new UserDTO("server", "", ""), "Karten wurden ausgewählt")));
    }

    //--------------------------------------
    // Help Methods
    //--------------------------------------

    /**
     * Hilfsmethode, die die Nachricht an alle Spieler in der Lobby sendet
     *
     * @param lobbyID Die LobbyID
     * @param msg     Die Nachricht
     * @author KenoO, Paula, Marvin
     * @since Sprint 2
     */
    public void sendToAll(UUID lobbyID, ServerMessage msg) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
        if (lobby.isPresent()) {
            msg.setReceiver(authenticationService.getSessions(lobby.get().getUsers()));
            post(msg);
        } else {
            LOG.error("Die Lobby mit der LobbyID " + lobbyID + " konnte nicht gefunden werden!");
        }
    }

    /**
     * Überprüft ob alle Spieler bereit sind
     * Spiel startet wenn alle in der Lobby vorhandenen Spieler Bereit sind
     *
     * @param lobby Die Lobby
     * @author Darian, Keno, Marvin
     * @since Sprint 4
     */
    private void allPlayersReady(Lobby lobby) {
        if (lobby.getPlayers() == 1) return;
        //Prüfen, ob jeder Spieler in der Lobby fertig ist
        if (lobby.getUsers().stream().anyMatch(user -> !lobby.getReadyStatus(user))) return;
        //Lobby starten
        LOG.debug("Spiel in Lobby: " + lobby.getName() + " startet.");
        lobby.setInGame(true);

        authenticationService.sendToLoggedInPlayers(new StartGameMessage(lobby.getLobbyID()));

        // Sendet eine interne Nachricht, welche die Erstellung des Games initiiert.
        post(new StartGameInternalMessage(lobby.getLobbyID()));
    }

    /**
     * Überprüft, ob der LobbyName schon vergeben ist
     *
     * @param name Name der Lobby
     * @return false, wenn der Name noch nicht existiert
     * @author Paula
     */
    private boolean containsLobbyName(String name) {
        return lobbyManagement.getLobbies().stream().anyMatch(lobby -> lobby.getName().equals(name));
    }

    /**
     * Methode die einen Bot erstellt, wenn der Owner "Bot Hinzufügen" drückt.
     *
     * @param request Die Anforderung einen Bot der Lobby hinzuzufügen.
     * @author Ferit
     */
    @Subscribe
    public void createBotRequest(AddBotRequest request) {
        Lobby thisLobby = lobbyManagement.getLobby(request.getLobbyID()).get();
        String[] collectionBotName = {"King Arthur", "Merlin", "Die Queen", "Prinzessin Diana", "Donald Trump"};
        if (thisLobby.getUsers().size() < thisLobby.getMaxPlayer()) {
            String theRandomBotName = collectionBotName[(int) (Math.random() * collectionBotName.length)] + (int) (Math.random() * 999);
            BotPlayer createdBot = new BotPlayer(theRandomBotName, eventBus, request.getLobbyID());
            UserDTO userDT = new UserDTO(createdBot.getTheUserInThePlayer().getUsername(), createdBot.getTheUserInThePlayer().getPassword(), createdBot.getTheUserInThePlayer().getEMail(), true);
            eventBus.post(new AuthBotInternalRequest(createdBot));
            eventBus.post(new LobbyJoinUserRequest(thisLobby.getLobbyID(), userDT, true));
        }
    }
}
