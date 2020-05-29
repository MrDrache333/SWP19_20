package de.uol.swp.common.lobby.request;

import java.util.UUID;

/**
 * Die RetrieveAllOnlineUsersInLobbyRequest
 *
 * @author Marco
 * @since Start
 */
public class RetrieveAllOnlineUsersInLobbyRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = 1185727535831267049L;
    private UUID lobbyId;

    /**
     * Instanziiert ein neues Abrufen aller Online-Benutzer in der Lobby-Anfrage.
     *
     * @param lobbyID die LobbyID
     * @author Marvin
     * @since Sprint 3
     */
    public RetrieveAllOnlineUsersInLobbyRequest(UUID lobbyID) {
        super(lobbyID, null);
    }

}
