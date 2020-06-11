package de.uol.swp.server.usermanagement;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import de.uol.swp.common.user.User;
import de.uol.swp.server.usermanagement.store.UserStore;

import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Die Serverseitige Benutzerverwaltung.
 */
public class UserManagement extends AbstractUserManagement {

    private final UserStore userStore;
    private final SortedMap<String, User> loggedInUsers = new TreeMap<>();

    @Inject
    public UserManagement(UserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * Impelemtiert den login Serverseitig.
     *
     * @param username Der Benutzername des Users
     * @param password Das Passwort des Users
     * @return den Benutzer
     * @throws SecurityException Eine SecurityException
     * @author Marco, Keno S
     * @since Basisprojekt
     */
    @Override
    public User login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            throw new SecurityException("Usernamen und Passwort eingeben!");
        }
        Optional<User> user = userStore.findUser(username, password);
        if (user.isPresent() && !loggedInUsers.containsKey(username)) {
            this.loggedInUsers.put(username, user.get());
            return user.get();
        } else if (user.isPresent() && loggedInUsers.containsKey(username)) {
            throw new SecurityException("User ist bereits angemeldet!");
        } else if (user.isEmpty()) {
            throw new SecurityException("Username oder Passwort falsch eingegeben!");
        } else {
            throw new SecurityException("Authentifizierung fehlgeschlagen, versuche es erneut!");
        }
    }

    /**
     * Überprüft, ob der aktuelle Nutzer eingeloggt ist oder nicht.
     *
     * @param username der Username
     * @return gibt true oder false zurück, ob der User eingeloggt ist.
     * @author Marco
     * @since Basisprojekt
     */
    @Override
    public boolean isLoggedIn(User username) {
        return loggedInUsers.containsKey(username.getUsername());
    }

    /**
     * Erstellen eines Users.
     *
     * @param userToCreate User Objekt des zu erstellenden Users
     * @return Gibt den neuen User zurück
     * @throws UserManagementException wenn Der Name bereits verwendet wurde.
     * @author Marco, Keno S
     * @since Basisprojekt
     */
    @Override
    public User createUser(User userToCreate) {
        Optional<User> user = userStore.findUser(userToCreate.getUsername());
        if (user.isPresent()) {
            throw new UserManagementException("Der Benutzername wird bereits genutzt!");
        }
        return userStore.createUser(userToCreate.getUsername(), userToCreate.getPassword(), userToCreate.getEMail());
    }

    /**
     * Aktualisiert Nutzerdaten.
     *
     * @param userToUpdate    der User
     * @param oldUser         User mit alten Daten
     * @param currentPassword das momentane Passwort des Users
     * @return den neuen User
     * @throws UserUpdateException     wenn Name/Email bereits vergeben sind.
     * @throws UserManagementException wenn der Benutzer nicht existiert.
     * @author Julia, Keno, Marco
     * @since Basisprojekt
     */
    @Override
    public User updateUser(User userToUpdate, User oldUser, String currentPassword) {
        Optional<User> user = userStore.findUser(oldUser.getUsername());
        if (user.isEmpty()) {
            throw new UserManagementException("Unbekannter Benutzername!");
        }
        //Überprüft ob der Username bereits vergeben ist
        user = userStore.findUser(userToUpdate.getUsername());
        if (user.isPresent() && !userToUpdate.getUsername().equals(oldUser.getUsername())) {
            throw new UserUpdateException("Dieser Name ist bereits vergeben!");
        }
        //Überprüft ob bereits ein Account mit der Email existiert
        List<String> mails = userStore.getAllUsers().stream().map(User::getEMail).collect(Collectors.toList());
        if (mails.contains(userToUpdate.getEMail()) && !userToUpdate.getEMail().equals(oldUser.getEMail())) {
            throw new UserUpdateException("Diese Email ist bereits vergeben!");
        }
        return userStore.updateUser(userToUpdate.getUsername(), userToUpdate.getPassword(), userToUpdate.getEMail(), oldUser, currentPassword);
    }

    /**
     * Löscht einen Nutzer.
     *
     * @param userToDrop Der zu löschende Benutzer
     * @throws UserManagementException Wenn der Benutzer nicht bekannt ist.
     * @author Marco, Julia
     * @since Basisprojekt
     */
    @Override
    public void dropUser(User userToDrop) {
        Optional<User> user = userStore.findUser(userToDrop.getUsername());
        if (user.isEmpty()) {
            throw new UserManagementException("Unbekannter Benutzername!");
        }
        logout(userToDrop);
        userStore.removeUser(userToDrop.getUsername());
    }

    private String firstNotNull(String firstValue, String secondValue) {
        return Strings.isNullOrEmpty(firstValue) ? secondValue : firstValue;
    }

    /**
     * Meldet einen Benutzer ab.
     *
     * @param user Der User
     * @author Marco
     * @since Basisprojekt
     */
    @Override
    public void logout(User user) {
        loggedInUsers.remove(user.getUsername());
    }

    @Override
    public void hardLogout(User user) {
        loggedInUsers.remove(user.getUsername());
    }

    /**
     * Gibt die Liste der User zurück.
     *
     * @return Userliste
     * @author Marco
     * @since Basisprojekt
     */
    @Override
    public List<User> retrieveAllUsers() {
        return userStore.getAllUsers();
    }
}
