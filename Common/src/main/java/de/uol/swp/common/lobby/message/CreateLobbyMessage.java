package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class CreateLobbyMessage extends AbstractLobbyMessage {
    private String name;
    private User user;
    private UUID ChatID;

    private Lobby lobby;

    public CreateLobbyMessage() {
    }

    public CreateLobbyMessage(String name, User user, UUID ChatID) {
        this.name = name;
        this.user = user;
        this.ChatID = ChatID;
    }

    public CreateLobbyMessage(Lobby lobby) {
        this.lobby = lobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public UUID getChatID() {
        return ChatID;
    }

    public void setChatID(UUID chatID) {
        ChatID = chatID;
    }
}