package de.uol.swp.common.message;

/**
 * Ein Basis Interface für alle Nachrichten vom Client zum Server.
 *
 * @author: Marco
 * @since Start
 */

public interface RequestMessage extends Message {

    /**
     * Gibt an ob diese Anfrage nur benutzt werden kann, wenn der Nutzer authorisiert ist
     * (hat typischerweise den Wert auth)
     */
    boolean authorizationNeeded();

}
