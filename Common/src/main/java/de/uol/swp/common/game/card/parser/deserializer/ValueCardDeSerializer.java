package de.uol.swp.common.game.card.parser.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.ValueCard;

import java.lang.reflect.Type;

public class ValueCardDeSerializer implements JsonDeserializer<ValueCard> {

    @Override
    public ValueCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new ValueCard(obj.getAsJsonPrimitive("name").getAsString(), obj.getAsJsonPrimitive("id").getAsShort(), obj.getAsJsonPrimitive("cost").getAsShort(), obj.getAsJsonPrimitive("value").getAsShort());
    }
}
