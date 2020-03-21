package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class GameExceptionMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -8889082861061823738L;
    private UUID gameID;
    private String message;

    public GameExceptionMessage(UUID gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }

    public UUID getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }
}
