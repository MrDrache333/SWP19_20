package de.uol.swp.common.game;

import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * The interface GameService.
 */
public interface GameService {

    /**
     * Send play card request.
     *
     * @param lobbyID      the lobby id
     * @param loggedInUser the logged in user
     * @param aShort       the a short
     * @param card         the card
     * @param handCards    the hand cards
     * @param b            the b
     * @author
     * @since
     */
    void sendPlayCardRequest(UUID lobbyID, User loggedInUser, Short aShort, ImageView card, ArrayList<ImageView> handCards, boolean b);
}
