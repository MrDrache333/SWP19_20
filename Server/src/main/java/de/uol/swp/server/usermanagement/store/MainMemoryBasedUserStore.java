package de.uol.swp.server.usermanagement.store;

import com.google.common.base.Strings;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

import java.util.*;

/**
 * Das ist ein UserStore.
 * <p>
 * Wichtig: Dieser Store wird nie das Passwort eines Nutzers zurückgeben
 *
 */

public class MainMemoryBasedUserStore extends AbstractUserStore implements UserStore {

    private final Map<String, User> users = new HashMap<>();

    /**
     * Sucht über den Namen den Nutzer aus dem Store und überprüft ihn mithilfe des Passworts.
     * Wenn der Nutzer existiert und die Passwörter übereinstimmen, wird dieser zurückgegeben.
     *
     * @param username der Username
     * @param password das Passwort
     * @return User oder nichts
     * @author Marco, Julia
     *@since Sprint0
     */
    @Override
    public Optional<User> findUser(String username, String password) {
        User usr = users.get(username);
        if (usr != null && usr.getPassword().equals(password)) {
            return Optional.of(usr);
        }
        return Optional.empty();
    }

    /**
     * Sucht über den Namen den User aus dem Store und gibt ihn zurück, allerdings ohne Passwort.
     *
     * @param username der Username
     * @return den User oder nichts
     * @author Marco
     * @since Sprint0
     */
    @Override
    public Optional<User> findUser(String username) {
        User usr = users.get(username);
        if (usr != null) {
            return Optional.of(usr.getWithoutPassword());
        }
        return Optional.empty();
    }

    /**
     * Erstellt aus den übergebenen Daten einen neuen Nutzer und speichert ihn im Store.
     *
     * @param username der Username
     * @param password das Passwort
     * @param eMail die e-Mail
     * @return den neuen Nutzer
     * @author Marco
     * @since Sprint0
     */
    @Override
    public User createUser(String username, String password, String eMail) {
        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Username must not be null");
        }
        User usr = new UserDTO(username, password, eMail);
        users.put(username, usr);
        return usr;
    }

    /**
     * Der alte Nutzer wird aus dem Store entfernt und der aus den übergeben Daten neu ertsellte dafür hinzugefügt.
     *
     * @param username der neue Username
     * @param password das Passwort
     * @param eMail die e-Mail
     * @param oldUsername der alte Username
     * @return den aktualisierten Nutzer
     * @author Marco, Julia
     * @since Sprint0
     */
    @Override
    public User updateUser(String username, String password, String eMail, String oldUsername) {
        users.remove(oldUsername);
        User usr = new UserDTO(username, password, eMail);
        users.put(username, usr);
        return usr;
    }

    /**
     * Der Nutzer wird mithilfe seines Usernamens aus dem Store gelöscht.
     *
     * @param username der Username
     * @author Marco
     * @since Sprint0
     */
    @Override
    public void removeUser(String username) {
        users.remove(username);
    }

    /**
     * Eine Liste aller User die sich im Store befinden wird erstellt und zurückgegeben, aber ohne Passwörter.
     *
     * @return eine Liste aller Nutzer
     * @author Marco
     * @since Sprint0
     */
    @Override
    public List<User> getAllUsers() {
        List<User> retUsers = new ArrayList<>();
        users.values().forEach(u -> retUsers.add(u.getWithoutPassword()));
        return retUsers;
    }

}
