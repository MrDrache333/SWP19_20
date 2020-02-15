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

}

class CardPack {

    @SerializedName("packname")
    private String name;
    @SerializedName("_description")
    private String description;

    @SerializedName("cards")
    private CardStack cards;

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
    }
}
