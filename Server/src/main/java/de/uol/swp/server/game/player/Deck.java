package de.uol.swp.server.game.player;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.CardPack;
import de.uol.swp.common.game.card.parser.JsonCardParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

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

    /**
     * Startdeck wird initialisiert (7x Money, 3x Anwesen)
     *
     * @return Kartendeck
     * @author Paula
     * @since Sprint5
     */
    private ArrayList<Card> initialiseStartDeck() {
        CardPack cardsPack = new JsonCardParser().loadPack("Basispack");
        for (int i = 0; i < 3; i++) {
            int d = ThreadLocalRandom.current().nextInt(3);
            Card card = cardsPack.getCards().getValueCards().get(d);
            cardsDeck.add(card);
        }
        for (int i = 0; i < 7; i++) {
            int d = ThreadLocalRandom.current().nextInt(3);
            Card card = cardsPack.getCards().getMoneyCards().get(d);
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

    public ArrayList<Card> getHand() {
        return hand;
    }
}
