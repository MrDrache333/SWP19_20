package de.uol.swp.common.lobby.request;

import java.util.UUID;

public class AddBotRequest extends AbstractLobbyRequest {
    public AddBotRequest(UUID lobbyID) {
        super(lobbyID);
    }
}
