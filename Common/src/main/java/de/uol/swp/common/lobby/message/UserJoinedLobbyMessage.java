package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    public UserJoinedLobbyMessage() {
    }

    public UserJoinedLobbyMessage(String lobbyName, User user) {
        super(lobbyName, user);
    }
}
