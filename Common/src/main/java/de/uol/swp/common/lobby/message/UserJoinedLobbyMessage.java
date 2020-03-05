package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Die Klasse UserJoinedLobbyMessage.
 */
public class UserJoinedLobbyMessage extends AbstractLobbyMessage {

    private static final long serialVersionUID = -2228023228166981549L;
    private UserDTO gameOwner;
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

    public UserJoinedLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID, UserDTO gameOwner, LobbyDTO lobby) {
        super(lobbyName, user, lobbyID);
        this.gameOwner = gameOwner;
        this.lobby = lobby;
    }

    public UserDTO getGameOwner() {
        return gameOwner;
    }

    /**
     * Gibt die aktuelle Lobby zurück.
     *
     * @return Die Lobby
     */
    public LobbyDTO getLobby() {
        return lobby;
    }
}
