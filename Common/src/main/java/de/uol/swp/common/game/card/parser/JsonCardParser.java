package de.uol.swp.common.game.card.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.CurseCard;
import de.uol.swp.common.game.card.MoneyCard;
import de.uol.swp.common.game.card.ValueCard;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.card.parser.components.deserializer.ActionCardDeSerializer;
import de.uol.swp.common.game.card.parser.components.deserializer.CurseCardDeSerializer;
import de.uol.swp.common.game.card.parser.components.deserializer.MoneyCardDeSerializer;
import de.uol.swp.common.game.card.parser.components.deserializer.ValueCardDeSerializer;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * JSonCardParser representiert eine Möglichkeit, Karten aus einer JSon-Datei zu laden und als Objekt zurück zu geben.
 */
public class JsonCardParser {

    /**
     * Methode, die versucht ein angegebenes Kartenpaket zu laden.
     *
     * @param packname Das zu ladene Kartenpack
     * @return Das Kartenpack, oder Null, falls ein Fehler aufgetreten ist
     * @author KenoO
     * @since Sprint 5
     */
    public CardPack loadPack(String packname) {
        GsonBuilder gsonobj = new GsonBuilder().
                registerTypeAdapter(ActionCard.class, new ActionCardDeSerializer()).
                registerTypeAdapter(ValueCard.class, new ValueCardDeSerializer()).
                registerTypeAdapter(MoneyCard.class, new MoneyCardDeSerializer()).
                registerTypeAdapter(CurseCard.class, new CurseCardDeSerializer());

        Gson gsonRealObj = gsonobj.create();
        CardPack pack;
        try {
            pack = gsonRealObj.fromJson(new FileReader(this.getClass().getResource("/cards/packs/" + packname + "/" + packname + ".json").toExternalForm().replace("file:", "")), CardPack.class);
        } catch (FileNotFoundException e) {
            pack = null;
        }
        return pack;
    }
}