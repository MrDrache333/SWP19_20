package de.uol.swp.common.lobby.response;

import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Subscriber: client.game.GameViewPresenter.userList
 *
 * @author Marvin
 */

public class AllLobbyUsersResponse extends AbstractResponseMessage {

    final private ArrayList<UserDTO> users = new ArrayList<>();
    private String lobbyName;

    public AllLobbyUsersResponse() {
        // needed for serialization
    }

    public AllLobbyUsersResponse(Collection<User> users, String lobbyName) {
        this.lobbyName = lobbyName;
        for (User user : users) {
            this.users.add(UserDTO.createWithoutPassword(user));
        }
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public String getName() {
        return lobbyName;
    }
}