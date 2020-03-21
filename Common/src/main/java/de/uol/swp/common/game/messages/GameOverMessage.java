package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractMessage;
import de.uol.swp.common.user.User;

import java.util.UUID;

public class GameOverMessage extends AbstractMessage {

    private static final long serialVersionUID = 8386864315993015152L;
    private UUID gameID;
    private User player;
    private String winner;

    /**
     * Instanziiert GameOverMessage
     *
     * @param gameID Die ID des Games
     * @param player Der Spieler
     * @param winner Der Gewinner
     * @author Anna
     * @since Sprint6
     */
    public GameOverMessage(UUID gameID, User player, String winner) {
        this.gameID = gameID;
        this.player = player;
        this.winner = winner;
    }

    /**
     * Gibt die ID des Spiels zurück.
     *
     * @return die ID
     * @author Anna
     * @since Sprint6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den Spieler zurück.
     *
     * @return der Spieler
     * @author Anna
     * @since Sprint6
     */
    public User getPlayer() {
        return player;
    }

    /**
     * Gibt den Gewinner des Spiels zurück.
     *
     * @return der Gewinner
     * @author Anna
     * @since Sprint6
     */
    public String getWinner() {
        return winner;
    }
}
