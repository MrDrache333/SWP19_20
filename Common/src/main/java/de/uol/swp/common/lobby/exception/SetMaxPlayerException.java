package de.uol.swp.common.lobby.exception;

public class SetMaxPlayerException extends RuntimeException{
    private static final long serialVersionUID = 2358921266197658614L;

    /**
     * Erstellt einen neuen LeaveLobby-Fehler
     *
     * @param message die Fehlernachricht
     * @author Darian
     */
    public SetMaxPlayerException(String message) {
        super(message);
    }
}

