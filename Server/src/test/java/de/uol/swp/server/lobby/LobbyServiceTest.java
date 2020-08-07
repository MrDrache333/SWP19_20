package de.uol.swp.server.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.exception.JoinLobbyExceptionMessage;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.UpdatedInGameMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.request.*;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse des LobbyService
 *
 * @author Julia
 * @since Sprint 3
 */
@SuppressWarnings("UnstableApiUsage")
class LobbyServiceTest {

    static final User lobbyOwner = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    static final User lobbyUser = new UserDTO("Testuser", "1234", "123@test.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final EventBus bus = new EventBus();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final LobbyManagement lobbyManagement = new LobbyManagement();
    final AuthenticationService authenticationService = new AuthenticationService(bus, userManagement, lobbyManagement);
    final LobbyService lobbyService = new LobbyService(lobbyManagement, authenticationService, new ChatManagement(), bus);
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    /**
     * Regelt den Umgang mit DeadEvents
     *
     * @author Julia
     * @since Sprint 3
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Registriert den EventBus
     *
     * @author Julia
     * @since Sprint 3
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Deregistriert den EventBus
     *
     * @author Julia
     * @since Sprint 3
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Prüft ob die Lobby angelegt wurde
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void onCreateLobbyRequestTest() throws InterruptedException {
        loginUsers();

        lobbyService.onCreateLobbyRequest(new CreateLobbyRequest(defaultLobbyName, (UserDTO) lobbyOwner, ""));

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof CreateLobbyMessage);
        CreateLobbyMessage message = (CreateLobbyMessage) event;
        assertEquals(defaultLobbyName, message.getLobbyName());
        assertEquals(lobbyOwner, message.getUser());

        //Test if lobby was created
        assertTrue(lobbyManagement.getLobby(message.getLobby().getLobbyID()).isPresent());
        assertEquals(lobbyOwner, lobbyManagement.getLobby(message.getLobby().getLobbyID()).get().getUsers().iterator().next());

        lobbyService.onCreateLobbyRequest(new CreateLobbyRequest(defaultLobbyName, (UserDTO) lobbyOwner, ""));
        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof CreateLobbyMessage);
        CreateLobbyMessage message2 = (CreateLobbyMessage) event;
        assertTrue(message2.getLobbyName() == null);
    }

    /**
     * Prüft, ob der User erfolgreich der Lobby beigetreten ist
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void onLobbyJoinUserRequestTest() throws InterruptedException {
        try {
            loginUsers();

            final UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            lobbyService.onLobbyJoinUserRequest(new LobbyJoinUserRequest(lobbyID, new UserDTO(lobbyUser.getUsername(), lobbyUser.getPassword(), lobbyUser.getEMail()), false));

            lock.await(200, TimeUnit.MILLISECONDS);

            assertTrue(event instanceof UserJoinedLobbyMessage);
            UserJoinedLobbyMessage message = (UserJoinedLobbyMessage) event;

            assertEquals(lobbyID, message.getLobbyID());
            assertEquals(defaultLobbyName, message.getLobby().getName());
            assertEquals(lobbyUser, message.getUser());

            //Test if user joined lobby
            assertEquals(2, lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getUsers().size());

            lobbyService.onLobbyJoinUserRequest(new LobbyJoinUserRequest(lobbyID, new UserDTO(lobbyUser.getUsername(), lobbyUser.getPassword(), lobbyUser.getEMail()), false));

            lock.await(200, TimeUnit.MILLISECONDS);
            assertTrue(event instanceof JoinLobbyExceptionMessage);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Prüft ob der User die Lobby erfolgreich verlassen hat
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void onLobbyLeaveUserRequestTest() throws InterruptedException {
        try {
            loginUsers();

            UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(lobbyUser);
            lobbyService.onLobbyLeaveUserRequest(new LobbyLeaveUserRequest(lobbyID, new UserDTO(lobbyOwner.getUsername(), lobbyOwner.getPassword(), lobbyOwner.getEMail())));

            lock.await(1000, TimeUnit.MILLISECONDS);

            assertTrue(event instanceof UserLeftLobbyMessage);
            UserLeftLobbyMessage message = (UserLeftLobbyMessage) event;

            assertEquals(lobbyOwner, message.getUser());
            assertEquals(defaultLobbyName, message.getLobby().getName());
            assertEquals(lobbyID, message.getLobbyID());

            //Test if user left lobby
            Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
            assertTrue(lobby.isPresent());
            assertEquals(lobbyUser, lobby.get().getOwner());

            //Test if user left lobby and lobby was deleted
            lobbyService.onLobbyLeaveUserRequest(new LobbyLeaveUserRequest(lobbyID, new UserDTO(lobbyUser.getUsername(), lobbyUser.getPassword(), lobbyUser.getEMail())));
            lobby = lobbyManagement.getLobby(lobbyID);
            assertTrue(lobby.isEmpty());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Prüft ob der User alle Lobbys verlassen hat
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void onLeaveAllLobbiesOnLogoutRequestTest() {
        try {
            loginUsers();

            UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            UUID lobbyID2 = lobbyManagement.createLobby("Lobby2", "", lobbyOwner);
            lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(lobbyUser);
            lobbyService.onLeaveAllLobbiesOnLogoutRequest(new LeaveAllLobbiesOnLogoutRequest(new UserDTO(lobbyOwner.getUsername(), lobbyOwner.getPassword(), lobbyOwner.getEMail())));

            //Test if user was removed from all lobbies
            Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
            assertTrue(lobby.isPresent());
            assertEquals(lobbyUser, lobby.get().getOwner());
            Optional<Lobby> lobby2 = lobbyManagement.getLobby(lobbyID2);
            assertTrue(lobby2.isEmpty());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Überprüft die Lobby-Liste
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void onRetrieveAllOnlineLobbiesRequestTest() throws InterruptedException {
        loginUsers();

        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
        lobbyManagement.createLobby("Lobby2", defaultLobbyPassword, lobbyOwner);
        lobbyService.onRetrieveAllOnlineLobbiesRequest(new RetrieveAllOnlineLobbiesRequest());

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof AllOnlineLobbiesResponse);
        AllOnlineLobbiesResponse response = (AllOnlineLobbiesResponse) event;

        //Test lobby list
        List<Lobby> lobbies = new ArrayList<>(response.getLobbies());
        assertEquals(2, lobbies.size());
        assertTrue((lobbies.get(0).getName().equals(defaultLobbyName) && lobbies.get(1).getName().equals("Lobby2"))
                || (lobbies.get(1).getName().equals(defaultLobbyName) && lobbies.get(0).getName().equals("Lobby2")));
    }

    /**
     * Prüft ob sich die Lobby nach einem Spiel noch im Spiel befindet.
     *
     * @author Julia
     * @since Sprint 6
     */
    @Test
    void onGameEndTest() throws InterruptedException {
        try {
            loginUsers();

            UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setInGame(true);
            lobbyService.onGameEnd(new UpdateInGameRequest(lobbyID));

            lock.await(1000, TimeUnit.MILLISECONDS);

            assertTrue(event instanceof UpdatedInGameMessage);
            UpdatedInGameMessage message = (UpdatedInGameMessage) event;
            assertEquals(lobbyID, message.getLobbyID());
            assertFalse(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getInGame());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Funktion onSendChoosenCards
     *
     * @author Anna
     * @since Sprint 7
     */
    @Test
    void onSendChosenCardsTest() throws InterruptedException {
        loginUsers();
        UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
        ArrayList<Short> chosenCards = new ArrayList<>();
        chosenCards.add((short) 5);
        chosenCards.add((short) 7);
        lobbyService.onSendChosenCardsRequest(new SendChosenCardsRequest(lobbyID, chosenCards));
        lock.await(500, TimeUnit.MILLISECONDS);
        assertTrue(event instanceof NewChatMessage);
    }

    /**
     * Testet ob das Password der Lobby erfolgreich gesetzt wurde
     *
     * @author Timo
     * @since Sprint 9
     */
    @Test
    void checkIfPasswordIsSet() {
        UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);

        // Prüft ob die Lobby vorhanden ist.
        assertTrue(lobbyManagement.getLobby(lobbyID).isPresent());

        // Prüft ob ein Password gesetzt wurde
        assertFalse(lobbyManagement.getLobby(lobbyID).get().getLobbyPassword().isEmpty());
    }

    /**
     * Testet ob das Password der Lobby korrekt gesetzt wurde
     *
     * @author Timo
     * @since Sprint 9
     */
    @Test
    void checkIfPasswordIsSetCorrectly() {
        UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);

        // Prüft ob die Lobby vorhanden ist.
        assertTrue(lobbyManagement.getLobby(lobbyID).isPresent());

        // Prüft ob das korrekte Password gesetzt wurde
        assertEquals(defaultLobbyPassword, lobbyManagement.getLobby(lobbyID).get().getLobbyPassword());
    }

    /**
     * Hilfsmethode zum Einloggen der User
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void onUpdateLobbyReadyStatusReqTest() {
        try {
            loginUsers();
            UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            lobbyService.onUpdateLobbyReadyStatusRequest(new UpdateLobbyReadyStatusRequest(lobbyID, (UserDTO) lobbyOwner, true));
            assertTrue(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getReadyStatus(lobbyOwner));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet, ob eine AllOnlineUsersInLobbyResponse auf Anfrage versendet wird.
     *
     * @throws InterruptedException die Exception
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void onRetrieveAllOnlineUsersInLobbyRequestTest() throws InterruptedException {
        UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
        lobbyService.onRetrieveAllOnlineUsersInLobbyRequest(new RetrieveAllOnlineUsersInLobbyRequest(lobbyID));
        lock.await(500, TimeUnit.MILLISECONDS);
        assertTrue(event instanceof AllOnlineUsersInLobbyResponse);
    }

    /**
     * Es wird getestet, ob ein Player gekickt werden kann.
     *
     * @throws InterruptedException die Exception
     * @author Ferit
     * @since Sprint 11
     */
    @Test
    void kickPlayerTest() throws InterruptedException {
        try {
            UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(lobbyUser);
            lobbyService.onKickUserRequest(new KickUserRequest(lobbyID, (UserDTO) lobbyOwner, (UserDTO) lobbyUser));
            lock.await(1000, TimeUnit.MILLISECONDS);
            assertTrue(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getPlayers() == 1);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Es wird getestet, ob gültige/ungültige Anfragen beim SetMaxPlayer richtig gehandelt werden.
     * @author Ferit
     * @since Sprint 11
     */
    @Test
    void onSetMaxPlayerReqTest() {
        try {
            UUID lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
            lobbyService.onSetMaxPlayerRequest(new SetMaxPlayerRequest(lobbyID, (UserDTO) lobbyOwner, 3));
            assertTrue(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getMaxPlayer() == 3);
            lobbyService.onSetMaxPlayerRequest(new SetMaxPlayerRequest(lobbyID, (UserDTO) lobbyOwner, 0));
            assertTrue(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getMaxPlayer() == 3);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }

    }

    //Hilfsmethode
    void loginUsers() {
        userManagement.createUser(lobbyUser);
        userManagement.createUser(lobbyOwner);

        authenticationService.onLoginRequest(new LoginRequest(lobbyOwner.getUsername(), lobbyOwner.getPassword()));
        authenticationService.onLoginRequest(new LoginRequest(lobbyUser.getUsername(), lobbyUser.getPassword()));
    }
}