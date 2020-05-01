package de.uol.swp.common.game.messages;


import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 3669837484946853842L;
    /**
     * Die Message die gesendet wird, wenn eine Handkarte angeklickt wurde
     *
     * @param gameID       Die ID des aktuellen Spiels
     * @param currentUser   Der Spieler der die Request stellt
     * @param handCardID    Die ID der angeklickten Karte
     * @author Rike
     * @since Sprint 5
     */

    private final UUID gameID;
    private final User currentUser;
    private final Short handCardID;


    public PlayCardMessage(UUID gameID, User currentUser, Short handCardID) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
    }

    public UUID getGameID() {
        return gameID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public short getHandCardID() {
        return handCardID;
    }

}
