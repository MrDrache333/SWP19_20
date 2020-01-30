package de.uol.swp.server.game.card.generating;

import com.google.gson.*;
import de.uol.swp.server.game.card.Card;

import java.lang.reflect.Type;

public class AbstractElementAdapter implements JsonSerializer<Card>, JsonDeserializer<Card> {
    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(card.getClass().getSimpleName()));
        result.add("properties", jsonSerializationContext.serialize(card, card.getClass()));
        return result;

    }

    @Override
    public Card deserialize(JsonElement jsonElement, Type typeOfCard, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("class").getAsString();
        JsonElement elem = jsonObject.get("properties");
        try {
            String thepackage = "de.uol.swp.server.game.card.";
            return jsonDeserializationContext.deserialize(elem, Class.forName(thepackage + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unkown element Type" + type, e);

        }
    }
}
