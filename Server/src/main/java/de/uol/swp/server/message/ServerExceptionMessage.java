package de.uol.swp.server.message;

public class ServerExceptionMessage extends AbstractServerInternalMessage {

    private final Exception e;

    /**
     * Instanziiert eine neue ServerExceptionMessage
     *
     * @param e die Exception
     * @author Marco
     * @since Sprint0
     */
    public ServerExceptionMessage(Exception e) {
        super();
        this.e = e;
    }

    /**
     * Gibt die Exception zurück
     *
     * @return die Exception
     * @author Marco
     * @since Sprint0
     */
    public Exception getException() {
        return e;
    }
}
