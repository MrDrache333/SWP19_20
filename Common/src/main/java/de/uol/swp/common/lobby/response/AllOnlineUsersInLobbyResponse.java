package de.uol.swp.common.lobby.response;

import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;

import java.util.*;

/**
 * Die Klasse des All online users in lobby response.
 *
 * @author Keno S., Keno O.
 * @since Sprint 3
 */
public class AllOnlineUsersInLobbyResponse extends AbstractResponseMessage {

    private static final long serialVersionUID = -2995292304508692414L;
    private final Set<User> users = new TreeSet<>();
    private final TreeMap<String, Boolean> readyStatus;
    private final UUID lobbyID;

    /**
     * Konstruktor der All online users in lobby response.
     *
     * @param lobbyID Die lobbyID
     * @param users   Die User
     * @author Keno O., Marvin
     * @since Sprint 3
     */
    public AllOnlineUsersInLobbyResponse(UUID lobbyID, Collection<User> users, TreeMap<String, Boolean> readyStatus) {
        this.lobbyID = lobbyID;
        this.users.addAll(users);
        this.readyStatus = readyStatus;
    }

    /**
     * Gibt die User zurück.
     *
     * @return users Die User
     * @author Keno O., Keno S.
     * @since Sprint 3
     */
    public Set<User> getUsers() {
        return users;
    }


    /**
     * Gibt zurück, ob der User bereit ist.
     *
     * @return users Die User
     * @author Keno O.
     * @since Sprint 3
     */
    public boolean getStatus(User user) {
        if (!readyStatus.containsKey(user.getUsername())) return false;
        return readyStatus.get(user.getUsername());
    }

    /**
     * Gibt die LobbyID zurück.
     *
     * @return lobbyID Die ID der Lobby
     * @author Keno O., Keno S.
     * @since Sprint 3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }
}
