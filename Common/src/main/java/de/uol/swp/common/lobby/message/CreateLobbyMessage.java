package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.dto.UserDTO;
import java.util.UUID;

public class CreateLobbyMessage extends AbstractLobbyMessage {
    private String lobbyName;
    private UserDTO user;
    private UUID ChatID;

    private Lobby lobby;

    public CreateLobbyMessage() {}

    public CreateLobbyMessage(String lobbyName, UserDTO user, UUID ChatID) {
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UUID getChatID() {
        return ChatID;
    }

    public void setChatID(UUID chatID) {
        ChatID = chatID;
    }
}