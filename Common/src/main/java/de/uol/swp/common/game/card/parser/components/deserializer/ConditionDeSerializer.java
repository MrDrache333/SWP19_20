package de.uol.swp.common.game.card.parser.components.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.types.subtypes.Condition;

import java.lang.reflect.Type;

/**
 * Deserialisert eine Bedingung.
 *
 * @author Keno0, Timo
 * @since Sprint 6
 */
public class ConditionDeSerializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject obj = jsonElement.getAsJsonObject();

        if (obj.has("left")) {
            JsonElement left = obj.get("left");
            if (left.isJsonObject() && !obj.has("operator") && !obj.has("right")) {
                GsonBuilder builder = new GsonBuilder().registerTypeAdapter(CardAction.class, new CardActionArrayDeSerializer());
                Gson gson = builder.create();

                return new Condition<CardAction>(gson.fromJson(left, CardAction.class));
            } else if (left.isJsonObject() && obj.has("operator") && obj.has("right")) {
                GsonBuilder builder = new GsonBuilder().registerTypeAdapter(CardAction.class, new CardActionArrayDeSerializer());
                Gson gson = builder.create();

                Condition.Operator operator = Condition.Operator.valueOf(obj.get("operator").getAsString());
                int r = obj.get("right").getAsInt();
                return new Condition<>(gson.fromJson(left, CardAction.class), operator, r);
            } else {

                int l = left.getAsInt();
                Condition.Operator operator;
                int r;
                if (obj.has("operator")) {
                    operator = Condition.Operator.valueOf(obj.get("operator").getAsString());
                } else throw new JsonParseException("Condition has no Operator!");
                if (obj.has("right")) {
                    r = obj.get("right").getAsInt();
                } else throw new JsonParseException("Condition has no right Parameter!");

                return new Condition<>(l, operator, r);
            }
        } else
            throw new JsonParseException("Condition has no left Parameter!");
    }
}
