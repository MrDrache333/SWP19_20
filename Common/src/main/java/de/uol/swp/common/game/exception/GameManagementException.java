package de.uol.swp.common.game.exception;

/**
 * Der GameManagment Fehler
 */
public class GameManagementException extends RuntimeException {

    private static final long serialVersionUID = 1878566395694942514L;

    /**
     * Erstellt einen neuen GameManagement-Fehler
     *
     * @param message the message
     */
    public GameManagementException(String message) {
        super(message);
    }
}
