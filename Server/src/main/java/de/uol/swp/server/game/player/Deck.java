package de.uol.swp.server.game.player;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.MoneyCard;
import de.uol.swp.common.game.card.parser.CardPack;
import de.uol.swp.common.game.card.parser.JsonCardParser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Kartendeck eines Spielers
 */
public class Deck {


    /**
     * Listen für Hand, Deck, Ablagestapel
     *
     * @author Paula
     * @since Sprint5
     */
    private ArrayList<Card> cardsDeck = new ArrayList<>();
    private ArrayList<Card> discardPile = new ArrayList<>();
    private ArrayList<Card> hand = new ArrayList<>();

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
     * Zieht eine neue Hand. Sind auf dem Nachziehstapel nicht mehr genügend Karten vorhanden,
     * wird der Ablagestapel zum neuen Nachziehstapel und die restlichen Karten werden gezogen.
     *
     * @author Julia
     * @since Sprint6
     */
    public void drawHand() {
        if (cardsDeck.size() < 5) {
            hand.addAll(cardsDeck);
            cardsDeck.clear();
            cardsDeck.addAll(discardPile);
            discardPile.clear();

            Collections.shuffle(cardsDeck);
            ArrayList<Card> tmp = new ArrayList<>();
            for (int i = 0; i < 5 - hand.size(); i++) {
                tmp.add(cardsDeck.get(i));
            }
            hand.addAll(tmp);
            tmp.forEach(card -> cardsDeck.remove(card));
        } else {
            initialiseHand();
        }
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
            Card card = cardsPack.getCards().getValueCards().get(0);
            cardsDeck.add(card);
        }
        for (int i = 0; i < 7; i++) {
            Card card = cardsPack.getCards().getMoneyCards().get(0);
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
        hand.forEach(card -> cardsDeck.remove(card));
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
        discardPile.remove(card);
    }

    /**
     * Methode, die den Geldwert eines Spielers berechnet und zurückgibt
     *
     * @return Geldwert der Karten eines Spielers
     * @author Paula
     * @since Sprint6
     */
    public int actualMoneyFromPlayer() {
        int money = 0;
        for (Card card : hand) {
            if (card instanceof MoneyCard) {
                money += ((MoneyCard) card).getValue();
            }
        }
        return money;
    }

    /**
     * Hilfsmethode, um bei einem Kauf Geldkarten für den Kauf abzuziehen
     *
     * @param value Wert einer Geldkarte
     * @author Paula
     * @since Sprint6
     */

    public void discardMoneyCardsForValue(int value) {
        int money = 0;
        int sizeHand = hand.size();
        for (int i=0; i<sizeHand; i++){
            if (hand.get(i) instanceof MoneyCard) {
                money += ((MoneyCard) hand.get(i)).getValue();
                hand.remove(hand.get(i));
                if (money >= value) {
                    break;
                }
            }
        }
    }

    /**
     * Hilfsmethode um eine Karte zum Ablagestapel hinzuzufügen
     *
     * @param card
     * @author Paula
     * @since Sprint6
     */
    public void addCardToDiscardPile(Card card) {
        discardPile.add(card);

    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getDiscardPile() {
        return discardPile;
    }
}
