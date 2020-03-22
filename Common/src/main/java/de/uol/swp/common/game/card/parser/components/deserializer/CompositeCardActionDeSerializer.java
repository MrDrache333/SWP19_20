package de.uol.swp.common.game.card.parser.components.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CompositeCardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;

import java.lang.reflect.Type;

/**
 * Deserialisierer von zusammengesetzen Aktionen
 * Die zusammengesetzen Aktionen benötigt einen eigenen Deserialisierer, da eine Kartenaktion einen beliebigen Typ
 * enthalten kann und dieser erstmal heruasgefunden werden muss und dann erst das Objekt dementsprechend initialisiert
 * werden kann
 */
public class CompositeCardActionDeSerializer implements JsonDeserializer<CompositeCardAction> {

    @Override
    public CompositeCardAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Value.class, new ValueDeSerializer());
        Gson gson = builder.create();

        //Je nach enthaltener Aktion die entsprechende Aktion in das zusammengesetzte Aktionsobjekt laden
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
            return new CompositeCardAction(gson.fromJson(obj.get("Until").toString(), While.class));
        if (obj.has("UseCard"))
            return new CompositeCardAction(gson.fromJson(obj.get("UseCard").toString(), UseCard.class));

        return null;
    }
}
