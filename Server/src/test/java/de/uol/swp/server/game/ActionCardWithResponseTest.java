package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.MoneyCard;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.card.parser.components.CardAction.response.OptionalActionResponse;
import de.uol.swp.common.game.phase.Phase;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse der ActionCardWithRespone
 *
 * @author Ferit
 * @since Sprint 8
 */
@SuppressWarnings("UnstableApiUsage")
public class ActionCardWithResponseTest {

    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final UserStore userStore = new MainMemoryBasedUserStore();
    static final EventBus bus = new EventBus();
    static final UserManagement userManagement = new UserManagement(userStore);
    static final AuthenticationService authService = new AuthenticationService(bus, userManagement, lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authService);
    static UUID id;
    static UUID gameID;
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    /**
     * Initialisiert alle benötigten Objekte/Parameter
     *
     * @author Ferit
     * @since Sprint 8
     */
    static void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdPlayer);
            ArrayList<Short> theChoosenCards = new ArrayList<>();
            theChoosenCards.add((short) 10);
            theChoosenCards.add((short) 11);
            theChoosenCards.add((short) 13);
            theChoosenCards.add((short) 15);
            theChoosenCards.add((short) 16);
            theChoosenCards.add((short) 19);
            theChoosenCards.add((short) 21);
            theChoosenCards.add((short) 22);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setChosenCards(theChoosenCards);
            bus.post(new StartGameInternalMessage(gameID));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
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
     * Die Karte wird getestet, in dem eine eine Zufällige Zahl fürs Abwerfen generiert wird und dann als ChooseCardResponse an den Server geschickt wird.
     * Die "ausgewählten" Karten beginnen hier im Test von Position 0 von der Hand.
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testKeller() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(2));
            int cardsToSelect = (int) (Math.random() * 4);
            ArrayList<Short> kartenAbwurf = new ArrayList<>();
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 10);
            assertEquals(2, playground.getActualPlayer().getAvailableActions());
            for (int i = 0; i < cardsToSelect; i++) {
                kartenAbwurf.add(playground.getActualPlayer().getPlayerDeck().getHand().get(i).getId());
            }
            ChooseCardResponse theResponse = new ChooseCardResponse(playground.getID(), playground.getActualPlayer().getTheUserInThePlayer(), kartenAbwurf);
            bus.post(theResponse);
            assertEquals(5, playground.getActualPlayer().getPlayerDeck().getHand().size());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Keller, wobei keine Karten von der Hand ausgewählt werden.
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testKellerKeineAuswahl() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(2));
            int deckSize = playground.getActualPlayer().getPlayerDeck().getCardsDeck().size();
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 10);
            ChooseCardResponse theResponse = new ChooseCardResponse(playground.getID(), playground.getActualPlayer().getTheUserInThePlayer(), new ArrayList<>());
            bus.post(theResponse);
            assertEquals(deckSize, playground.getActualPlayer().getPlayerDeck().getCardsDeck().size());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Mine
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testMine() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(4));
            int selectedCardvalue = -1;
            ArrayList<Short> kartenAbwurf = new ArrayList<>();
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 13);
            int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
            for (int i = 0; i < handSize; i++) {
                if (playground.getActualPlayer().getPlayerDeck().getHand().get(i) instanceof MoneyCard) {
                    kartenAbwurf.add(((MoneyCard) playground.getActualPlayer().getPlayerDeck().getHand().get(i)).getValue());
                    selectedCardvalue = playground.getActualPlayer().getPlayerDeck().getHand().get(i).getCosts();
                    break;
                }
            }
            ChooseCardResponse theResponse = new ChooseCardResponse(playground.getID(), playground.getActualPlayer().getTheUserInThePlayer(), kartenAbwurf);
            bus.post(theResponse);
            ChooseCardResponse theResponse2 = new ChooseCardResponse(playground.getID(), playground.getActualPlayer().getTheUserInThePlayer(), playground.getCardsPackField().getCards().getMoneyCards().get(1).getId());
            bus.post(theResponse2);
            if (kartenAbwurf.size() >= 1) {
                assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().get(4).getCosts() >= selectedCardvalue);
            }
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Mine, wobei keine Karte von der Hand ausgewählt wird.
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testMineKeineAuswahl() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(4));
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 13);
            ChooseCardResponse theResponse = new ChooseCardResponse(playground.getID(), playground.getActualPlayer().getTheUserInThePlayer(), new ArrayList<>());
            bus.post(theResponse);
            assertEquals(5, playground.getActualPlayer().getPlayerDeck().getHand().size());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Umbau
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testUmbau() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(6));
            int cardsToSelect = (int) (Math.random() * 4);
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 15);
            ChooseCardResponse theResponse = new ChooseCardResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), playground.getActualPlayer().getPlayerDeck().getHand().get(cardsToSelect).getId());
            bus.post(theResponse);
            ChooseCardResponse theResponse2 = new ChooseCardResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), playground.getCardsPackField().getCards().getActionCards().get(3).getId());
            bus.post(theResponse2);
            assertTrue(playground.getActualPlayer().getPlayerDeck().getDiscardPile().get(0).getCosts() >= playground.getActualPlayer().getPlayerDeck().getHand().get(cardsToSelect).getCosts());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Umbau, wobei keine Karte von der Hand ausgewählt wird.
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testUmbauKeineAuswahl() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(6));
            int discardSize = playground.getActualPlayer().getPlayerDeck().getDiscardPile().size();
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 15);
            ChooseCardResponse theResponse = new ChooseCardResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), new ArrayList<Short>());
            bus.post(theResponse);
            assertEquals(discardSize, playground.getActualPlayer().getPlayerDeck().getDiscardPile().size());
            assertEquals(5, playground.getActualPlayer().getPlayerDeck().getHand().size());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Werkstatt
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testWerkstatt() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(7));
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 16);
            ChooseCardResponse theResponse = new ChooseCardResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), (short) 15);
            bus.post(theResponse);
            assertTrue(playground.getActualPlayer().getPlayerDeck().getDiscardPile().get(0).equals(playground.getCardsPackField().getCards().getCardForId((short) 15)));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Festmahl
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testFestmahl() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(8));
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 19);
            ChooseCardResponse theResponse = new ChooseCardResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), (short) 15);
            bus.post(theResponse);
            assertTrue(playground.getActualPlayer().getPlayerDeck().getDiscardPile().contains(playground.getCardsPackField().getCards().getCardForId((short) 15)));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Kanzler
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testKanzler() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(9));
            playground.getActualPlayer().getPlayerDeck().getDiscardPile().add(playground.getCardsPackField().getCards().getActionCards().get(2));
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 21);
            OptionalActionResponse theResponse = new OptionalActionResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), true);
            bus.post(theResponse);
            assertTrue(playground.getActualPlayer().getPlayerDeck().getCardsDeck().size() == 0);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Kapelle
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void testKapelle() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getActionCards().get(10));
            ArrayList<Short> kartenAbwurf = new ArrayList<>();
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 22);
            for (int i = 0; i < 4; i++) {
                kartenAbwurf.add(playground.getActualPlayer().getPlayerDeck().getHand().get(i).getId());
            }
            ChooseCardResponse theResponse = new ChooseCardResponse(gameID, playground.getActualPlayer().getTheUserInThePlayer(), kartenAbwurf);
            bus.post(theResponse);
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().size() == 1);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}
