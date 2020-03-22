package de.uol.swp.common.game.card.parser;

import de.uol.swp.common.game.card.parser.components.CardPack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonCardParserTest {


    @Test
    void loadPack() {

        JsonCardParser parser = new JsonCardParser();
        CardPack pack = parser.loadPack("Basispack");
        assertNotNull(pack);
    }


}