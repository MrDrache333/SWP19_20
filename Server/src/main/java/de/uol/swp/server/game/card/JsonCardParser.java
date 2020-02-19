package de.uol.swp.server.game.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Der Cardstack representiert eine Sammlung der verschiedenen Kartentypen.
 */
class CardStack {

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

/**
 * Das CardPack representiert ein Kartenpaket.
 */
class CardPack {

    @SerializedName("packname")
    private String name;
    @SerializedName("_description")
    private String description;

    @SerializedName("cards")
    private CardStack cards;

    @Override
    public String toString() {
        return "Name: " + name + "\nDescription: " + description + "\n" + cards.toString();
    }

    /**
     * Gibt den Paketnamen zurück
     *
     * @return Der Paketname
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Paketbeschreibung zurück
     *
     * @return Die Beschreibung
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gibt alle im Pack enthaltenen Karten zurück
     *
     * @return Die Karten
     */
    public CardStack getCards() {
        return cards;
    }
}

/**
 * JSonCardParser representiert eine Möglichkeit, Karten aus einer JSon-Datei zu laden und als Objekt zurück zu geben.
 */
public class JsonCardParser {
    ;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        CardPack pack = new JsonCardParser().loadPack("Basispack");
        System.out.println(pack.toString());
    }

    /**
     * Methode, die versucht ein angegebenes Kartenpaket zu laden.
     *
     * @param packname Das zu ladene Kartenpack
     * @return Das Kartenpack, oder Null, falls ein Fehler aufgetreten ist
     * @author KenoO
     * @since Sprint 5
     */
    public CardPack loadPack(String packname) {
        GsonBuilder gsonobj = new GsonBuilder();
        Gson gsonRealObj = gsonobj.create();
        CardPack pack;
        try {
            pack = gsonRealObj.fromJson(new FileReader(this.getClass().getResource("/cards/packs/" + packname + "/" + packname + ".json").toExternalForm().replace("file:", "")), CardPack.class);
        } catch (FileNotFoundException e) {
            pack = null;
        }
        return pack;
    }
}