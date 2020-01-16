package de.uol.swp.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type My object decoder.
 *
 * @author Marco Grawunder
 */
public class MyObjectDecoder extends ObjectDecoder {

    private static final Logger LOG = LogManager.getLogger(MyObjectDecoder.class);

    /**
     * Initialisiert einen neuen MyObjectDecoder.
     *
     * @param classResolver Der classresolver
     */
    public MyObjectDecoder(ClassResolver classResolver) {
        super(classResolver);
    }

    /**
     * Mit viel Raten würde ich sagen, dass die Funktion einen Bytebuffer versucht zu dekodieren, um ihn dann an die Superklasse weiterzugeben. lul
     *
     * @param ctx Der ChannelHandlerContext
     * @param in Der ByteBuffer
     * @return Der dekodierte ByteBuffer
     * @throws Exception Wenn beim Dekodieren eine Fehlermeldung auftaucht, wird der Vorgang abgebrochen.
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded;
        try {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Trying to decode " + in);
            }
            decoded = super.decode(ctx, in);
            if (LOG.isTraceEnabled()) {
                LOG.trace(in + " " + decoded);
            }
        } catch (Exception e) {
            LOG.error(e);
            throw e;
        }
        return decoded;
    }
}
