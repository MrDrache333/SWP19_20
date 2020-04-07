package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;
import de.uol.swp.common.game.messages.CardMovedMessage;
import de.uol.swp.common.game.messages.ChooseNextActionMessage;
import de.uol.swp.common.game.messages.SelectCardsFromHandMessage;
import de.uol.swp.common.game.messages.ShowCardMessage;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ActionCardExecution {

    private short cardID;
    private Playground playground;
    private Player player;
    private ActionCard theCard;
    private UUID gameID;

    public ActionCardExecution(short cardID, Playground playground) {
        this.playground = playground;
        this.cardID = cardID;
        this.player = playground.getActualPlayer();
        getCardFromHand(cardID);
        this.gameID = playground.getID();
    }

    /**
     * Führt beim Aufruf alle enthaltenen Aktionen der Aktionskarte aus
     *
     * @author KenoO
     */
    public void execute() {
        //TODO Some pretty nice and clean Code to Execute the Shit out of that Card
        for (CardAction action : theCard.getActions()) {
            executeCardAction(action);
        }
    }

    /**
     * Führt unabhängige Kartenaktionen aus. (NUR AKTIONEN, DIE KEINE RÜCKGABE BENÖTIGEN (ODER AKTIONEN, AUF OBERSTER EBENE))
     *
     * @param action Die auszuführende Kartenaktion
     * @return Ob die Ausführung erfolgreich war.
     * @author KenoO
     */
    private boolean executeCardAction(CardAction action) {
        if (action instanceof AddCapablePlayerActivity)
            return executeAddCapablePlayerActivity((AddCapablePlayerActivity) action);
        if (action instanceof ChooseCard)
            return executeChooseCard((ChooseCard) action);
        if (action instanceof ChooseNextAction)
            return executeChooseNextAction((ChooseNextAction) action);
        if (action instanceof Count)
            return executeCount((Count) action);
        if (action instanceof ForEach)
            return executeForEach((ForEach) action);
        if (action instanceof GetCard)
            return executeGetCard((GetCard) action);
        if (action instanceof If)
            return executeIf((If) action);
        if (action instanceof Move)
            return executeMoveAction((Move) action);
        if (action instanceof ShowCard)
            return executeShowCard((ShowCard) action);
        if (action instanceof UseCard)
            return executeUseCard((UseCard) action);
        if (action instanceof While)
            return executeWhile((While) action);

        return false;
    }

    private boolean executeWhile(While action) {
        //TODO
        return false;
    }

    private boolean executeUseCard(UseCard action) {
        //TODO
        return false;
    }

    private boolean executeIf(If action) {
        //TODO
        return false;
    }

    private boolean executeGetCard(GetCard action) {
        //TODO
        return false;
    }

    private boolean executeForEach(ForEach action) {
        //TODO
        return false;
    }

    /**
     * Erhöht availableActions, availableBuys oder additionalMoney des Players um den angegebenen Wert.
     *
     * @param activity Aktion
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeAddCapablePlayerActivity(AddCapablePlayerActivity activity) {
        if (activity.getActivity() == AbstractPlayground.PlayerActivityValue.ACTION) {
            player.setAvailableActions(activity.getCount() + player.getAvailableActions());
        } else if (activity.getActivity() == AbstractPlayground.PlayerActivityValue.BUY) {
            player.setAvailableBuys(activity.getCount() + player.getAvailableBuys());
        } else {
            player.setAdditionalMoney(activity.getCount() + player.getAdditionalMoney());
        }
        return true;
    }

    /**
     * Sendet eine Message mit der ID der Karte, die angezeigt werden soll und der Zone,
     * in der sie angezeigt werden soll an den aktuellen Spieler.
     *
     * @param cardID die ID der Karte
     * @param zone   die Zone im Spielfeld, in der die Karte angezeigt werdem soll
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeShowCard(short cardID, AbstractPlayground.ZoneType zone) {
        playground.getGameService().sendToSpecificPlayer(player, new ShowCardMessage(cardID, zone, playground.getID()));
        return true;
    }

    /**
     * Zählt ein übergebenes KartenArray und übergibt die Größe.
     *
     * @param cards das Übergebene KartenArray
     * @return 0 = Übergebenes Array ist leer. | sonst die größe des KartenArrays
     */

    private int executeCount(ArrayList<Card> cards) {
        if (!cards.isEmpty()) {
            return cards.size();
        }
        return 0;
    }

    /**
     * Schickt dem Spieler eine Nachricht das er eine bestimmte Anzahl an Karten auswählen kann.
     *
     * @param theValue Die Anzahl der Karten
     * @param from     Woher
     * @param where    Wohin damit
     * @return true(? ? ?)
     */
    private boolean executeChooseCard(Value theValue, AbstractPlayground.ZoneType from, AbstractPlayground.ZoneType where) {
        playground.getGameService().sendToSpecificPlayer(player, new SelectCardsFromHandMessage(theValue, from, where));
        return true;
    }

    /**
     * Sendet dem Spieler eine Nachricht mit den Aktionen, aus denen er eine auswählen kann
     *
     * @param actions Liste aller möglichen nächsten Aktionen
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeChooseNextAction(List<CardAction> actions) {
        playground.getGameService().sendToSpecificPlayer(player, new ChooseNextActionMessage(gameID, actions));
        return true;
    }

    /**
     * Bewegt Karten von einem Ort zum anderen. ----------WIP----------
     *
     * @param cardsToMove
     * @param source      Woher
     * @param destination Wohin
     * @return true(? ? ?)
     */
    private boolean executeMoveAction(ArrayList<Card> cardsToMove, AbstractPlayground.ZoneType source, AbstractPlayground.ZoneType destination) {
        ArrayList<Short> theIds = new ArrayList<>();
        if (source.equals(AbstractPlayground.ZoneType.HAND) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                if (player.getPlayerDeck().getHand().contains(card)) {
                    player.getPlayerDeck().getHand().remove(card);
                    player.getPlayerDeck().getDiscardPile().add(card);
                    theIds.add(card.getId());
                }
            }
        } else if (source.equals(AbstractPlayground.ZoneType.HAND) && destination.equals(AbstractPlayground.ZoneType.TRASH)) {
            for (Card card : cardsToMove) {
                if (player.getPlayerDeck().getHand().contains(card)) {
                    player.getPlayerDeck().getHand().remove(card);
                    playground.getTrash().add(card);
                    theIds.add(card.getId());
                }
            }
        } else if (source.equals(AbstractPlayground.ZoneType.BUY) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                // TODO: BuyCard Implementierung von Paula hier einfügen.
                player.getPlayerDeck().getDiscardPile().add(card);
                theIds.add(card.getId());
            }
        } else if (source.equals(AbstractPlayground.ZoneType.DRAW) && destination.equals(AbstractPlayground.ZoneType.TEMP)) {
            // TODO: Implementierung im Playerdeck, dass der Spieler eine bestimmte Anzahl an Karten nachziehen kann.
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getCardsDeck().remove(card);
                player.getPlayerDeck().getTemp().add(card);
            }
        } else if (source.equals(AbstractPlayground.ZoneType.DRAW) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getCardsDeck().remove(card);
                player.getPlayerDeck().getDiscardPile().add(card);
            }
        } else if (source.equals(AbstractPlayground.ZoneType.DISCARD) && destination.equals(AbstractPlayground.ZoneType.DRAW)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getDiscardPile().remove(card);
                player.getPlayerDeck().getCardsDeck().add(card);
                Collections.shuffle(player.getPlayerDeck().getCardsDeck());
            }
        } else if (source.equals(AbstractPlayground.ZoneType.TEMP) && destination.equals(AbstractPlayground.ZoneType.HAND)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getTemp().remove(card);
                player.getPlayerDeck().getHand().add(card);
                theIds.add(card.getId());
            }
            if (true) {
                // TODO: Checken, ob nextAction gegeben und dann von Hand auf Discard.
            }
        } else if (source.equals(AbstractPlayground.ZoneType.TEMP) && destination.equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : cardsToMove) {
                player.getPlayerDeck().getTemp().remove(card);
                player.getPlayerDeck().getDiscardPile().add(card);
                theIds.add(card.getId());
            }
        }
        if (!theIds.isEmpty()) {
            CardMovedMessage cardMovedMessage = new CardMovedMessage(theIds, source, destination, gameID);
            playground.getGameService().sendToSpecificPlayer(player, cardMovedMessage);
        }
        return true;
    }

    // vorläufige Hilfsmethode //
    private void getCardFromHand(short cardID) {

        for (int i = 0; i < player.getPlayerDeck().getHand().size(); i++) {

            if (player.getPlayerDeck().getHand().get(i).getId() == cardID) {
                theCard = (ActionCard) player.getPlayerDeck().getHand().get(i);
                break;
            }

        }
    }
}
