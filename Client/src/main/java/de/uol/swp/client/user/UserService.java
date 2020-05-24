package de.uol.swp.client.user;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.request.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * This class is used to hide the communication details
 * implements IClientUserService
 *
 * @author Marco Grawunder
 */

public class UserService implements de.uol.swp.common.user.UserService {

    private static final Logger LOG = LogManager.getLogger(UserService.class);
    private final EventBus bus;

    /**
     * Injector der einer Instanz automatisch den final EventBus der Klasse zuordnet
     *
     * @param bus Der EventBus
     * @author Marco Grawunder
     * @since Start
     */
    @Inject
    public UserService(EventBus bus) {
        this.bus = bus;
        // Currently not need, will only post on bus
        //bus.register(this);
    }

    /**
     * Postet LoginRequest auf den Bus
     *
     * @param username Der Username des Users
     * @param password Das Passwort des Users
     * @return null
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public User login(String username, String password) {
        LoginRequest msg = new LoginRequest(username, password);
        bus.post(msg);
        return null; // async call
    }

    /**
     * Noch nicht implementiert, würde überprüfen ob der Nutzer angemeldet ist
     *
     * @param user Der zu überprüfende Benutzer
     * @return momentan nichts (true wenn angemeldet, false falls nicht)
     * @throws UnsupportedOperationException Noch nicht implementiert
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public boolean isLoggedIn(User user) {
        throw new UnsupportedOperationException("Currently not implemented");
    }

    /**
     * Postet LogoutRequest auf den Bus
     *
     * @param username Der auszuloggende Nutzer
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public void logout(User username) {
        LogoutRequest msg = new LogoutRequest();
        bus.post(msg);
    }

    public void hardLogout(User username) {
        LogoutRequest msg = new LogoutRequest(true);
        bus.post(msg);
    }

    /**
     * Postet RegisterUserRequest auf den Bus
     *
     * @param user User, welcher angelegt werden soll
     * @return null
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public User createUser(User user) {
        RegisterUserRequest request = new RegisterUserRequest(user);
        bus.post(request);
        return null;
    }

    /**
     * Postet DropUserRequest auf den Bus
     *
     * @param user User, welcher gelöscht werden soll
     * @author Anna
     * @since Sprint 4
     */
    public void dropUser(User user) {
        DropUserRequest request = new DropUserRequest(user);
        bus.post(request);
    }

    /**
     * Postet UpdateUserRequest auf den Bus
     *
     * @param user            User mit neuen Daten
     * @param oldUser         User mit alten Daten
     * @param currentPassword Das Passwort des Users
     * @return null
     * @author (Marco Grawunder), Julia
     * @since (Start), Sprint 4
     */
    @Override
    public User updateUser(User user, User oldUser, String currentPassword) {
        UpdateUserRequest request = new UpdateUserRequest(user, oldUser, currentPassword);
        bus.post(request);
        return null;
    }

    /**
     * Postet RetrieveAllOnlineUsersRequest auf den Bus
     *
     * @return null
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public List<User> retrieveAllUsers() {
        RetrieveAllOnlineUsersRequest cmd = new RetrieveAllOnlineUsersRequest();
        bus.post(cmd);
        return null; // async call
    }

}