package de.uol.swp.server.game;


import com.google.common.eventbus.EventBus;
import de.uol.swp.common.game.card.ActionCard;
import de.uol.swp.common.game.card.parser.components.CardAction.CardAction;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.*;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest<œTest> {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    ArrayList<CardAction> actions = new ArrayList<CardAction>();
    ActionCard card = new ActionCard("Provinz", (short) 2, (short) 300, actions, ActionCard.ActionType.Attack);
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final UserStore userStore = new MainMemoryBasedUserStore();
    static final EventBus bus = new EventBus();
    static final UserManagement userManagement = new UserManagement(userStore);
    static final AuthenticationService authService = new AuthenticationService(bus, userManagement, lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authService);
    private final CountDownLatch lock = new CountDownLatch(1);
    private static final ArrayList<Short> chosenCards = new ArrayList<>();
    static UUID lobbyId;


    /**
     * Initialisiert die benötigten Objekte/Parameter
     *
     * @author Paula
     * @since Sprint10
     */
    @BeforeAll
    static void init() {
        try {
            lobbyId = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(lobbyId.toString());
            lobbyManagement.getLobby(lobbyId).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
            lobbyManagement.getLobby(lobbyId).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdPlayer);
            lobbyManagement.getLobby(lobbyId).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setChosenCards(chosenCards);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet, ob das Spiel gestartet wird
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void startGameTest() {
        StartGameInternalMessage msg = new StartGameInternalMessage(lobbyId);
        gameService.startGame(msg);
        assertTrue(gameManagement.getGame(msg.getLobbyID()).isPresent());
    }

    /**
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void startGameFailTest() {
        UUID lobbyId = UUID.randomUUID();
        StartGameInternalMessage msg = new StartGameInternalMessage(lobbyId);
        gameService.startGame(msg);
        assertFalse(gameManagement.getGame(msg.getLobbyID()).isPresent());
    }

    /**
     * Testet den onSkipPhaseRequest
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onSkipPhaseRequestTest() {
        // Prepare Lobby Creation
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        SkipPhaseRequest req = new SkipPhaseRequest(playground.getActualPlayer().getTheUserInThePlayer(), lobbyId);
        gameService.onSkipPhaseRequest(req);
        assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
    }

    /**
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onSkipPhaseRequestClearPhaseTest() {
        // Prepare Lobby Creation
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        playground.setActualPhase(Phase.Type.ClearPhase);
        SkipPhaseRequest req = new SkipPhaseRequest(playground.getActualPlayer().getTheUserInThePlayer(), lobbyId);
        gameService.onSkipPhaseRequest(req);
        assertEquals(Phase.Type.ClearPhase, playground.getActualPhase());
    }

    /**
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onSkipPhaseRequestGamNotPresentTest() {
        // Prepare Lobby Creation
        UUID lobbyId = UUID.randomUUID();
        SkipPhaseRequest req = new SkipPhaseRequest(defaultOwner, lobbyId);
        gameService.onSkipPhaseRequest(req);
        Optional<Game> game = gameManagement.getGame(req.getGameID());
        assertFalse(game.isPresent());
    }

    /**
     * Testet, ob ein fertiges Spiel auch wieder gelöscht wird
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void dropFinishedGame() {
        UUID lobbyId = UUID.randomUUID();
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        gameService.dropFinishedGame(lobbyId);
        assertFalse(gameService.getGameManagement().getGame(lobbyId).isPresent());
    }

    /**
     * Testet das Aufgeben
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void userGivesUp() {
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        assertTrue(gameService.getGameManagement().getGame(lobbyId).get().getPlayground().getPlayers().size() == 3);
        GameGiveUpRequest req = new GameGiveUpRequest((UserDTO) thirdPlayer, lobbyId);
        gameService.userGivesUp(req);
        assertTrue(gameService.getGameManagement().getGame(lobbyId).get().getPlayground().getPlayers().size() == 2);
    }

    /**
     * Testet, ob der User der ein Spiel aufgibt aus der Lobby entfernt wird
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void userGavesUpLeavesLobby() {
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        LobbyLeaveUserRequest req = new LobbyLeaveUserRequest(lobbyId, (UserDTO) secondPlayer);
        gameService.userGavesUpLeavesLobby(lobbyId, (UserDTO) secondPlayer);
        assertFalse(gameService.getGameManagement().getGame(lobbyId).get().getPlayground().getPlayers().contains(secondPlayer));
    }

    /**
     * Testet den BuyCardRequest
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onBuyCardRequest() {
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        BuyCardRequest req = new BuyCardRequest(lobbyId, playground.getActualPlayer().getTheUserInThePlayer(), card.getId());
        gameService.onBuyCardRequest(req);
        assertFalse(gameService.getGameManagement().getGame(lobbyId).get().getPlayground().getActualPlayer().getPlayerDeck().getCardsDeck().contains(card));
    }


    /**
     * Testet den PlayCardRequest
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onPlayCardRequestFailed() {
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        PlayCardRequest req = new PlayCardRequest(lobbyId, defaultOwner, (short) 22);
        playground.setActualPhase(Phase.Type.ActionPhase);
        gameService.onPlayCardRequest(req);
        assertFalse(playground.getActualPlayer().getPlayerDeck().getCardsDeck().contains(card));
    }

    /**
     * Testet den PoopBreakRequest
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onPoopBreakRequest() {
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        PoopBreakRequest req = new PoopBreakRequest(thirdPlayer, lobbyId);
        gameService.onPoopBreakRequest(req);
    }

    /**
     * Testet den CancelPoopBreakRequest
     *
     * @author Paula
     * @since Sprint10
     */
    @Test
    public void onCancelPoopBreakRequest() {
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        CancelPoopBreakRequest req = new CancelPoopBreakRequest(thirdPlayer, lobbyId);
        gameService.onCancelPoopBreakRequest(req);
    }
}