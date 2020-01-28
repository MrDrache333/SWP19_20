package de.uol.swp.common.exception;

/**
 * Exception um z.B. darauf hinzuweisen, dass ein auth benötigt wird.
 *
 * @author Marco Grawunder
 * @since Start
 */
public class SecurityException extends RuntimeException {

    private static final long serialVersionUID = -6908340347082873591L;

    /**
     * Instanziiert eine neue SecurityException.
     *
     * @param message die Nachricht
     */
    public SecurityException(String message) {
        super(message);
    }

}
