package de.uol.swp.client.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.request.*;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Der Game Service Test
 *
 * @author Anna
 * @since Sprint 10
 */
class GameServiceTest {
    /**
     * Standard Benutzer, Lobby- und Karten-ID zum Testen
     *
     * @author Anna
     * @since Sprint 10
     */
    User defaultUser = new UserDTO("Marco", "test", "marco@test.de");
    UUID lobbyID = UUID.randomUUID();
    Short cardID = (short) 10;

    /**
     * Der zu verwendete EventBus
     *
     * @author Anna
     * @since Sprint 10
     */
    EventBus bus = new EventBus();
    CountDownLatch lock = new CountDownLatch(1);
    Object event;

    /**
     * Methode zum Behandeln von auf dem Bus aufgetretene Dead-Events.
     *
     * @param e Das aufgetretene Dead-Event
     * @author Anna
     * @since Sprint 10
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Eventbus initialisieren
     *
     * @author Anna
     * @since Sprint 10
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Klasse vom EventBus deregistrieren
     *
     * @author Anna
     * @since Sprint 10
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Hilfsmethode zum Schicken einer Anfrage zum Überspringen der aktuellen Phase.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void skipPhase() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.skipPhase(defaultUser, lobbyID);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Anfrage zum Ausspielen einer Karte.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void playCard() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.playCard(lobbyID, defaultUser, cardID);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Anfrage zum Kaufen einer Karte.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void buyCard() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.buyCard(new BuyCardRequest(lobbyID, defaultUser, cardID));
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Antwort auf eine optionale Aktion.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void optionalActionTrue() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.optionalAction(defaultUser, lobbyID, true, 13);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Antwort auf eine optionale Aktion.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void optionalActionFalse() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.optionalAction(defaultUser, lobbyID, false, 13);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken der ausgewählten Karten.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void chooseCardResponse() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.chooseCardResponse(lobbyID, defaultUser, new ArrayList<>(Arrays.asList(cardID)), 13);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Anfrage zum Pausieren des Spiels.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void requestPoopBreak() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.requestPoopBreak(defaultUser, lobbyID);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken des Votes des Users.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void answerPoopBreak() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.answerPoopBreak(defaultUser, lobbyID, true);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Hilfsmethode zum Schicken einer Anfrage zum Abbrechen der Pause.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    private void cancelPoopBreak() throws InterruptedException {
        GameService gameService = new GameService(bus);
        gameService.cancelPoopBreak(defaultUser, lobbyID);
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Versuch die Anfrage zum Überspringen der aktuellen Phase zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void skipPhaseTest() throws InterruptedException {
        skipPhase();
        assertTrue(event instanceof SkipPhaseRequest);
        SkipPhaseRequest req = (SkipPhaseRequest) event;
        assertEquals(defaultUser, req.getUser());
        assertEquals(lobbyID, req.getGameID());
    }

    /**
     * Versuch die Anfrage zum Ausspielen einer Karte zu schicken.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void playCardTest() throws InterruptedException {
        playCard();
        assertTrue(event instanceof PlayCardRequest);
        PlayCardRequest req = (PlayCardRequest) event;
        assertEquals(lobbyID, req.getGameID());
        assertEquals(cardID, req.getHandCardID());
        assertEquals(defaultUser, req.getCurrentUser());
    }

    /**
     * Versuch die Anfrage zum Kauf einer Karte zu schicken.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void buyCardTest() throws InterruptedException {
        buyCard();
        assertTrue(event instanceof BuyCardRequest);
        BuyCardRequest req = (BuyCardRequest) event;
        assertEquals(lobbyID, req.getLobbyID());
        assertEquals(cardID, req.getCardID());
        assertEquals(defaultUser, req.getCurrentUser());
    }

    /**
     * Versuch die Antwort auf eine optionale Aktion zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void optionalActionTest() throws InterruptedException {
        optionalActionTrue();
        assertTrue(event instanceof OptionalActionResponse);
        OptionalActionResponse res = (OptionalActionResponse) event;
        assertEquals(lobbyID, res.getGameID());
        assertEquals(13, res.getActionExecutionID());
        assertEquals(defaultUser, res.getPlayer());
        assertTrue(res.isExecute());
        optionalActionFalse();
        assertTrue(event instanceof OptionalActionResponse);
        OptionalActionResponse res2 = (OptionalActionResponse) event;
        assertEquals(lobbyID, res2.getGameID());
        assertEquals(13, res2.getActionExecutionID());
        assertEquals(defaultUser, res2.getPlayer());
        assertFalse(res2.isExecute());
    }

    /**
     * Versuch die ausgewählten Karten zu schicken.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void ChooseCardResponseTest() throws InterruptedException {
        chooseCardResponse();
        assertTrue(event instanceof ChooseCardResponse);
        ChooseCardResponse res = (ChooseCardResponse) event;
        assertEquals(lobbyID, res.getGameID());
        assertEquals(1, res.getCards().size());
        assertEquals(new ArrayList<>(Arrays.asList(cardID)), res.getCards());
        assertEquals(defaultUser, res.getPlayer());
        assertEquals(13, res.getActionExecutionID());
    }

    /**
     * Versuch eine Anfrage für eine Pause zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void RequestPoopBreakTest() throws InterruptedException {
        requestPoopBreak();
        assertTrue(event instanceof PoopBreakRequest);
        PoopBreakRequest req = (PoopBreakRequest) event;
        assertEquals(lobbyID, req.getGameID());
        assertEquals(defaultUser, req.getPoopInitiator());
    }

    /**
     * Versuch eine Antwort auf die Anfrage zur Pause zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void AnswerPoopBreakTest() throws InterruptedException {
        answerPoopBreak();
        assertTrue(event instanceof PoopBreakRequest);
        PoopBreakRequest req = (PoopBreakRequest) event;
        assertEquals(lobbyID, req.getGameID());
        assertEquals(defaultUser, req.getUser());
        assertTrue(req.getPoopDecision());
    }

    /**
     * Versuch eine Anfrage zum Unterbrechen der Pause zu senden.
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Anna
     * @since Sprint 10
     */
    @Test
    void CancelPoopBreakTest() throws InterruptedException {
        cancelPoopBreak();
        assertTrue(event instanceof CancelPoopBreakRequest);
        CancelPoopBreakRequest req = (CancelPoopBreakRequest) event;
        assertEquals(lobbyID, req.getGameID());
        assertEquals(defaultUser, req.getUser());
    }
}
