package de.uol.swp.common.lobby.response;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AllOnlineUsersInLobbyResponse extends AbstractResponseMessage {

    final private ArrayList<User> users = new ArrayList<>();

    AllOnlineUsersInLobbyResponse (Collection<User> users) {
        for (User user : users) {
            this.users.add(user);
        }
    }
    public List<User> getUsers() {
        return users;
    }


}
