package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class CreateLobbyMessage extends AbstractLobbyMessage {
    private String lobbyName;
    private UserDTO user;
    private UUID chatID;

    private Lobby lobby;

    public CreateLobbyMessage() {
    }

    public CreateLobbyMessage(String lobbyName, UserDTO user, UUID chatID) {
        this.lobbyName = lobbyName;
        this.user = user;
        this.chatID = chatID;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UUID getChatID() {
        return chatID;
    }

    public void setChatID(UUID chatID) {
        chatID = chatID;
    }
}