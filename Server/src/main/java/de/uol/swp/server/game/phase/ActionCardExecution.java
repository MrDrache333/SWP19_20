package de.uol.swp.server.game.phase;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.ComplexCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.request.ChooseCardRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.request.OptionalActionRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.Playground;
import de.uol.swp.server.game.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@SuppressWarnings("UnstableApiUsage, unused")
public class ActionCardExecution {

    private static final Logger LOG = LogManager.getLogger(ActionCardExecution.class);

    private final short cardID;
    private final Playground playground;
    private final Player player;
    private ActionCard theCard;
    private final UUID gameID;
    private final List<Player> players;
    private final List<User> chooseCardPlayers = new ArrayList<>();
    //Liste aller Unteraktionen einer Aktion
    private final List<CardAction> nextActions = new ArrayList<>();
    private Card inputCard;
    private boolean useCardExecution;
    private ActionCardExecution parent;
    private Integer useCardCounter = 0;
    private UseCard useAction;
    private int actionExecutionID;
    private ActionCardExecution execution;

    //Ob auf eine Auswahl oder Reaktion des Spielers gewartet werden muss
    private boolean waitedForPlayerInput;

    //Index der aktuell auszuführenden Aktion
    private int actualStateIndex;

    private boolean startedNextActions;
    //Ob alle Unteraktionen einer Aktion ausgeführt wurden
    private boolean finishedNextActions;

    //Index der aktuell auszuführenden Unteraktion
    private int nextActionIndex;
    //Ob eine optionale Aktion ausgeführt werden soll
    private boolean executeOptionalAction;

    //Ob die Aktionskarte entsorgt werden soll
    private boolean removeCardAfter;

    //Ob die Ausfühung einer Karte schon fertig ist
    private boolean finishedExecution;

    public ActionCardExecution(short cardID, Playground playground, boolean useCardExecution, int actionExecutionID) {
        this.waitedForPlayerInput = false;
        this.actualStateIndex = 0;
        this.playground = playground;
        this.cardID = cardID;
        this.player = playground.getActualPlayer();
        getCardFromHand(cardID);
        this.gameID = playground.getID();
        this.players = playground.getPlayers();
        this.nextActionIndex = 0;
        this.finishedNextActions = true;
        this.executeOptionalAction = false;
        this.startedNextActions = false;
        this.useCardExecution = useCardExecution;
        this.actionExecutionID = actionExecutionID;
    }

    /**
     * Führt eine optionale Aktion oder überspringt diese (je nach Entscheidung des Spielers)
     *
     * @param response Die OptionalActionResponse
     * @author Julia
     * @since Sprint 7
     */
    @Subscribe
    public void onOptionalActionResponse(OptionalActionResponse response) {
        LOG.debug("OptionalActionResponse von " + response.getPlayer().getUsername() + " erhalten");
        if (response.getGameID().equals(gameID) && response.getActionExecutionID() == actionExecutionID) {
            if (!response.isExecute()) actualStateIndex++;
            else executeOptionalAction = true;
            waitedForPlayerInput = false;
            execute();
        }
    }

    /**
     * Reaktion auf eine ChooseCardResponse
     *
     * @param response Die ChooseCardResponse
     * @author Ferit, Julia
     * @since Sprint 7
     */
    @Subscribe
    public void onChooseCardResponse(ChooseCardResponse response) {
        if (response.getGameID().equals(gameID) && response.getActionExecutionID() == actionExecutionID && waitedForPlayerInput && chooseCardPlayers.contains(response.getPlayer())) {
            LOG.debug("ChooseCardResponse von " + response.getPlayer().getUsername() + " erhalten");
            List<Player> p = new ArrayList<>();
            Player helpPlayer = helpMethodToGetThePlayerFromUser(response.getPlayer());
            p.add(helpPlayer);
            waitedForPlayerInput = false;
            if (!startedNextActions) actualStateIndex++;
            if (nextActions.get(nextActionIndex) instanceof ChooseCard) nextActionIndex++;
            if (response.getCards().isEmpty()) {
                finishedNextActions = true;
                nextActionIndex = 0;
                nextActions.clear();
                execute();
                return;
            }
            if (response.getCards().size() == 1) {
                inputCard = helpMethod2GetTheCard(response.getCards().get(0));
            }

            ArrayList<Card> cards = new ArrayList<>();
            response.getCards().forEach(c -> cards.add(helpMethod2GetTheCard(c)));
            if (nextActions.get(nextActionIndex) instanceof Move) {
                ((Move) nextActions.get(nextActionIndex)).setCardsToMove(cards);
            } else if (nextActions.get(nextActionIndex) instanceof ForEach) {
                ((ForEach) nextActions.get(nextActionIndex)).setCards(cards);
            } else if (nextActions.get(nextActionIndex) instanceof UseCard) {
                ((UseCard) nextActions.get(nextActionIndex)).setCardId(response.getCards().get(0));
            }

            executeNextActions(p);
        }
    }

    /**
     * Führt beim Aufruf alle enthaltenen Aktionen der Aktionskarte aus
     *
     * @return Boolean, ob die Aktionen ausgeführt worden sind
     * @author KenoO, Julia
     * @since Sprint 6
     */
    public boolean execute() {
        while (actualStateIndex < theCard.getActions().size() && !waitedForPlayerInput && finishedNextActions) {
            startedNextActions = false;
            CardAction action = theCard.getActions().get(actualStateIndex);
            if (action instanceof ComplexCardAction && ((ComplexCardAction) action).isExecutionOptional() && !executeOptionalAction) {
                playground.getGameService().sendToSpecificPlayer(player, new OptionalActionRequest(gameID, player.getTheUserInThePlayer(), ((ComplexCardAction) action).getExecutionOptionalMessage(), actionExecutionID));
                waitedForPlayerInput = true;
            } else {
                getNextActions(action);
                if (!(action instanceof ChooseNextAction) && !(action instanceof ChooseCard)) {
                    actualStateIndex++;
                }
                List<Player> playerList = getAffectedPlayers(action);
                if (!(action instanceof GetCard) && !(action instanceof Move)) {
                    if (!executeCardAction(action, playerList)) return false;
                } else {
                    executeCardAction(action, null, playerList, false);
                }
                if (nextActions.isEmpty()) {
                    finishedNextActions = true;
                    nextActionIndex = 0;
                } else {
                    finishedNextActions = false;
                }

                if (!nextActions.isEmpty() && !waitedForPlayerInput) {
                    if (!executeNextActions(playerList)) return false;
                }
            }
            if (action instanceof ComplexCardAction && ((ComplexCardAction) action).isRemoveCardAfter()) {
                removeCardAfter = true;
            }
            executeOptionalAction = false;
        }

        checkIfComplete();
        return true;
    }

    /**
     * Führt alle Unteraktionen einer Action aus
     *
     * @return false sobald eine Aktion nicht erfolgreich ausgeführt werden konnte
     * @author Julia
     * @since Sprint 7
     */
    private boolean executeNextActions(List<Player> playerList) {
        startedNextActions = true;
        while (nextActionIndex < nextActions.size() && !waitedForPlayerInput) {
            CardAction action = nextActions.get(nextActionIndex);
            if (!(action instanceof ChooseNextAction) && !(action instanceof ChooseCard)) {
                nextActionIndex++;
            }
            if (!(action instanceof GetCard) && !(action instanceof Move)) {
                if (!executeCardAction(action, playerList)) return false;
            } else {
                executeCardAction(action, null, playerList, false);
            }
        }

        //Alle Unteraktionen wurden vollständig ausgeführt
        if (nextActionIndex == nextActions.size() && !waitedForPlayerInput) {
            finishedNextActions = true;
            nextActions.clear();
            nextActionIndex = 0;
            if (!checkIfComplete()) {
                execute();
            }
        }

        return true;
    }

    /**
     * Führt unabhängige Kartenaktionen aus. (NUR AKTIONEN, DIE KEINE RÜCKGABE BENÖTIGEN (ODER AKTIONEN, AUF OBERSTER EBENE))
     *
     * @param action     Die auszuführende Kartenaktion
     * @param thePlayers Liste aller Player, auf die die Aktion ausgeführt werden soll
     * @return Ob die Ausführung erfolgreich war.
     * @author KenoO
     * @since Sprint 6
     */
    private boolean executeCardAction(CardAction action, List<Player> thePlayers) {
        if (action == null) throw new NullPointerException("Action can't be null");
        if (action instanceof AddCapablePlayerActivity)
            return executeAddCapablePlayerActivity((AddCapablePlayerActivity) action);
        if (action instanceof ChooseCard)
            return executeChooseCard((ChooseCard) action, thePlayers);
        if (action instanceof ChooseNextAction)
            return executeChooseNextAction((ChooseNextAction) action);
        if (action instanceof ForEach)
            return executeForEach((ForEach) action, thePlayers);
        if (action instanceof If)
            return executeIf((If) action, thePlayers);
        if (action instanceof ShowCard)
            return executeShowCard((ShowCard) action, thePlayers);
        if (action instanceof UseCard)
            return executeUseCard((UseCard) action);
        if (action instanceof While)
            return executeWhile((While) action, thePlayers);

        return false;
    }

    /**
     * Führt eine Kartenaktion mit Eingabe von Karten aus und gibt das Ergebnis zurück
     *
     * @param action Auszuführende Aktion
     * @param input  Eingabe an Karten
     * @return Ergebnis
     * @author KenoO, Julia
     * @since Sprint 6
     */
    private ArrayList<Card> executeCardAction(CardAction action, ArrayList<Card> input, List<Player> playerList, boolean foreach) throws NullPointerException {
        if (action == null) throw new NullPointerException("Action can't be null");
        for (Player player : playerList) {
            if (action instanceof Move) {
                if (input == null && ((Move) action).getCardsToMove() == null)
                    throw new NullPointerException("No cards to move!");
                if (input != null) ((Move) action).setCardsToMove(input);
                executeMoveAction((Move) action, player);
            } else if (action instanceof GetCard) {
                GetCard getAction = (GetCard) action;
                ArrayList<Card> c = executeGetCard(getAction, player);
                if (!foreach) {
                    CardAction nextAction = nextActions.get(nextActionIndex);
                    if (nextAction instanceof Move) {
                        ((Move) nextAction).setCardsToMove(c);
                    } else if (nextAction instanceof ShowCard && c.size() == 1) {
                        ((ShowCard) nextAction).setCard(c.get(0));
                    } else if (nextAction instanceof ForEach) {
                        ((ForEach) nextAction).setCards(c);
                    }
                }
                return c;
            }
        }
        return null;
    }

    /**
     * Filtert ein Array an Karten anhand der Eigenschaften in der übergebenen Aktion
     *
     * @param action Die Kartenaktion mit den Filtereigenschaften
     * @param cards  Die zu filternden Karten
     * @return Das gefilterte Kartenarray
     * @author Julia
     * @since Sprint 6
     */
    private ArrayList<Card> filterCards(ComplexCardAction action, ArrayList<Card> cards) {
        ArrayList<Card> result = new ArrayList<>(cards);
        Card.Type allowedType = action.getAllowedCardType();
        if (allowedType == Card.Type.ACTIONCARD) {
            result.removeIf(c -> c == null || c.getCardType() != Card.Type.ACTIONCARD);
        } else if (allowedType == Card.Type.REACTIONCARD) {
            result.removeIf(c -> c == null || c.getCardType() != Card.Type.REACTIONCARD);
        } else if (allowedType == Card.Type.MONEYCARD) {
            result.removeIf(c -> c == null || c.getCardType() != Card.Type.MONEYCARD);
        } else if (allowedType == Card.Type.VALUECARD) {
            result.removeIf(c -> c == null || c.getCardType() != Card.Type.VALUECARD);
        } else if (allowedType == Card.Type.CURSECARD) {
            result.removeIf(c -> c == null || c.getCardType() != Card.Type.CURSECARD);
        }

        if (action.getHasCost() != null) {
            result.removeIf(c -> c == null || c.getCosts() < action.getHasCost().getMin() || c.getCosts() > action.getHasCost().getMax());
        }
        if (action.getHasWorth() != null) {
            result.removeIf(c -> c.getCardType() == Card.Type.ACTIONCARD || c.getCardType() == Card.Type.REACTIONCARD);
            List<Card> tmp = new ArrayList<>();
            result.forEach(card -> {
                if (card.getCardType() == Card.Type.VALUECARD) {
                    if (((ValueCard) card).getValue() < action.getHasWorth().getMin() || ((ValueCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                } else if (card.getCardType() == Card.Type.MONEYCARD) {
                    if (((MoneyCard) card).getValue() < action.getHasWorth().getMin() || ((MoneyCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                } else {
                    if (((CurseCard) card).getValue() < action.getHasWorth().getMin() || ((CurseCard) card).getValue() > action.getHasWorth().getMax()) {
                        tmp.add(card);
                    }
                }
            });

            result.removeAll(tmp);
        }

        return result;
    }

    private boolean executeWhile(While action, List<Player> thePlayers) {
        //TODO
        return false;
    }

    /**
     * Spielt eine Aktionskarte aus
     *
     * @param action Die UseCard Action
     * @return Ob die Ausführung erfolgreich war
     * @author Keno O., Julia
     * @since Sprint 6
     */
    private boolean executeUseCard(UseCard action) {
        waitedForPlayerInput = true;
        useAction = action;
        if (action.getCardId() == 0) {
            LOG.debug("No CardId specified! Using current Card(" + cardID + ")");
            action.setCardId(cardID);
        }
        if (useCardCounter < action.getCount()) {
            if (useCardCounter == 0) {
                Card card = player.getPlayerDeck().getHand().stream().filter(c -> c.getId() == action.getCardId()).findFirst().orElse(null);
                execution = new ActionCardExecution(action.getCardId(), playground, true, 1);
                playground.getGameService().getBus().register(execution);
                execution.setParent(this);
                player.getPlayerDeck().getHand().remove(card);
            }
            execution.reset();
            execution.execute();
            PlayCardMessage msg = new PlayCardMessage(gameID, player.getTheUserInThePlayer(), action.getCardId(), true, execution.isRemoveCardAfter());
            playground.getGameService().sendToAllPlayers(gameID, msg);
        } else {
            playground.getGameService().getBus().unregister(execution);
            waitedForPlayerInput = false;
            useCardCounter = 0;
            List<Player> playerList = new ArrayList<>();
            playerList.add(player);
            executeNextActions(playerList);
        }
        return true;
    }

    private boolean executeIf(If action, List<Player> thePlayers) {
        //TODO
        return false;
    }

    /**
     * Methode, um eine bestimmte Anzahl Karten aus einer Zone zu bekommen
     *
     * @param action Die Aktion
     * @param player Der Spieler
     * @return Liste der Karten
     * @author KenoO, Julia
     * @since Sprint 6
     */
    private ArrayList<Card> executeGetCard(GetCard action, Player player) {
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
                                if (action.getCards() == null) {
                                    ArrayList<Card> list = new ArrayList<>();
                                    action.setCards(list);
                                }
                                action.getCards().add(card);
                            }
                        }
                    });
                    break;
                case DRAW:
                    action.setCards(player.getPlayerDeck().getCardsDeck());
                    if (action.getCards().size() < action.getCount() && action.getCount() < 254) {
                        int missingCards = action.getCount() - action.getCards().size();
                        List<Card> discard = player.getPlayerDeck().getDiscardPile();
                        Collections.shuffle(discard);
                        int i = 0;
                        int size = discard.size();
                        while (i < missingCards && i < size) {
                            Card card = discard.get(i);
                            action.getCards().add(card);
                            player.getPlayerDeck().getCardsDeck().remove(card);
                            if (discard.size() != size) {
                                discard.add(i, card);
                            }
                            i++;
                        }
                        player.getPlayerDeck().getCardsDeck().addAll(discard);
                        player.getPlayerDeck().getDiscardPile().clear();
                    }
                    break;
                case DISCARD:
                    action.setCards(player.getPlayerDeck().getDiscardPile());
                    break;
                case TEMP:
                    action.setCards(player.getPlayerDeck().getTemp());
                    break;
            }
            ArrayList<Card> cards = filterCards(action, action.getCards());
            if (cards.size() > action.getCount()) {
                ArrayList<Card> tmp = new ArrayList<>();
                for (int i = 0; i < action.getCount(); i++) {
                    tmp.add(cards.get(i));
                }
                action.setCards(tmp);
            } else action.setCards(cards);
        }
        return action.getCards();
    }

    /**
     * Führt eine Reihe von Aktionen auf eine beliebige Anzahl an Karten aus
     *
     * @param action Die ForEach-Aktion
     * @return Ob die Ausführung erfolgreich war
     * @author KenoO, Julia
     * @since Sprint 6
     */
    private boolean executeForEach(ForEach action, List<Player> playerList) {
        try {
            int size = action.getCards().size();
            for (int i = 0; i < size; i++) {
                Card card = action.getCards().get(i);
                ArrayList<Card> cards = new ArrayList<>();
                cards.add(card);
                for (CardAction cardAction : action.getActions()) {
                    if (cardAction instanceof Move || cardAction instanceof GetCard) {
                        ArrayList<Card> c = executeCardAction(cardAction, cards, playerList, true);
                        if (c != null) {
                            cards = c;
                        }
                    } else executeCardAction(cardAction, playerList);
                }
                if (action.getCards().size() != size) {
                    action.getCards().add(i, card);
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
     * @since Sprint 6
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
     * @param showCard Die anzuzeigende Karte
     * @return true
     * @author Julia
     * @since Sprint 6
     */
    private boolean executeShowCard(ShowCard showCard, List<Player> thePlayers) {
        for (Player player : thePlayers) {
            playground.getGameService().sendToSpecificPlayer(player, new ShowCardMessage(showCard.getCard().getId(), showCard.getCardSource(), gameID));
        }
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
     * Schickt den ausgewählten Spielern eine Nachricht, dass sie eine Anzahl an Karten auswählen können.
     *
     * @param action     Die Anzahl der Karten
     * @param thePlayers Die Player, die Karten auswählen dürfen.
     * @return Ob die Methode erfolgreich war
     * @author Ferit, Julia
     * @since Sprint 7
     */

    private boolean executeChooseCard(ChooseCard action, List<Player> thePlayers) {
        waitedForPlayerInput = true;
        for (Player playerChooseCard : thePlayers) {
            this.chooseCardPlayers.add(playerChooseCard.getTheUserInThePlayer());
            ArrayList<Card> tmp = new ArrayList<>();
            ArrayList<Short> theSelectableCards = new ArrayList<>();
            if (action.getCardSource() == AbstractPlayground.ZoneType.HAND) {
                tmp.addAll(playerChooseCard.getPlayerDeck().getHand());
            } else if (action.getCardSource() == AbstractPlayground.ZoneType.BUY) {
                List<Short> l = new ArrayList<>(playground.getCardField().keySet());
                for (Short card : l) {
                    Card c = playground.getCompositePhase().getCardFromId(playground.getCardsPackField().getCards(), card);
                    if (playground.getCardField().get(card) > 0) {
                        tmp.add(c);
                    }
                }
            }
            tmp = filterCards(action, tmp);
            if (action.getHasMoreCostThanInput() != null) {
                if (inputCard == null) return false;
                tmp.forEach(card -> {
                    if (card != null && card.getCosts() - inputCard.getCosts() <= action.getHasMoreCostThanInput().getMax()) {
                        theSelectableCards.add(card.getId());
                    }
                });
            } else {
                tmp.forEach(card -> theSelectableCards.add(card.getId()));
            }

            ChooseCardRequest request;
            if (action.getCount().getMin() == action.getCount().getMax()) {
                request = new ChooseCardRequest(this.gameID, playerChooseCard.getTheUserInThePlayer(), theSelectableCards, action.getCount().getMin(), action.getCardSource(), "Bitte die Anzahl der Karten auswählen von dem Bereich der dir angezeigt wird!", actionExecutionID);
                if (nextActionIndex < nextActions.size() && nextActions.get(nextActionIndex) instanceof UseCard) {
                    request.setUseCard(true);
                }
            } else if (action.getCount().getMin() == 0) {
                request = new ChooseCardRequest(this.gameID, playerChooseCard.getTheUserInThePlayer(), theSelectableCards, action.getCount().getMax(), action.getCardSource(), "Bitte die Anzahl der Karten auswählen von dem Bereich der dir angezeigt wird!", actionExecutionID);
            } else {
                request = new ChooseCardRequest(this.gameID, playerChooseCard.getTheUserInThePlayer(), theSelectableCards, action.getCount(), action.getCardSource(), "Bitte die Anzahl der Karten auswählen von dem Bereich der dir angezeigt wird!", actionExecutionID);
            }
            playground.getGameService().sendToSpecificPlayer(player, request);
        }
        return true;

    }

    /**
     * Sendet dem Spieler eine Nachricht mit den Aktionen, aus denen er eine auswählen kann
     *
     * @param chooseNextAction Die ChooseNextAction
     * @return true
     * @author Julia
     * @since Sprint 6
     */
    private boolean executeChooseNextAction(ChooseNextAction chooseNextAction) {
        waitedForPlayerInput = true;
        playground.getGameService().sendToSpecificPlayer(player, new ChooseNextActionMessage(gameID, chooseNextAction.getNextActions()));
        return true;
    }

    /**
     * Bewegt Karten von einem Ort zum anderen
     *
     * @return true
     * @author Julia
     * @since Sprint 8
     */
    private boolean executeMoveAction(Move action, Player player) {
        switch (action.getCardDestination()) {
            case DISCARD:
                player.getPlayerDeck().getDiscardPile().addAll(action.getCardsToMove());
                break;
            case TRASH:
                playground.getTrash().addAll(action.getCardsToMove());
                break;
            case DRAW:
                player.getPlayerDeck().getCardsDeck().addAll(action.getCardsToMove());
                break;
            case TEMP:
                player.getPlayerDeck().getTemp().addAll(action.getCardsToMove());
                break;
            case HAND:
                player.getPlayerDeck().getHand().addAll(action.getCardsToMove());
                break;
        }
        int size = action.getCardsToMove().size();
        ArrayList<Card> toRemove = new ArrayList<>();
        switch (action.getCardSource()) {
            case HAND:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getHand().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getHand().remove(c));
                break;
            case TEMP:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getTemp().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getTemp().remove(c));
                break;
            case DISCARD:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getDiscardPile().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getDiscardPile().remove(c));
                break;
            case DRAW:
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    player.getPlayerDeck().getCardsDeck().stream().filter(c -> c.getId() == card.getId()).findFirst().ifPresent(toRemove::add);
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                toRemove.forEach(c -> player.getPlayerDeck().getCardsDeck().remove(c));
                action.setCardsToMove(toRemove);
                break;
            case BUY:
                Map<Short, Integer> newCount = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    Card card = action.getCardsToMove().get(i);
                    short id = card.getId();
                    int count = playground.getCardField().get(id);
                    if (count > 0) {
                        playground.getCardField().replace(id, --count);
                        newCount.put(id, count);
                    }
                    if (action.getCardsToMove().size() != size) {
                        action.getCardsToMove().add(i, card);
                    }
                }
                UpdateCardCounterMessage message = new UpdateCardCounterMessage(gameID, player.getTheUserInThePlayer(), newCount);
                playground.getGameService().sendToAllPlayers(gameID, message);
                break;
        }
        MoveCardMessage msg = new MoveCardMessage(gameID, player.getTheUserInThePlayer(), new Move(action.getCardsToMove(), action.getCardSource(), action.getCardDestination()));
        //TODO Evtl. Daten, die andere Spieler nicht erhalten dürfen irgendwie unkenntlich machen? (Karten anderer Spieler)
        playground.getGameService().sendToAllPlayers(gameID, msg);

        return true;
    }

    /**
     * Liefert eine Liste mit allen Unteraktionen einer Aktion
     *
     * @param action die Kartenaktion, deren Unteraktionen herausgefiltert werden sollen
     * @return Liste mit allen Unteraktionen der Aktion
     * @author Julia
     * @since Sprint 7
     */
    private List<CardAction> getNextActions(CardAction action) {
        if (!(action instanceof ComplexCardAction)) return nextActions;
        ComplexCardAction complexCardAction = (ComplexCardAction) action;
        if (complexCardAction.getNextAction() == null) return nextActions;
        nextActions.add(complexCardAction.getNextAction());
        return getNextActions(complexCardAction.getNextAction());
    }


    /**
     * Ermittelt alle Spieler, auf die eine Aktion ausgeführt werden muss
     *
     * @param action Die CardAction
     * @return Spielerliste mit allen betroffenen Spielern
     * @author Julia
     * @since Sprint 7
     */
    private List<Player> getAffectedPlayers(CardAction action) {
        List<Player> affectedPlayers = new ArrayList<>();
        if (!(action instanceof ComplexCardAction)) {
            affectedPlayers.add(player);
            return affectedPlayers;
        }

        CardAction.ExecuteType executeType = ((ComplexCardAction) action).getExecuteType();
        if (executeType == CardAction.ExecuteType.ALL) {
            affectedPlayers = players;
        } else if (executeType == CardAction.ExecuteType.OTHERS) {
            for (Player p : players) {
                if (!p.equals(player)) affectedPlayers.add(p);
            }
        } else if (executeType == CardAction.ExecuteType.NEXT) {
            int index = players.indexOf(player);
            affectedPlayers.add(players.get(++index % players.size()));
        } else if (executeType == CardAction.ExecuteType.LAST) {
            int index = players.indexOf(player);
            affectedPlayers.add(players.get(--index % players.size()));
        } else {
            affectedPlayers.add(player);
        }

        //Prüfe ob einer der betroffenen Spieler eine Reaktionskarte auf der Hand hat
        if (theCard.getType() == ActionCard.ActionType.Attack) {
            List<Player> toRemove = new ArrayList<>();
            for (Player p : affectedPlayers) {
                if (!p.equals(player) && checkForReactionCard(p)) {
                    toRemove.add(p);
                }
            }
            toRemove.forEach(affectedPlayers::remove);
        }
        return affectedPlayers;
    }

    /**
     * Prüft, ob ein Spieler eine Reaktionskarte auf der Hand hat und spielt diese dann aus
     *
     * @param player Der Spieler, dessen Hand überprüft werden soll
     * @return true, wenn er eine Reaktionskarte auf der Hand hat, sonst false
     * @author Julia
     * @since Sprint 7
     */
    public boolean checkForReactionCard(Player player) {
        for (Card card : player.getPlayerDeck().getHand()) {
            if (card instanceof ActionCard && ((ActionCard) card).getType() == ActionCard.ActionType.Reaction) {
                playground.getGameService().sendToAllPlayers(gameID, new PlayedReactionCardMessage(card.getId(), player.getTheUserInThePlayer(), gameID));
                return true;
            }
        }
        return false;
    }

    /**
     * Überprüft ob die Karte vollständig abgearbeitet ist
     *
     * @author Julia
     * @since Sprint 7
     */
    private boolean checkIfComplete() {
        if (actualStateIndex == theCard.getActions().size() && !waitedForPlayerInput && finishedNextActions) {
            if(!finishedExecution) {
                finishedExecution = true;
                if (!removeCardAfter && (!useCardExecution || parent.getUseCardCounter() == 0)) {
                    player.getPlayerDeck().getActionPile().add(theCard);
                } else if (!useCardExecution || parent.getUseCardCounter() == 0) {
                    playground.getTrash().add(theCard);
                }
                playground.sendCardsDeckSize();
                if (useCardExecution) {
                    parent.setUseCardCounter(parent.getUseCardCounter() + 1);
                    parent.executeUseCard(parent.getUseAction());
                } else {
                    playground.getGameService().getBus().unregister(this);
                    playground.getCompositePhase().finishedActionCardExecution(player);
                }
            }
            return true;
        }
        return false;
    }


    /**
     * Hilfsmethode, um die gespielte Aktionskarte aus der Hand des Spielers zu erhalten
     *
     * @param cardID ID der Aktionskarte
     * @author KenoO
     * @since Sprint 6
     */
    private void getCardFromHand(short cardID) {
        theCard = player.getPlayerDeck().getHand().stream().filter(card -> card.getId() == cardID).findFirst().map(card -> (ActionCard) card).orElse(theCard);
    }

    /**
     * Hilsmethode, um den zu einem User gehörigen Player zu bekommmen
     *
     * @param user Der User
     * @return Player
     * @author Ferit
     * @since Sprint 7
     */
    private Player helpMethodToGetThePlayerFromUser(User user) {
        return players.stream().filter(p -> user.equals(p.getTheUserInThePlayer())).findFirst().orElse(null);
    }

    /**
     * Hilfsmethode, um die zu einer ID gehörige Karte zu bekommen
     *
     * @param id ID der Karte
     * @return die Karte, wenn sie gefunden wurde, sonst null
     * @author Ferit
     * @since Sprint 7
     */
    private Card helpMethod2GetTheCard(Short id) {
        return playground.getCardsPackField().getCards().getAllCards().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    /**
     * Setzt alle Attribute auf ihren ursprünglichen Zustand zurück
     *
     * @author Julia
     * @since Sprint 10
     */
    private void reset() {
        actualStateIndex = 0;
        nextActionIndex = 0;
        finishedNextActions = true;
        executeOptionalAction = false;
        startedNextActions = false;
        finishedExecution = false;
        removeCardAfter = false;
    }

    public boolean isRemoveCardAfter() {
        return removeCardAfter;
    }

    public ActionCardExecution getParent() {
        return parent;
    }

    public void setParent(ActionCardExecution parent) {
        this.parent = parent;
    }

    public Integer getUseCardCounter() {
        return useCardCounter;
    }

    public void setUseCardCounter(int count) {
        useCardCounter = count;
    }

    public UseCard getUseAction() {
        return useAction;
    }
}