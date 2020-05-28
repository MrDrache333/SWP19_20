package de.uol.swp.common.game.messages;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameOverMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 8386864315993015152L;
    private UUID gameID;
    private List<String> winners;
    private Map<String, Integer> results;

    /**
     * Instanziiert GameOverMessage
     *
     * @param gameID  Die ID des Games
     * @param winners Der/die Gewinner
     * @author Anna
     * @since Sprint 6
     */
    public GameOverMessage(UUID gameID, List<String> winners, Map<String, Integer> results) {
        this.gameID = gameID;
        this.winners = winners;
        this.results = results;
    }

    /**
     * Gibt die ID des Spiels zurück.
     *
     * @return die ID
     * @author Anna
     * @since Sprint 6
     */
    public UUID getGameID() {
        return gameID;
    }

    /**
     * Gibt den/die Gewinner des Spiels zurück.
     *
     * @return der Gewinner
     * @author Anna
     * @since Sprint 6
     */
    public List<String> getWinners() {
        return winners;
    }

    /**
     * Gibt die Punkteanzahl aller Spieler zurück.
     *
     * @return die Map mit dem Namen des Spieler und seiner Punkte
     * @author Anna
     * @since Sprint 6
     */
    public Map<String, Integer> getResults() {
        return results;
    }
}