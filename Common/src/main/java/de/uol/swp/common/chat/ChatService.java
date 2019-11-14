package de.uol.swp.common.chat;

import de.uol.swp.common.user.User;

/**
 * The interface Chat service.
 */
public interface ChatService {

    /**
     * Send message.
     *
     * @param message the message
     */
    void sendMessage(ChatMessage message);

    /**
     * Send message.
     *
     * @param ChatId  the chat id
     * @param message the message
     */
    void sendMessage(String ChatId, ChatMessage message);

    /**
     * Gets chat history.
     */
    void getChatHistory(User sender);

    /**
     * Gets chat history.
     *
     * @param ChatId the chat id
     */
    void getChatHistory(String ChatId, User sender);
}
