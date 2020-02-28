package de.uol.swp.common.game;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class ShowCardRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -4996091811171165930L;
    private User user;
    private String cardID;

    /**
     * Der Konstruktor der OpenSettingsRequest
     *
     * @param user den übergebenen User
     * @author Rike
     * @since Sprint 5
     */
    public ShowCardRequest(User user, String cardID) {
        this.user = user;
        this.cardID = cardID;
    }

    /**
     * Getter der Klasse
     *
     * @return den gegebenen User
     * @author Rike
     * @since Sprint 5
     */
    public User getUser() {
        return user;
    }

    public String getCardID() {
        return cardID;
    }
}
