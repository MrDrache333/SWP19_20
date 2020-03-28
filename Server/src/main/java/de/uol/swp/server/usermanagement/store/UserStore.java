package de.uol.swp.server.usermanagement.store;

import de.uol.swp.common.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Der UserStore
 *
 * @author Marco
 * @since Start
 */

public interface UserStore {

    /**
     * Findet den User mit dem Username und dem Passwort
     *
     * @param username der Username
     * @param password das Passwort des Users
     * @return Der User ohne Passwortinformationen, wenn er gefunden wurde
     * @author Marco
     * @since Start
     */
    Optional<User> findUser(String username, String password);

    /**
     * Findet einen User nur mit dem Namen
     *
     * @param username der Username
     * @return der User ohne Passwortinformationen, wenn er gefunden wurde
     * @author Marco
     * @since Start
     */
    Optional<User> findUser(String username);

    /**
     * Erstellt einen neuen User
     *
     * @param username der gewählte Username
     * @param password das gewählte Passwort
     * @param eMail    die angegebene E-Mail-Adresse
     * @return der User ohne Passwortinformationen
     * @author Marco
     * @since Start
     */
    User createUser(String username, String password, String eMail);

    /**
     * Aktualisiert einen User
     *
     * @param username        der neue Username
     * @param password        das Passwort des Users
     * @param eMail           die E-Mail-Adresse des Users
     * @param oldUsername     der alter Username
     * @param currentPassword das momentane Passwort des Users
     * @return den aktualisierten User
     * @author Marco, Julia
     * @since Start
     */
    User updateUser(String username, String password, String eMail, String oldUsername, String currentPassword);

    /**
     * Löscht einen User aus der Liste
     *
     * @param username der zu löschende User
     * @author Marco
     * @since Start
     */
    void removeUser(String username);


    /**
     * Gibt die Liste aller User wieder
     *
     * @return Eine Liste mit allen Usern ohne Passwortinformationenen
     * @author Marco
     * @since Start
     */
    List<User> getAllUsers();


}
