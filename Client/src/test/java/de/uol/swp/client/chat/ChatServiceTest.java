package de.uol.swp.client.chat;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.request.NewChatMessageRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Der Chat Service Test
 *
 * @author Anna
 * @since Sprint 10
 */
class ChatServiceTest {

    /**
     * Standard Benutzer, Nachricht und Chat-ID zum Testen
     *
     * @author Anna
     * @since Sprint 10
     */
    User defaultUser = new UserDTO("Marco", "test", "marco@test.de");
    String message = "Testnachricht";
    String chatID = "chatID";

    /**
     * Der zu verwendete EventBus
     *
     * @author Anna
     * @since Sprint 10
     */
    EventBus bus = new EventBus();
    CountDownLatch lock = new CountDownLatch(1);
    Object event;

    /**
     * Methode zum Behandeln von auf dem Bus aufgetretene Dead-Events.
     *
     * @param e Das aufgetretene Dead-Event
     * @author Anna
     * @since Sprint 10
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Eventbus initialisieren
     *
     * @author Anna
     * @since Sprint 10
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Klasse vom EventBus deregistrieren
     *
     * @author Anna
     * @since Sprint 10
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Hilfsmethode zum Schicken einer Chatnachricht.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void sendMessage() throws InterruptedException {
        ChatService chatService = new ChatService(bus);
        chatService.sendMessage(new ChatMessage(defaultUser, message));
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Chatnachricht mit einer bestimmten Chat-ID
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void sendMessageWithID() throws InterruptedException {
        ChatService chatService = new ChatService(bus);
        chatService.sendMessage(chatID, new ChatMessage(defaultUser, message));
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Versuch eine Nachricht zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void sendMessageTest() throws InterruptedException {
        sendMessage();
        assertTrue(event instanceof NewChatMessageRequest);
        NewChatMessageRequest chatMessage = (NewChatMessageRequest) event;
        assertEquals(message, chatMessage.getMessage().getMessage());
        assertEquals(defaultUser, chatMessage.getMessage().getSender());
    }

    /**
     * Versuch eine Nachricht mit eienr bestimmten Chat-ID zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void sendMessageWithIDTest() throws InterruptedException {
        sendMessageWithID();
        assertTrue(event instanceof NewChatMessageRequest);
        NewChatMessageRequest chatMessage = (NewChatMessageRequest) event;
        assertEquals(message, chatMessage.getMessage().getMessage());
        assertEquals(defaultUser, chatMessage.getMessage().getSender());
        assertEquals(chatID, chatMessage.getChatid());
    }
}
