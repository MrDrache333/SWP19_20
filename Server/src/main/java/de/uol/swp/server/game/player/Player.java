package de.uol.swp.server.game.player;

/**
 * Der InGame Spieler
 */
public class Player {

    private String playerName;
    private Deck playerDeck;

    /**
     * Erstellt einen neuen Spieler
     *
     * @param playerName Der Spielername
     */
    public Player(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Gibt den Spielernamen zurück
     *
     * @return Der Spielername
     */
    public String getPlayerName() {
        return playerName;
    }
}
