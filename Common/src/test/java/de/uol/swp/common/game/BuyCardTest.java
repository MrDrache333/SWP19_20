package de.uol.swp.common.game;

import de.uol.swp.common.game.messages.BuyCardMessage;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BuyCardTest {
    /**
     * Testet die BuyCardMessage und BuyCardRequest
     *
     * @author Paula
     * @since Sprint10
     */
    private static final User defaultUser = new UserDTO("Marius", "test123", "marius@test.de");


    @Test
    public void createNewBuyCardMessage () {
        UUID gameId = UUID.randomUUID();
        BuyCardMessage msg = new BuyCardMessage(gameId, defaultUser, (short) 12, 15, (short) 2);
        assertEquals(gameId, msg.getGameID());
        assertEquals(defaultUser, msg.getCurrentUser());
        assertEquals((short)12, msg.getCardID());
        assertEquals(15, msg.getCounterCard());
        assertEquals((short) 2, msg.getCostCard());
        //assertEquals(buycardmessage.getCardID(), msg.getCardID());
        //assertEquals(buycardmessage.getCardID(),  card.getId());

    }

    @Test
    public void createNewBuyCardRequest () {
        UUID lobbyId = UUID.randomUUID();
        BuyCardRequest request = new BuyCardRequest(lobbyId, defaultUser, (short) 10);
        assertEquals((short) 10,request.getCardID());
        assertEquals(lobbyId, request.getLobbyID());
        assertEquals(defaultUser, request.getCurrentUser());


    }

}
