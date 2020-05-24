package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;


import java.util.UUID;

public class ActualPointMessage extends AbstractServerMessage {

    private UUID lobbyID;
    private UserDTO actualPlayer;
    private String cardID;
    private String actualPhase;
    private int points;


    public ActualPointMessage(UUID lobbyID, UserDTO actualPlayer, String actualPhase, int points) {
        this.lobbyID = lobbyID;
        this.actualPlayer = actualPlayer;
        this.actualPhase = actualPhase;
        this.points = points;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public UserDTO getActualPlayer() {
        return actualPlayer;
    }


    public String getActualPhase() {
        return actualPhase;
    }

    public Integer getPoints() {
        return points;
    }


}
