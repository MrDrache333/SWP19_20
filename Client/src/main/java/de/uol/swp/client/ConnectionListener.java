package de.uol.swp.client;

import io.netty.channel.Channel;

public interface ConnectionListener {

    /**
     * Wird aufgerufen, wenn die Verbindung zum Server hergestellt wurde
     *
     * @param channel Der Channel der Verbindung
     * @author Marco
     * @since Start
     */
    void connectionEstablished(Channel channel);

    /**
     * Wenn der Server eine Ausnahme sendet, wird diese Methode aufgerufen
     *
     * @param cause Der Grund derException
     * @author Marco
     * @since Start
     */
    void exceptionOccurred(String cause);

}
