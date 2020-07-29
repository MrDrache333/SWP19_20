package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class KickUserMessage extends AbstractLobbyMessage {

    final LobbyDTO lobby;

    /**
     * Instanziiert eine KickUserMessage
     *
     * @param lobbyID    Die LobbyID der Lobby, aus der der User entfernt wurde
     * @param userToKick Der User, der aus der Lobby entfernt wurde
     * @param lobby      Die Lobby
     * @author Darian, Julia, Marvin
     * @since Sprint 4
     */
    public KickUserMessage(UUID lobbyID, UserDTO userToKick, LobbyDTO lobby) {
        super(lobbyID, userToKick);
        this.lobby = lobby;
    }

    /**
     * Gibt die Lobby zurück, aus der der User entfernt wurde
     *
     * @return Die Lobby
     * @author Julia
     * @since Sprint 4
     */
    public LobbyDTO getLobby() {
        return lobby;
    }
}
