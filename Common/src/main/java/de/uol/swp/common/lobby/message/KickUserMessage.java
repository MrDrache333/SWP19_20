package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class KickUserMessage extends AbstractLobbyMessage {

    public KickUserMessage(String lobbyName, UserDTO userToKick, UUID lobbyID) {
        super(lobbyName, userToKick, lobbyID);
    }
}
