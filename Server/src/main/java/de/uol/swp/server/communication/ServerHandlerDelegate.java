package de.uol.swp.server.communication;

import de.uol.swp.common.message.RequestMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Ein Interface zum Entkoppeln des ServerHandlers und der ServerApplication.
 *
 * @author Marco Grawunder
 */

interface ServerHandlerDelegate {

    /**
     * Wird aufgerufen wenn sich ein neuer Client verbindet.
     *
     * @param ctx Der ChannelHandlerContext für den Client
     */
    void newClientConnected(ChannelHandlerContext ctx);

    /**
     * Wird aufgerufen wenn ein Client die Verbindung trennt.
     *
     * @param ctx Der ChannelHandlerContext für den Client
     */
    void clientDisconnected(ChannelHandlerContext ctx);

    /**
     * Eine Nachricht von einem verbundenem Client mit dem ChannelHandlerContext ctx wurde empfangen und kann weiterverarbeitet werden.
     *
     * @param ctx Der ChannelHandlerContext für diese Verbindung (Identifiziert den Client)
     * @param msg Die message gesendet von dem Client
     */
    void process(ChannelHandlerContext ctx, RequestMessage msg);

}
