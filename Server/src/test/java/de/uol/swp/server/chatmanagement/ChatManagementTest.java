package de.uol.swp.server.chatmanagement;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Die Testklasse ChatManagementTest .
 *
 * @author Keno Oelrichs Garcia
 * @since Sprint 5
 */
class ChatManagementTest {

    /**
     * Der neue Chat Member (riecht ein bisschen).
     */
    static final User chatMember = new UserDTO("Keno riecht", "nach Sportstunde", "Keno@OG.com");

    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;
    /**
     * Der Eventbus.
     */
    final EventBus bus = new EventBus();

    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Der Chat Management.
     */
    private final ChatManagement chatManagement = new ChatManagement();

    /**
     * Erstellt den Chat "global" und prüft daraufhin, das der Chat "global" nicht null ist.
     *
     * @author Keno O.
     * @since Sprint 5
     */
    @Test
    void getChat() {
        //Create a global Chat
        if (chatManagement.getChat("global").isEmpty()) chatManagement.createChat("global");
        assertTrue(chatManagement.getChat("global").isPresent());
    }

    /**
     * Erstellt eine Variable, die den Wert eines neu generierten Chats annimmt und prüft, dass dieser nicht null ist.
     *
     * @author Keno O.
     * @since Sprint 2
     */
    @Test
    void createChat() {
        String newChat = chatManagement.createChat();
        assertNotNull(chatManagement.getChat(newChat));
    }

    /**
     * Erstellt einen Chat und speichert dessen Wert in einer Variable und löscht diesen wieder. Pfüfung, ob der Chat Null ist.
     *
     * @author Keno O.
     * @since Sprint 2
     */
    @Test
    void deleteChat() throws InterruptedException {
        String newChat = chatManagement.createChat();
        chatManagement.deleteChat(newChat);
        assertFalse(chatManagement.getChat(newChat).isPresent());
    }

    /**
     * Erstellt einen Chat und fügt diesem Chat eine Nachricht mit Nutzer hinzu. Prüfung, ob der Chat nun die zuvor gesendete Nachricht enthält.
     *
     * @author Keno O.
     * @since Sprint 2
     */
    @Test
    void addMessage() {
        //Create a global Chat
        chatManagement.createChat("global");
        chatManagement.addMessage("global", new ChatMessage(chatMember, "Test"));
        assertEquals("Test", chatManagement.getChat("global").get().getMessages().get(0).getMessage());
    }
}