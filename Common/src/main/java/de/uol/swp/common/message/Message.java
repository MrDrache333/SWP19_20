package de.uol.swp.common.message;

import de.uol.swp.common.user.Session;

import java.io.Serializable;
import java.util.Optional;

/**
 * Base interface of all messages
 *
 * @author Marco Grawunder
 */

public interface Message extends Serializable {

    /**
     * Retrieve the current message context
     *
     * @return
     */
    Optional<MessageContext> getMessageContext();

    /**
     * Allows to set a MessageContext, e.g. for network purposes
     *
     * @param messageContext
     */
    void setMessageContext(MessageContext messageContext);

    /**
     * Retrieve current session
     *
     * @return
     */
    Optional<Session> getSession();

    /**
     * Set the current session
     *
     * @param session
     */
    void setSession(Session session);

    /**
     * Allow to create a new message, based on
     * the given one (copy)
     *
     * @param otherMessage
     */
    void initWithMessage(Message otherMessage);
}
