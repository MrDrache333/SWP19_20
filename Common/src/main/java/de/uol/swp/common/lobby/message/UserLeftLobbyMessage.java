package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class UserLeftLobbyMessage extends AbstractLobbyMessage {

    private static final long serialVersionUID = -5546005208705943803L;
    private UserDTO gameOwner;
    private LobbyDTO lobby;

    /**
     * Konstruktor für Serialisierung
     */
    public UserLeftLobbyMessage() {
    }

    /**
     * Instanziiert die Message
     *
     * @param lobbyID   Die LobbyID
     * @param lobby     Die Lobby
     * @param gameOwner Der Besitzer
     * @param user      Der User
     * @author Julia, Darian, Marvin
     * @since Sprint 2
     */
    public UserLeftLobbyMessage(UUID lobbyID, UserDTO user, UserDTO gameOwner, LobbyDTO lobby) {
        super(lobbyID, user);
        this.lobby = lobby;
        this.gameOwner = gameOwner;
    }

    public UserDTO getGameOwner() {
        return gameOwner;
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
