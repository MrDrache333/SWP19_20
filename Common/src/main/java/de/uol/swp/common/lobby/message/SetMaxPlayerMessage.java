package de.uol.swp.common.lobby.message;

import de.uol.swp.common.user.User;

import java.util.Set;
import java.util.UUID;

/**
 * @author Timo, Rike
 * @since Sprint 3
 * @implNote SetMaxPlayer Message als Rückgabemessage inkl. Getter & Setter
 */
public class SetMaxPlayerMessage extends AbstractLobbyMessage
{
    private Integer maxPlayer;
    private UUID lobbyID;
    private boolean setMaxPlayerSet;
    private User owner;

    public SetMaxPlayerMessage(){
        //Nothing
    }

    public SetMaxPlayerMessage(Integer maxPlayer, UUID lobbyID, boolean setMaxPlayerSet, User owner)
    {
        this.maxPlayer = maxPlayer;
        this.lobbyID = lobbyID;
        this.setMaxPlayerSet = setMaxPlayerSet;
        this.owner = owner;
    }

    public Integer getMaxPlayer() {
        return maxPlayer;
    }

    public boolean getSetMaxPlayerSet() {
        return setMaxPlayerSet;
    }

    public boolean isSetMaxPlayerSet() {
        return setMaxPlayerSet;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }


}

