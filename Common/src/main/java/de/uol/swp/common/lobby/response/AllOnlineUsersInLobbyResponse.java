package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.message.AbstractResponseMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * The type All online users in lobby response.
 */
public class AllOnlineUsersInLobbyResponse extends AbstractResponseMessage {

    private ArrayList<LobbyUser> users = new ArrayList<>();
    private UUID lobbyID;

    /**
     * Instantiates a new All online users in lobby response.
     *
     * @param lobbyID the lobbyID
     * @param users     the users
     */
    public AllOnlineUsersInLobbyResponse(UUID lobbyID, Collection<LobbyUser> users) {
        this.lobbyID = lobbyID;
        this.users.addAll(users);
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public ArrayList<LobbyUser> getUsers() {
        return users;
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
