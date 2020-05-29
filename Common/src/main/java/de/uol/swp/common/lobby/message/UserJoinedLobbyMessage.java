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
     * @param lobbyID   Die Lobby ID
     * @param user      Der User
     * @param gameOwner Der Besitzer
     * @param lobby     Die Lobby
     * @author Julia Debkowski, Marvin
     * @since Sprint 1
     */

    public UserJoinedLobbyMessage(UUID lobbyID, UserDTO user, UserDTO gameOwner, LobbyDTO lobby) {
        super(lobbyID, user);
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
