package de.uol.swp.common;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UserDTOTest {
    private static final User defaultUser = new UserDTO("marco", "marco", "marco@grawunder.de");
    private static final User secondsUser = new UserDTO("marco2", "marco", "marco@grawunder.de");

    /**
     * Es wird getestet was passiert, wenn ein User null als Wert für den Namen hat.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void createUserWithEmptyName() {
        assertThrows(AssertionError.class, () -> new UserDTO(null, "", ""));
    }

    /**
     * Es wird getestet was passiert, wenn ein User null als Wert für das Passwort hat.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void createUserWithEmptyPassword() {
        assertThrows(AssertionError.class, () -> new UserDTO("", null, ""));
    }

    /**
     * Es wird getestet, ob der kopierte User genau gleich ist.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void createWithExistingUser() {
        User newUser = UserDTO.create(defaultUser);
        // Test with equals method
        assertEquals(newUser, defaultUser);
        // Test every attribute
        assertEquals(newUser.getUsername(), defaultUser.getUsername());
        assertEquals(newUser.getPassword(), defaultUser.getPassword());
        assertEquals(newUser.getEMail(), defaultUser.getEMail());
    }

    /**
     * Es wird die Funktion getestet ob man einen User ohne Passwort erstellen kann.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void createWithExistingUserWithoutPassword() {
        User newUser = UserDTO.createWithoutPassword(defaultUser);
        // Test every attribute
        assertEquals(newUser.getUsername(), defaultUser.getUsername());
        assertEquals(newUser.getPassword(), "");
        assertEquals(newUser.getEMail(), defaultUser.getEMail());
        // Test with equals method
        assertEquals(newUser, defaultUser);
    }

    /**
     * Es wird getestet ob man einen User anfragen kann ohne Passwort.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void getWithoutPassword() {
        User userWithoutPassword = defaultUser.getWithoutPassword();
        assertEquals(userWithoutPassword.getUsername(), defaultUser.getPassword());
        assertEquals(userWithoutPassword.getPassword(), "");
        assertEquals(userWithoutPassword.getUsername(), defaultUser.getUsername());
    }

    /**
     * Es wird getestet ob zwei unterschiedliche User auch nicht gleich sind.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void usersNotEquals_User() {
        assertNotEquals(defaultUser, secondsUser);
    }

    /**
     * Es wird getestet ob der User nicht gleich einem String.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void usersNotEquals_String() {
        assertNotEquals(defaultUser, "Test");
    }

    /**
     * @author Marco
     * @since Start
     */
    @Test
    void userCompare() {
        assertEquals(defaultUser.compareTo(secondsUser), -1);
    }

    /**
     * Es wird getestet ob der Hashwert gleich ist wenn man einen gleichen User wieder hasht
     *
     * @author Marco
     * @since Start
     */
    @Test
    void testHashCode() {
        User newUser = UserDTO.create(defaultUser);
        assertEquals(newUser.hashCode(), defaultUser.hashCode());
    }
}