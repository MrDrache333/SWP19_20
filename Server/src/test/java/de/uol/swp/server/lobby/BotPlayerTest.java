package de.uol.swp.server.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardAction.Value;
import de.uol.swp.common.game.card.parser.components.CardAction.request.ChooseCardRequest;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.request.AddBotRequest;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.UpdateLobbyReadyStatusRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.game.player.Player;
import de.uol.swp.server.game.player.bot.BotPlayer;
import de.uol.swp.server.game.player.bot.internal.messages.AuthBotInternalRequest;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse des Botplayers
 *
 * @author Ferit
 * @since Sprint 8
 */
@SuppressWarnings("UnstableApiUsage")
class BotPlayerTest {

    static final User lobbyOwner = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final EventBus bus = new EventBus();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final LobbyManagement lobbyManagement = new LobbyManagement();
    final LobbyService lobbyService = new LobbyService(lobbyManagement, new AuthenticationService(bus, userManagement, lobbyManagement), new ChatManagement(), bus);
    private UUID lobbyID;
    final GameManagement gameManagement = new GameManagement(new ChatManagement(), lobbyManagement);
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    /**
     * Definiert den Umgang mit Dead-Events
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Registriert den EventBus
     *
     * @author Ferit
     * @since Sprint 8
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Deregistriert den EventBus
     *
     * @author Ferit
     * @since Sprint 8
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Vor jedem Test wird eine Lobby erstellt.
     *
     * @author Julia, Marvin
     * @since Sprint 3
     */
    @BeforeEach
    void createLobby() {
        lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
    }

    /**
     * Löscht eine Lobby
     *
     * @author Ferit
     * @since Sprint 8
     */
    @AfterEach
    void dropLobby() {
        lobbyManagement.dropLobby(lobbyID);
    }

    /**
     * Erzeugt einen BotPlayer
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void createBotPlayerTest() {
        if (lobbyManagement.getLobby(lobbyID).isPresent()) {
            AddBotRequest newReq = new AddBotRequest(lobbyID);
            bus.post(newReq);
            assertTrue(lobbyManagement.getLobby(lobbyID).get().getPlayers() == 2);
        }
    }

    /**
     * Prüft ob der Bot bereit ist
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void botIsReadyTest() {
        if (lobbyManagement.getLobby(lobbyID).isPresent()) {
            AddBotRequest newReq = new AddBotRequest(lobbyID);
            bus.post(newReq);

            ArrayList<User> theUser;
            theUser = new ArrayList<>(lobbyManagement.getLobby(lobbyID).get().getUsers());

            User theBotPlayer;
            if (theUser.get(0).getIsBot()) {
                theBotPlayer = theUser.get(0);
            } else {
                theBotPlayer = theUser.get(1);
            }
            assertTrue(lobbyManagement.getLobby(lobbyID).get().getReadyStatus(theBotPlayer));
        }
    }

    @Test
    void botPlayingGameTest(){
        if (lobbyManagement.getLobby(lobbyID).isPresent()) {
            String[] collectionBotName = {"King Arthur", "Merlin", "Die Queen", "Prinzessin Diana", "Donald Trump"};
            String theRandomBotName = collectionBotName[(int) (Math.random() * collectionBotName.length)] + (int) (Math.random() * 999);
            BotPlayer createdBot = new BotPlayer(theRandomBotName, bus, lobbyID);
            UserDTO userDTO = new UserDTO(createdBot.getTheUserInThePlayer().getUsername(), createdBot.getTheUserInThePlayer().getPassword(), createdBot.getTheUserInThePlayer().getEMail(), true);
            bus.post(new AuthBotInternalRequest(createdBot));
            bus.post(new LobbyJoinUserRequest(lobbyID, userDTO, true));

            ArrayList<Short> hand = new ArrayList<Short>();
            hand.add((short)1);hand.add((short)6);hand.add((short)13);hand.add((short)1);hand.add((short)4);

            bus.post(new DrawHandMessage(hand,lobbyID, (short) 2, createdBot.getTheUserInThePlayer()));
            for(Short cardID : hand){
                assertTrue(createdBot.getCardsOnHandIDs().contains((Short) cardID));
            }

            ArrayList<Short> cardsToChoose = new ArrayList<Short>();
            cardsToChoose.add((short)1);cardsToChoose.add((short)6);cardsToChoose.add((short)1);cardsToChoose.add((short)4);
            Map<Short, Integer> cardField = new HashMap<>();
            for(int i = 1;i<=38;i++){
                cardField.put((short)i,10);
            }
            bus.post(new SendCardFieldMessage(lobbyID,cardField));

            bus.post(new StartActionPhaseMessage(createdBot.getTheUserInThePlayer(), lobbyID, new Timestamp(System.currentTimeMillis())));
            assertTrue(createdBot.getPlayCardID() == 13);

            bus.post(new PlayCardMessage(lobbyID, createdBot.getTheUserInThePlayer(), (short) 13, true, false));
            assertTrue(createdBot.getPlayCardID() != 0);
            assertTrue(!createdBot.getCardsOnHandIDs().contains((short) 13));

            bus.post(new ChooseCardRequest(lobbyID, createdBot.getTheUserInThePlayer(), cardsToChoose, new Value((short) 4), createdBot.getTheUserInThePlayer(), AbstractPlayground.ZoneType.NONE, ""));
            assertTrue(Collections.frequency(createdBot.getCardsOnHandIDs(), (short)1)==1);
            assertTrue(createdBot.getTakeCardWithSpecificValue() == 3);

            bus.post(new StartBuyPhaseMessage(createdBot.getTheUserInThePlayer(), lobbyID));
            bus.post(new BuyCardMessage(lobbyID, createdBot.getTheUserInThePlayer(), (short)2, 9, (short) 3));
            assertTrue(createdBot.getCardsInPossessionIDs().contains((short) 2));

            bus.post(new StartBuyPhaseMessage(createdBot.getTheUserInThePlayer(), lobbyID));
            bus.post(new BuyCardMessage(lobbyID, createdBot.getTheUserInThePlayer(), (short)1, 9, (short) 3));
            assertTrue(Collections.frequency(createdBot.getCardsInPossessionIDs(), (short)1)==2);
        }
    }
}