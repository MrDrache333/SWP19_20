package de.uol.swp.common.game;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.CurseCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CursedCardTest {



    @Test
    public void createCardTest() {
        CurseCard card = new CurseCard("Provinz",(short)02, (short)5);
        assertEquals("Provinz", card.getName());
        assertEquals(02, card.getId());
        assertEquals(5, card.getValue());
        assertEquals(Card.Type.CURSECARD, card.getCardType());
        assertEquals((short) 0, card.getCosts());
    }


    @Test
    public void setValueCardTest() {
        CurseCard card = new CurseCard("Provinz",(short)02, (short)5);
        card.setValue((short) 100);
        assertEquals("Provinz", card.getName());
        assertEquals(02, card.getId());
        assertEquals(100, card.getValue());
        assertEquals(Card.Type.CURSECARD, card.getCardType());
        assertEquals((short) 0, card.getCosts());
    }



}
