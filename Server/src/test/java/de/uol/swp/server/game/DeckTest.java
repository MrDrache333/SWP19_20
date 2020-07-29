package de.uol.swp.server.game;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.server.game.player.Deck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testklasse des Decks
 *
 * @author Julia
 * @since Sprint 10
 */
public class DeckTest {

    CardPack cardPack = new JsonCardParser().loadPack("Basispack");

    /**
     * Testet, ob das Deck richtig initialisiert wird (7 Kupfer- + 3 Anwesenkarten)
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testStartDeck() {
        Deck deck = new Deck();
        assertEquals(5, deck.getCardsDeck().size());
        assertEquals(5, deck.getHand().size());
        assertEquals(0, deck.getDiscardPile().size());
        assertEquals(0, deck.getActionPile().size());
        List<Card> cards = new ArrayList<>();
        cards.addAll(deck.getHand());
        cards.addAll(deck.getCardsDeck());
        int kupfer = 0;
        int anwesen = 0;
        for (Card card : cards) {
            if (card.getId() == 1) {
                kupfer++;
            } else if (card.getId() == 4) {
                anwesen++;

            }
        }
        assertEquals(7, kupfer);
        assertEquals(3, anwesen);
    }

    /**
     * Testet, ob die Siegpunkte richtig gezählt werden
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testCountSiegpunkte() {
        Deck deck = new Deck();
        deck.countSiegpunkte();
        assertEquals(3, deck.getSiegpunkte());
        Card provinz = cardPack.getCards().getCardForId((short) 6);
        deck.getCardsDeck().add(provinz);
        deck.countSiegpunkte();
        assertEquals(9, deck.getSiegpunkte());
        Card fluch = cardPack.getCards().getCardForId((short) 38);
        deck.getCardsDeck().add(fluch);
        deck.countSiegpunkte();
        assertEquals(8, deck.getSiegpunkte());
    }

    /**
     * Testet, ob das Geld auf der Hand richtig gezählt wird
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testActualMoneyFromPlayer() {
        Deck deck = new Deck();
        deck.getHand().clear();
        Card kupfer = cardPack.getCards().getCardForId((short) 1);
        deck.getHand().add(kupfer);
        deck.getHand().add(kupfer);
        deck.getHand().add(kupfer);
        Card silber = cardPack.getCards().getCardForId((short) 2);
        deck.getHand().add(silber);
        assertEquals(5, deck.actualMoneyFromPlayer());
    }

    /**
     * Testet, ob Geldkarten richtig abgelegt werden
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testDiscardMoneyCardsForValue() {
        Deck deck = new Deck();
        while (deck.actualMoneyFromPlayer() < 3) {
            deck.getDiscardPile().addAll(deck.getHand());
            deck.getHand().clear();
            deck.drawHand();
        }
        int moneyOnHand = deck.actualMoneyFromPlayer();
        int discardSize = deck.getDiscardPile().size();
        deck.discardMoneyCardsForValue(3);
        assertEquals(moneyOnHand - 3, deck.actualMoneyFromPlayer());
        assertEquals(discardSize + 3, deck.getDiscardPile().size());
        assertEquals(2, deck.getHand().size());
    }

    /**
     * Testet, ob eine neue Hand richtig gezogen wird
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testDrawHand() {
        Deck deck = new Deck();
        deck.getDiscardPile().addAll(deck.getHand());
        deck.getHand().clear();
        deck.drawHand();
        assertEquals(0, deck.getCardsDeck().size());
        assertEquals(5, deck.getDiscardPile().size());
        assertEquals(5, deck.getHand().size());
        deck.getDiscardPile().addAll(deck.getHand());
        deck.getHand().clear();
        deck.drawHand();
        assertEquals(5, deck.getCardsDeck().size());
        assertEquals(5, deck.getHand().size());
        assertEquals(0, deck.getDiscardPile().size());
    }
}
