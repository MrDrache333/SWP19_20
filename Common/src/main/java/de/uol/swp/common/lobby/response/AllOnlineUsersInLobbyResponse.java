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

    private Set<User> users = new TreeSet<>();
    private TreeMap<String, Boolean> readyStatus = new TreeMap<>();
    private UUID lobbyID;

    /**
     * Konstruktor der All online users in lobby response.
     *
     * @author Keno O., Marvin
     * @since Sprint 3
     * @param lobbyID Die lobbyID
     * @param users   Die User
     */
    public AllOnlineUsersInLobbyResponse(UUID lobbyID, Collection<User> users, TreeMap<String, Boolean> readyStatus) {
        this.lobbyID = lobbyID;
        this.users.addAll(users);
        this.readyStatus = readyStatus;
    }

    /**
     * Gibt die User zurück.
     *
     * @author Keno O., Keno S.
     * @since Sprint 3
     * @return users Die User
     */
    public Set<User> getUsers() {
        return users;
    }


    /**
     * Gibt zurück, ob der User bereit ist.
     *
     * @author Keno O.
     * @since Sprint 3
     * @return users Die User
     */
    public boolean getStatus(User user) {
        if (!readyStatus.containsKey(user.getUsername())) return false;
        return readyStatus.get(user.getUsername());
    }

    /**
     * Gibt die LobbyID zurück.
     *
     * @author Keno O., Keno S.
     * @since Sprint 3
     * @return lobbyID Die ID der Lobby
     */
    public UUID getLobbyID() {
        return lobbyID;
    }
}
