package de.uol.swp.common.game.card.parser.components.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;

import java.lang.reflect.Type;

/**
 * Deserialisierer von Value
 * Die Karte benötigt einen eigenen Deserialisierer, da der min- bzw. max-Wert festgelegt werden muss, dies aber im Kostruktor
 * passieren würde, welcher beim DeSerialisieren nicht aufgerufen werden würde
 */
public class ValueDeSerializer implements JsonDeserializer<Value> {
    @Override
    public Value deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        if (obj.has("min") && obj.has("max")) {
            return new Value(obj.get("min").getAsShort(), obj.get("max").getAsShort());
        } else if (obj.has("min")) {
            return new Value(obj.get("min").getAsShort());
        } else if (obj.has("max")) {
            return new Value((short) 0, obj.get("max").getAsShort());
        }
        return new Value((short) 1);
    }
}
