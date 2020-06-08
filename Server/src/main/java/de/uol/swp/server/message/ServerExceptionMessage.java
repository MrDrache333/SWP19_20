package de.uol.swp.server.message;

/**
 * Die ServerExceptionMessage
 *
 * @author Marco
 * @since Start
 */
public class ServerExceptionMessage extends AbstractServerInternalMessage {

    private final Exception e;

    /**
     * Instanziiert eine ServerExceptionMessage
     *
     * @param e die Exception
     */
    public ServerExceptionMessage(Exception e) {
        super();
        this.e = e;
    }

    /**
     * Gibt die Exception zurück
     *
     * @return die Exception
     */
    public Exception getException() {
        return e;
    }
}
