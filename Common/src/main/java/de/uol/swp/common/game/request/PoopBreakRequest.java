package de.uol.swp.common.game.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PoopBreakRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 4438745468468138864L;
    private final User user;
    private final User poopInitiator;
    private final UUID gameID;
    private final boolean poopDecision;


    public PoopBreakRequest(User user, UUID gameID) {
        this.poopInitiator = user;
        this.user = user;
        this.gameID = gameID;
        this.poopDecision = true;
    }
    public PoopBreakRequest(User user, UUID gameID, boolean poopDecision) {
        poopInitiator = null;
        this.user = user;
        this.gameID = gameID;
        this.poopDecision = poopDecision;
    }
    public boolean getPoopDecision() {
        return poopDecision;
    }

    public User getUser() {
        return user;
    }

    public UUID getGameID() {
        return gameID;
    }

    public User getPoopInitiator() {
        return poopInitiator;
    }
}
