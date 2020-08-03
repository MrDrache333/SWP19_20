package de.uol.swp.common.game;

import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.card.parser.components.CardAction.types.UseCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionCardTest {


    @Test
    public void createCardTest() {
        ArrayList<CardAction>  actions = new ArrayList<CardAction>();
        UseCard cardAction = new UseCard((short) 12);
        actions.add(cardAction);
        ActionCard card = new ActionCard("Provinz",(short)02, (short)5, actions, ActionCard.ActionType.Attack);
        assertEquals("Provinz", card.getName());
        assertEquals(02, card.getId());
        assertEquals(5, card.getCosts());
        assertEquals(ActionCard.ActionType.Attack, card.getType());
        assertEquals(Card.Type.ACTIONCARD, card.getCardType());
        assertEquals(actions, card.getActions());

    }


}
