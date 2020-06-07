package de.uol.swp.common.lobby.exception;

public class LeaveLobbyException extends RuntimeException {
    private static final long serialVersionUID = 734370128259446556L;

    /**
     * Erstellt einen neuen LeaveLobby-Fehler
     *
     * @param message die Fehlernachricht
     * @author Darian
     */
    public LeaveLobbyException(String message) {
        super(message);
    }
}