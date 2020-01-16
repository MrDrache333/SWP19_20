package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Request zum Beitreten einer Lobby
 *
 * @author Julia, Paula
 * @since Sprint3
 */
public class LobbyJoinUserRequest extends AbstractLobbyRequest {

    private UUID lobbyID;

    /**
     * Konstruktor für die Serialisierung
     *
     * @author Julia, Paula
     * @since Sprint3
     */
    public LobbyJoinUserRequest() {
    }

    /**
     * Instanziiert ein LobbyJoinUserRequest
     *
     * @param lobbyName der Name der Lobby
     * @param user      der User, der der Lobby beitreten will
     * @param lobbyID   die ID der Lobby
     * @author Julia, Paula
     * @since Sprint3
     */
    public LobbyJoinUserRequest(String lobbyName, UserDTO user, UUID lobbyID) {
        super(lobbyName, user);
        this.lobbyID = lobbyID;
    }

    /**
     * Gibt die ID der Lobby zurück
     *
     * @return die lobbyID
     * @author Julia, Paula
     * @since Sprint3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }
}
