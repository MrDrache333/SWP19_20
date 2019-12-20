package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class KickUserMessage extends AbstractLobbyMessage {

    public KickUserMessage(String lobbyName, UserDTO user, UUID lobbyID) {
        super(lobbyName, user, lobbyID);
    }

}
