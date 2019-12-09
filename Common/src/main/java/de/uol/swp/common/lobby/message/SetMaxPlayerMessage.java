package de.uol.swp.common.lobby.message;

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

    public SetMaxPlayerMessage(){
        //Nothing
    }

    public SetMaxPlayerMessage(Integer maxPlayer, UUID lobbyID)
    {
        this.maxPlayer = maxPlayer;
        this.lobbyID = lobbyID;
    }

    public Integer getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(Integer maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    @Override
    public UUID getLobbyID() {
        return lobbyID;
    }

    @Override
    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }
}

