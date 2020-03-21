package de.uol.swp.server.message;

import java.util.UUID;

public class StartGameInternalMessage extends AbstractServerInternalMessage {
    public UUID getLobbyID() {
        return lobbyID;
    }

    private UUID lobbyID;

    public StartGameInternalMessage(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }
}
