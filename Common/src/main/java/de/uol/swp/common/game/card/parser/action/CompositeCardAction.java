package de.uol.swp.common.game.card.parser.action;

import com.google.gson.annotations.SerializedName;
import de.uol.swp.common.game.card.parser.action.types.*;

public class CompositeCardAction {

    @SerializedName("AddCapablePlayerActivity")
    private AddCapablePlayerActivity addCapablePlayerActivity = null;
    @SerializedName("ChooseCard")
    private ChooseCard chooseCard = null;
    @SerializedName("ChooseNextAction")
    private ChooseNextAction chooseNextAction = null;
    @SerializedName("ForEach")
    private ForEach forEach = null;
    @SerializedName("GetCard")
    private GetCard getCard = null;
    @SerializedName("If")
    private If anIf = null;
    @SerializedName("Move")
    private Move move = null;
    @SerializedName("ShowCard")
    private ShowCard showCard = null;
    @SerializedName("Until")
    private Until until = null;
    @SerializedName("UseCard")
    private UseCard useCard = null;

    public CompositeCardAction(AddCapablePlayerActivity addCapablePlayerActivity) {
        this.addCapablePlayerActivity = addCapablePlayerActivity;
    }

    public CompositeCardAction(ChooseCard chooseCard) {
        this.chooseCard = chooseCard;
    }

    public CompositeCardAction(ChooseNextAction chooseNextAction) {
        this.chooseNextAction = chooseNextAction;
    }

    public CompositeCardAction(ForEach forEach) {
        this.forEach = forEach;
    }

    public CompositeCardAction(GetCard getCard) {
        this.getCard = getCard;
    }

    public CompositeCardAction(If anIf) {
        anIf = anIf;
    }

    public CompositeCardAction(Move move) {
        this.move = move;
    }

    public CompositeCardAction(ShowCard showCard) {
        this.showCard = showCard;
    }

    public CompositeCardAction(Until until) {
        this.until = until;
    }

    public CompositeCardAction(UseCard useCard) {
        this.useCard = useCard;
    }

}
