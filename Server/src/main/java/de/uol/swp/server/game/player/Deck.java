package de.uol.swp.server.game.player;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.MoneyCard;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.card.parser.components.CardPack;

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
     * @since Sprint 5
     */
    private final ArrayList<Card> cardsDeck = new ArrayList<>();
    private final ArrayList<Card> discardPile = new ArrayList<>();
    private final ArrayList<Card> hand = new ArrayList<>();
    private final ArrayList<Card> actionPile = new ArrayList<>();

    /**
     * Konstruktor
     *
     * @author Paula
     * @since Sprint 5
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
     * @since Sprint 6
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
     * @since Sprint 5
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
     * @since Sprint 5
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
     * @since Sprint 5
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
     * @since Sprint 6
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
     * @since Sprint 6
     */

    public void discardMoneyCardsForValue(int value) {
        int money = 0;
        int sizeHand = hand.size();
        ArrayList<Card> removeCards = new ArrayList<>();
        for (int i=0; i<sizeHand; i++){
            if (hand.get(i) instanceof MoneyCard) {
                money += ((MoneyCard) hand.get(i)).getValue();
                discardPile.add(hand.get(i));
                removeCards.add(hand.get(i));
                if (money >= value) {
                    break;
                }

            }
        }
        for (Card card : removeCards) {
            hand.remove(card);
        }
    }

    /**
     * Überprüft, ob der Ablagestapel leer ist.
     *
     * @return true wenn er leer ist, sonst false
     * @author Fenja, Anna
     * @since Sprint 7
     */
    public boolean discardPileWasCleared() {
        return discardPile.size() == 0;
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

    public ArrayList<Card> getCardsDeck() {
        return cardsDeck;
    }

    public ArrayList<Card> getActionPile() {
        return actionPile;
    }
}
