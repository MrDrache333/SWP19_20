package de.uol.swp.server.game.card.generating;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uol.swp.server.game.card.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class JsonConverter {
    private static Card[] cardElemente;
    private String jsonFilePath = "Common/src/main/java/de/uol/swp/common/json/AlleKarten.json";
    private static ArrayList<MoneyCard> moneyCardArray = new ArrayList<>();


    /**
     * Diese Methode holt sich alle Karten aus der vorgegebenen Json Datei und wandelt diese in Objekte der zugehörigen Klasse um und speichert
     * diese dann in einem Karten Array.
     *
     * @throws FileNotFoundException Wirft eine Exception, wenn die JSON Datei nicht gefunden werden kann.
     */
    public void generateAllCards() throws FileNotFoundException {
        GsonBuilder gsonobj = new GsonBuilder();
        gsonobj.registerTypeAdapter(Card.class, new AbstractElementAdapter());
        Gson gsonRealObj = gsonobj.create();
        cardElemente = gsonRealObj.fromJson(new FileReader(jsonFilePath), Card[].class);
    }

    // Test-Methode
    public static void main(String[] args) throws FileNotFoundException {
        JsonConverter abc = new JsonConverter();
        abc.generateAllCards();

        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(Arrays.asList(cardElemente));
        for (Card c : cards) {
            try {
                if (c.getCardtype().equals(Card.Type.MoneyCard))
                    moneyCardArray.add((MoneyCard) c);
            } catch (NullPointerException ignored) {
            }
        }

        moneyCardArray.stream().forEach(e -> System.out.println(e.getName() + " V: " + e.getValue()));



    }

    /**
     * Aufruf übergibt ein MoneyCard Array.
     *
     * @return MoneyCard-Array
     */
    public MoneyCard getMoneyCards() {

        return null;
    }

    /**
     * @return Übergibt ein ActionCard-Array
     */
    public ActionCard getActionCards() {
        return null;
    }

    /**
     * @return Übergibt ein ReactionCard Array.
     */
    public ReactionCard getReactionCards() {
        return null;
    }

    /**
     * @return Übergibt ValueCard-Array
     */
    public ValueCard getValueCards() {
        return null;
    }

    // TODO: PACK-Implementation?
}

