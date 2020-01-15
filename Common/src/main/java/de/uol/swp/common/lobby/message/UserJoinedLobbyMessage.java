package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Die Klasse UserJoinedLobbyMessage.
 */
public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    private LobbyDTO lobby;

    /**
     * Initialisiert eine neue UserJoinedLobbyMessage.
     *
     * @param lobbyName Der Lobbyname
     * @param user      Der User
     * @param lobbyID   Die Lobby ID
     * @param lobby     Die Lobby
     * @author Julia Debkowski
     * @since Sprint1
     */

    public UserJoinedLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, LobbyDTO lobby) {
        super(lobbyName, user, lobbyID);
        this.lobby = lobby;
    }

    /**
     * Gibt die aktuelle Lobby zurück.
     *
     * @return Die Lobby
     */
    public LobbyDTO getLobby() { return lobby; }
}
