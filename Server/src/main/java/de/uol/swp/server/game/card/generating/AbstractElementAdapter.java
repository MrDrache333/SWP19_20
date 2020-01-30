package de.uol.swp.server.game.card.generating;

import com.google.gson.*;
import de.uol.swp.server.game.card.Card;

import java.lang.reflect.Type;

/**
 * Diese Klasse ist dafür da, dass die Kartenobjekte welche in der Json definiert sind zu den richtigen Klassen zugeordnet werden.
 * Dies ist nötig, da Gson leider nicht anhand eines Attributes auf Subklassen von Abstrakten Klassen zuordnen kann.
 * Zurzeit benötigen wir nur die Deserialize-Methode.
 * Die Methode ordnet den Elementen aus der Json zu dem Attribut "type" die richtige Klasse zu.
 * Für tiefergehende Information zum Ablauf mit Gson, bitte bei mir melden.
 *
 * @author Ferit
 * @version 1
 * @since Sprint5
 */
public class AbstractElementAdapter implements JsonSerializer<Card>, JsonDeserializer<Card> {
    // Wird z.Z noch nicht benötigt.
    // TODO: Wenn Packs hinzugefügt werden können. Implementierung anpassen.
    @Override
    public JsonElement serialize(Card card, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("class", new JsonPrimitive(card.getClass().getSimpleName()));
        result.add("properties", jsonSerializationContext.serialize(card, card.getClass()));
        return result;

    }

    // Konvertiert von Json -> Java Card Objects.
    @Override
    public Card deserialize(JsonElement jsonElement, Type typeOfCard, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String type = jsonObject.get("class").getAsString();
        JsonElement elem = jsonObject.get("properties");
        try {
            String thepackage = "de.uol.swp.server.game.card.";
            return jsonDeserializationContext.deserialize(elem, Class.forName(thepackage + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Ich kenne folgende Klasse nicht: " + type, e);

        }
    }
}
