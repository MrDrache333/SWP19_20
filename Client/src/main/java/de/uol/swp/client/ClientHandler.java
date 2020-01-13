package de.uol.swp.client;

import de.uol.swp.common.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Netty Handler für eingehende Verbindungen
 *
 * @author Marco Grawunder
 */
class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);

    private final ClientConnection clientConnection;

    /**
     * Instanziiert einen neuen ClientHandler
     *
     * @param clientConnection die clientConnection
     */
    ClientHandler(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }

    /**
     * Ruft in der ClientConnection die Methode fireConnectionEstablished mit dem Channel,
     * der an den übergebenen ChannelHandlerContext gebunden ist, auf
     *
     * @param ctx der ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOG.debug("Connected to server " + ctx);
        clientConnection.fireConnectionEstablished(ctx.channel());
    }

    /**
     * Wenn es sich bei dem übergebenen Objekt um eine Message handelt, wird es zu einer Message gecastet und
     * damit in der ClientConnection die Methode receivedMessage aufgerufen
     * Andernfalls wird eine Fehlermeldung ausgegeben
     *
     * @param ctx der ChannelHandlerContext
     * @param in  das eingehende Objekt
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object in) {
        if (in instanceof Message) {
            clientConnection.receivedMessage((Message) in);
        } else {
            LOG.error("Illegal Object read from channel. Ignored!");
        }
    }

    /**
     * Gibt bei einer gefangenen Exception die Ursache aus, ruft damit in der clientConnection die Methode process auf
     * und schließt den Channel
     *
     * @param ctx   der ChannelHandlerContext
     * @param cause die Fehlerursache
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error(cause);
        clientConnection.process(cause);
        ctx.close();
    }
}
