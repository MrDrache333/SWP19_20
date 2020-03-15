package de.uol.swp.common.game.card.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * JSonCardParser representiert eine Möglichkeit, Karten aus einer JSon-Datei zu laden und als Objekt zurück zu geben.
 */
public class JsonCardParser {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        CardPack pack = new JsonCardParser().loadPack("Basispack");
        System.out.println(pack.toString());
    }

    /**
     * Methode, die versucht ein angegebenes Kartenpaket zu laden.
     *
     * @param packname Das zu ladene Kartenpack
     * @return Das Kartenpack, oder Null, falls ein Fehler aufgetreten ist
     * @author KenoO
     * @since Sprint 5
     */
    public CardPack loadPack(String packname) {
        GsonBuilder gsonobj = new GsonBuilder();
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