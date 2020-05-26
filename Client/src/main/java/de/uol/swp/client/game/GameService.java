package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.game.request.GameGiveUpRequest;
import de.uol.swp.common.game.request.PlayCardRequest;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

public class GameService {
    private static final Logger LOG = LogManager.getLogger(GameService.class);
    private final EventBus bus;

    /**
     * Instanziiert einen neuen GameService.
     *
     * @param bus der Bus
     * @author Haschem, Ferit
     * @since Sprint5
     */
    @Inject
    public GameService(EventBus bus) {
        this.bus = bus;
        bus.register(this);
    }

    /**
     * Erstellt eine GameGiveUpRequest und postet diese auf den EventBus.
     *
     * @param lobbyID           die LobbyID zum Lobbynamen
     * @param theUserWhoGivedUp der User der aufgeben will.
     * @author Haschem, Ferit
     * @since Sprint5
     */
    public void giveUp(UUID lobbyID, UserDTO theUserWhoGivedUp) {
        GameGiveUpRequest request = new GameGiveUpRequest(theUserWhoGivedUp, lobbyID);
        bus.post(request);
    }

    /**
     * Erstellt skipPhaseRequest und postet diese auf den EventBus.
     *
     * @param gameID die LobbyID zum Lobbynamen
     * @param user   der User der seine Phase skippen möchte
     * @author Devin
     * @since Sprint5
     */
    public void skipPhase(User user, UUID gameID) {
        SkipPhaseRequest req = new SkipPhaseRequest(user, gameID);
        bus.post(req);
    }

    /**
     * Erstellt ChooseCardResponse
     *
     * @param gameID die LobbyID zum Lobbynamen
     * @param user   der User der Karten ausgewählt hat
     * @author Devin
     * @since Sprint5
     */
    public void chooseCard(User user, UUID gameID, ArrayList<Short> cardIDs, boolean directHand) {
        ChooseCardResponse resp = new ChooseCardResponse(gameID, user, cardIDs, directHand);
        bus.post(resp);
    }

    public void playCard(UUID gameID, User loggedInUser, Short id) {
        PlayCardRequest req = new PlayCardRequest(gameID, loggedInUser, id);
        bus.post(req);
    }

    public void buyCard(BuyCardRequest req) {
        bus.post(req);
    }

}
