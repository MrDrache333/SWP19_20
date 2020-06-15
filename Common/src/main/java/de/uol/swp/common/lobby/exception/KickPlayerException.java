package de.uol.swp.common.lobby.exception;

public class KickPlayerException extends RuntimeException {
    private static final long serialVersionUID = 3260958977583132518L;

    /**
     * Erstellt einen neuen KickPlayer-Fehler
     *
     * @param message die Fehlernachricht
     * @author Darian
     */
    public KickPlayerException(String message) {
        super(message);
    }
}
