package de.uol.swp.server.game.player;

import de.uol.swp.server.game.card.Card;
import de.uol.swp.server.game.card.MoneyCard;
import de.uol.swp.server.game.card.ValueCard;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Kartendeck eines Spielers
 */
public class Deck {


    /**
     * Listen für Hand, Deck, TrayStack
     *
     * @author Paula
     * @since Sprint5
     */
    private ArrayList<Card> cardsDeck = new ArrayList<Card>();
    private ArrayList<Card> trayStack = new ArrayList<Card>();
    private ArrayList<Card> hand = new ArrayList<Card>();

    /**
     * Konstruktor
     *
     * @author Paula
     * @since Sprint5
     */
    public Deck() {
        initialiseStartDeck();
        initialiseHand();
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Startdeck wird initialisiert (7x Money, 3x Anwesen)
     *
     * @return Kartendeck
     * @author Paula
     * @since Sprint5
     */
    private ArrayList<Card> initialiseStartDeck() {
        for (int i = 0; i < 3; i++) {
            Card card = new ValueCard("", (short) 10);
            cardsDeck.add(card);
        }
        for (int i = 0; i < 7; i++) {
            Card card = new MoneyCard("", (short) 10);
            cardsDeck.add(card);
        }
        return cardsDeck;
    }

    /**
     * Hand wird initialisiert; 5 Karten aus Startdeck
     *
     * @return Hand
     * @author Paula
     * @since Sprint5
     */
    private ArrayList<Card> initialiseHand() {
        Collections.shuffle(cardsDeck);
        for (int i = 0; i < 5; i++) {
            Card tmpCard = cardsDeck.get(i);
            hand.add(tmpCard);
        }
        return hand;
    }


    /**
     * Karte wird aus Arrays gelöscht
     *
     * @author Pauia
     * @since Sprint5
     */
    private void deleteCard(Card card) {
        cardsDeck.remove(card);
        hand.remove(card);
        trayStack.remove(card);
    }
}
