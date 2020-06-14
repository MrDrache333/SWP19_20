package de.uol.swp.common.game.card.parser.components;

import com.google.gson.annotations.SerializedName;
import de.uol.swp.common.game.card.*;

import java.util.ArrayList;

/**
 * Der Cardstack representiert eine Sammlung der verschiedenen Kartentypen.
 */
public class CardStack {

    @SerializedName("MoneyCards")
    private final ArrayList<MoneyCard> moneyCards = new ArrayList<>();
    @SerializedName("ValueCards")
    private final ArrayList<ValueCard> valueCards = new ArrayList<>();
    @SerializedName("ActionCards")
    private final ArrayList<ActionCard> actionCards = new ArrayList<>();
    @SerializedName("CurseCards")
    private final ArrayList<CurseCard> curseCards = new ArrayList<>();

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
     * Gibt alle Karten zurück
     *
     * @return cards alle Karten
     * @author Timo, Fenja, Keno O.
     */
    public ArrayList<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(moneyCards);
        cards.addAll(valueCards);
        cards.addAll(actionCards);
        cards.addAll(curseCards);
        return cards;
    }

    /**
     * Gibt die Karte aus dem Kartenstapel zurück mit der die übergebenen ID übereinstimmt
     *
     * @param id Die Kartenid
     * @return Die Karte oder Null
     * @author Timo, Fenja, KenoO
     * @since Sprint 7
     */
    public Card getCardForId(short id) {
        return getAllCards().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
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

}
