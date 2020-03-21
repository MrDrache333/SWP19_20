package de.uol.swp.common.game.card.parser.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.parser.action.CompositeCardAction;
import de.uol.swp.common.game.card.parser.action.types.*;

import java.lang.reflect.Type;

public class CompositeCardActionDeSerializer implements JsonDeserializer<CompositeCardAction> {

    @Override
    public CompositeCardAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        if (obj.has("AddCapablePlayerActivity"))
            return new CompositeCardAction(gson.fromJson(obj.get("AddCapablePlayerActivity").toString(), AddCapablePlayerActivity.class));
        if (obj.has("ChooseCard"))
            return new CompositeCardAction(gson.fromJson(obj.get("ChooseCard").toString(), ChooseCard.class));
        if (obj.has("ChooseNextAction"))
            return new CompositeCardAction(gson.fromJson(obj.get("ChooseNextAction").toString(), ChooseNextAction.class));
        if (obj.has("ForEach"))
            return new CompositeCardAction(gson.fromJson(obj.get("ForEach").toString(), ForEach.class));
        if (obj.has("GetCard"))
            return new CompositeCardAction(gson.fromJson(obj.get("GetCard").toString(), GetCard.class));
        if (obj.has("If"))
            return new CompositeCardAction(gson.fromJson(obj.get("If").toString(), If.class));
        if (obj.has("Move"))
            return new CompositeCardAction(gson.fromJson(obj.get("Move").toString(), Move.class));
        if (obj.has("ShowCard"))
            return new CompositeCardAction(gson.fromJson(obj.get("ShowCard").toString(), ShowCard.class));
        if (obj.has("Until"))
            return new CompositeCardAction(gson.fromJson(obj.get("Until").toString(), Until.class));
        if (obj.has("UseCard"))
            return new CompositeCardAction(gson.fromJson(obj.get("UseCard").toString(), UseCard.class));

        return null;
    }
}
