package de.uol.swp.common.chat.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

/**
 * The type Chat history request.
 */
public class ChatHistoryRequest extends AbstractRequestMessage {

    private String ChatId;
    private User Sender;

    /**
     * Instantiates a new Chat history request.
     *
     * @param chatId the chat id
     * @param sender the sender
     */
    public ChatHistoryRequest(String chatId, User sender) {
        this.ChatId = chatId;
        this.Sender = sender;
    }

    /**
     * Instantiates a new Chat history request.
     *
     * @param sender the sender
     */
    public ChatHistoryRequest(User sender) {
        this.ChatId = "global";
        this.Sender = sender;
    }


    /**
     * Gets ChatId.
     *
     * @return Value of ChatId.
     */
    public String getChatId() {
        return ChatId;
    }

    /**
     * Gets sender.
     *
     * @return the sender
     */
    public User getSender() {
        return Sender;
    }
}
