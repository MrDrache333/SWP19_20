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
     * @param isPlayed      Sagt aus, ob die Karte ausgespielt werden darf
     * @author Rike, Devin
     * @since Sprint 5
     */

    private final UUID gameID;
    private final User currentUser;
    private final Short handCardID;
    private final Boolean isPlayed;
    private Short userPlaceNumber = null;



    private Short enemyPlaceNumber = null;


    public PlayCardMessage(UUID gameID, User currentUser, Short handCardID, Boolean isPlayed) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.isPlayed = isPlayed;
    }

    public PlayCardMessage (UUID gameID, User currentUser, Short handCardID, Boolean isPlayed, Short userPlaceNumber, Short enemyPlaceNumber) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.isPlayed = isPlayed;
        this.userPlaceNumber = userPlaceNumber;
        this.enemyPlaceNumber = enemyPlaceNumber;

    }

    public UUID getGameID() {
        return gameID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getHandCardID() {
        return handCardID.toString();
    }

    public Boolean getIsPlayed() {return isPlayed;}

    public Short getEnemyPlaceNumber() {
        return enemyPlaceNumber;
    }

    public Short getUserPlaceNumber() {
        return userPlaceNumber;
    }
}
