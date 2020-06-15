package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.exception.ChatException;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Das serverseitige Chat-Management.
 */
public class ChatManagement extends AbstractChatManagement {

    private final static short CHATMESSAGEHISTORYSIZE = 20;

    private final SortedMap<String, Chat> chats = new TreeMap<>();

    /**
     * Gibt den Chat mit Chat ID zurück.
     *
     * @param chatID die Chat ID
     * @return den Chat
     * @throws NullPointerException NullPointerException
     * @author Keno O
     * @since Sprint 1
     */
    public Optional<Chat> getChat(String chatID) {
        try {
            Chat chat = chats.get(chatID);
            return Optional.of(chat);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    /**
     * Erstellt einen neuen Chat.
     *
     * @return die Chat ID
     * @author Keno O
     * @since Sprint 1
     */
    synchronized public String createChat() {
        String id = String.valueOf(UUID.randomUUID());
        chats.put(id, new Chat(id));
        return id;
    }

    /**
     * Chat wird erstellt.
     *
     * @param chatID die Chat ID
     * @throws ChatException die Chat Fehlermeldung
     * @author Keno O
     * @since Sprint 1
     */
    synchronized public void createChat(String chatID) throws ChatException {
        if (getChat(chatID).isPresent()) throw new ChatException("Chat mit der ID " + chatID + " existiert bereits!");
        chats.put(chatID, new Chat(chatID));
    }

    /**
     * Chat nach Chat ID löschen.
     *
     * @param chatID die Chat ID
     * @throws ChatException beim Fehler den Chat zu löschen.
     * @author Keno O
     * @since Sprint 1
     */
    public void deleteChat(String chatID) throws ChatException {
        if (chats.size() > 0 && chats.get(chatID) != null) chats.remove(chatID);
        if (getChat(chatID).isPresent())
            throw new ChatException("Chat mit der ID " + chatID + " konnte nicht entfernt werden!");
    }

    /**
     * Nachricht zu einem Chat hinzufügen.
     *
     * @param chatID  die Chat ID
     * @param message die Nachricht
     * @throws ChatException wenn es den Chat mir der ID nicht gibt.
     * @author Keno O
     * @since Sprint 1
     */
    synchronized public void addMessage(String chatID, ChatMessage message) throws ChatException {
        Optional<Chat> chat = getChat(chatID);
        //CHeck if this is an existing chat
        if (chat.isPresent()) {
            chat.get().getMessages().add(message);
            //Keep only the newest Messages
            if (chat.get().getMessages().size() > CHATMESSAGEHISTORYSIZE) chat.get().getMessages().remove(0);
        } else throw new ChatException("Chat mit der ID " + chatID + " existiert nicht!");
    }

    /**
     * Sendet eine Nachricht an den globalen Chat.
     *
     * @param message the message
     * @author Keno O
     * @since Sprint 1
     */
    @Override
    public void sendMessage(ChatMessage message) {
        addMessage("global", message);
    }

    /**
     * sendet eine Nachricht an Chat mit bestimmter ID.
     *
     * @param chatID  the chat id
     * @param message the message
     * @author Keno O
     * @since Sprint 1
     */
    @Override
    public void sendMessage(String chatID, ChatMessage message) {
        addMessage(chatID, message);
    }
}
