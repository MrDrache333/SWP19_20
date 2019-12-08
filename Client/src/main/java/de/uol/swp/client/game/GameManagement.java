package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.LobbyPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.UUID;

/**
 * The type Game management.
 */
public class GameManagement {

    /**
     * The Log.
     */
    static final Logger LOG = LogManager.getLogger(GameManagement.class);
    /**
     * The Style sheet.
     */
    static final String styleSheet = "css/swp.css";
    private LobbyPresenter lobbyPresenter;
    private GameViewPresenter gameViewPresenter;
    private ChatViewPresenter chatViewPresenter;
    private UUID ID;    //Die Lobby, Chat and GameID
    private User loggedInUser;  //Der aktuell angemeldete Benutzer
    private String LobbyName;

    private Scene gameScene;
    private Scene lobbyScene;

    private Injector injector;
    private EventBus eventBus;

    private Stage primaryStage;


    /**
     * Instantiates a new Game management.
     *
     * @param eventBus     the event bus
     * @param id           the id
     * @param lobbyName    the lobby name
     * @param loggedInUser the logged in user
     * @param chatService  the chat service
     * @param lobbyService the lobby service
     * @param userService  the user service
     * @param injector     the injector
     */
    public GameManagement(EventBus eventBus, UUID id, String lobbyName, User loggedInUser, ChatService chatService, LobbyService lobbyService, UserService userService, Injector injector) {
        this.ID = id;
        this.loggedInUser = loggedInUser;
        this.injector = injector;
        this.primaryStage = new Stage();
        this.LobbyName = lobbyName;
        this.eventBus = eventBus;

        //Nötige Controller initialisieren
        this.chatViewPresenter = new ChatViewPresenter(lobbyName, id, loggedInUser, ChatViewPresenter.THEME.Light, chatService, injector);
        this.gameViewPresenter = new GameViewPresenter(loggedInUser, id, chatService, chatViewPresenter, lobbyService, userService, injector, this);
        this.lobbyPresenter = new LobbyPresenter(loggedInUser, lobbyName, id, chatService, chatViewPresenter, lobbyService, userService, injector, this);


        //Controller auf dem EventBus registrieren
        eventBus.register(chatViewPresenter);
        eventBus.register(lobbyPresenter);
        eventBus.register(gameViewPresenter);
    }

    @Subscribe
    private void userLeft(UserLeftLobbyMessage msg) {
        //Wenn es der aktuelle Benutzer in dieser Lobby ist, dann schließe das Fenster
        if (msg.getLobbyID().equals(ID) && msg.getUser().getUsername().equals(loggedInUser.getUsername())) {
            close();
        }
    }

    @Subscribe
    private void userLoggedOut(UserLoggedOutMessage msg) {
        //Wenn es der aktuelle Benutzer in dieser Lobby ist, dann schließe das Fenster
        if (msg.getUsername().equals(loggedInUser.getUsername())) {
            close();
        }
    }


    private void initGameView() {
        if (gameScene == null) {
            Parent rootPane = initPresenter(gameViewPresenter, GameViewPresenter.fxml);
            gameScene = new Scene(rootPane, 1280, 750);
            gameScene.getStylesheets().add(styleSheet);
        }
    }

    // LobbyView wird initalisiert und deklariert.
    //neue Szene für die neue Lobby wird erstellt und gespeichert
    private void initLobbyView() {
        if (lobbyScene == null) {
            Parent rootPane = initPresenter(lobbyPresenter, LobbyPresenter.fxml);
            lobbyScene = new Scene(rootPane, 900, 750);
            lobbyScene.getStylesheets().add(styleSheet);
        }
    }

    //initPresenter für Lobbies, hier wird dann der jeweilige lobbyPresenter als Controller gesetzt
    private Parent initPresenter(AbstractPresenter presenter, String fxml) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(fxml);
            LOG.debug("Loading " + url);
            loader.setLocation(url);
            //Controller wird gesetzt (Instanz der LobbyPresenter Klasse)
            loader.setController(presenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load View!" + e.getMessage(), e);
        }

        return rootPane;
    }

    private void showScene(final Scene scene, final String title) {
        Platform.runLater(() -> {
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        });
    }

    /**
     * Methode zum Anzeigen der Lobby
     */
    public void showLobbyView() {
        initLobbyView();
        showScene(lobbyScene, LobbyName);
    }

    /**
     * Methode zum Anzeigen des Spiels
     */
    public void showGameView() {
        initGameView();
        showScene(gameScene, LobbyName);
    }

    /**
     * Closes the Current Stage.
     */
    public void close() {
        Platform.runLater(() -> primaryStage.close());
    }
}
