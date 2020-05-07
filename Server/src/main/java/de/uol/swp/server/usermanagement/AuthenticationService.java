package de.uol.swp.server.usermanagement;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.request.LeaveAllLobbiesOnLogoutRequest;
import de.uol.swp.common.lobby.request.UpdateLobbiesRequest;
import de.uol.swp.common.message.ServerMessage;
import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UpdateUserFailedMessage;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.*;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.communication.UUIDSession;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.ClientAuthorizedMessage;
import de.uol.swp.server.message.ServerExceptionMessage;
import de.uol.swp.server.message.ServerInternalMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.*;

/**
 * Request zum Authenfizieren werden hier behandelt
 *
 * @author Marco Grawunder
 * @since Start
 */
public class AuthenticationService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(AuthenticationService.class);

    /**
     * Die Liste mit den aktuell eingeloggten User
     *
     * @author Marco
     * @since Start
     */
    final private Map<Session, User> userSessions = new HashMap<>();

    private final UserManagement userManagement;
    private final LobbyManagement lobbyManagement;

    /**
     * AuthenticationService wird initialisiert
     *
     * @param bus            EventBus
     * @param userManagement das UserManagement
     * @author Marco
     * @since Start
     */
    @Inject
    public AuthenticationService(EventBus bus, UserManagement userManagement, LobbyManagement lobbyManagement) {
        super(bus);
        this.userManagement = userManagement;
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Session von einem User wird übergeben
     *
     * @param user User
     * @return Map mit Sessions
     * @author Marco
     * @since Start
     */
    public Optional<Session> getSession(User user) {
        Optional<Map.Entry<Session, User>> entry = userSessions.entrySet().stream().filter(e -> e.getValue().equals(user)).findFirst();
        return entry.map(Map.Entry::getKey);
    }

    /**
     * mehrere Sessions von mehreren Usern werden übergeben
     *
     * @param users ein Set von Usern
     * @return Liste von Sessions
     * @author Marco
     * @since Start
     */
    public List<Session> getSessions(Set<User> users) {
        List<Session> sessions = new ArrayList<>();
        users.forEach(u -> {
            Optional<Session> session = getSession(u);
            session.ifPresent(sessions::add);
        });
        return sessions;
    }

    /**
     * Servlogik vom LoginRequest
     *
     * @param msg LoginRequest
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void onLoginRequest(LoginRequest msg) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Got new auth message with " + msg.getUsername() + " " + msg.getPassword());
        }
        ServerInternalMessage returnMessage;
        try {
            User newUser = userManagement.login(msg.getUsername(), msg.getPassword());
            returnMessage = new ClientAuthorizedMessage(newUser);
            Session newSession = UUIDSession.create(newUser);
            userSessions.put(newSession, newUser);
            returnMessage.setSession(newSession);
        } catch (Exception e) {
            LOG.error(e);
            returnMessage = new ServerExceptionMessage(new LoginException("Cannot auth user " + msg.getUsername()));
        }
        if (msg.getMessageContext().isPresent()) {
            returnMessage.setMessageContext(msg.getMessageContext().get());
        }
        post(returnMessage);
    }

    /**
     * Serverlogikg vom LogoutRequest
     *
     * @param msg LogoutRequest
     * @author Marco, Darian
     * @since Start
     */
    @Subscribe
    public void onLogoutRequest(LogoutRequest msg) {
        if (msg.getSession().isPresent()) {
            Session session = msg.getSession().get();
            User userToLogOut = session.getUser();
            if (!lobbyManagement.isUserIngame(userToLogOut)) {
                // Could be already logged out
                if (userToLogOut != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Logging out user " + userToLogOut.getUsername());
                    }
                    userManagement.logout(userToLogOut);
                    userSessions.remove(session);
                    LeaveAllLobbiesOnLogoutRequest request = new LeaveAllLobbiesOnLogoutRequest((UserDTO) userToLogOut);
                    post(request);
                    ServerMessage returnMessage = new UserLoggedOutMessage(userToLogOut.getUsername());
                    post(returnMessage);
                }
            }
            else{
                UpdateUserFailedMessage returnMessage = new UpdateUserFailedMessage(session.getUser(), "Account is in Game. You can not logout.");
                post(returnMessage);
            }
        }
    }

    /**
     * Serverlogik vom RetrieveAllOnlineUsersRequest
     *
     * @param msg RetrieveAllOnlineUsersRequest
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void onRetrieveAllOnlineUsersRequest(RetrieveAllOnlineUsersRequest msg) {
        AllOnlineUsersResponse response = new AllOnlineUsersResponse(userSessions.values());
        response.initWithMessage(msg);
        post(response);
    }

    /**
     * Aktualisierung des Users wird versucht, bei Erfolg wird eine UpdatedUserMessage gesendet, andernfalls wird eine UpdateUserFailedMessage
     * mit entsprechender Fehlermeldung gesendet
     *
     * @param msg die UpdateUserRequest
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void onUpdateUserRequest(UpdateUserRequest msg) {
        userSessions.put(msg.getSession().get(), msg.getUser());
        msg.getSession().get().updateUser(msg.getUser());
        ServerMessage returnMessage;
        try {
            User user = userManagement.updateUser(msg.getUser(), msg.getOldUser(), msg.getCurrentPassword());
            returnMessage = new UpdatedUserMessage(user, msg.getOldUser());
            LOG.info("User " + msg.getOldUser().getUsername() + " updated successfully");
            post(new UpdateLobbiesRequest((UserDTO) user, (UserDTO) msg.getOldUser()));
        } catch (UserUpdateException e) {
            userSessions.replace(msg.getSession().get(), msg.getOldUser());
            msg.getSession().get().updateUser(msg.getOldUser());
            returnMessage = new UpdateUserFailedMessage(msg.getOldUser(), e.getMessage());
            LOG.info("Update of user " + msg.getOldUser().getUsername() + " failed");
        }
        post(returnMessage);
    }

    /**
     * Der Nutzer wird gelöscht und eine entprechende Message zurückgesendet
     *
     * @author Anna, Julia, Darian
     * @since Sprint4
     */
    @Subscribe
    public void onDropUserRequest(DropUserRequest msg) {
        User userToDrop = msg.getUser();

        // Could be already logged out/removed or he is in a game
        if (userToDrop != null ) {
            if (!lobbyManagement.isUserIngame(userToDrop)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Dropping user " + userToDrop.getUsername());
                }
                userSessions.remove(msg.getSession().get());
                userManagement.dropUser(userToDrop);

                ServerMessage returnMessage = new UserDroppedMessage(userToDrop);
                post(returnMessage);
            }
            else{
                UpdateUserFailedMessage returnMessage = new UpdateUserFailedMessage(userToDrop,"Account is in Game. You can not delete your Account.");
                post(returnMessage);
            }
        }
    }
}