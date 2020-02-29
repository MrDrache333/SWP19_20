package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.request.LoginRequest;
import de.uol.swp.server.chat.Chat;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DrawHandMessageTest {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final String lobbyName = "DrawHandMessageLobyTest";
    static final String lobbyPassword = "";
    static final ChatManagement chatManagment = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagment, lobbyManagement);
    static final UserStore userStore = new MainMemoryBasedUserStore();
    static final EventBus bus = new EventBus();
    static final UserManagement userManagement = new UserManagement(userStore);
    static final AuthenticationService authService = new AuthenticationService(bus, userManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authService);
    static UUID id;
    private Object event;

    @BeforeAll
    static void init() {
        id = lobbyManagement.createLobby(lobbyName, lobbyPassword, defaultOwner);
        lobbyManagement.getLobby(id).get().joinUser(secondPlayer);
    }

    /**
     * Setzt vor jedem Test das aktuelle Event auf null und registriert diese Testklasse auf dem Eventbus
     *
     * @author Marco
     * @since Start
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    @BeforeEach
    void setUp() {
        gameManagement.createGame(id);
    }

    /**
     * Meldet diese Testklasse nach jedem Test vom Eventbus ab
     *
     * @author Marco
     * @since Start
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    @AfterEach
    void afterEach() {
        gameManagement.deleteGame(id);
        chatManagment.deleteChat(id.toString());
    }

    /**
     * Testet ob UserSessions korrekt erstellt werden
     *
     * @author Marco
     * @since Start
     */
    @Test
    void getSessionsForUsersTest() {
        loginUser(defaultOwner);
        loginUser(secondPlayer);
        Set<User> users = new TreeSet<>();
        users.add(defaultOwner);
        users.add(secondPlayer);
        Optional<Session> session1 = authService.getSession(defaultOwner);
        Optional<Session> session2 = authService.getSession(secondPlayer);
        assertTrue(session1.isPresent());
        assertTrue(session2.isPresent());
        List<Session> sessions = authService.getSessions(users);
        assertEquals(sessions.size(), 2);
        assertTrue(sessions.contains(session1.get()));
        assertTrue(sessions.contains(session2.get()));

        //TODO: Implementierung des weiteren Tests
    }

    /**
     * Loggt einen User ein
     *
     * @param userToLogin der User, der eingeloggt werden soll
     * @author Marco
     * @since Start
     */
    private void loginUser(User userToLogin) {
        userManagement.createUser(userToLogin);
        final LoginRequest loginRequest = new LoginRequest(userToLogin.getUsername(), userToLogin.getPassword());
        bus.post(loginRequest);

        assertTrue(userManagement.isLoggedIn(userToLogin));
        userManagement.dropUser(userToLogin);
    }
}
