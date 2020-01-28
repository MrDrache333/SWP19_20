package de.uol.swp.common.message;

/**
 * Kapselt eine Exception in einem Message-Objekt
 *
 * @author Marco
 * @since Start
 */
public class ExceptionMessage extends AbstractResponseMessage {

    private static final long serialVersionUID = -7739395567707525535L;
    private final String exception;

    /**
     * Instanziiert eine ExceptionMessage
     *
     * @param message die Nachricht
     */
    public ExceptionMessage(String message) {
        this.exception = message;
    }

    /**
     * Gibt die ExceptionMessage zurück
     *
     * @return die Message der Exception als String
     */
    public String getException() {
        return exception;
    }

}
