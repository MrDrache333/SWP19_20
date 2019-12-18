package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    public UserJoinedLobbyMessage() {
    }

    public UserJoinedLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID) {
        super(lobbyName, user, lobbyID);
    }
}
