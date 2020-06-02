package de.uol.swp.common.lobby.request;

import java.util.UUID;

/**
 * Anforderung einen Bot der Lobby hinzuzufügen.
 *
 * @author Fenja, Ferit
 * @since Sprint7
 */
public class AddBotRequest extends AbstractLobbyRequest {
    public AddBotRequest(UUID lobbyID) {
        super(lobbyID);
    }
}
