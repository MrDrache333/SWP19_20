package de.uol.swp.server.game.phase;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;
import de.uol.swp.common.game.messages.CardMovedMessage;
import de.uol.swp.common.game.messages.ChooseNextActionMessage;
import de.uol.swp.common.game.messages.SelectCardsFromHandMessage;
import de.uol.swp.common.game.messages.ShowCardMessage;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ActionCardExecution {

    private static final Logger LOG = LogManager.getLogger(ActionCardExecution.class);

    //TODO
    /*

    Was passiert, wenn das Attribut executeType andere Spieler involviert?


     */

    //TODO: Subscribe Methode -> waitedForPlayerInput prüfen -> auf false setzen -> benötigen Antwort vom Client
    // -> ChooseCard (IDs der gewählten Karten) und ChooseNextAction (gewählte Aktion)

    private short cardID;
    private Playground playground;
    private Player player;
    private ActionCard theCard;
    private UUID gameID;
    private boolean waitedForPlayerInput;
    private int actualStateIndex;

    public ActionCardExecution(short cardID, Playground playground) {
        this.waitedForPlayerInput = false;
        this.actualStateIndex = 0;
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
     * @return
     */
    public boolean execute() {
        //????????????????????????????????????
        //TODO Some pretty nice and clean Code to Execute the Shit out of that Card
        if (actualStateIndex < theCard.getActions().size() && !waitedForPlayerInput) {
            return executeCardAction(theCard.getActions().get(actualStateIndex));
        }
        //for (CardAction action : theCard.getActions()) {
        //  if (!executeCardAction(action)) return false;
        //}
        return false;
    }

    /**
     * Führt unabhängige Kartenaktionen aus. (NUR AKTIONEN, DIE KEINE RÜCKGABE BENÖTIGEN (ODER AKTIONEN, AUF OBERSTER EBENE))
     *
     * @param action Die auszuführende Kartenaktion
     * @return Ob die Ausführung erfolgreich war.
     * @author KenoO
     */
    private boolean executeCardAction(CardAction action) {
        if (!(action instanceof ChooseNextAction) && !(action instanceof ChooseCard)) actualStateIndex++;
        if (action instanceof AddCapablePlayerActivity)
            return executeAddCapablePlayerActivity((AddCapablePlayerActivity) action);
        if (action instanceof ChooseCard)
            return executeChooseCard((ChooseCard) action);
        if (action instanceof ChooseNextAction)
            return executeChooseNextAction((ChooseNextAction) action);
        if (action instanceof ForEach)
            return executeForEach((ForEach) action);
        if (action instanceof If)
            return executeIf((If) action);
        if (action instanceof ShowCard)
            return executeShowCard((ShowCard) action);
        if (action instanceof UseCard)
            return executeUseCard((UseCard) action);
        if (action instanceof While)
            return executeWhile((While) action);

        return false;
    }

    /**
     * Führt eine Kartenaktion mit Eingabe von Karten aus und gibt das Ergebnis zurück
     *
     * @param action Auszuführende Aktion
     * @param input  Eingabe an Karten
     * @return Ergebnis
     */
    private ArrayList<Card> executeCardAction(CardAction action, ArrayList<Card> input) throws NullPointerException {
        if (action == null || input == null) throw new NullPointerException("Actions nor input can't be null");
        //TODO
        return null;
    }

    /**
     * Filtert ein Array an Karten anhand der Eigenschaften in der übergebenen Aktion
     *
     * @param action Die Kartenaktion mit den Filtereigenschaften
     * @param cards  Die zu filternden Karten
     * @return Das gefilterte Kartenarray
     * @author Julia
     * @since Sprint6
     */
    private ArrayList<Card> filterCards(ComplexCardAction action, ArrayList<Card> cards) {
        Card.Type allowedType = action.getAllowedCardType();
        if (allowedType == Card.Type.ActionCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.ActionCard);
        } else if (allowedType == Card.Type.ReactionCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.ReactionCard);
        } else if (allowedType == Card.Type.MoneyCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.MoneyCard);
        } else if (allowedType == Card.Type.ValueCard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.ValueCard);
        } else if (allowedType == Card.Type.Cursecard) {
            cards.removeIf(c -> c.getCardType() != Card.Type.Cursecard);
        }

        if (action.getHasCost().isSet()) {
            cards.removeIf(c -> c.getCosts() < action.getHasCost().getMin() || c.getCosts() > action.getHasCost().getMax());
        }

        if (action.getHasWorth().isSet()) {
            cards.removeIf(c -> c.getCardType() == Card.Type.ActionCard || c.getCardType() == Card.Type.ReactionCard);
            List<Card> tmp = new ArrayList<>();
            cards.forEach(card -> {
                if (card.getCardType() == Card.Type.ValueCard) {
                    if (((ValueCard) card).getValue() < action.getHasWorth().getMin() || ((ValueCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                } else if (card.getCardType() == Card.Type.MoneyCard) {
                    if (((MoneyCard) card).getValue() < action.getHasWorth().getMin() || ((MoneyCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                } else {
                    if (((CurseCard) card).getValue() < action.getHasWorth().getMin() || ((CurseCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                }
            });

            cards.removeAll(tmp);
        }

        return cards;
    }

    private boolean executeWhile(While action) {
        //TODO
        return false;
    }

    private boolean executeUseCard(UseCard action) {
        if (action.getCardId() == 0) {
            LOG.debug("No CardId specified! Using current Card(" + cardID + ")");
            action.setCardId(cardID);
        }
        ActionCardExecution execution = new ActionCardExecution(action.getCardId(), playground);

        for (int i = 0; i < action.getCount(); i++) {
            if (!execution.execute()) return false;
        }
        return true;
    }

    private boolean executeIf(If action) {
        //TODO
        return false;
    }

    private ArrayList<Card> executeGetCard(GetCard action) {
        if (action.getCardSource() != AbstractPlayground.ZoneType.NONE) {
            switch (action.getCardSource()) {

                case TRASH:
                    action.setCards(playground.getTrash());
                    break;
                case HAND:
                    action.setCards(player.getPlayerDeck().getHand());
                    break;
                case BUY:
                    playground.getCardsPackField().getCards().getAllCards().forEach(card -> {
                        if (playground.getCardField().containsKey(card.getId())) {
                            if (playground.getCardField().get(card.getId()) > 0) {
                                action.getCards().add(card);
                            }
                        }
                    });
                    break;
                case DRAW:
                    action.setCards(player.getPlayerDeck().getCardsDeck());
                    break;
                case DISCARD:
                    action.setCards(player.getPlayerDeck().getDiscardPile());
                    break;
                case TEMP:
                    action.setCards(player.getPlayerDeck().getTemp());
                    break;
            }
            action.setCards(filterCards(action, action.getCards()));
        }
        return action.getCards();
    }

    /**
     * Führt eine Reihe von Aktionen auf eine beliebige Anzahl an Karten aus
     *
     * @param action Die ForEach-Aktion
     * @return
     */
    private boolean executeForEach(ForEach action) {
        try {
            for (Card card : action.getCards()) {
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card);
                for (CardAction cardAction : action.getActions()) {
                    cards = executeCardAction(cardAction, cards);
                }
            }
        } catch (NullPointerException e) {
            LOG.error(e.fillInStackTrace());
            return false;
        }
        return true;
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
     * @param showCard
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeShowCard(ShowCard showCard) {
        playground.getGameService().sendToSpecificPlayer(player, new ShowCardMessage(showCard.getCard().getId(), showCard.getCardSource(), gameID));
        return true;
    }

    /**
     * Zählt ein übergebenes KartenArray und übergibt die Größe.
     *
     * @return 0 = Übergebenes Array ist leer. | sonst die größe des KartenArrays
     */

    private int executeCount(Count action) {
        if (!action.getCards().isEmpty()) {
            return action.getCards().size();
        }
        return 0;
    }

    /**
     * Schickt dem Spieler eine Nachricht das er eine bestimmte Anzahl an Karten auswählen kann.
     *
     * @param action Die Anzahl der Karten
     * @return true(? ? ?)
     */
    private boolean executeChooseCard(ChooseCard action) {
        waitedForPlayerInput = true;
        playground.getGameService().sendToSpecificPlayer(player, new SelectCardsFromHandMessage(action.getCount(), action.getCardSource(), action.getCardDestination()));
        return true;

    }

    /**
     * Sendet dem Spieler eine Nachricht mit den Aktionen, aus denen er eine auswählen kann
     *
     * @param chooseNextAction
     * @return true
     * @author Julia
     * @since Sprint6
     */
    private boolean executeChooseNextAction(ChooseNextAction chooseNextAction) {
        waitedForPlayerInput = true;
        playground.getGameService().sendToSpecificPlayer(player, new ChooseNextActionMessage(gameID, chooseNextAction.getNextActions()));
        return true;
    }

    /**
     * Bewegt Karten von einem Ort zum anderen. ----------WIP----------
     *
     * @return true(? ? ?)
     */
    private boolean executeMoveAction(Move action) {
        ArrayList<Short> theIds = new ArrayList<>();
        if (action.getCardSource().equals(AbstractPlayground.ZoneType.HAND) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : action.getCardsToMove()) {
                if (player.getPlayerDeck().getHand().contains(card)) {
                    player.getPlayerDeck().getHand().remove(card);
                    player.getPlayerDeck().getDiscardPile().add(card);
                    theIds.add(card.getId());
                }
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.HAND) && action.getCardDestination().equals(AbstractPlayground.ZoneType.TRASH)) {
            for (Card card : action.getCardsToMove()) {
                if (player.getPlayerDeck().getHand().contains(card)) {
                    player.getPlayerDeck().getHand().remove(card);
                    playground.getTrash().add(card);
                    theIds.add(card.getId());
                }
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.BUY) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : action.getCardsToMove()) {
                // TODO: BuyCard Implementierung von Paula hier einfügen.
                player.getPlayerDeck().getDiscardPile().add(card);
                theIds.add(card.getId());
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.DRAW) && action.getCardDestination().equals(AbstractPlayground.ZoneType.TEMP)) {
            // TODO: Implementierung im Playerdeck, dass der Spieler eine bestimmte Anzahl an Karten nachziehen kann.
            for (Card card : action.getCardsToMove()) {
                player.getPlayerDeck().getCardsDeck().remove(card);
                player.getPlayerDeck().getTemp().add(card);
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.DRAW) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : action.getCardsToMove()) {
                player.getPlayerDeck().getCardsDeck().remove(card);
                player.getPlayerDeck().getDiscardPile().add(card);
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.DISCARD) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DRAW)) {
            for (Card card : action.getCardsToMove()) {
                player.getPlayerDeck().getDiscardPile().remove(card);
                player.getPlayerDeck().getCardsDeck().add(card);
                Collections.shuffle(player.getPlayerDeck().getCardsDeck());
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.TEMP) && action.getCardDestination().equals(AbstractPlayground.ZoneType.HAND)) {
            for (Card card : action.getCardsToMove()) {
                player.getPlayerDeck().getTemp().remove(card);
                player.getPlayerDeck().getHand().add(card);
                theIds.add(card.getId());
            }
            if (true) {
                // TODO: Checken, ob nextAction gegeben und dann von Hand auf Discard.
            }
        } else if (action.getCardSource().equals(AbstractPlayground.ZoneType.TEMP) && action.getCardDestination().equals(AbstractPlayground.ZoneType.DISCARD)) {
            for (Card card : action.getCardsToMove()) {
                player.getPlayerDeck().getTemp().remove(card);
                player.getPlayerDeck().getDiscardPile().add(card);
                theIds.add(card.getId());
            }
        }
        if (!theIds.isEmpty()) {
            CardMovedMessage cardMovedMessage = new CardMovedMessage(theIds, action.getCardSource(), action.getCardDestination(), gameID);
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
