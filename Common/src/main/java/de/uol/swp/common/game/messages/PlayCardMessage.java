package de.uol.swp.common.game.messages;

import de.uol.swp.common.game.response.AbstractGameMessage;
import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;

import java.util.UUID;

public class PlayCardMessage extends AbstractGameMessage {

    /**
     * Die Message die gesendet wird, wenn eine Handkarte angeklickt wurde
     *
     * @param cardImage     Die ImageView der Karte
     * @param handCards     Das Array mit den ImageViews die auf der Hand sind
     * @param smallSpace    gibt an, ob die Karten zusammen gerückt sind oder nicht
     * @param count         gibt an, die wievielte Karte gespielt wurde
     * @param playCard      gibt an, ob die karte gespielt werden kann
     * @author Rike
     * @since Sprint 5
     */

    private ImageView cardImage;
    private int count;
    private boolean playCard;

    public PlayCardMessage(UUID lobbyID, User currentUser, Short handCardID, ImageView cardImage, int count,
                           boolean playCard) {
        super(lobbyID, currentUser, handCardID);
        this.cardImage = cardImage;
        this.count = count;
        this.playCard = playCard;
    }

    public ImageView getCardImage() {
        return cardImage;
    }

    public int getCount() {
        return count;
    }

    public boolean isPlayCard() {
        return playCard;
    }
}
