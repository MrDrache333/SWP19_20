package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

import java.util.UUID;

public class UserJoinedLobbyMessage extends AbstractLobbyMessage {


    public UserJoinedLobbyMessage() {
    }

    public UserJoinedLobbyMessage(String lobbyName, User user, UUID lobbyID) {
        super(lobbyName, user, lobbyID);
    }
}
