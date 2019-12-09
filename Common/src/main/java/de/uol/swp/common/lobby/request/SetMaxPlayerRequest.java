package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

import java.util.UUID;

/**
 * @author Timo, Rike
 * @since Sprint 3
 * @implNote SetMaxPlayerRequest inkl. Getter und Setter für die Integer-Variable maxPlayerValue
 */
public class SetMaxPlayerRequest extends AbstractRequestMessage
{
    private Integer maxPlayerValue;
    private UUID lobbyID;

    public SetMaxPlayerRequest(Integer maxPlayerValue, UUID lobbyID)
    {
        this.maxPlayerValue = maxPlayerValue;
        this.lobbyID = lobbyID;
    }

    public Integer getMaxPlayerValue() {
        return maxPlayerValue;
    }

    public void setMaxPlayerValue(Integer maxPlayerValue) {
        this.maxPlayerValue = maxPlayerValue;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }
}

