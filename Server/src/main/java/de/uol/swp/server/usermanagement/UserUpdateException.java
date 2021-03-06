package de.uol.swp.server.usermanagement;

/**
 * Eine Exception, die beim Updaten eines Nutzers benutzt wird.
 *
 * @author Juli
 * @since Sprint 2
 */
public class UserUpdateException extends RuntimeException {
    /**
     * Der Konstruktor bekommt einen String übergeben, den er als Nachricht an die RuntimeException weitergibt.
     *
     * @author Julia
     * @since Sprint 2
     */
    public UserUpdateException(String message) {
        super(message);
    }
}
