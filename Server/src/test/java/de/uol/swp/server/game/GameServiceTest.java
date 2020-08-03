package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.SkipPhaseRequest;
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

public class GameServiceTest {
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
    private final CountDownLatch lock = new CountDownLatch(1);
    private static final ArrayList<Short> chosenCards = new ArrayList<>();

    static UUID lobbyId;


    /**
     * Initialisiert die benötigten Objekte/Parameter
     *
     * @author Julia
     * @since Sprint 5
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

    @Test
    public void startGameTest()
    {
        StartGameInternalMessage msg = new StartGameInternalMessage(lobbyId);
        gameService.startGame(msg);
        assertTrue(gameManagement.getGame(msg.getLobbyID()).isPresent());
    }

    @Test
    public void startGameFailTest()
    {
        UUID lobbyId = UUID.randomUUID();
        StartGameInternalMessage msg = new StartGameInternalMessage(lobbyId);
        gameService.startGame(msg);
        assertFalse(gameManagement.getGame(msg.getLobbyID()).isPresent());
    }

    @Test
    public void onSkipPhaseRequestTest()
    {
        // Prepare Lobby Creation
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        SkipPhaseRequest req = new SkipPhaseRequest(playground.getActualPlayer().getTheUserInThePlayer(), lobbyId);
        gameService.onSkipPhaseRequest(req);
        assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
    }

    @Test
    public void onSkipPhaseRequestClearPhaseTest()
    {
        // Prepare Lobby Creation
        gameService.startGame(new StartGameInternalMessage(lobbyId));
        Playground playground = gameManagement.getGame(lobbyId).get().getPlayground();
        playground.setActualPhase(Phase.Type.ClearPhase);
        SkipPhaseRequest req = new SkipPhaseRequest(playground.getActualPlayer().getTheUserInThePlayer(), lobbyId);
        gameService.onSkipPhaseRequest(req);
        assertEquals(Phase.Type.ClearPhase, playground.getActualPhase());
    }

    @Test
    public void onSkipPhaseRequestGamNotPresenteTest()
    {
        // Prepare Lobby Creation
        UUID lobbyId = UUID.randomUUID();
        SkipPhaseRequest req = new SkipPhaseRequest(defaultOwner, lobbyId);
        gameService.onSkipPhaseRequest(req);
        Optional<Game> game = gameManagement.getGame(req.getGameID());
        assertFalse(game.isPresent());

    }
}
