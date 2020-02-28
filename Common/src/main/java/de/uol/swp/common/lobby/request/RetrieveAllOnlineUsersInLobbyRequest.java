package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

import java.util.UUID;

/**
 * Die RetrieveAllOnlineUsersInLobbyRequest
 *
 * @author Marco
 * @since Start
 */
public class RetrieveAllOnlineUsersInLobbyRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 1185727535831267049L;
    private UUID lobbyId;

    /**
     * Instanziiert ein neues Abrufen aller Online-Benutzer in der Lobby-Anfrage.
     * Alternative mit ID statt Name.
     *
     * @param LobbyID die LobbyID
     *                TODO: Auf lobbyID oder lobbyName als Standard einigen.
     * @author Marvin
     */
    public RetrieveAllOnlineUsersInLobbyRequest(UUID LobbyID) {
        this.lobbyId = LobbyID;
    }

    /**
     * Gibt die LobbyID wieder.
     *
     * @return die LobbyID
     * @author Marco
     * @since Start
     */
    public UUID getLobbyId() {
        return lobbyId;
    }
}
