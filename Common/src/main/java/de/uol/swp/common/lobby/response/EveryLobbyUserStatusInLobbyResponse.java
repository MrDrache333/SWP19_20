package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.message.AbstractResponseMessage;

import java.util.ArrayList;

/**
 * The type Every lobby user status in lobby response.
 */
public class EveryLobbyUserStatusInLobbyResponse extends AbstractResponseMessage {

    private ArrayList<LobbyUser> Users = new ArrayList<>();

    /**
     * Instantiates a new Every lobby user status in lobby response.
     *
     * @param users the users
     */
    public EveryLobbyUserStatusInLobbyResponse(ArrayList<LobbyUser> users) {
        this.Users = users;
    }

    /**
     * Gets users.
     *
     * @return the users
     */
    public ArrayList<LobbyUser> getUsers() {
        return Users;
    }
}
