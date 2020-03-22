package de.uol.swp.common.game.card.parser.components.CardAction;

import com.google.gson.annotations.SerializedName;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;

/**
 * Die zusammengesetzte Aktion, welche alle möglichen Aktionen enthalten kann, aber immer nur eine gleichzeitig
 */
public class CompositeCardAction {

    /**
     * Aktion, welche dem Benutzer mögliche SPielaktionen hinzufügt
     */
    @SerializedName("AddCapablePlayerActivity")
    private AddCapablePlayerActivity addCapablePlayerActivity = null;
    /**
     * Aktion, welche dem Benutzer eine auswahl von Karten ermöglicht
     */
    @SerializedName("ChooseCard")
    private ChooseCard chooseCard = null;
    /**
     * Aktion, welche den Benutzer die nächste Aktion auswählen lässt
     */
    @SerializedName("ChooseNextAction")
    private ChooseNextAction chooseNextAction = null;
    /**
     * Aktion, welche Aktionen auf mehrere Karten anwendet
     */
    @SerializedName("ForEach")
    private ForEach forEach = null;
    /**
     * Aktion, welche Karten von einer Quelle ausließt
     */
    @SerializedName("GetCard")
    private GetCard getCard = null;
    /**
     * Aktion, welche eine Aktion bedingt ausführt
     */
    @SerializedName("If")
    private If anIf = null;
    /**
     * Aktion, welche Karten von einer Quelle zu einem Ziel bewegt
     */
    @SerializedName("Move")
    private Move move = null;
    /**
     * Aktion, welche Karten einem selbst oder den Mitspielern anzeigt bzw. aufdeckt
     */
    @SerializedName("ShowCard")
    private ShowCard showCard = null;
    /**
     * Aktion, welche bestimmte Aktionen ausführt, solange eine Bedingung gilt
     */
    @SerializedName("While")
    private While aWhile = null;
    /**
     * Aktion, welche die Aktionen einer Karte anwendet
     */
    @SerializedName("UseCard")
    private UseCard useCard = null;

    /**
     * The Action type.
     */
    private Type actionType;

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param addCapablePlayerActivity Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(AddCapablePlayerActivity addCapablePlayerActivity) {
        this.actionType = Type.AddCapablePlayerActivity;
        this.addCapablePlayerActivity = addCapablePlayerActivity;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param chooseCard Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(ChooseCard chooseCard) {
        this.actionType = Type.ChooseCard;
        this.chooseCard = chooseCard;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param chooseNextAction Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(ChooseNextAction chooseNextAction) {
        this.actionType = Type.ChooseNextAction;
        this.chooseNextAction = chooseNextAction;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param forEach Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(ForEach forEach) {
        this.actionType = Type.ForEach;
        this.forEach = forEach;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param getCard Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(GetCard getCard) {
        this.actionType = Type.GetCard;
        this.getCard = getCard;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param anIf Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(If anIf) {
        this.actionType = Type.If;
        this.anIf = anIf;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param move Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(Move move) {
        this.actionType = Type.Move;
        this.move = move;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param showCard Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(ShowCard showCard) {
        this.actionType = Type.ShowCard;
        this.showCard = showCard;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param aWhile Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(While aWhile) {
        this.actionType = Type.Until;
        this.aWhile = aWhile;
    }

    /**
     * Erstellt eine neue Zusammengesetzte Kartenaktion
     *
     * @param useCard Die zu speichernde Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public CompositeCardAction(UseCard useCard) {
        this.actionType = Type.UseCard;
        this.useCard = useCard;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public AddCapablePlayerActivity getAddCapablePlayerActivity() {
        return addCapablePlayerActivity;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseCard getChooseCard() {
        return chooseCard;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public ChooseNextAction getChooseNextAction() {
        return chooseNextAction;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public ForEach getForEach() {
        return forEach;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public GetCard getGetCard() {
        return getCard;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public If getAnIf() {
        return anIf;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public Move getMove() {
        return move;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public ShowCard getShowCard() {
        return showCard;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public While getaWhile() {
        return aWhile;
    }

    /**
     * Gibt die gespeicherte Aktion zurück
     *
     * @return Die gespeicherte Kartenaktion
     * @author KenoO
     * @since Sprint 6
     */
    public UseCard getUseCard() {
        return useCard;
    }

    /**
     * Gibt den gespeicherten Typ der Aktion zurück
     *
     * @return Typ der gespeicherten Aktion
     * @author KenoO
     * @since Sprint 6
     */
    public Type getActionType() {
        return actionType;
    }

    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * Add capable player activity type.
         */
        AddCapablePlayerActivity,
        /**
         * Choose card type.
         */
        ChooseCard,
        /**
         * Choose next action type.
         */
        ChooseNextAction,
        /**
         * For each type.
         */
        ForEach,
        /**
         * Get card type.
         */
        GetCard,
        /**
         * If type.
         */
        If,
        /**
         * Move type.
         */
        Move,
        /**
         * Show card type.
         */
        ShowCard,
        /**
         * Until type.
         */
        Until,
        /**
         * Use card type.
         */
        UseCard,
        /**
         * None type.
         */
        NONE
    }

}
