package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.ValueCard;
import de.uol.swp.common.game.messages.ActualPointMessage;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("UnstableApiUsage")
public class ActualPointMessageTest {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final EventBus bus = new EventBus();
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()), lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authenticationService);
    private final ArrayList<Short> chosenCards = new ArrayList<>();

    static UUID gameID;
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    /**
     * Initialisiert die benötigten Objekte/Parameter
     *
     * @author Paula
     * @since Sprint 6
     */
    void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdPlayer);
            chosenCards.add((short) 10);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setChosenCards(chosenCards);
            bus.post(new StartGameInternalMessage(gameID));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Löscht das Spiel, die Lobby und den Chat nach jedem Testdurchlauf
     *
     * @author Paula
     * @since Sprint 6
     */
    @AfterEach
    void afterEach() {
        gameManagement.deleteGame(gameID);
        lobbyManagement.dropLobby(gameID);
        chatManagement.deleteChat(gameID.toString());
        bus.unregister(this);
    }

    /**
     * Setzt vor jedem Test das aktuelle Event auf null und registriert diese Testklasse auf dem Eventbus
     *
     * @author Paula
     * @since Sprint 6
     */
    @BeforeEach
    void registerBus() {
        bus.register(this);
        init();
    }

    /**
     * Bei Auftreten eines DeadEvents wird dieses ausgegeben und der CountDownLatch wird um eins verringert
     *
     * @param e das DeadEvent
     * @author Paula
     * @since Sprint 6
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Testet, ob die ActualPointMessage versendet wird.
     *
     * @author Haschem
     * @since Sprint 10
     */
    @Test
    void ActualPointsVersendetTest() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.newTurn();
            assertTrue(event instanceof ActualPointMessage);

        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet, ob die Initialen Punkte Richtig gesetzt und versendet werden.
     *
     * @author Haschem
     * @since Sprint 10
     */
    @Test
    void ActualPointsInitialUeberpruefenTest() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.newTurn();
            assertTrue(event instanceof ActualPointMessage);
            ActualPointMessage message = (ActualPointMessage) event;
            assertTrue(message.getPoints() == 3);

        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet, ob die LobbyID richtig gesetzt und mitgeteilt wird.
     *
     * @author Haschem
     * @since Sprint 10
     */
    @Test
    void ActualPointsLobbyIdTest() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.newTurn();
            assertTrue(event instanceof ActualPointMessage);
            ActualPointMessage message = (ActualPointMessage) event;
            assertTrue(message.getLobbyID().equals(gameID));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}
