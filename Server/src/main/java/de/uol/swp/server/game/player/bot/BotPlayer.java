package de.uol.swp.server.game.player.bot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.game.messages.StartActionPhaseMessage;
import de.uol.swp.common.game.messages.StartBuyPhaseMessage;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BotPlayer extends Player {
    protected EventBus eventBus;

    private UUID gameId;
    private boolean isBot = true;

    /**
     * Erstellt einen neuen Spieler
     *
     * @param playerName Der Spielername
     */

    public BotPlayer(String playerName, EventBus bus, UUID gameID) {
        super(playerName);
        this.eventBus = bus;
        eventBus.register(this);
        this.gameId = gameID;
        User botFakeUser = new User() {
            @Override
            public String getUsername() {
                return playerName;
            }

            @Override
            public String getPassword() {
                return gameID.toString();
            }

            @Override
            public String getEMail() {
                return "";
            }

            @Override
            public User getWithoutPassword() {
                return null;
            }

            @Override
            public int compareTo(@NotNull User o) {
                return 0;
            }
        };
        this.setTheUserInThePlayer(botFakeUser);
    }

    public boolean isBot() {
        return isBot;
    }

    @Subscribe
    public void onStartBuyPhaseMessage(StartBuyPhaseMessage msg) {
        if (msg.getUser().getUsername().equals(getTheUserInThePlayer().getUsername())) {
            SkipPhaseRequest req = new SkipPhaseRequest(this.getTheUserInThePlayer(), gameId);
            eventBus.post(req);
        }
    }

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
