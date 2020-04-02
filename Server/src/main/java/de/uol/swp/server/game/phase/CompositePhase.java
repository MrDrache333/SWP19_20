package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;

/**
 * Die Funktionsklasse aller Phasen
 */
public class CompositePhase implements ActionPhase, BuyPhase, ClearPhase {
    private Playground playground;

    /**
     * Der Konstruktor
     *
     * @param playground das Spielfeld
     * @author Fenja
     * @since Sprint6
     */
    public CompositePhase(Playground playground) {
        this.playground = playground;
    }

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
        deck.drawHand();
        checkIfGameIsFinished();

    }

    @Override
    public void executeActionPhase(Player player, short cardId) {
        /*
        1. Verifiziere, dass Karte existiert
        2. Überprüfe, ob Spieler diese Karte in der Hand hat
        3. Führe die auf der Karte befindlichen Aktionen aus
         */
    }

    /**
     * Überprüft, ob das Spiel in der Clearphase beendet ist
     *
     * @return false, wenn das Spiel nicht vorbei ist
     * @author Fenja
     * @since Sprint6
     */
    public boolean checkIfGameIsFinished() {
        if (playground.getCardField().get((short) 6) == 0) {
            return true;
        }
        int counter = 0;
        for (Card card : playground.getCardsPackField().getCards().getActionCards()) {
            if (playground.getCardField().containsKey(card.getId()) && playground.getCardField().get(card.getId()) == 0) {
                counter++;
                if (counter >= 3) {
                    return true;
                }
            }
        }
        return false;
    }
}
