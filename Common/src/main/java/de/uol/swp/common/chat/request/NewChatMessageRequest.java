package de.uol.swp.common.chat.request;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * The type New chat message request.
 */
public class NewChatMessageRequest extends AbstractRequestMessage {

    private String ChatId;
    private ChatMessage Message;

    /**
     * Instantiates a new New chat message request.
     *
     * @param message the message
     */
    public NewChatMessageRequest(ChatMessage message) {
        this.ChatId = "global";
        this.Message = message;
    }

    /**
     * Instantiates a new New chat message request.
     *
     * @param chatId  the chatid
     * @param message the message
     */
    public NewChatMessageRequest(String chatId, ChatMessage message) {
        this.Message = message;
        this.ChatId = chatId;
    }

    /**
     * Gets chatid.
     *
     * @return the chatid
     */
    public String getChatid() {
        return ChatId;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public ChatMessage getMessage() {
        return Message;
    }
}
