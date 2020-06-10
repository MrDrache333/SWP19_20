package de.uol.swp.common.lobby.exception;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class LobbyExceptionMessage extends AbstractServerMessage {
    private static final long serialVersionUID = 4391739520074356159L;
    private final UUID lobbyID;
    private final String message;

    /**
     * Konstruktor der LobbyExceptionMessage
     *
     * @param lobbyID  die Game-ID
     * @param message die Nachricht
     * @author Darian
     * @since Sprint 8
     */
    public LobbyExceptionMessage(UUID lobbyID, String message) {
        this.lobbyID = lobbyID;
        this.message = message;
    }

    /**
     * Gibt die lobbyID zurück
     *
     * @return lobbyID die LobbyID
     * @author Darian
     * @since Sprint 8
     */
    public UUID getlobbyID() {
        return lobbyID;
    }

    /**
     * Gibt die Nachricht zurück
     *
     * @return message die Nachricht
     * @author Darian
     * @since Sprint 8
     */
    public String getMessage() {
        return message;
    }
}
