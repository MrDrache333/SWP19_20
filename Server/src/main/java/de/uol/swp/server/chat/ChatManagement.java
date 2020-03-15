package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.user.User;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Das serverseitige Chat-Management.
 */
public class ChatManagement extends AbstractChatManagement {

    private final short CHATMESSAGEHISTORYSIZE = 20;

    private SortedMap<String, Chat> Chats = new TreeMap<>();

    /**
     * Gibt den Chat mit Chat ID zurück.
     *
     * @param chatId die Chat ID
     * @return den Chat
     * @throws NullPointerException
     * @author Keno O
     * @since Sprint 1
     */
    public Optional<Chat> getChat(String chatId) {
        try {
            Chat chat = Chats.get(chatId);
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
        Chats.put(id, new Chat(id));
        return id;
    }

    /**
     * Chat wird erstellt.
     *
     * @param ChatId die Chat ID
     * @throws ChatException die Chat Fehlermeldung
     * @author Keno O
     * @since Sprint 1
     */
    synchronized public void createChat(String ChatId) throws ChatException {
        if (getChat(ChatId).isPresent()) throw new ChatException("Chat with Id " + ChatId + " already exists!");
        Chats.put(ChatId, new Chat(ChatId));
    }

    /**
     * Chat nach Chat ID löschen.
     *
     * @param ChatId die Chat ID
     * @throws ChatException beim Fehler den Chat zu löschen.
     * @author Keno O
     * @since Sprint 1
     */
    public void deleteChat(String ChatId) throws ChatException {
        if (Chats.size() > 0 && Chats.get(ChatId) != null) Chats.remove(ChatId);
        if (getChat(ChatId).isPresent()) throw new ChatException("Chat with Id " + ChatId + " failed to remove!");
    }

    /**
     * Nachricht zu einem Chat hinzufügen.
     *
     * @param chatId  die Chat ID
     * @param message die Nachricht
     * @throws ChatException wenn es den Chat mir der ID nicht gibt.
     * @author Keno O
     * @since Sprint 1
     */
    synchronized public void addMessage(String chatId, ChatMessage message) throws ChatException {
        Optional<Chat> chat = getChat(chatId);
        //CHeck if this is an existing chat
        if (chat.isPresent()) {
            chat.get().getMessages().add(message);
            //Keep only the newest Messages
            if (chat.get().getMessages().size() > CHATMESSAGEHISTORYSIZE) chat.get().getMessages().remove(0);
        } else throw new ChatException("Chat with Id " + chatId + " does not exist!");
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
     * @param ChatId  the chat id
     * @param message the message
     * @author Keno O
     * @since Sprint 1
     */
    @Override
    public void sendMessage(String ChatId, ChatMessage message) {
        addMessage(ChatId, message);
    }

    /**
     * Gibt den Chatverlauf des globalen Chats zurück.
     *
     * @param sender der Benutzer
     * @return der Chatverlauf
     * @author Keno O
     * @since Sprint 1
     */
    @Override
    public Chat getChatHistory(User sender) {
        return getChatHistory(sender);
    }

    /**
     * Gibt den Chatverlauf eines bestimmten Chats zurück.
     *
     * @param ChatId the chat id
     * @param sender der Benutzer
     * @return der Chatverlauf
     * @author Keno O
     * @since Sprint 1
     */
    @Override
    public Chat getChatHistory(String ChatId, User sender) {
        return getChatHistory(ChatId, sender);
    }
}
