package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;


import java.util.UUID;

public class ActualPointMessage extends AbstractServerMessage {

    private UUID lobbyID;
    private int points;


    public ActualPointMessage(UUID lobbyID, int points) {
        this.lobbyID = lobbyID;
        this.points = points;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public Integer getPoints() {
        return points;
    }


}
