package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.request.*;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class GameService {
    private static final Logger LOG = LogManager.getLogger(GameService.class);
    private final EventBus bus;

    /**
     * Instanziiert einen neuen GameService.
     *
     * @param bus der Bus
     * @author Haschem, Ferit
     * @since Sprint 5
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
     * @since Sprint 5
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
     * @since Sprint 5
     */
    public void skipPhase(User user, UUID gameID) {
        SkipPhaseRequest req = new SkipPhaseRequest(user, gameID);
        bus.post(req);
    }

    public void playCard(UUID gameID, User loggedInUser, Short id) {
        PlayCardRequest req = new PlayCardRequest(gameID, loggedInUser, id);
        bus.post(req);
    }

    /**
     * Sendet einen BuyCardRequest
     *
     * @param req der Request
     * @author Devin
     * @since Sprint 5
     */
    public void buyCard(BuyCardRequest req) {
        bus.post(req);
    }

    /**
     * Erstellt OptionalActionResponse und postet diese auf den EventBus.
     *
     * @param gameID die LobbyID der zugehörigen Lobby
     * @param user   der User der die Entscheidung getroffen hat
     * @param answer die Antwort von dem User auf die Frage von der Request
     * @author Darian
     * @since Sprint8
     */
    public void optionalAction(User user, UUID gameID, boolean answer) {
        OptionalActionResponse msg = new OptionalActionResponse(gameID, user, answer);
        bus.post(msg);
    }

    /**
     * Erstellt eine ChooseCardResponse und postet diese auf den EventBus.
     *
     * @param gameID       die LobbyID der zugehörigen Lobby
     * @param loggedInUser der User, der Karten auswählen durfte
     * @param chosenCards  die ausgewählten Karten
     * @author Anna, Fenja, Devin
     * @since Sprint 5
     */
    public void chooseCardResponse(UUID gameID, User loggedInUser, ArrayList<Short> chosenCards) {
        ChooseCardResponse response = new ChooseCardResponse(gameID, loggedInUser, chosenCards);
        bus.post(response);
    }

    /**
     * Erstellt eine PoopBreakRequest und postet diese auf den Eventbus.
     *
     */
    public void requestPoopBreak(User user, UUID gameID) {
        bus.post(new PoopBreakRequest(user, gameID));
    }

    public void answerPoopBreak(User user, UUID gameID, boolean vote) {
        bus.post(new PoopBreakRequest(user, gameID, vote));
    }

    public void cancelPoopBreak(User user, UUID gameID) {
        bus.post(new CancelPoopBreakRequest(user, gameID));
    }
}
