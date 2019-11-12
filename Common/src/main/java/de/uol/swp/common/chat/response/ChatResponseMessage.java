package de.uol.swp.common.chat.response;

import de.uol.swp.common.chat.Chat;
import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.message.ResponseMessage;

/**
 * The type Chat response message.
 */
public class ChatResponseMessage extends AbstractResponseMessage implements ResponseMessage {

    private Chat chat;

    /**
     * Instantiates a new Chat response message.
     */
    public ChatResponseMessage() {
    }

    /**
     * Instantiates a new Chat response message.
     *
     * @param chat the chat
     */
    public ChatResponseMessage(Chat chat) {
        this.chat = chat;
    }


    /**
     * Gets chat.
     *
     * @return Value of chat.
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Sets new chat.
     *
     * @param chat New value of chat.
     */
    public void setChat(Chat chat) {
        this.chat = chat;
    }
}
