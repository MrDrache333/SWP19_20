package de.uol.swp.server.chat;

import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.exception.ChatException;
import de.uol.swp.common.user.User;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

/**
 * The type Chat management.
 */
public class ChatManagement extends AbstractChatManagement {

    private static final short CHATMESSAGEHISTORYSIZE = 20;

    private static SortedMap<String, Chat> Chats = new TreeMap<>();

    /**
     * Get the Chat with chatId.
     *
     * @param chatId the chat id
     * @return the chat
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
     * Create a new Chat.
     *
     * @return the ChatId
     */
    synchronized public String createChat() {
        String id = String.valueOf(UUID.randomUUID());
        Chats.put(id, new Chat(id));
        return id;
    }

    /**
     * Create chat.
     *
     * @param ChatId the chat id
     * @throws ChatException the chat exception
     */
    synchronized public void createChat(String ChatId) throws ChatException {
        if (getChat(ChatId).isPresent()) throw new ChatException("Chat with Id " + ChatId + " already exists!");
        Chats.put(ChatId, new Chat(ChatId));
    }

    /**
     * Delete chat by given ChatId.
     *
     * @param ChatId the chat id
     */
    public void deleteChat(String ChatId) throws ChatException {
        if (Chats.size() > 0 && Chats.get(ChatId) != null) Chats.remove(ChatId);
        if (getChat(ChatId).isPresent()) throw new ChatException("Chat with Id " + ChatId + " failed to remove!");
    }

    /**
     * Add message.
     *
     * @param chatId  the chat id
     * @param message the message
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

    @Override
    public void sendMessage(ChatMessage message) {
        addMessage("global", message);
    }

    @Override
    public void sendMessage(String ChatId, ChatMessage message) {
        addMessage(ChatId, message);
    }

    @Override
    public Chat getChatHistory(User sender) {
        return getChatHistory(sender);
    }

    @Override
    public Chat getChatHistory(String ChatId, User sender) {
        return getChatHistory(ChatId, sender);
    }
}
