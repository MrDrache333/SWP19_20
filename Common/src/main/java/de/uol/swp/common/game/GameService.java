package de.uol.swp.common.game;

import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Das Interface GameService.
 */
public interface GameService {

    /**
     * Sende eine SendPlayCardRequest
     *
     * @param lobbyID      Die LobbyID
     * @param loggedInUser Der eingeloggte Nutzer
     * @param aShort       Der aShort
     * @param cardImage    Das Bild der Karte
     * @param handCards    Die Handkarten
     * @param b            Der B
     * @author Micheal Wendler
     * @since seiner Geburt
     */
    void sendPlayCardRequest(UUID lobbyID, User loggedInUser, Short aShort, ImageView cardImage, ArrayList<ImageView> handCards, boolean b);

    /**
     * Send buy card request.
     *
     * @param lobbyID      Die LobbyID
     * @param loggedInUser Der eingeloggte Nutzer
     * @param valueOf      Das Value-of
     * @param cardImage    Das Bild der Karte
     * @author KenoO
     * @since Sprint 4
     */
    void sendBuyCardRequest(UUID lobbyID, User loggedInUser, Short valueOf, ImageView cardImage);
}
