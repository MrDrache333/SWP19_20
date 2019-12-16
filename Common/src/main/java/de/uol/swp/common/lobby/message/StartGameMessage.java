package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import java.util.UUID;

public class StartGameMessage extends AbstractServerMessage {

    private String lobbyName;
    private UUID lobbyID;

    public StartGameMessage(String lobbyName, UUID lobbyID) {
        this.lobbyName = lobbyName;
        this.lobbyID = lobbyID;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }
}
