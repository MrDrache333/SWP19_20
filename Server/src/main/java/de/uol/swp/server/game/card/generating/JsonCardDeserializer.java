package de.uol.swp.server.game.card.generating;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uol.swp.server.game.card.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Diese Klasse konvertiert Cards die in einer Json formatiert sind zu Spezialisierten-Card Objekten mit den zugehörigen Eigenschaften.
 *
 * @author Ferit
 * @version 1.0
 * @since Sprint 5
 */
public class JsonCardDeserializer {
    private ArrayList<Card> cards = new ArrayList<>();
    private Card[] newCardElementsArray;
    private String jsonFilePath = "Common/src/main/resources/cards.json";
    private ArrayList<MoneyCard> moneyCardArrayList = new ArrayList<>();
    private ArrayList<ActionCard> actionCardArrayList = new ArrayList<>();
    private ArrayList<ReactionCard> reactionCardArrayList = new ArrayList<>();
    private ArrayList<ValueCard> valueCardArrayList = new ArrayList<>();

    /**
     * Diese Methode holt sich alle Karten aus der vorgegebenen Json Datei.
     *
     * @throws FileNotFoundException Wirft eine Exception, wenn die JSON Datei nicht gefunden werden kann.
     */
    public void generateAllCards() throws FileNotFoundException {
        GsonBuilder gsonobj = new GsonBuilder();
        gsonobj.registerTypeAdapter(Card.class, new AbstractElementAdapter());
        Gson gsonRealObj = gsonobj.create();
        newCardElementsArray = gsonRealObj.fromJson(new FileReader(jsonFilePath), Card[].class);
        cards.addAll(Arrays.asList(newCardElementsArray));
    }

    /**
     * Aufruf übergibt ein MoneyCard ArrayList
     *
     * @return MoneyCard-ArrayList
     */
    public ArrayList<MoneyCard> getMoneyCards() {
        for (Card c : cards) {
            try {
                if (c.getCardtype().equals(Card.Type.MoneyCard) && !moneyCardArrayList.contains(c))
                    moneyCardArrayList.add((MoneyCard) c);
            } catch (NullPointerException ignored) {
            }
        }
        return moneyCardArrayList;
    }

    /**
     * @return Übergibt ein ActionCard-ArrayList
     */
    public ArrayList<ActionCard> getActionCards() {
        for (Card c : cards) {
            try {
                if (c.getCardtype().equals(Card.Type.ActionCard) && !actionCardArrayList.contains(c))
                    actionCardArrayList.add((ActionCard) c);
            } catch (NullPointerException ignored) {
            }
        }
        return actionCardArrayList;
    }

    /**
     * @return Übergibt ein ReactionCard ArrayList.
     */
    public ArrayList<ReactionCard> getReactionCards() {
        for (Card c : cards) {
            try {
                if (c.getCardtype().equals(Card.Type.ReactionCard) && !moneyCardArrayList.contains(c))
                    reactionCardArrayList.add((ReactionCard) c);
            } catch (NullPointerException ignored) {
            }
        }
        return reactionCardArrayList;
    }

    /**
     * @return Übergibt ValueCard-ArrayList.
     */
    public ArrayList<ValueCard> getValueCards() {
        for (Card c : cards) {
            try {
                if (c.getCardtype().equals(Card.Type.ValueCard) && !valueCardArrayList.contains(c))
                    valueCardArrayList.add((ValueCard) c);
            } catch (NullPointerException ignored) {
            }
        }
        return valueCardArrayList;
    }
}