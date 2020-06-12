package de.uol.swp.common.chat.response;

import de.uol.swp.common.chat.Chat;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.message.ResponseMessage;

/**
 * Die ChatResponseMessage vom Server an den Client, wenn der Client eine NewChatMessage zum Server geschickt hat.
 */
public class ChatResponseMessage extends AbstractResponseMessage implements ResponseMessage {

    private static final long serialVersionUID = -7034555716301881050L;

    private final Chat chat;
    private final String sender;

    /**
     * Initiierung einer ChatResponseMessage mit dem übergebenen Chat und dem Namen des Senders.
     *
     * @param chat   der übergebene Chat
     * @param sender der Username an den die ChatResponseMessage gehen soll
     */
    public ChatResponseMessage(Chat chat, String sender) {
        this.chat = chat;
        this.sender = sender;
    }

    /**
     * Getter um den Chat zurückzugeben
     *
     * @return den Chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Getter um den Sender zurückzugeben
     *
     * @return den Sender
     */
    public String getSender() {
        return sender;
    }
}
