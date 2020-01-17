package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Nachrricht welche beim Erstellen einer Lobby gesendet wird.
 */
public class CreateLobbyMessage extends AbstractLobbyMessage {

    private String lobbyName;
    private String lobbyPassword;
    private UUID chatID;
    private UserDTO user;
    private LobbyDTO lobby;

    public CreateLobbyMessage() {
    }

    /**
     * Konstruktor einer CreateLobbyMessage.
     *
     * @param lobbyName Der Name der Lobby
     * @param lobbyPassword Das Lobby Passwort
     * @param user Der User
     * @param ChatID die UUID des Chats der Lobby
     * @param lobby Das Objekt der Lobby
     * @author Paula, Rike, Darian
     */
    public CreateLobbyMessage(String lobbyName, String lobbyPassword, UserDTO user, UUID ChatID, LobbyDTO lobby) {
        this.lobbyName = lobbyName;
        this.user = user;
        this.chatID = ChatID;
        this.lobbyPassword = lobbyPassword;
        this.lobby = lobby;
    }

    /**
     * Gibt den Namen der Lobby zurück.
     *
     * @return der Lobbyname
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Gibt das UserDTO Objekt zurück.
     *
     * @return das UserDTO Objekt
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Gibt die UUID des Chats zurück.
     *
     * @return chat UUID
     */
    public UUID getChatID() {
        return chatID;
    }

    /**
     * Gibt die Lobby zurück.
     *
     * @return LobbyDTO der Lobby
     */
    public LobbyDTO getLobby() {
        return lobby;
    }

    /**
     * Gibt das Lobby Passwort zurück.
     *
     * @return Das Passwort
     */
    public String getLobbyPassword() {
        return lobbyPassword;
    }
}