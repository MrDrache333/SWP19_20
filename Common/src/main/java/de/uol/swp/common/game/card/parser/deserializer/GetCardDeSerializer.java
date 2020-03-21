package de.uol.swp.common.game.card.parser.deserializer;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GetCardDeSerializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        short count;
        if (obj.has("count")) count = obj.getAsJsonPrimitive("count").getAsShort();


        return null;
    }
}
