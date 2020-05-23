package de.uol.swp.server.game.player.bot;

import de.uol.swp.common.user.User;
import de.uol.swp.server.game.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class BotPlayer extends Player {
    private Optional<BotService> botService;
    private UUID gameId;
    private boolean isBot = true;

    /**
     * Erstellt einen neuen Spieler
     *
     * @param playerName Der Spielername
     */
    public BotPlayer(String playerName, Optional<BotService> botService, UUID gameID) {
        super(playerName);
        this.botService = botService;
        this.gameId = gameID;
        User botFakeUser = new User() {
            @Override
            public String getUsername() {
                return playerName;
            }

            @Override
            public String getPassword() {
                return "";
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

    // TODO: Subscribe Methode auf die Phasen und dann direkt eine SkipPhaseRequest senden.
    // TODO: Benötigt wird der BotService für den Bot

}
