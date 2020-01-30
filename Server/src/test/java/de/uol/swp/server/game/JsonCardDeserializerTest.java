package de.uol.swp.server.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uol.swp.server.game.card.*;
import de.uol.swp.server.game.card.generating.AbstractElementAdapter;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonCardDeserializerTest {

    ArrayList<Card> cards = new ArrayList<>();
    private Card[] newCardElementsArray;
    private String jsonFilePath = "Common/src/main/java/de/uol/swp/common/json/AlleKarten.json";
    private ArrayList<MoneyCard> moneyCardArrayList = new ArrayList<>();
    private ArrayList<ActionCard> actionCardArrayList = new ArrayList<>();
    private ArrayList<ReactionCard> reactionCardArrayList = new ArrayList<>();
    private ArrayList<ValueCard> valueCardArrayList = new ArrayList<>();

    @Test
    public void testJsonFileNotFound() throws FileNotFoundException {
        GsonBuilder gsonobj = new GsonBuilder();
        gsonobj.registerTypeAdapter(Card.class, new AbstractElementAdapter());
        Gson gsonRealObj = gsonobj.create();
        assertThrows(FileNotFoundException.class, () -> newCardElementsArray = gsonRealObj.fromJson(new FileReader(jsonFilePath), Card[].class)
        );
    }

}
