package de.uol.swp.common.game.card.parser.components.deserializer;

import com.google.gson.*;
import de.uol.swp.common.game.card.CurseCard;

import java.lang.reflect.Type;

/**
 * Deserialisierer von Anwesenkarten
 * Die Karte benötigt einen eigenen Deserialisierer, da der Kartentyp festgelegt werden muss, dies aber im Kostruktor
 * passieren würde, welcher beim DeSerialisieren nicht aufgerufen werden würde
 */
public class CurseCardDeSerializer implements JsonDeserializer<CurseCard> {

    @Override
    public CurseCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new CurseCard(obj.getAsJsonPrimitive("name").getAsString(), obj.getAsJsonPrimitive("id").getAsShort(), obj.getAsJsonPrimitive("value").getAsShort());
    }
}
