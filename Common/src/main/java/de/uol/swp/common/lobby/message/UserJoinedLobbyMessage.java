package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    UserDTO gameOwner;

    public UserJoinedLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, UserDTO gameOwner) {
        super(lobbyName, user, lobbyID);
        this.gameOwner = gameOwner;
    }

    public UserDTO getGameOwner() {
        return gameOwner;
    }
}
