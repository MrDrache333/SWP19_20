package de.uol.swp.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class MyObjectEncoder extends ObjectEncoder {

    private static final Logger LOG = LogManager.getLogger(MyObjectEncoder.class);

    /**
     * Kodiert die Message in den ByteBuffer
     *
     * @param ctx Ein ChannelHandlerContext (Netty)
     * @param msg Die serialisierbare Message
     * @param out Eine Instanz des Netty-Buffers ByteBuf auf den die Message kodiert wird
     * @throws Exception Wirft und loggt Exception e
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Trying to encode " + msg);
        }
        try {
            super.encode(ctx, msg, out);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            throw e;
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(msg + " " + out);
        }
    }
}
