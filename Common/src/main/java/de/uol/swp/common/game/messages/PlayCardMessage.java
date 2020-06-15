package de.uol.swp.common.game.messages;


import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 3669837484946853842L;

    private final UUID gameID;
    private final User currentUser;
    private final Short handCardID;
    private final Boolean isPlayed;
    private boolean removeCardAfter;

    /**
     * Die Message die gesendet wird, wenn eine Handkarte angeklickt wurde
     *
     * @param gameID      Die ID des aktuellen Spiels
     * @param currentUser Der Spieler der die Request stellt
     * @param handCardID  Die ID der angeklickten Karte
     * @param isPlayed    Sagt aus, ob die Karte ausgespielt werden darf
     * @author Rike, Devin
     * @since Sprint 5
     */
    public PlayCardMessage(UUID gameID, User currentUser, Short handCardID, Boolean isPlayed) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.isPlayed = isPlayed;
    }

    /**
     * Überladener Konstruktor
     * Die Message die gesendet wird, wenn eine Handkarte angeklickt wurde
     *
     * @param gameID           Die ID des aktuellen Spiels
     * @param currentUser      Der Spieler der die Request stellt
     * @param handCardID       Die ID der angeklickten Karte
     * @param isPlayed         Sagt aus, ob die Karte ausgespielt werden darf
     * @author Rike, Devin
     * @since Sprint 5
     */
    public PlayCardMessage(UUID gameID, User currentUser, Short handCardID, Boolean isPlayed, boolean removeCardAfter) {
        this.gameID = gameID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.isPlayed = isPlayed;
        this.removeCardAfter = removeCardAfter;
    }

    /**
     * Gibt die Game-ID zurück
     *
     * @return gameID die Game-ID
     * @author Devin
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den aktuellen User zurück
     *
     * @return currentUser der aktuelle User
     * @author Rike
     * @since Sprint 6
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Gibt die Handkarten-ID
     *
     * @return Die ausgespielte Karte
     * @author Darian
     * @since Sprint 9
     */
    public short getHandCardID() {
        return handCardID;
    }

    /**
     * Gibt die Handkarten-ID als String zurück
     *
     * @return handCardID.toString() die Handkarten-ID als String (Text)
     * @author Devin
     * @since Sprint 6
     */
    public String getHandCardIdAsString() {
        return handCardID.toString();
    }

    /**
     * Gibt zurück, ob die Karte gespielt wurde
     *
     * @return isPlayed wurde die Karte gespielt (Boolean)
     * @author Devin
     * @since Sprint 6
     */
    public Boolean getIsPlayed() {
        return isPlayed;
    }

    public boolean isRemoveCardAfter() {
        return removeCardAfter;
    }
}
