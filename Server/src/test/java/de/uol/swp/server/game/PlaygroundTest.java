package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.GameGiveUpRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class PlaygroundTest {

    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final String lobbyName = "DrawHandMessageLobyTest";
    static final String lobbyPassword = "";
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final UserStore userStore = new MainMemoryBasedUserStore();
    static final EventBus bus = new EventBus();
    static final UserManagement userManagement = new UserManagement(userStore);
    static final AuthenticationService authService = new AuthenticationService(bus, userManagement, lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authService);
    static UUID id;
    private final CountDownLatch lock = new CountDownLatch(1);
    private static ArrayList<Short> chosenCards = new ArrayList<Short>();



    static UUID gameID;
    private Object event;

    @BeforeAll
    static void init() {
        gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
        chatManagement.createChat(gameID.toString());
        lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
        lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
        lobbyManagement.getLobby(gameID).get().setChosenCards(chosenCards);
        bus.post(new StartGameInternalMessage(gameID));
    }

    /**
     * Bei Auftreten eines DeadEvents wird dieses ausgegeben und der CountDownLatch wird um eins verringert
     *
     * @param e das DeadEvent
     * @author Marco
     * @since Start
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Setzt vor jedem Test das aktuelle Event auf null und registriert diese Testklasse auf dem Eventbus
     *
     * @author Marco
     * @since Start
     */
    @BeforeEach
    void registerBus() {
        init();
        event = null;
        bus.register(this);
    }

    /**
     * Meldet diese Testklasse nach jedem Test vom Eventbus ab
     *
     * @author Marco
     * @since Start
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Testet ob actual- und nextPlayer korrekt aktualisiert werden
     *
     * @author Julia
     * @since Sprint5
     */
    @Test
    void testNewTurn() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        int actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        int next = playground.getPlayers().indexOf(playground.getNextPlayer());
        //Player an erster Position der Liste beginnt
        assertEquals(0, actual);
        assertEquals(1, next);
        assertEquals(1, playground.getPlayerTurns().get(playground.getActualPlayer()));

        playground.setActualPhase(Phase.Type.Clearphase);
        playground.newTurn();
        actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        next = playground.getPlayers().indexOf(playground.getNextPlayer());
        assertEquals(1, actual);
        assertEquals(2, next);
        assertEquals(1, playground.getPlayerTurns().get(playground.getActualPlayer()));

        playground.setActualPhase(Phase.Type.Clearphase);
        playground.newTurn();
        actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        next = playground.getPlayers().indexOf(playground.getNextPlayer());
        assertEquals(2, actual);
        assertEquals(0, next);
        assertEquals(1, playground.getPlayerTurns().get(playground.getActualPlayer()));

        playground.setActualPhase(Phase.Type.Clearphase);
        playground.newTurn();
        assertEquals(2, playground.getPlayerTurns().get(playground.getActualPlayer()));
    }

    /**
     * Testet die checkForActionCard - Methode
     *
     * @author Julia
     * @since Sprint5
     */
    @Test
    void testCheckForActionCard() {
        //Bei Spielbeginn hat der Spieler keine Aktionskarten auf der Hand
        assertFalse(gameManagement.getGame(gameID).get().getPlayground().checkForActionCard());

        //TODO: weitere Fälle testen, wenn weitere Funktion (Kauf von Aktionskarten) implementiert wurde
    }

    /**
     * Testet, ob korrekt zur nächsten Phase gewechselt wird
     *
     * @author Julia
     * @since Sprint5
     */
    @Test
    void testNextPhase() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);

        playground.nextPhase();
        assertEquals(Phase.Type.Buyphase, playground.getActualPhase());

        playground.nextPhase();
        if (playground.checkForActionCard()) {
            assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
        } else {
            assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
        }

        playground.setActualPhase(Phase.Type.Clearphase);
        assertThrows(GamePhaseException.class, () -> playground.nextPhase());
    }

    /**
     * Testet, ob Requests ankommen und alles richtig durchlaufen wird und ein Spieler entfernt worden ist.
     *
     * @author Ferit
     * @since Sprint6
     */
    @Test
    void playerGaveUpTest() {
        UUID spielID = gameID;
        GameGiveUpRequest testRequest = new GameGiveUpRequest((UserDTO) secondPlayer, spielID);
        bus.post(testRequest);
        assertEquals(2, gameManagement.getGame(gameID).get().getPlayground().getPlayers().size());
    }

    /**
     * Testet, ob der spezifizierte Spieler der Aufgeben will, nach Aufgabe noch im Game befindet.
     *
     * @author Ferit
     * @since Sprint6
     */
    @Test
    void specificPlayerGaveUpTest() {
        GameGiveUpRequest testRequest = new GameGiveUpRequest((UserDTO) secondPlayer, gameID);
        bus.post(testRequest);
        assertTrue(!gameManagement.getGame(gameID).get().getPlayground().getPlayers().contains(secondPlayer.getUsername()));
    }

    /**
     * Testet, ob der mit den meisten Punkten gewinnt
     *
     * @author Julia
     * @since Sprint6
     */
    @Test
    void calculateWinnerHighestScoreTest() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        CardPack cardsPackField = playground.getCardsPackField();
        Card card = cardsPackField.getCards().getValueCards().get(2);
        playground.getActualPlayer().getPlayerDeck().getCardsDeck().add(card);
        List<String> winners = playground.calculateWinners();
        assertEquals(1, winners.size());
        assertTrue(winners.contains(playground.getActualPlayer().getPlayerName()));
    }

    /**
     * Testet, ob bei Punktegleichstand der mit den wenigsten Zügen gewinnt
     *
     * @author Julia
     * @since Sprint6
     */
    @Test
    void calculateWinnerFewestTurnsTest() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        List<String> winners = playground.calculateWinners();
        assertEquals(2, winners.size());
        assertFalse(winners.contains(playground.getActualPlayer().getPlayerName()));

        playground.setActualPhase(Phase.Type.Clearphase);
        playground.newTurn();
        winners = playground.calculateWinners();
        assertEquals(1, winners.size());
        assertTrue(winners.contains(playground.getNextPlayer().getPlayerName()));
    }

    /**
     * Testet ob die korrekte Anzahl an Karten auf dem Nachziehstapel gesendet wird
     *
     * @author Julia
     * @since Sprint7
     */
    @Test
    void cardsDeckSizeTest() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        int size = playground.sendCardsDeckSize();
        assertEquals(5, size);
        playground.getCompositePhase().executeClearPhase(playground.getActualPlayer());
        size = playground.sendCardsDeckSize();
        assertEquals(0, size);
        playground.getCompositePhase().executeClearPhase(playground.getActualPlayer());
        size = playground.sendCardsDeckSize();
        assertEquals(5, size);
    }

}
