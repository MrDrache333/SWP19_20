package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.message.AbstractResponseMessage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The type All online users in lobby response.
 */
public class AllOnlineUsersInLobbyResponse extends AbstractResponseMessage {

    private ArrayList<LobbyUser> users = new ArrayList<>();
    private String LobbyName;

    /**
     * Instantiates a new All online users in lobby response.
     *
     * @param lobbyname the lobbyname
     * @param users     the users
     */
    public AllOnlineUsersInLobbyResponse(String lobbyname, Collection<LobbyUser> users) {
        this.LobbyName = lobbyname;
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
    public String getLobbyName() {
        return LobbyName;
    }
}
