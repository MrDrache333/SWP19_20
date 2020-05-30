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
     * @param username Der Username des Users
     * @param password Das Password des Users
     * @return Userobjekt Der User
     * @author Marco Grawunder
     * @since Sprint 0
     */
    User login(String username, String password);

    /**
     * Prüft, ob der User eingeloggt ist.
     *
     * @param user Userobjekt
     * @return boolean Ob eingeloggt (true) oder nicht (false)
     * @author Marco Grawunder
     * @since Sprint 0
     */
    boolean isLoggedIn(User user);

    /**
     * Loggt den User aus.
     *
     * @param user Userobjekt
     * @author Marco Grawunder
     * @since Sprint 0
     */
    void logout(User user);

    /**
     * Loggt den User aus ohne mögliche Fehlermeldungen (insbesondere "Du bist gerade noch in einem Spiel",
     * da diese, nach Schließen des Fensters, sowieso nicht mehr angezeigt werden können.)
     *
     * @param user Userobjekt
     * @author Marvin
     * @since Sprint 8
     */
    void hardLogout(User user);

    /**
     * Legt einen neuen User an.
     *
     * @param user User, welcher angelegt werden soll
     * @return Userobjekt Der erstellte User
     * @author Marco Grawunder
     * @since Sprint 0
     */
    User createUser(User user);

    /**
     * Löscht einen User.
     *
     * @param user User, welcher gelöscht werden soll
     * @author Marco Grawunder
     * @since Sprint 0
     */
    void dropUser(User user);

    /**
     * Aktualisiert einen User
     *
     * @param user            User mit neuen Daten
     * @param oldUser         User mit alten Daten
     * @param currentPassword das momentane Passwort des Users
     * @return Userobjekt Der aktualisierte User
     * @author Julia
     * @since Sprint 4
     */
    User updateUser(User user, User oldUser, String currentPassword);

    /**
     * Gibt alle eingeloggten User als Liste zurück.
     *
     * @return List<User> Eine List mit allen eingeloggten Usern.
     * @author Marco Grawunder
     * @since Sprint 0
     */
    List<User> retrieveAllUsers();
}
