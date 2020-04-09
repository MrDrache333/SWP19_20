package de.uol.swp.common.game.card.parser.components.CardAction;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;

/**
 * The type ComplexCardAction.
 */
public abstract class ComplexCardAction extends CardAction {

    /**
     * Gibt an, ob eine durch eine Aktionskarte gezogene Karte direkt auf die Hand genommen werden muss
     */
    private boolean directHand = false;
    /**
     * Gibt an, welcher Kartentyp bei der Aktion verwendet werden darf(z.B. beim Ablegen/ Ziehen einer Karte)
     */
    private Card.Type allowedCardType = Card.Type.None;
    /**
     * Gibt an, wie hoch der Wert der durch die Aktion aufgenommene/ abgelegte Karte mindestens/ höchstens sein muss/darf
     */
    private Value hasWorth = new Value((short) 0);
    /**
     * Gibt an, wie hoch der Preis der durch die Aktion aufgenommene/ abgelegte Karte mindestens/ höchstens sein muss/darf
     * im Vergleih zur Karte, die als Eingabe in diese Aktion übergeben wurde
     */
    private Value hasMoreCostThanInput = new Value((short) 0);
    /**
     * Gibt an, wie hoch der Wert der durch die Aktion aufgenommene/ abgelegte Karte mindestens/ höchstens sein muss/darf
     * im Vergleih zur Karte, die als Eingabe in diese Aktion übergeben wurde
     */
    private Value hasMoreWorthThanInput = new Value((short) 0);
    /**
     * Gibt an, wie hoch der Preis der durch die Aktion aufgenommene/ abgelegte Karte mindestens/ höchstens sein muss/darf
     */
    private Value hasCost = new Value((short) 0);
    /**
     * Gibt an, auf wen die Aktion anzuwenden ist aus sicht des aktuellen Spielers.
     */
    private ExecuteType executeType = ExecuteType.NONE;
    /**
     * Gibt an, ob für jede hinzugefügte Karte eine entfernt werden muss und umgekehrt.
     */
    private boolean equalizeCards = false;
    /**
     * Gibt an, woher eine Karte genommen werden muss.
     */
    private AbstractPlayground.ZoneType cardSource = AbstractPlayground.ZoneType.NONE;
    /**
     * Gibt an, wohin eine Karte gelegt werden muss.
     */
    private AbstractPlayground.ZoneType cardDestination = AbstractPlayground.ZoneType.NONE;
    /**
     * Gibt an, ob die Ausführung der Aktion durch den Benutzer abgebrochen werden kann (liefert bei Abbruch false als
     * Rückgabewert bei execute())
     */
    private boolean executionOptional = false;
    /**
     * Gibt an, ob die Karte, nachdem sie gespielt wurde, Entsorgt wird.
     */
    private boolean removeCardAfter = false;
    /**
     * Gibt an, ob Karten, die durch diese Aktion abgelegt/ gezogen wurden, den anderen Spielern sichtbar sind. 
     */
    private boolean hideCardDuringAction = true;
    /**
     * Aktion, die das ergebnis der vorrangegangenen Aktion als Eingabe erhält. 
     */
    private CardAction nextAction = null;

    /**
     * Instantiates a new Complex card action.
     *
     * @author KenoO
     * @since Sprint 6
     */
    public ComplexCardAction() {

    }

    /**
     * Gets executeType
     *
     * @return Value of executeType.
     * @author KenoO
     * @since Sprint 6
     */
    public ExecuteType getExecuteType() {
        return executeType;
    }

    /**
     * Sets new executeType.
     *
     * @param executeType New value of executeType.
     * @author KenoO
     * @since Sprint 6
     */
    public void setExecuteType(ExecuteType executeType) {
        this.executeType = executeType;
    }

    /**
     * Gets nextAction.
     *
     * @return Value of nextAction.
     * @author KenoO
     * @since Sprint 6
     */
    public CardAction getNextAction() {
        return nextAction;
    }

    /**
     * Sets new nextAction.
     *
     * @param nextAction New value of nextAction.
     * @author KenoO
     * @since Sprint 6
     */
    public void setNextAction(CardAction nextAction) {
        this.nextAction = nextAction;
    }

    /**
     * Gets allowedCardType.
     *
     * @return Value of allowedCardType.
     * @author KenoO
     * @since Sprint 6
     */
    public Card.Type getAllowedCardType() {
        return allowedCardType;
    }

    /**
     * Sets new allowedCardType.
     *
     * @param allowedCardType New value of allowedCardType.
     * @author KenoO
     * @since Sprint 6
     */
    public void setAllowedCardType(Card.Type allowedCardType) {
        this.allowedCardType = allowedCardType;
    }

    /**
     * Gets executionOptional.
     *
     * @return Value of executionOptional.
     * @author KenoO
     * @since Sprint 6
     */
    public boolean isExecutionOptional() {
        return executionOptional;
    }

    /**
     * Sets new executionOptional.
     *
     * @param executionOptional New value of executionOptional.
     * @author KenoO
     * @since Sprint 6
     */
    public void setExecutionOptional(boolean executionOptional) {
        this.executionOptional = executionOptional;
    }

    /**
     * Gets removeCardAfter.
     *
     * @return Value of removeCardAfter.
     * @author KenoO
     * @since Sprint 6
     */
    public boolean isRemoveCardAfter() {
        return removeCardAfter;
    }

    /**
     * Sets new removeCardAfter.
     *
     * @param removeCardAfter New value of removeCardAfter.
     * @author KenoO
     * @since Sprint 6
     */
    public void setRemoveCardAfter(boolean removeCardAfter) {
        this.removeCardAfter = removeCardAfter;
    }

    /**
     * Gets directHand.
     *
     * @return Value of directHand.
     * @author KenoO
     * @since Sprint 6
     */
    public boolean isDirectHand() {
        return directHand;
    }

    /**
     * Sets new directHand.
     *
     * @param directHand New value of directHand.
     * @author KenoO
     * @since Sprint 6
     */
    public void setDirectHand(boolean directHand) {
        this.directHand = directHand;
    }

    /**
     * Gets hasCost.
     *
     * @return Value of hasCost.
     * @author KenoO
     * @since Sprint 6
     */
    public Value getHasCost() {
        return hasCost;
    }

    /**
     * Sets new hasCost.
     *
     * @param hasCost New value of hasCost.
     * @author KenoO
     * @since Sprint 6
     */
    public void setHasCost(Value hasCost) {
        this.hasCost = hasCost;
    }

    /**
     * Gets cardSource.
     *
     * @return Value of cardSource.
     * @author KenoO
     * @since Sprint 6
     */
    public AbstractPlayground.ZoneType getCardSource() {
        return cardSource;
    }

    /**
     * Sets new cardSource.
     *
     * @param cardSource New value of cardSource.
     * @author KenoO
     * @since Sprint 6
     */
    public void setCardSource(AbstractPlayground.ZoneType cardSource) {
        this.cardSource = cardSource;
    }

    /**
     * Gets cardDestination.
     *
     * @return Value of cardDestination.
     * @author KenoO
     * @since Sprint 6
     */
    public AbstractPlayground.ZoneType getCardDestination() {
        return cardDestination;
    }

    /**
     * Sets new cardDestination.
     *
     * @param cardDestination New value of cardDestination.
     * @author KenoO
     * @since Sprint 6
     */
    public void setCardDestination(AbstractPlayground.ZoneType cardDestination) {
        this.cardDestination = cardDestination;
    }

    /**
     * Gets equalizeCards.
     *
     * @return Value of equalizeCards.
     * @author KenoO
     * @since Sprint 6
     */
    public boolean isEqualizeCards() {
        return equalizeCards;
    }

    /**
     * Sets new equalizeCards.
     *
     * @param equalizeCards New value of equalizeCards.
     * @author KenoO
     * @since Sprint 6
     */
    public void setEqualizeCards(boolean equalizeCards) {
        this.equalizeCards = equalizeCards;
    }

    /**
     * Gets hasWorth.
     *
     * @return Value of hasWorth.
     * @author KenoO
     * @since Sprint 6
     */
    public Value getHasWorth() {
        return hasWorth;
    }

    /**
     * Sets new hasWorth.
     *
     * @param hasWorth New value of hasWorth.
     * @author KenoO
     * @since Sprint 6
     */
    public void setHasWorth(Value hasWorth) {
        this.hasWorth = hasWorth;
    }

    /**
     * Gets has more cost than input.
     *
     * @return the has more cost than input
     * @author Julia
     * @since Sprint 6
     */
    public Value getHasMoreCostThanInput() {
        return hasMoreCostThanInput;
    }

    /**
     * Sets has more cost than input.
     *
     * @param hasMoreCostThanInput the has more cost than input
     * @author Julia
     * @since Sprint 6
     */
    public void setHasMoreCostThanInput(Value hasMoreCostThanInput) {
        this.hasMoreCostThanInput = hasMoreCostThanInput;
    }

    /**
     * Gets has more worth than input.
     *
     * @return the has more worth than input
     * @author KenoO
     * @since Sprint 6
     */
    public Value getHasMoreWorthThanInput() {
        return hasMoreWorthThanInput;
    }

    /**
     * Sets has more worth than input.
     *
     * @param hasMoreWorthThanInput the has more worth than input
     * @author KenoO
     * @since Sprint 6
     */
    public void setHasMoreWorthThanInput(Value hasMoreWorthThanInput) {
        this.hasMoreWorthThanInput = hasMoreWorthThanInput;
    }

    /**
     * Gets hideCardDuringAction.
     *
     * @return Value of hideCardDuringAction.
     * @author KenoO
     * @since Sprint 6
     */
    public boolean isHideCardDuringAction() {
        return hideCardDuringAction;
    }

    /**
     * Sets new hideCardDuringAction.
     *
     * @param hideCardDuringAction New value of hideCardDuringAction.
     * @author KenoO
     * @since Sprint 6
     */
    public void setHideCardDuringAction(boolean hideCardDuringAction) {
        this.hideCardDuringAction = hideCardDuringAction;
    }
}

