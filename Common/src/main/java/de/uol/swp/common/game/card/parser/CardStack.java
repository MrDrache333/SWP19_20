package de.uol.swp.common.game.card.parser;

import com.google.gson.annotations.SerializedName;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.MoneyCard;
import de.uol.swp.common.game.card.ReactionCard;
import de.uol.swp.common.game.card.ValueCard;

import java.util.ArrayList;

/**
 * Der Cardstack representiert eine Sammlung der verschiedenen Kartentypen.
 */
public class CardStack {

    @SerializedName("MoneyCards")
    private ArrayList<MoneyCard> moneyCards = new ArrayList<>();
    @SerializedName("ValueCards")
    private ArrayList<ValueCard> valueCards = new ArrayList<>();
    @SerializedName("ActionCards")
    private ArrayList<ActionCard> actionCards = new ArrayList<>();
    @SerializedName("ReactionCards")
    private ArrayList<ReactionCard> reactionCards = new ArrayList<>();

    /**
     * @return Objekt als String
     * @author KenoO
     * @since Sprint 5
     */
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("Cards:\n");
        string.append("\tMoneycards:\n");
        for (MoneyCard card : moneyCards) {
            string.append("\t\t").append(card.getId()).append(": ").append(card.getName()).append(", Value: ").append(card.getValue()).append(", Cost: ").append(card.getCosts()).append("\n");
        }
        string.append("\tValueCards:\n");
        for (ValueCard card : valueCards) {
            string.append("\t\t").append(card.getId()).append(": ").append(card.getName()).append(", Value: ").append(card.getValue()).append(", Cost: ").append(card.getCosts()).append("\n");
        }
        string.append("\tReactionCards:\n");
        for (ReactionCard card : reactionCards) {
            string.append("\t\t").append(card.getId()).append(": ").append(card.getName()).append(", Cost: ").append(card.getCosts()).append("\n");
        }
        string.append("\tActionCards:\n");
        for (ActionCard card : actionCards) {
            string.append("\t\t").append(card.getId()).append(": ").append(card.getName()).append(", Cost: ").append(card.getCosts()).append("\n");
        }

        return string.toString();
    }

    /**
     * Gibt die money cards zurück
     *
     * @return Die money cards
     */
    public ArrayList<MoneyCard> getMoneyCards() {
        return moneyCards;
    }

    /**
     * Gibt die value cards zurück
     *
     * @return Die value cards
     */
    public ArrayList<ValueCard> getValueCards() {
        return valueCards;
    }

    /**
     * Gibt die action cards zurück
     *
     * @return Die action cards
     */
    public ArrayList<ActionCard> getActionCards() {
        return actionCards;
    }

    /**
     * Gibt die reaction cards zurück
     *
     * @return Die reaction cards
     */
    public ArrayList<ReactionCard> getReactionCards() {
        return reactionCards;
    }
}
