package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.MediaPlayer;
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
    private String lobbyName;

    private Scene gameScene;
    private Scene lobbyScene;

    private Injector injector;
    private EventBus eventBus;

    private Stage primaryStage;


    /**
     * Instanziiere einen neuen GameManagement. Dafür werden nötige Controller initialisiert und Controller auf dem Eventbus registriert
     *
     * @param eventBus     the event bus
     * @param id           the id
     * @param lobbyName    the lobby name
     * @param loggedInUser the logged in user
     * @param chatService  the chat service
     * @param lobbyService the lobby service
     * @param userService  the user service
     * @param injector     the injector
     * @author Keno
     *
     */
    public GameManagement(EventBus eventBus, UUID id, String lobbyName, User loggedInUser, ChatService chatService, LobbyService lobbyService, UserService userService, Injector injector) {
        this.ID = id;
        this.loggedInUser = loggedInUser;
        this.injector = injector;
        this.primaryStage = new Stage();
        this.lobbyName = lobbyName;
        this.eventBus = eventBus;

        this.chatViewPresenter = new ChatViewPresenter(lobbyName, id, loggedInUser, ChatViewPresenter.THEME.Light, chatService, injector);
        this.gameViewPresenter = new GameViewPresenter(loggedInUser, id, chatService, chatViewPresenter, lobbyService, userService, injector, this);
        this.lobbyPresenter = new LobbyPresenter(loggedInUser, lobbyName, id, chatService, chatViewPresenter, lobbyService, userService, injector, this);

        eventBus.register(chatViewPresenter);
        eventBus.register(lobbyPresenter);
        eventBus.register(gameViewPresenter);
    }

    /**
     * Schließe das Fenster, wenn es der aktuelle Benutzer in dieser Lobby ist, der die Lobby verlässt.
     *
     * @param msg
     * @author Keno
     */
    @Subscribe
    private void userLeft(UserLeftLobbyMessage msg) {
        if (msg.getLobbyID().equals(ID) && msg.getUser().getUsername().equals(loggedInUser.getUsername())) {
            close();
        }
    }

    /**
     * Wenn es der aktuelle Benutzer in dieser Lobby ist, dann schließe das Fenster
     *
     * @param msg
     * @author Keno
     */
    @Subscribe
    private void userLoggedOut(UserLoggedOutMessage msg) {
        if (msg.getUsername().equals(loggedInUser.getUsername())) {
            close();
        }
    }

    /**
     * Initialisieren der GameView
     *
     * @param
     * @author Keno
     */
    private void initGameView() {
        if (gameScene == null) {
            Parent rootPane = initPresenter(gameViewPresenter, GameViewPresenter.fxml);
            gameScene = new Scene(rootPane, 1280, 750);
            gameScene.getStylesheets().add(styleSheet);
        }
    }

    /**
     * LobbyView wird initalisiert und deklariert.
     * Neue Szene für die neue Lobby wird erstellt und gespeichert
     *
     * @param
     * @Keno
     */
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
            //User wird aus der Lobby ausgeloggt, wenn er das Lobbyfenster schließt
            primaryStage.setOnCloseRequest(windowEvent -> {
                if(primaryStage.getScene().equals(lobbyScene)) {
                    lobbyPresenter.getLobbyService().leaveLobby(lobbyName, loggedInUser, ID);
                }
            });
            primaryStage.show();
            new MediaPlayer(MediaPlayer.Sound.Window_Opened, MediaPlayer.Type.Sound).play();
        });
    }

    /**
     * Methode zum Anzeigen der Lobby
     */
    public void showLobbyView() {
        initLobbyView();
        showScene(lobbyScene, lobbyName);
    }

    /**
     * Methode zum Anzeigen des Spiels
     */
    public void showGameView() {
        initGameView();
        showScene(gameScene, lobbyName);
    }

    /**
     * Methode zum Schließen der aktuellen Stage
     */
    public void close() {
        Platform.runLater(() -> primaryStage.close());
    }
}
