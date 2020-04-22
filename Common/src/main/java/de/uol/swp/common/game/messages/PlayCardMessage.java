package de.uol.swp.common.game.messages;


import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardMessage extends AbstractServerMessage {

    /**
     * Die Message die gesendet wird, wenn eine Handkarte angeklickt wurde
     *
     * @param lobbyID       Die ID der aktuellen Lobby
     * @param currentUser   Der Spieler der die Request stellt
     * @param handCardID    Die ID der angeklickten Karte
     * @param count         gibt an, die wievielte Karte gespielt wurde
     * @param playCard      gibt an, ob die karte gespielt werden kann
     * @author Rike
     * @since Sprint 5
     */

    private UUID lobbyID;
    private User currentUser;
    private Short handCardID;
    private int count;
    private boolean playCard;

    public PlayCardMessage() {
    }

    public PlayCardMessage(UUID gameID, User player, short cardID, int count) {
        this.lobbyID = gameID;
        this.currentUser = player;
        this.handCardID = cardID;
        this.count = count;
    }


    public PlayCardMessage(UUID lobbyID, User currentUser, Short handCardID, int count,
                           boolean playCard) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.count = count;
        this.playCard = playCard;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Short getHandCardID() {
        return handCardID;
    }

    public int getCount() {
        return count;
    }

    public boolean isPlayCard() {
        return playCard;
    }
}
