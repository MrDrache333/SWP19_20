package de.uol.swp.common.game.card.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.uol.swp.common.game.card.parser.action.ComplexCardAction;
import de.uol.swp.common.game.card.parser.action.types.GetCard;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonCardParserTest {


    @Test
    void loadPack() {

        JsonCardParser parser = new JsonCardParser();
        CardPack pack = parser.loadPack("Basispack");
        assertNotNull(pack);
    }

    @Test
    void deSerialliserComplexCardAction() {
        GsonBuilder gsonobj = new GsonBuilder();

        Gson gsonRealObj = gsonobj.create();
        ComplexCardAction pack;
        try {
            pack = gsonRealObj.fromJson(new FileReader(this.getClass().getResource("/cards/parser/deserialiser/ComplexCardAction.json").toExternalForm().replace("file:", "")), GetCard.class);
        } catch (FileNotFoundException e) {
            pack = null;
        }
        assertNotNull(pack);

    }


}