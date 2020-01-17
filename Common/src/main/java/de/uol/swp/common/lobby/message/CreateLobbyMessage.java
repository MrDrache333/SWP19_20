package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class CreateLobbyMessage extends AbstractLobbyMessage {
    private String lobbyName;
    private UserDTO user;
    private UUID chatID;
    private String lobbyPassword;

    private LobbyDTO lobby;

    public CreateLobbyMessage() {
    }

    public CreateLobbyMessage(String lobbyName, String lobbyPassword, UserDTO user, UUID chatID, LobbyDTO lobby) {
        this.lobbyName = lobbyName;
        this.user = user;
        this.chatID = chatID;
        this.lobbyPassword = lobbyPassword;
        this.chatID = chatID;
        this.lobby = lobby;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public UserDTO getUser() {
        return user;
    }

    public UUID getChatID() {
        return chatID;
    }
    public LobbyDTO getLobby() { return lobby; }

    public String getLobbyPassword() {
        return lobbyPassword;
    }

    public void setChatID(UUID chatID) {
        chatID = chatID;
    }
}