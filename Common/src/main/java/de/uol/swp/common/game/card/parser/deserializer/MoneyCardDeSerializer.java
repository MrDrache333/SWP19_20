package de.uol.swp.common.game.card.parser.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.MoneyCard;

import java.lang.reflect.Type;

public class MoneyCardDeSerializer implements JsonDeserializer<MoneyCard> {

    @Override
    public MoneyCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new MoneyCard(obj.getAsJsonPrimitive("name").getAsString(), obj.getAsJsonPrimitive("id").getAsShort(), obj.getAsJsonPrimitive("cost").getAsShort(), obj.getAsJsonPrimitive("value").getAsShort());
    }
}
