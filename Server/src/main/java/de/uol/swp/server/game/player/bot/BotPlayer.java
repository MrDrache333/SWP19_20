package de.uol.swp.server.game.player.bot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.messages.StartActionPhaseMessage;
import de.uol.swp.common.game.messages.StartBuyPhaseMessage;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.game.player.Player;

import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class BotPlayer extends Player {
    protected EventBus eventBus;

    private final UUID gameId;

    /**
     * Erstellt einen neuen Bot.
     *
     * @param playerName Der Spielername
     * @author Fenja, Ferit
     * @since Sprint7
     */

    public BotPlayer(String playerName, EventBus bus, UUID gameID) {
        super(playerName);
        this.eventBus = bus;
        eventBus.register(this);
        this.gameId = gameID;
        User botFakeUser = new UserDTO(playerName, gameID.toString(), "", true);
        this.setTheUserInThePlayer(botFakeUser);
    }

    public boolean isBot() {
        return true;
    }

    /**
     * Skippt die BuyPhase, wenn der Bot dran ist.
     *
     * @param msg BuyPhase Mitteilung
     */
    @Subscribe
    public void onStartBuyPhaseMessage(StartBuyPhaseMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername())) {
            SkipPhaseRequest req = new SkipPhaseRequest(this.getTheUserInThePlayer(), gameId);
            eventBus.post(req);
        }
    }

    /**
     * Skippt die ActionPhase, wenn der Bot dran ist.
     *
     * @param msg Actionphase Mitteilung
     */
    @Subscribe
    public void onStartActionPhaseMessage(StartActionPhaseMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername())) {
            SkipPhaseRequest req = new SkipPhaseRequest(this.getTheUserInThePlayer(), gameId);
            eventBus.post(req);
        }
    }


    /**
     * Es wird der gesetzte Eventbus gelöscht
     *
     * @author Marco
     * @since Start
     */
    public void clearEventBus() {
        this.eventBus.unregister(this);
        this.eventBus = null;
    }
}
