package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

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
    private User loggedInUser;

    public SetMaxPlayerRequest(Integer maxPlayerValue, UUID lobbyID, User loggedInUser)
    {
        this.maxPlayerValue = maxPlayerValue;
        this.lobbyID = lobbyID;
        this.loggedInUser = loggedInUser;
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

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}

