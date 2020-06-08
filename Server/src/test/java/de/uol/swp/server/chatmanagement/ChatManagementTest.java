package de.uol.swp.server.chatmanagement;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * die Testklasse ChatManagementTest .
 *
 * @author Keno Oelrichs Garcia
 */
class ChatManagementTest {

    /**
     * Der neue Chat Member (riecht ein bisschen).
     */
    static final User chatMember = new UserDTO("Keno riecht", "nach Sportstunde", "Keno@OG.com");

    /**
     * Der Eventbus.
     */
    final EventBus bus = new EventBus();
    /**
     * Der Chat Management.
     */
    private final ChatManagement chatManagement = new ChatManagement();

    /**
     * Erstellt den Chat "global" und prüft daraufhin, das der Chat "global" nicht null ist.
     */
    @Test
    void getChat() {
        //Create a global Chat
        if (chatManagement.getChat("global").isEmpty()) chatManagement.createChat("global");
        assertTrue(chatManagement.getChat("global").isPresent());
    }

    /**
     * Erstellt eine Variable, die den Wert eines neu generierten Chats annimmt und prüft, dass dieser nicht null ist.
     */
    @Test
    void createChat() {
        String newChat = chatManagement.createChat();
        assertNotNull(chatManagement.getChat(newChat));
    }

    /**
     * Erstellt einen Chat und speichert dessen Wert in einer Variable und löscht diesen wieder. Pfüfung, ob der Chat Null ist.
     */
    @Test
    void deleteChat() {
        String newChat = chatManagement.createChat();
        chatManagement.deleteChat(newChat);
        assertFalse(chatManagement.getChat(newChat).isPresent());
    }

    /**
     * Erstellt einen Chat und fügt diesem Chat eine Nachricht mit Nutzer hinzu. Prüfung, ob der Chat nun die zuvor gesendete Nachricht enthält.
     */
    @Test
    void addMessage() {
        //Create a global Chat
        chatManagement.createChat("global");
        chatManagement.addMessage("global", new ChatMessage(chatMember, "Test"));
        assertEquals("Test", chatManagement.getChat("global").get().getMessages().get(0).getMessage());
    }
}