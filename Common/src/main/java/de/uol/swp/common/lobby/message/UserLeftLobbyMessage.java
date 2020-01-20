package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserLeftLobbyMessage extends AbstractLobbyMessage {

    private static final long serialVersionUID = -5546005208705943803L;
    private LobbyDTO lobby;

    public UserLeftLobbyMessage() {
    }

    /**
     * Instanziiert die Message
     *
     * @param lobbyID   Die LobbyID
     * @param lobby     Die Lobby
     * @param lobbyName Der Lobbyname
     * @param user      Der User
     * @author Julia, Darian
     * @since Sprint 2
     */
    public UserLeftLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, LobbyDTO lobby) {
        super(lobbyName, user, lobbyID);
        this.lobby = lobby;
    }

    /**
     * Getter für Lobby
     *
     * @return Lobby
     * @author Julia
     * @since Sprint 3
     */
    public LobbyDTO getLobby() {
        return lobby;
    }
}
