package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

public class LobbyJoinUserRequest extends AbstractLobbyRequest {

    private UUID lobbyID;

    /**
     * Instanziiert einen neuen LobbyJoinUserRequest
     *
     * @param lobbyName der Name der Lobby
     * @param user der User, von dem der Request gesendet wurde
     * @param lobbyID die ID der Lobby
     * @author Julia, Paula
     * @since Sprint3
     */
    public LobbyJoinUserRequest(String lobbyName, UserDTO user, UUID lobbyID) {
        super(lobbyName, user);
        this.lobbyID = lobbyID;
    }

    /**
     * Gibt die lobbyID zurück
     *
     * @return die lobbyID
     * @author Julia, Paula
     * @since Sprint3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }
}
