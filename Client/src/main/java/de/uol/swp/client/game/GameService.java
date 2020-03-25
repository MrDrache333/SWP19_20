package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.game.request.PlayCardRequest;
import de.uol.swp.common.user.User;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.UUID;

public class GameService implements de.uol.swp.common.game.GameService {

    private static final Logger LOG = LogManager.getLogger(GameService.class);
    private final EventBus bus;

    /**
     * Instanziiert einen neuen LobbyService.
     *
     * @param bus der Bus
     * @author Marco
     * @since Start
     */

    @Inject
    public GameService(EventBus bus) {
        this.bus = bus;
    }

    public void sendPlayCardRequest(UUID lobbyID, User loggedInUser, Short aShort, ImageView card, ArrayList<ImageView> handCards, boolean b) {
        PlayCardRequest request = new PlayCardRequest(lobbyID, loggedInUser, aShort, card, handCards, b);
        bus.post(request);
    }
}
