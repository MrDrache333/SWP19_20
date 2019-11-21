package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.message.AbstractServerMessage;

public class LobbyCreatedMessage extends AbstractServerMessage {


    /**
     * A message to indicate a new lobby
     */

    private static final long serialVersionUID = -2071886836547126480L;
    private Lobby lobby;

    public LobbyCreatedMessage() {
    }

    public Lobby getLobby() {
        return lobby;
    }

}


