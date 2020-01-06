package de.uol.swp.common.user;

import java.util.List;

/**
 * An interface for all methods of the user service
 *
 * @author Marco Grawunder
 */

public interface UserService {

    /**
     * Login with username and password
     *
     * @param username the name of the user
     * @param password the password of the user
     * @return a new user object
     */
    User login(String username, String password);


    /**
     * Test, if given user is logged in
     *
     * @param user
     * @return
     */
    boolean isLoggedIn(User user);

    /**
     * Login out from server
     */
    void logout(User user);

    /**
     * Create a new persistent user
     *
     * @param user The user to create
     * @return the new created user
     */
    User createUser(User user);

    /**
     * Removes a user from the sore
     *
     * @param user The user to remove
     */
    void dropUser(User user);

    /**
     * Aktualisiert einen User
     *
     * @param user User mit neuen Daten
     * @param oldUser User mit alten Daten
     * @return
     */
    User updateUser(User user, User oldUser);

    /**
     * Retrieve the list of all current logged in users
     *
     * @return a list of users
     */
    List<User> retrieveAllUsers();

}
