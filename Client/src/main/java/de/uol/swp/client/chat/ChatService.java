package de.uol.swp.client.chat;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
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
        LOG.debug("ChatMessageRequest mit Parameternachricht an den Bus gesendet");
        bus.post(request);
    }

    /**
     * Diese Methode bekommt eine ChatMessage und ChatID übergeben und erstellt eine NewChatMessageRequest mit der übergebenen ChatMessage und der ChatID.
     * Diese NewChatMessageRequest wird dann auf den EventBus gepackt.
     *
     * @param chatID  die ChatID der zugehörigen übergebenen Nachricht
     * @param message ChatMessage mit dem Nachrichten Inhalt.
     */
    @Override
    public void sendMessage(String chatID, ChatMessage message) {
        NewChatMessageRequest request = new NewChatMessageRequest(chatID, message);
        LOG.debug("NewChatMessageRequest an den Bus mit den Parametern ChatId, message gesendet");
        bus.post(request);
    }
}
