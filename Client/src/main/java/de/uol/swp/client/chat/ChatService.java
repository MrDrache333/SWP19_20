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
 * Der Clientseitige ChatService-Handler.
 */
@SuppressWarnings("UnstableApiUsage")
public class ChatService implements de.uol.swp.common.chat.ChatService {

    private static final Logger LOG = LogManager.getLogger(ChatService.class);
    private final EventBus bus;

    /**
     * Initiert einen Chatservice mit dem Injizierten EventBus
     *
     * @param bus the bus
     */
    @Inject
    public ChatService(EventBus bus) {
        this.bus = bus;
    }

    /**
     * Diese Methode bekommt eine ChatMessage übergeben und erstellt eine NewChatMessageRequest mit der übergebenen ChatMessage.
     * Diese NewChatMessageRequest wird dann auf den EventBus gepackt.
     *
     * @param message ChatMessage mit dem Nachrichten Inhalt.
     */
    @Override
    public void sendMessage(ChatMessage message) {
        NewChatMessageRequest request = new NewChatMessageRequest(message);
        LOG.debug("posting ChatMessageRequest to bus with parameter message");
        bus.post(request);
    }

    /**
     * Diese Methode bekommt eine ChatMessage und ChatID übergeben und erstellt eine NewChatMessageRequest mit der übergebenen ChatMessage und der ChatID.
     * Diese NewChatMessageRequest wird dann auf den EventBus gepackt.
     *
     * @param ChatId  die ChatID der zugehörigen übergebenen Nachricht
     * @param message ChatMessage mit dem Nachrichten Inhalt.
     */
    @Override
    public void sendMessage(String ChatId, ChatMessage message) {
        NewChatMessageRequest request = new NewChatMessageRequest(ChatId, message);
        LOG.debug("posting NewChatMessageRequest to bus with parameters ChatId, message");
        bus.post(request);
    }

    /**
     * Diese Methode fordert eine Chat-History an für den übergebenen User.
     *
     * @param sender Der User, welcher die ChatHistory angefordert hat.
     * @return null, da es nur die Anfrage ist.
     */
    @Override
    public Chat getChatHistory(User sender) {
        ChatHistoryRequest req = new ChatHistoryRequest(sender);
        LOG.debug("posting ChatHistoryRequest to bus with parameter sender");
        bus.post(req);
        return null;
    }

    /**
     * Diese Methode fordert eine Chat-History an für den übergebenen User und der zugehörigen LobbyID.
     *
     * @param sender Der User, welcher die ChatHistory angefordert hat.
     * @param ChatId Die ChatID, für den spezifizierten Chat.
     * @return null, da es nur die Anfrage ist.
     */
    @Override
    public Chat getChatHistory(String ChatId, User sender) {
        ChatHistoryRequest req = new ChatHistoryRequest(ChatId, sender);
        LOG.debug("posting ChatHistoryRequest to bus with parameter ChatId, sender");
        bus.post(req);
        return null;
    }
}
