package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Subscriber: client.game.GameViewPresenter.userList
 *
 * @author Marvin
 * @since Sprint3
 */

public class AllLobbyUsersResponse extends AbstractResponseMessage {

    final private ArrayList<UserDTO> users = new ArrayList<>();
    private String lobbyName;
    private Lobby lobby;

    public AllLobbyUsersResponse() {
        // needed for serialization
    }

    public AllLobbyUsersResponse(Collection<User> users, String lobbyName) {
        this.lobbyName = lobbyName;
        for (User user : users) {
            this.users.add(UserDTO.createWithoutPassword(user));
        }
    }

    public AllLobbyUsersResponse(Collection<User> users, String lobbyName, Lobby lobby) {
        this.lobbyName = lobbyName;
        for (User user : users) {
            this.users.add(UserDTO.createWithoutPassword(user));
        }
        this.lobby = lobby;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public String getName() {
        return lobbyName;
    }

    public Optional<Lobby> getLobby() {
        return lobby != null ? Optional.of(lobby) : Optional.empty();
    }
}