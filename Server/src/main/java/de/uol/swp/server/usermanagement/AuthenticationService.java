package de.uol.swp.server.usermanagement;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.game.request.GameGiveUpRequest;
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
import de.uol.swp.server.game.player.bot.internal.messages.AuthBotInternalRequest;
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
@SuppressWarnings("UnstableApiUsage")
public class AuthenticationService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(AuthenticationService.class);

    /**
     * Die Liste mit den aktuell eingeloggten User
     *
     * @author Marco
     * @since Start
     */
    final private Map<Session, User> userSessions = new HashMap<>();
    final private Map<Session, User> botSessions = new HashMap<>();

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
        if (entry.isEmpty()) {
            entry = botSessions.entrySet().stream().filter(e -> e.getValue().getUsername().equals(user.getUsername())).findFirst();
            return entry.map(Map.Entry::getKey);
        }
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
     * @param req LoginRequest
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void onLoginRequest(LoginRequest req) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Neue Authentifizierung Nachricht erhalten mit " + req.getUsername() + " " + req.getPassword());
        }
        ServerInternalMessage returnMessage;
        try {
            User newUser = userManagement.login(req.getUsername(), req.getPassword());
            returnMessage = new ClientAuthorizedMessage(newUser);
            Session newSession = UUIDSession.create(newUser);
            userSessions.put(newSession, newUser);
            returnMessage.setSession(newSession);
        } catch (Exception e) {
            LOG.error(e);
            returnMessage = new ServerExceptionMessage(new LoginException(e.getMessage()));
        }
        if (req.getMessageContext().isPresent()) {
            returnMessage.setMessageContext(req.getMessageContext().get());
        }
        post(returnMessage);
    }

    @Subscribe
    public void onBotCreatedRequest(AuthBotInternalRequest req) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Bot Request kommt im Auth Service an");
        }
        try {
            Session newSession = UUIDSession.create(req.getBotPlayer().getTheUserInThePlayer());
            botSessions.put(newSession, req.getBotPlayer().getTheUserInThePlayer());
        } catch (Exception e) {
            LOG.error(e);
        }
    }


    /**
     * Serverlogik vom LogoutRequest
     *
     * @param req LogoutRequest
     * @author Marco, Darian, Marvin
     * @since Start
     */
    @Subscribe
    public void onLogoutRequest(LogoutRequest req) {
        if (req.getSession().isPresent()) {
            Session session = req.getSession().get();
            User userToLogOut = session.getUser();
            if (req.isHardLogout()) {
                lobbyManagement.activeGamesOfUser(userToLogOut)
                        .forEach(game -> post(new GameGiveUpRequest((UserDTO) userToLogOut, game)));
            }
            if (!lobbyManagement.isUserIngame(userToLogOut) || req.isHardLogout()) {
                // Could be already logged out
                if (userToLogOut != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Logge User " + userToLogOut.getUsername() + " aus");
                    }
                    userManagement.logout(userToLogOut);
                    userSessions.remove(session);
                    LeaveAllLobbiesOnLogoutRequest request = new LeaveAllLobbiesOnLogoutRequest((UserDTO) userToLogOut);
                    post(request);
                    ServerMessage returnMessage = new UserLoggedOutMessage(userToLogOut.getUsername());
                    post(returnMessage);
                }
            } else {
                UpdateUserFailedMessage returnMessage = new UpdateUserFailedMessage(session.getUser(), "Der Account befindet sich in einem laufenden Spiel. Du kannst dich nicht ausloggen!");
                sendToLoggedInPlayers(returnMessage);
            }
        }
    }

    /**
     * Serverlogik vom RetrieveAllOnlineUsersRequest
     *
     * @param req RetrieveAllOnlineUsersRequest
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void onRetrieveAllOnlineUsersRequest(RetrieveAllOnlineUsersRequest req) {
        AllOnlineUsersResponse response = new AllOnlineUsersResponse(userSessions.values());
        response.initWithMessage(req);
        post(response);
    }

    /**
     * Aktualisierung des Users wird versucht, bei Erfolg wird eine UpdatedUserMessage gesendet, andernfalls wird eine UpdateUserFailedMessage
     * mit entsprechender Fehlermeldung gesendet
     *
     * @param req die UpdateUserRequest
     * @author Julia
     * @since Sprint 4
     */
    @Subscribe
    public void onUpdateUserRequest(UpdateUserRequest req) {
        if (lobbyManagement.userInLobby(req.getOldUser())) {
            sendToUser(new UpdateUserFailedMessage(req.getOldUser(), "Du kannst deine Daten nicht ändern,\nwenn du in einer Lobby bist."), req.getOldUser());
        } else {
            userSessions.put(req.getSession().get(), req.getUser());
            req.getSession().get().updateUser(req.getUser());
            ServerMessage returnMessage;
            try {
                User user = userManagement.updateUser(req.getUser(), req.getOldUser(), req.getCurrentPassword());
                returnMessage = new UpdatedUserMessage(user, req.getOldUser());
                LOG.info("User " + req.getOldUser().getUsername() + " erfolreich aktualisiert");
                post(new UpdateLobbiesRequest((UserDTO) user, (UserDTO) req.getOldUser()));
            } catch (UserUpdateException e) {
                userSessions.replace(req.getSession().get(), req.getOldUser());
                req.getSession().get().updateUser(req.getOldUser());
                returnMessage = new UpdateUserFailedMessage(req.getOldUser(), e.getMessage());
                LOG.info("Aktualisierung des Users " + req.getOldUser().getUsername() + " fehlgeschlagen");
            }
            sendToLoggedInPlayers(returnMessage);
        }
    }

    /**
     * Der Nutzer wird gelöscht und eine entprechende Message zurückgesendet
     *
     * @author Anna, Julia, Darian
     * @since Sprint 4
     */
    @Subscribe
    public void onDropUserRequest(DropUserRequest req) {
        User userToDrop = req.getUser();

        // Could be already logged out/removed or he is in a game
        if (userToDrop != null) {
            if (!lobbyManagement.isUserIngame(userToDrop)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Lösche User " + userToDrop.getUsername());
                }
                userSessions.remove(req.getSession().get());
                userManagement.dropUser(userToDrop);

                sendToLoggedInPlayers(new UserDroppedMessage(userToDrop));
            } else {
                UpdateUserFailedMessage returnMessage = new UpdateUserFailedMessage(userToDrop, "Der Account befindet sich in einem laufenden Spiel. Du kannst deinen Account nicht löschen!");
                sendToLoggedInPlayers(returnMessage);
            }
        }
    }

    /**
     * Die Funktion sendet eine Nachricht an alle angemeldeten User.
     *
     * @param msg Die zu übertragende Nachricht
     * @author Keno S.
     * @since Sprint 7
     */

    public void sendToLoggedInPlayers(ServerMessage msg) {
        if(!userSessions.isEmpty()) {
            msg.setReceiver(getSessions(new TreeSet<>(userSessions.values())));
            post(msg);
        }
    }

    public void sendToUser(ServerMessage msg, User user) {
        Set<User> user2 = new HashSet<>(1);
        user2.add(user);
        msg.setReceiver(getSessions(user2));
        post(msg);
    }
}