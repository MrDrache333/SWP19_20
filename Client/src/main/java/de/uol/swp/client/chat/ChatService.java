package de.uol.swp.client.chat;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.chat.Chat;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.request.ChatHistoryRequest;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Chat service.
 */
@SuppressWarnings("UnstableApiUsage")
public class ChatService implements de.uol.swp.common.chat.ChatService {

    private static final Logger LOG = LogManager.getLogger(ChatService.class);
    private final EventBus bus;

    /**
     * Instantiates a new Chat service.
     *
     * @param bus the bus
     */
    @Inject
    public ChatService(EventBus bus) {
        this.bus = bus;
    }

    /**
     *
     * Posts a New Message Request to the bus
     *
     * @param message the message
     */
    @Override
    public void sendMessage(ChatMessage message) {
        NewChatMessageRequest request = new NewChatMessageRequest(message);
        LOG.debug("posting ChatMessageRequest to bus with parameter message");
        bus.post(request);
    }

    /**
     *
     * Posts a New Message Request to the bus for specified ChatID
     *
     * @param ChatId  the chat id of any Specific ChatID
     * @param message the message
     */
    @Override
    public void sendMessage(String ChatId, ChatMessage message) {
        NewChatMessageRequest request = new NewChatMessageRequest(ChatId, message);
        LOG.debug("posting NewChatMessageRequest to bus with parameters ChatId, message");
        bus.post(request);
    }

    @Override
    public Chat getChatHistory(User sender) {
        ChatHistoryRequest req = new ChatHistoryRequest(sender);
        LOG.debug("posting ChatHistoryRequest to bus with parameter sender");
        bus.post(req);
        return null;
    }

    @Override
    public Chat getChatHistory(String ChatId, User sender) {
        ChatHistoryRequest req = new ChatHistoryRequest(ChatId, sender);
        LOG.debug("posting ChatHistoryRequest to bus with parameter ChatId, sender");
        bus.post(req);
        return null;
    }
}
