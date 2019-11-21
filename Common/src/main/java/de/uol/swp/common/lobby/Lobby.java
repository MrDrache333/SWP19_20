package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;

import java.util.Set;
import java.util.UUID;

public interface Lobby {

    String getName();

    void updateOwner(User user);

    User getOwner();

    void joinUser(User user);

    void leaveUser(User user);

    Set<User> getUsers();

    UUID getLobbyID();

    void setLobbyID(UUID lobbyID);

}
