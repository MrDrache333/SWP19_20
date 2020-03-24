package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.CardPack;
import de.uol.swp.common.game.card.parser.CardStack;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.exception.NotEnoughMoneyException;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;


/**
 * Die Funktionsklasse aller Phasen
 */
public class CompositePhase implements ActionPhase, BuyPhase, ClearPhase {


    private Playground playGround;

    public CompositePhase(Playground playGround) {
        this.playGround = playGround;
    }

    /**
     * Methode um eine Karte zu kaufen. Diese wird dem Ablagestapel des Spielers hinzugefügt, wenn er genug Geld hat
     *
     * @param player Der Spieler
     * @param cardId Die Karten-ID
     * @author Paula
     * @since Sprint6
     */
    @Override
    public void executeBuyPhase(Player player, short cardId) {
        CardPack cardsPackField = new JsonCardParser().loadPack("Basispack");
        Card currentCard = getCardFromId(cardsPackField.getCards(), cardId);
        // Karten und deren Anzahl werden aus dem Spielfeld geladen.
        int count = Playground.getCardField().get(cardId);
        if (count > 0) {
            // Falls die ID der Karte nicht vorhanden ist, wird eine Exception geworfen
            if (currentCard == null) {
                throw new IllegalArgumentException("CardID wurde nicht gefunden");
            }
            //Falls die ID vorhanden ist wird der Geldwert des Spielers wird berechnet, hat er
            // genug Geld, wird die Karte seinem Ablagestapel hinzugefügt, das Geld wird ihm entzogen
            // und die Anzahl der Karte auf dem Spielfeld verringert sich um eins
            int moneyValuePlayer = player.getPlayerDeck().actualMoneyFromPlayer();
            if (moneyValuePlayer < currentCard.getCosts()) {
                throw new NotEnoughMoneyException("Nicht genug Geld vorhanden");
            }
            player.getPlayerDeck().addCardToDiscardPile(currentCard);
            moneyValuePlayer -= currentCard.getCosts();
            player.getPlayerDeck().discardMoneyCardsForValue(currentCard.getCosts());
            Playground.getCardField().put(cardId, --count);
        }
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
     * Hilfsmethode um an die Daten über die ID zu kommen
     *
     * @param cardStack
     * @param cardId
     * @return
     * @author Paula
     * @since Sprint6
     */

    private Card getCardFromId(CardStack cardStack, short cardId) {
        for (Card card : cardStack.getActionCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        for (Card card : cardStack.getMoneyCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        for (Card card : cardStack.getReactionCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        for (Card card : cardStack.getValueCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        return null;
    }
}

