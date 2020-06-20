package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.UUID;

public class PoopBreakMessage extends AbstractServerMessage {

    private final User poopInitiator;
    private final UUID gameID;
    private final ArrayList<Boolean> decisions;

    public PoopBreakMessage(User poopInitiator, UUID gameID, ArrayList<Boolean> decisions) {
        this.poopInitiator = poopInitiator;
        this.gameID = gameID;
        this.decisions = decisions;
    }

    public User getPoopInitiator() {
        return poopInitiator;
    }

    public UUID getGameID() {
        return gameID;
    }

    public ArrayList<Boolean> getDecisions() {
        return decisions;
    }
    public int getAcceptedVotes() {
        return (int) decisions.stream().filter(d -> d).count();
    }
    public int getDeclinedVotes() {
        return (int) decisions.stream().filter(d -> !d).count();
    }
}
