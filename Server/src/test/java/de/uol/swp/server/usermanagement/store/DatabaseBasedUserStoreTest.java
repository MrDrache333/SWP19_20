package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasse, die den MainMemoryBaseUserStore testet
 *
 * @author Marco
 * @since Start
 */

class DatabaseBasedUserStoreTest {

    private static final int NO_USERS = 10;
    private static final List<UserDTO> users;

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

    DatabaseBasedUserStore getDefaultStore() {
        DatabaseBasedUserStore store = new DatabaseBasedUserStore(true);
        List<UserDTO> users = getDefaultUsers();
        store.getAllUsers().forEach(user -> store.removeUser(user.getUsername()));
        users.forEach(u -> {
            if (store.findUser(u.getUsername()).isEmpty())
                store.createUser(u.getUsername(), u.getPassword(), u.getEMail());
        });
        return store;
    }

    @Test
    void findUserByName() {
        // arrange
        DatabaseBasedUserStore store = getDefaultStore();
        User userToCreate = getDefaultUsers().get(0);

        // act
        Optional<User> userFound = store.findUser(userToCreate.getUsername());

        // assert
        assertTrue(userFound.isPresent());
        assertEquals(userToCreate, userFound.get());
        assertEquals("", userFound.get().getPassword());
    }

    @Test
    void findUserByName_NotFound() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToFind = getDefaultUsers().get(0);

        Optional<User> userFound = store.findUser("öööö" + userToFind.getUsername());

        assertTrue(userFound.isEmpty());
    }

    @Test
    void findUserByNameAndPassword() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToCreate = getDefaultUsers().get(1);
        store.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());

        Optional<User> userFound = store.findUser(userToCreate.getUsername(), userToCreate.getPassword());

        assertTrue(userFound.isPresent());
        assertEquals(userToCreate, userFound.get());
        assertEquals("", userFound.get().getPassword());
    }

    @Test
    void findUserByNameAndPassword_NotFound() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToFind = getDefaultUsers().get(0);

        Optional<User> userFound = store.findUser(userToFind.getUsername(), "");

        assertTrue(userFound.isEmpty());
    }

    @Test
    void findUserByNameAndPassword_EmptyUser_NotFound() {
        DatabaseBasedUserStore store = getDefaultStore();

        Optional<User> userFound = store.findUser(null, "");

        assertTrue(userFound.isEmpty());
    }


    @Test
    void overwriteUser() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToCreate = getDefaultUsers().get(1);
        store.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());
        store.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());

        Optional<User> userFound = store.findUser(userToCreate.getUsername(), userToCreate.getPassword());

        assertEquals(store.getAllUsers().size(), NO_USERS);
        assertTrue(userFound.isPresent());
        assertEquals(userToCreate, userFound.get());

    }


    @Test
    void updateUser() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToUpdate = getDefaultUsers().get(2);

        store.updateUser(userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getEMail() + "@TESTING", userToUpdate, "marco2");

        Optional<User> userFound = store.findUser(userToUpdate.getUsername());

        assertTrue(userFound.isPresent());
        assertEquals(userToUpdate.getEMail() + "@TESTING", userFound.get().getEMail());

    }

    @Test
    void changePassword() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToUpdate = getDefaultUsers().get(2);

        store.updateUser(userToUpdate.getUsername(), userToUpdate.getPassword() + "_NEWPASS", userToUpdate.getEMail(), userToUpdate, userToUpdate.getPassword());

        Optional<User> userFound = store.findUser(userToUpdate.getUsername(), userToUpdate.getPassword() + "_NEWPASS");

        assertTrue(userFound.isPresent());
        assertEquals(userToUpdate.getEMail(), userFound.get().getEMail());

    }

    @Test
    void dropUser() {
        DatabaseBasedUserStore store = getDefaultStore();
        User userToRemove = getDefaultUsers().get(3);

        store.removeUser(userToRemove.getUsername());

        Optional<User> userFound = store.findUser(userToRemove.getUsername());

        assertTrue(userFound.isEmpty());
    }

    @Test
    void createEmptyUser() {
        DatabaseBasedUserStore store = getDefaultStore();

        assertThrows(IllegalArgumentException.class,
                () -> store.createUser("", "", "")
        );
    }

    @Test
    void getAllUsers() {
        DatabaseBasedUserStore store = getDefaultStore();
        List<UserDTO> allUsers = getDefaultUsers();

        List<User> allUsersFromStore = store.getAllUsers();

        allUsersFromStore.forEach(u -> assertEquals("", u.getPassword()));
        Collections.sort(allUsersFromStore);
        assertEquals(allUsers, allUsersFromStore);
    }
}