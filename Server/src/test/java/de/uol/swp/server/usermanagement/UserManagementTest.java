package de.uol.swp.server.usermanagement;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Klasse für den UserManagementTest
 *
 * @author Marco
 * @since Start
 */
class UserManagementTest {

    private static final int NO_USERS = 10;
    private static final List<UserDTO> users;
    private static final User userNotInStore = new UserDTO("marco" + NO_USERS, "marco" + NO_USERS, "marco" + NO_USERS + "@grawunder.de");

    static {
        users = new ArrayList<>();
        for (int i = 0; i < NO_USERS; i++) {
            users.add(new UserDTO("marco" + i, "marco" + i, "marco" + i + "@grawunder.de"));
        }
        Collections.sort(users);
    }

    List<UserDTO> getDefaultUsers() {
        return Collections.unmodifiableList(users);
    }

    UserManagement getDefaultManagement() {
        MainMemoryBasedUserStore store = new MainMemoryBasedUserStore();
        List<UserDTO> users = getDefaultUsers();
        users.forEach(u -> store.createUser(u.getUsername(), u.getPassword(), u.getEMail()));
        return new UserManagement(store);
    }

    /**
     * Angemeldeter Benutzer
     *
     * @author Marco
     * @since Start
     */
    @Test
    void loginUser() {
        UserManagement management = getDefaultManagement();
        User userToLogIn = users.get(0);

        management.login(userToLogIn.getUsername(), userToLogIn.getPassword());

        assertTrue(management.isLoggedIn(userToLogIn));
    }

    /**
     * Testet ob es keine Passwort eingabe des Benutzers gab
     *
     * @author Marco
     * @since Start
     */
    @Test
    void loginUserEmptyPassword() {
        UserManagement management = getDefaultManagement();
        User userToLogIn = users.get(0);

        assertThrows(SecurityException.class, () -> management.login(userToLogIn.getUsername(), ""));

        assertFalse(management.isLoggedIn(userToLogIn));
    }

    /**
     * Testet ob es ein Falsches Passwort des Benutzers ist
     *
     * @author Marco
     * @since Start
     */
    @Test
    void loginUserWrongPassword() {
        UserManagement management = getDefaultManagement();
        User userToLogIn = users.get(0);
        User secondUser = users.get(1);

        assertThrows(SecurityException.class, () -> management.login(userToLogIn.getUsername(), secondUser.getPassword()));

        assertFalse(management.isLoggedIn(userToLogIn));
    }

    /**
     * Benutzer Ausloggen
     *
     * @author Marco
     * @since Start
     */
    @Test
    void logoutUser() {
        UserManagement management = getDefaultManagement();
        User userToLogin = users.get(0);

        management.login(userToLogin.getUsername(), userToLogin.getPassword());

        assertTrue(management.isLoggedIn(userToLogin));

        management.logout(userToLogin);

        assertFalse(management.isLoggedIn(userToLogin));

    }

    /**
     * Benutzer erstellen
     *
     * @author Marco
     * @since Start
     */
    @Test
    void createUser() {
        UserManagement management = getDefaultManagement();

        management.createUser(userNotInStore);

        // Creation leads not to log in
        assertFalse(management.isLoggedIn(userNotInStore));

        // Only way to test, if user is stored
        management.login(userNotInStore.getUsername(), userNotInStore.getPassword());

        assertTrue(management.isLoggedIn(userNotInStore));
    }

    /**
     * Benutzer Löschen
     *
     * @author Marco
     * @since Start
     */
    @Test
    void dropUser() {
        UserManagement management = getDefaultManagement();
        management.createUser(userNotInStore);

        management.dropUser(userNotInStore);

        assertThrows(SecurityException.class,
                () -> management.login(userNotInStore.getUsername(), userNotInStore.getPassword()));
    }

    /**
     * nicht vorhandener Benutzer Löschen
     *
     * @author Marco
     * @since Start
     */
    @Test
    void dropUserNotExisting() {
        UserManagement management = getDefaultManagement();
        assertThrows(UserManagementException.class,
                () -> management.dropUser(userNotInStore));
    }

    /**
     * Benutzer erstellen, der bereits vorhanden ist
     *
     * @author Marco
     * @since Start
     */
    @Test
    void createUserAlreadyExisting() {
        UserManagement management = getDefaultManagement();
        User userToCreate = users.get(0);

        assertThrows(UserManagementException.class, () -> management.createUser(userToCreate));

    }

    /**
     * Benutzer Passwort aktualisieren
     * nicht Eingeloggt
     *
     * @author Marco
     * @since Start
     */
    @Test
    void updateUserPassword_NotLoggedIn() {
        UserManagement management = getDefaultManagement();
        User userToUpdate = users.get(0);
        User updatedUser = new UserDTO(userToUpdate.getUsername(), "newPassword", null);

        assertFalse(management.isLoggedIn(userToUpdate));
        management.updateUser(updatedUser, updatedUser, userToUpdate.getPassword());

        management.login(updatedUser.getUsername(), updatedUser.getPassword());
        assertTrue(management.isLoggedIn(updatedUser));
    }

    /**
     * Benutzer Mail aktualisieren
     *
     * @author Marco
     * @since Start
     */
    @Test
    void updateUser_Mail() {
        UserManagement management = getDefaultManagement();
        User userToUpdate = users.get(0);
        User updatedUser = new UserDTO(userToUpdate.getUsername(), "newPassword", "newMail@mail.com");

        management.updateUser(updatedUser, updatedUser, userToUpdate.getPassword());

        User user = management.login(updatedUser.getUsername(), updatedUser.getPassword());
        assertTrue(management.isLoggedIn(updatedUser));
        assertEquals(user.getEMail(), updatedUser.getEMail());
    }

    /**
     * Benutzer Passwort aktualisieren
     * Eingeloggt
     *
     * @author Marco
     * @since Start
     */
    @Test
    void updateUserPassword_LoggedIn() {
        UserManagement management = getDefaultManagement();
        User userToUpdate = users.get(0);
        User updatedUser = new UserDTO(userToUpdate.getUsername(), "newPassword", null);

        management.login(userToUpdate.getUsername(), userToUpdate.getPassword());
        assertTrue(management.isLoggedIn(userToUpdate));

        management.updateUser(updatedUser, updatedUser, userToUpdate.getPassword());
        assertTrue(management.isLoggedIn(updatedUser));

        management.logout(updatedUser);
        assertFalse(management.isLoggedIn(updatedUser));

        management.login(updatedUser.getUsername(), updatedUser.getPassword());
        assertTrue(management.isLoggedIn(updatedUser));

    }

    /**
     * Unbekannter Benutzer aktualisieren
     *
     * @author Marco
     * @since Start
     */
    @Test
    void updateUnknownUser() {
        UserManagement management = getDefaultManagement();
        assertThrows(UserManagementException.class, () -> management.updateUser(userNotInStore, userNotInStore, "marco10"));
    }

    /**
     * Alle Benutzer abrufen
     *
     * @author Marco
     * @since Start
     */
    @Test
    void retrieveAllUsers() {
        UserManagement management = getDefaultManagement();

        List<User> allUsers = management.retrieveAllUsers();

        Collections.sort(allUsers);
        assertEquals(allUsers, getDefaultUsers());

        // check, if there are no passwords
        // TODO: typically, there should be no logic in tests
        allUsers.forEach(u -> assertEquals(u.getPassword(), ""));
    }


}