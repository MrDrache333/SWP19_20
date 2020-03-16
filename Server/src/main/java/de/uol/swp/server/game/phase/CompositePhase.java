package de.uol.swp.server.game.phase;

import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;

/**
 * Die Funktionsklasse aller Phasen
 */
public class CompositePhase implements ActionPhase, BuyPhase, ClearPhase {

    @Override
    public void executeBuyPhase(Player player, short cardId) {
        /*
        1. Verifiziere, dass Karte existiert
        2. Überprüfe, ob Karte, durch auf der Hand befindliche Geldkarten, gekauft werden kann
        3. Führe Kauf aus

        Werfe bei fehlern eine Exception, sodass aufrufender den Kauf abbrechen kann
         */
    }

    /**
     * Alle Karten auf der Hand des Spielers werden auf den Ablagestapel verschoben und eine neue Hand gezogen
     *
     * @param player Der aktuelle Spieler
     * @author Julia
     * @since Sprint6
     */
    @Override
    public void executeClearPhase(Player player) {
        Deck deck = player.getPlayerDeck();
        deck.getDiscardPile().addAll(deck.getHand());
        deck.getHand().clear();
        deck.newHand();
    }

    @Override
    public void executeActionPhase(Player player, short cardId) {
        /*
        1. Verifiziere, dass Karte existiert
        2. Überprüfe, ob Spieler diese Karte in der Hand hat
        3. Führe die auf der Karte befindlichen Aktionen aus
         */
    }
}
