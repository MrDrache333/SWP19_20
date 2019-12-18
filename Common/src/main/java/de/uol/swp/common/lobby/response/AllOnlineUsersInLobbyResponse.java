package de.uol.swp.common.lobby.response;

import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;

import java.util.*;

/**
 * The type All online users in lobby response.
 */
public class AllOnlineUsersInLobbyResponse extends AbstractResponseMessage {

    private Set<User> users = new TreeSet<>();
    private TreeMap<String, Boolean> readyStatus = new TreeMap<>();
    private UUID lobbyID;

    /**
     * Instantiates a new All online users in lobby response.
     *
     * @param lobbyID the lobbyID
     * @param users   the users
     */
    public AllOnlineUsersInLobbyResponse(UUID lobbyID, Collection<User> users, TreeMap<String, Boolean> readyStatus) {
        this.lobbyID = lobbyID;
        this.users.addAll(users);
        this.readyStatus = readyStatus;
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public Set<User> getUsers() {
        return users;
    }

    public boolean getStatus(User user) {
        if (!readyStatus.containsKey(user.getUsername())) return false;
        return readyStatus.get(user.getUsername());
    }

    /**
     * Gets lobby name.
     *
     * @return the lobby name
     */
    public UUID getLobbyID() {
        return lobbyID;
    }
}
