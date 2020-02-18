package de.uol.swp.server.game.card;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import de.uol.swp.server.game.card.generating.AbstractElementAdapter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

class CardStack {

    @SerializedName("MoneyCards")
    private ArrayList<MoneyCard> moneyCards = new ArrayList<>();
    @SerializedName("ValueCards")
    private ArrayList<ValueCard> valueCards = new ArrayList<>();
    @SerializedName("ActionCards")
    private ArrayList<ActionCard> actionCards = new ArrayList<>();
    @SerializedName("ReactionCards")
    private ArrayList<ReactionCard> reactionCards = new ArrayList<>();

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

}

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

}

public class JsonCardParser {

    private static CardPack pack;
    private static String jsonFilePath = "Common/src/main/resources/cards_new.json";

    public static void main(String[] args) throws FileNotFoundException {
        GsonBuilder gsonobj = new GsonBuilder();
        gsonobj.registerTypeAdapter(Card.class, new AbstractElementAdapter());
        Gson gsonRealObj = gsonobj.create();
        pack = gsonRealObj.fromJson(new FileReader(jsonFilePath), CardPack.class);
        System.out.println("Done");

        System.out.println(pack.toString());

    }
}
