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
    private UUID lobbyID;

    public LobbyLeaveUserRequest() {
    }

    /**
     * Instanziiert ein LobbyLeaveUserRequest
     *
     * @param lobbyName der Lobby Name
     * @param user der Benutzer
     * @param lobbyID die Lobby ID
     * @author Julia
     * @since Sprint 3
     *
     */
    public LobbyLeaveUserRequest(String lobbyName, UserDTO user, UUID lobbyID) {
        super(lobbyName, user);
        this.lobbyID = lobbyID;
    }

    /**
     * Gibt die ID der Lobby zurück
     *
     * @return
     * @author Julia
     * @since Sprint 3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * vergibt der Lobby eine ID
     *
     * @param lobbyID
     * @author Julia
     * @since Sprint 3
     */
    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

}
