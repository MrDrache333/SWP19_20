package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class CreateLobbyMessage extends AbstractLobbyMessage {
    private String lobbyName;
    private User user;
    private UUID ChatID;

    private Lobby lobby;

    public CreateLobbyMessage() {
    }

    public CreateLobbyMessage(String lobbyName, User user, UUID ChatID) {
        this.lobbyName = lobbyName;
        this.user = user;
        this.ChatID = ChatID;
    }

    public CreateLobbyMessage(Lobby lobby) {
        this.lobby = lobby;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setName(String lobbyName) {
        this.lobbyName = lobbyName;
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