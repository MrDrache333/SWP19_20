package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class GameExceptionMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -8889082861061823738L;
    private UUID gameID;
    private String message;

    /**
     * Konstruktor der GameExceptionMessage
     *
     * @param gameID  die Game-ID
     * @param message die Nachricht
     * @author Julia
     * @since Sprint 6
     */
    public GameExceptionMessage(UUID gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Julia
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt die Nachricht zurück
     *
     * @return message die Nachricht
     * @author Julia
     * @since Sprint 6
     */
    public String getMessage() {
        return message;
    }
}
