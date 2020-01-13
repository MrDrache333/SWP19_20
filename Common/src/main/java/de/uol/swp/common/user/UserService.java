package de.uol.swp.common.user;

import java.util.List;

/**
 * Das User Service Interface
 *
 * @author Marco Grawunder
 * @since Sprint 0
 */
public interface UserService {

    /**
     * Login-Methode mit dem Username und dem Passwort als Übergabeparameter
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @param username Der Username des Users
     * @param password Das Password des Users
     * @return Userobjekt Der User
     */
    User login(String username, String password);

    /**
     * Prüft, ob der User eingeloggt ist.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @param user Userobjekt
     * @return boolean Ob eingeloggt (true) oder nicht (false)
     */
    boolean isLoggedIn(User user);

    /**
     * Loggt den User aus.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @param user Userobjekt
     */
    void logout(User user);

    /**
     * Legt einen neuen User an.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @param user User, welcher angelegt werden soll
     * @return Userobjekt Der erstellte User
     */
    User createUser(User user);

    /**
     * Löscht einen User.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @param user User, welcher gelöscht werden soll
     */
    void dropUser(User user);

    /**
     * Aktualisiert einen User
     *
     * @author Julia
     * @since Sprint 4
     * @param user User mit neuen Daten
     * @param oldUser User mit alten Daten
     * @return Userobjekt Der aktualisierte User
     */
    User updateUser(User user, User oldUser);

    /**
     * Gibt alle eingeloggten User als Liste zurück.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @return List<User> Eine List mit allen eingeloggten Usern.
     */
    List<User> retrieveAllUsers();

}
