package de.uol.swp.common.game.card.parser;

import com.google.gson.*;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.MoneyCard;
import de.uol.swp.common.game.card.ValueCard;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

/**
 * JSonCardParser representiert eine Möglichkeit, Karten aus einer JSon-Datei zu laden und als Objekt zurück zu geben.
 */
public class JsonCardParser {

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
        GsonBuilder gsonobj = new GsonBuilder().
                registerTypeAdapter(ActionCard.class, new ActionCardDeSerializer()).
                registerTypeAdapter(ValueCard.class, new ValueCardDeSerializer()).
                registerTypeAdapter(MoneyCard.class, new MoneyCardDeSerializer());

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

class ActionCardDeSerializer implements JsonDeserializer<ActionCard> {

    @Override
    public ActionCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new ActionCard(obj.getAsJsonPrimitive("name").getAsString(), obj.getAsJsonPrimitive("id").getAsShort(), obj.getAsJsonPrimitive("cost").getAsShort());
    }
}

class ValueCardDeSerializer implements JsonDeserializer<ValueCard> {

    @Override
    public ValueCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new ValueCard(obj.getAsJsonPrimitive("name").getAsString(), obj.getAsJsonPrimitive("id").getAsShort(), obj.getAsJsonPrimitive("cost").getAsShort(), obj.getAsJsonPrimitive("value").getAsShort());
    }
}

class MoneyCardDeSerializer implements JsonDeserializer<MoneyCard> {

    @Override
    public MoneyCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new MoneyCard(obj.getAsJsonPrimitive("name").getAsString(), obj.getAsJsonPrimitive("id").getAsShort(), obj.getAsJsonPrimitive("cost").getAsShort(), obj.getAsJsonPrimitive("value").getAsShort());
    }
}
