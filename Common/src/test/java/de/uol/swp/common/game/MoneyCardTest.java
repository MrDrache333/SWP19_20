package de.uol.swp.common.game;

import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.MoneyCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyCardTest {

    /**
     * Testet, ob eine Karte des Typs Money erstellt werden kann
     *
     * @author Paula
     * @since Sprint10
     */

    @Test
    public void createCardTest() {
        MoneyCard card = new MoneyCard("Provinz", (short) 02, (short) 5, (short) 10);
        assertEquals("Provinz", card.getName());
        assertEquals(02, card.getId());
        assertEquals(10, card.getValue());
        assertEquals(Card.Type.MONEYCARD, card.getCardType());
        assertEquals((short) 5, card.getCosts());
    }

    /**
     * Testet den Setter der MoneyCard
     *
     * @author Paula
     * @since Sprint10
     */

    @Test
    public void setValueCardTest() {
        MoneyCard card = new MoneyCard("Provinz", (short) 02, (short) 5, (short) 10);
        card.setValue((short) 100);
        assertEquals("Provinz", card.getName());
        assertEquals(02, card.getId());
        assertEquals(100, card.getValue());
        assertEquals(Card.Type.MONEYCARD, card.getCardType());
        assertEquals((short) 5, card.getCosts());
    }
}
