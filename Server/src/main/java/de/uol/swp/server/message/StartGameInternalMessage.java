package de.uol.swp.server.message;

import java.util.UUID;

public class StartGameInternalMessage extends AbstractServerInternalMessage {
    private UUID lobbyID;

    public StartGameInternalMessage(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }


    public UUID getLobbyID() {
        return lobbyID;
    }
}
