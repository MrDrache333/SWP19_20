package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

import java.util.UUID;

public class UserLeftLobbyMessage extends AbstractLobbyMessage {

    public UserLeftLobbyMessage() {
    }

    public UserLeftLobbyMessage(String lobbyName, User user, UUID lobbyID) {
        super(lobbyName, user, lobbyID);
    }
}
