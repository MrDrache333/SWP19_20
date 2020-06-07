package de.uol.swp.common.lobby.exception;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class JoinLobbyExceptionMessage extends AbstractServerMessage {
    private static final long serialVersionUID = -7911144560778128510L;
    private String message;

    /**
     * Konstruktor der JoinLobbyExceptionMessage
     *
     * @param message die Nachricht
     * @author Darian
     * @since Sprint 8
     */
    public JoinLobbyExceptionMessage(String message) {
        this.message = message;
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
