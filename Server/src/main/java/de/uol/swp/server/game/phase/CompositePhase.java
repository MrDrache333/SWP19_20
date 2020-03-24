package de.uol.swp.server.game.phase;

import com.google.inject.internal.cglib.reflect.$FastMethod;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.CardPack;
import de.uol.swp.common.game.card.parser.CardStack;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.exception.NotEnoughMoneyException;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;
import io.netty.util.internal.logging.AbstractInternalLogger;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Die Funktionsklasse aller Phasen
 */
public class CompositePhase implements ActionPhase, BuyPhase, ClearPhase {

    private Playground playGround;

    public CompositePhase(Playground playGround) {
        this.playGround = playGround;
    }

    @Override
    public void executeBuyPhase(Player player, short cardId){
        CardPack cardsPackField = new JsonCardParser().loadPack("Basispack");
        Card currentCard = getCardFromId(cardsPackField.getCards(), cardId);
        if (currentCard == null) {
            throw new IllegalArgumentException("CardID wurde nicht gefunden");
        }
        int count = Playground.getCardField().get(cardId);

        //Falls gefunden: Geldwert des Spielers wird berechnet
        int moneyValuePlayer = player.getPlayerDeck().actualMoneyFromPlayer();
        if (moneyValuePlayer < currentCard.getCosts()) {
            throw new NotEnoughMoneyException("Kein Geld");
        }
        // Kauf gelungen: Karte dem Ablagstapel hinzufügen, aus der Mitte entfernen und Geld abziehen
        player.getPlayerDeck().addCardToDiscardPile(currentCard);
        // Geld abziehen
        moneyValuePlayer -= currentCard.getCosts();
        player.getPlayerDeck().discardMoneyCardsForValue(currentCard.getCosts());
        // Karte aus der Mitte entfernen
        if (count > 0) {
            Playground.getCardField().put(cardId, --count);
        }

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
     * Hilfsmethode um von der ID an die Karte zu kommen
     *
     * @param hand
     * @param cardId
     * @return
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

