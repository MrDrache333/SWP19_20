package de.uol.swp.common.game.card.parser.components.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.game.card.parser.components.CardAction.types.*;
import de.uol.swp.common.game.card.parser.components.CardAction.types.subtypes.Condition;

import java.lang.reflect.Type;

/**
 * Deserialisierer von zusammengesetzen Aktionen
 * Die zusammengesetzen Aktionen benötigt einen eigenen Deserialisierer, da eine Kartenaktion einen beliebigen Typ
 * enthalten kann und dieser erstmal heruasgefunden werden muss und dann erst das Objekt dementsprechend initialisiert
 * werden kann
 */
public class CardActionArrayDeSerializer implements JsonDeserializer<CardAction> {

    @Override
    public CardAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Value.class, new ValueDeSerializer()).registerTypeAdapter(CardAction.class, new CardActionArrayDeSerializer()).registerTypeAdapter(Condition.class, new ConditionDeSerializer());
        Gson gson = builder.create();

        //Je nach enthaltener Aktion die entsprechende Aktion in das zusammengesetzte Aktionsobjekt laden
        if (obj.has("AddCapablePlayerActivity"))
            return gson.fromJson(obj.get("AddCapablePlayerActivity").toString(), AddCapablePlayerActivity.class);
        if (obj.has("ChooseCard"))
            return (gson.fromJson(obj.get("ChooseCard").toString(), ChooseCard.class));
        if (obj.has("ChooseNextAction"))
            return (gson.fromJson(obj.get("ChooseNextAction").toString(), ChooseNextAction.class));
        if (obj.has("Count"))
            return (gson.fromJson(obj.get("Count").toString(), Count.class));
        if (obj.has("ForEach"))
            return (gson.fromJson(obj.get("ForEach").toString(), ForEach.class));
        if (obj.has("GetCard"))
            return (gson.fromJson(obj.get("GetCard").toString(), GetCard.class));
        if (obj.has("If"))
            return (gson.fromJson(obj.get("If").toString(), If.class));
        if (obj.has("Move"))
            return (gson.fromJson(obj.get("Move").toString(), Move.class));
        if (obj.has("ShowCard"))
            return (gson.fromJson(obj.get("ShowCard").toString(), ShowCard.class));
        if (obj.has("UseCard"))
            return (gson.fromJson(obj.get("UseCard").toString(), UseCard.class));
        if (obj.has("While"))
            return (gson.fromJson(obj.get("While").toString(), While.class));

        return null;
    }
}
