package de.uol.swp.common.game.messages;


import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class PlayCardMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 3669837484946853842L;
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

    private final UUID lobbyID;
    private final User currentUser;
    private final Short handCardID;
    private boolean playCard;

    public PlayCardMessage(UUID gameID, User player, short cardID) {
        this.lobbyID = gameID;
        this.currentUser = player;
        this.handCardID = cardID;
    }


    public PlayCardMessage(UUID lobbyID, User currentUser, Short handCardID, boolean playCard) {
        this.lobbyID = lobbyID;
        this.currentUser = currentUser;
        this.handCardID = handCardID;
        this.playCard = playCard;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public short getHandCardID() {
        return handCardID;
    }

    public boolean isPlayCard() {
        return playCard;
    }
}
