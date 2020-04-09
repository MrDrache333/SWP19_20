package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.card.parser.components.CardStack;
import de.uol.swp.common.game.exception.NotEnoughMoneyException;
import de.uol.swp.common.game.messages.GameOverMessage;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Deck;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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

    private static final Logger LOG = LogManager.getLogger(GameService.class);


    /**
     * Methode um eine Karte zu kaufen. Diese wird dem Ablagestapel des Spielers hinzugefügt, wenn er genug Geld hat
     *
     * @param player Der Spieler
     * @param cardId Die Karten-ID
     * @author Paula
     * @since Sprint6
     */
    @Override
    public int executeBuyPhase(Player player, short cardId) {
        CardPack cardsPackField = playground.getCardsPackField();
        Card currentCard = getCardFromId(cardsPackField.getCards(), cardId);
        // Karten und deren Anzahl werden aus dem Spielfeld geladen.
        int count = playground.getCardField().get(cardId);
        if (count > 0) {
            // Falls die ID der Karte nicht vorhanden ist, wird eine Exception geworfen
            if (currentCard == null) {
                throw new IllegalArgumentException("CardID wurde nicht gefunden");
            }
            /*Falls die ID vorhanden ist wird der Geldwert des Spielers berechnet, hat er
              genug Geld, wird die Karte seinem Ablagestapel hinzugefügt, das Geld wird ihm entzogen
              und die Anzahl der Karte auf dem Spielfeld verringert sich um eins
            */
            int moneyValuePlayer = player.getPlayerDeck().actualMoneyFromPlayer();
            if (moneyValuePlayer < currentCard.getCosts()) {
                LOG.error("Nicht genug Geld");
                // TODO: Client muss eine Fehlermeldung erstellen, die angezeigt wird, wenn der Spieler nicht genug Geld hat
                throw new NotEnoughMoneyException("Nicht genug Geld vorhanden");
            }
            player.getPlayerDeck().getDiscardPile().add(currentCard);
            moneyValuePlayer -= currentCard.getCosts();
            // TODO: Client muss Geldkarten aus Hand abziehen (und in Ablagestapel legen?), wenn Kauf gelungen.
            player.getPlayerDeck().discardMoneyCardsForValue(currentCard.getCosts());
            // TODO: Client: Stückzahl der Karten anpassen bzw.: wenn Karte nicht vorhanden, keine Bild der Karte
            playground.getCardField().put(cardId, --count);
        }
        return count;
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
        if (checkIfGameIsFinished()) {
            List<String> winners = playground.calculateWinners();
            playground.endGame(playground.getID(), new GameOverMessage(playground.getID(), winners, playground.getResultsGame()));
        } else {
            playground.newTurn();
        }
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
     * @return card Karte, zu der die ID gehört
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

        for (Card card : cardStack.getValueCards()) {
            if (card.getId() == cardId) {
                return card;
            }
        }
        return null;
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

