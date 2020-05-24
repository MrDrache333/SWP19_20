package de.uol.swp.common.game.card.parser.components;

import com.google.gson.annotations.SerializedName;
import de.uol.swp.common.game.card.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @SerializedName("CurseCards")
    private ArrayList<CurseCard> curseCards = new ArrayList<>();

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
        string.append("\tActionCards:\n");
        for (ActionCard card : actionCards) {
            string.append("\t\t").append(card.getId()).append(": ").append(card.getName()).append(", Cost: ").append(card.getCosts()).append("\n");
        }
        string.append("\tCurseCards:\n");
        for (CurseCard card : curseCards) {
            string.append("\t\t").append(card.getId()).append(": ").append(card.getName()).append(", Value: ").append(card.getValue()).append("\n");
        }

        return string.toString();
    }

    /**
     * Gibt die Karte mit der entsprechenden ID zurück
     *
     * @return cards die eine Karte
     * @author Timo, Fenja, Keno O.
     */
    public Card getCardById(short id) {
        List<Card> cards = getAllCards().stream().filter(c -> c.getId() == id).collect(Collectors.toList());
        if (cards.size() > 0) return cards.get(0);
        return null;
    }

    /**
     * Gibt die money cards zurück
     *
     * @return Die Geldkarten
     */
    public ArrayList<MoneyCard> getMoneyCards() {
        return moneyCards;
    }

    /**
     * Gibt die value cards zurück
     *
     * @return Die Anwesenkarten
     */
    public ArrayList<ValueCard> getValueCards() {
        return valueCards;
    }

    /**
     * Gibt die action cards zurück
     *
     * @return Die Aktionskarten
     */
    public ArrayList<ActionCard> getActionCards() {
        return actionCards;
    }

    /**
     * Gibt die curse cards zurück
     *
     * @return Die Fuchkarten
     */
    public ArrayList<CurseCard> getCurseCards() {
        return curseCards;
    }

    public ArrayList<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(moneyCards);
        cards.addAll(valueCards);
        cards.addAll(actionCards);
        cards.addAll(curseCards);
        return cards;
    }
}
