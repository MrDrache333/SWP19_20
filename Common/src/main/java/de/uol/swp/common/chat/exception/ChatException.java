package de.uol.swp.common.chat.exception;

public class ChatException extends RuntimeException {

    /**
     * Definiert eine ChatException, die einen String bekommt und diese als Nachricht an RuntimeException weitergibt.
     *
     * @param constructor
     * @author Keno O.
     * @since Sprint2
     */
    public ChatException(String message) {
        super(message);
    }
}
