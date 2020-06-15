package de.uol.swp.client.user;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.request.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type User service test.
 *
 * @author Marco, KenoS
 * @since Sprint 4
 */
@SuppressWarnings("UnstableApiUsage")
class UserServiceTest {

    /**
     * Der Standardbenutzer zum testen.
     */
    private final User defaultUser = new UserDTO("Marco", "test", "marco@test.de");

    /**
     * Der zu verwendene Event-Bus.
     */
    private final EventBus bus = new EventBus();
    /**
     * Der Counterdown-Timer zum warten für ausgeführte Befehle auf dem Bus.
     */
    private final CountDownLatch lock = new CountDownLatch(1);
    /**
     * Speicher für erstellte Instanzen von Bus-Events.
     */
    private Object event;

    /**
     * Methode zum behandeln von auf dem Bus aufgetretene Dead-Events.
     *
     * @param e Das aufgetretene Dead-Event
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Eventbus initialisieren.
     *
     * @author Marco
     * @since Start
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Klasse vom Eventbus deregistrieren
     *
     * @author Marco
     * @since Start
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Loggt einen den User ein
     *
     * @throws InterruptedException Fehler, der auftreten könnte
     * @author Maro
     * @since Start
     */
    private void loginUser() throws InterruptedException {
        UserService userService = new UserService(bus);
        userService.login(defaultUser.getUsername(), defaultUser.getPassword());
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Überprüfen, ob ein User auf dem Server erfolgreich anmelden kann.
     *
     * @throws InterruptedException Fehler, der auftreten könnte
     * @author Marco
     * @since Start
     */
    @Test
    void loginTest() throws InterruptedException {
        loginUser();
        assertTrue(event instanceof LoginRequest);
        LoginRequest loginRequest = (LoginRequest) event;
        assertEquals(loginRequest.getUsername(), defaultUser.getUsername());
        assertEquals(loginRequest.getPassword(), defaultUser.getPassword());
    }

    /**
     * Überprüfen, ob ein Benutzer auf dem Server bereits angemeldet ist.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void isLoggedInTest() {
        UserService userService = new UserService(bus);
        assertThrows(UnsupportedOperationException.class, () -> userService.isLoggedIn(defaultUser));
    }

    /**
     * Einen Benutzer versuchen vom Server abzumelden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Marco
     * @since Start
     */
    @Test
    void logoutTest() throws InterruptedException {
        loginUser();
        event = null;
        UserService userService = new UserService(bus);
        userService.logout(defaultUser);
        lock.await(1000, TimeUnit.MILLISECONDS);
        assertTrue(event instanceof LogoutRequest);
        LogoutRequest request = (LogoutRequest) event;
        assertTrue(request.authorizationNeeded());
    }

    /**
     * Einen Benutzer versuchen in dem Server-Speicher zu erstellen
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Marco
     * @since Start
     */
    @Test
    void createUserTest() throws InterruptedException {
        UserService userService = new UserService(bus);
        userService.createUser(defaultUser);
        lock.await(1000, TimeUnit.MILLISECONDS);
        assertTrue(event instanceof RegisterUserRequest);
        RegisterUserRequest request = (RegisterUserRequest) event;
        assertEquals(request.getUser().getUsername(), defaultUser.getUsername());
        assertEquals(request.getUser().getPassword(), defaultUser.getPassword());
        assertEquals(request.getUser().getEMail(), defaultUser.getEMail());
        assertFalse(request.authorizationNeeded());

    }

    /**
     * Einen Benutzer versuchen in dem Server-Speicher zu aktualisieren
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Marco
     * @since Start
     */
    @Test
    void updateUserTest() throws InterruptedException {
        UserService userService = new UserService(bus);
        userService.updateUser(defaultUser, defaultUser, "test");
        lock.await(1000, TimeUnit.MILLISECONDS);
        assertTrue(event instanceof UpdateUserRequest);
        UpdateUserRequest request = (UpdateUserRequest) event;
        assertEquals(request.getUser().getUsername(), defaultUser.getUsername());
        assertEquals(request.getUser().getPassword(), defaultUser.getPassword());
        assertEquals(request.getUser().getEMail(), defaultUser.getEMail());
        assertEquals(request.getOldUser().getUsername(), defaultUser.getUsername());
        assertEquals(request.getOldUser().getPassword(), defaultUser.getPassword());
        assertEquals(request.getOldUser().getEMail(), defaultUser.getEMail());
        assertTrue(request.authorizationNeeded());
    }

    /**
     * Einen Benutzer versuchen aus dem Server-Speicher zu löschen
     *
     * @author Marco
     * @since Start
     */
    @Test
    void dropUserTest() {
        UserService userService = new UserService(bus);
        userService.dropUser(defaultUser);

        // TODO: Add when method is implemented
    }

    /**
     * Versuchen alle angemeldeten Benutzer vom Server abzurufen.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Marco
     * @since Start
     */
    @Test
    void retrieveAllUsersTest() throws InterruptedException {
        UserService userService = new UserService(bus);
        userService.retrieveAllUsers();

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof RetrieveAllOnlineUsersRequest);
    }

}