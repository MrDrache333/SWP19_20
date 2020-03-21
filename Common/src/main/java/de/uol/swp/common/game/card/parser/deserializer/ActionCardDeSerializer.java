package de.uol.swp.common.game.card.parser.deserializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.parser.action.CompositeCardAction;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActionCardDeSerializer implements JsonDeserializer<ActionCard> {

    @Override
    public ActionCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        Type cardAction = new TypeToken<ArrayList<CompositeCardAction>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(CompositeCardAction.class, new CompositeCardActionDeSerializer());
        Gson gson = builder.create();

        ArrayList<CompositeCardAction> actions = obj.has("actions") ? gson.fromJson(obj.get("actions").toString(), cardAction) : null;

        return new ActionCard(obj.getAsJsonPrimitive("name").getAsString(),
                obj.getAsJsonPrimitive("id").getAsShort(),
                obj.getAsJsonPrimitive("cost").getAsShort(),
                actions
        );
    }
}
