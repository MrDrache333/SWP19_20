package de.uol.swp.common.lobby.response;

import de.uol.swp.common.message.AbstractResponseMessage;

import java.util.UUID;

/**
 * Sendet eine Antwort, wenn die Nachricht erfolgreich gesendet wurde, welche Karten ausgewählt wurden
 *
 * @author Fenja, Anna
 * @since Sprint 7
 */
public class SetChosenCardsResponse extends AbstractResponseMessage {

    private static final long serialVersionUID = -4238932243675136534L;
    private boolean success;
    private UUID lobbyID;

    public SetChosenCardsResponse(UUID lobbyID, boolean success) {
        this.lobbyID = lobbyID;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

}
