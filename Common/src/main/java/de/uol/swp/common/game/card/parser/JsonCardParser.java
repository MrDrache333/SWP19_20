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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;

/**
 * JSonCardParser representiert eine Möglichkeit, Karten aus einer JSon-Datei zu laden und als Objekt zurück zu geben.
 */
public class JsonCardParser {

    private static final Logger LOG = LogManager.getLogger(JsonCardParser.class);

    /**
     * Methode, die versucht ein angegebenes Kartenpaket zu laden.
     *
     * @param packname Das zu ladene Kartenpack
     * @return Das Kartenpack, oder Null, falls ein Fehler aufgetreten ist
     * @author KenoO
     * @since Sprint 5
     */
    public CardPack loadPack(String packname) {
        GsonBuilder gsonObj = new GsonBuilder().
                registerTypeAdapter(ActionCard.class, new ActionCardDeSerializer()).
                registerTypeAdapter(ValueCard.class, new ValueCardDeSerializer()).
                registerTypeAdapter(MoneyCard.class, new MoneyCardDeSerializer()).
                registerTypeAdapter(CurseCard.class, new CurseCardDeSerializer());

        Gson gsonRealObj = gsonObj.create();
        CardPack pack = null;
        FileReader fr;
        try {
            fr = new FileReader(this.getClass().getResource("/cards/packs/" + packname + "/" + packname + ".json").toExternalForm().replace("file:", ""));

            try {
                pack = gsonRealObj.fromJson(fr, CardPack.class);
            } finally {
                fr.close();
            }
        } catch (IOException e) {
            LOG.debug("Fehler beim Laden des Kartenpaketes!");
        }
        return pack;
    }
}