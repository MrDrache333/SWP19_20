package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Request zum verlassen einer Lobby
 *
 * @author Julia
 * @since Sprint 3
 */
public class LobbyLeaveUserRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = -1015160817744213145L;

    /**
     * Konstruktor für Serialisierung
     */
    public LobbyLeaveUserRequest() {
    }

    /**
     * Instanziiert ein LobbyLeaveUserRequest
     *
     * @param lobbyID die Lobby ID
     * @param user    der Benutzer
     * @author Julia, Marvin
     * @since Sprint 3
     */
    public LobbyLeaveUserRequest(UUID lobbyID, UserDTO user) {
        super(lobbyID, user);
    }
}
