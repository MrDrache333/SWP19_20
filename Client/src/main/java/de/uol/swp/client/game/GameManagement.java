package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.LobbyPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
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

public class GameManagement {

    static final Logger LOG = LogManager.getLogger(GameManagement.class);
    static final String styleSheet = "css/swp.css";

    private LobbyPresenter lobbyPresenter;
    private GameViewPresenter gameViewPresenter;
    private ChatViewPresenter chatViewPresenter;
    private UUID id;
    private User loggedInUser;
    private String lobbyName;

    private Scene gameScene;
    private Scene lobbyScene;

    private Injector injector;
    private EventBus eventBus;

    private Stage primaryStage;


    /**
     * Instanziiert ein neues GameManagement. Dafür werden nötige Controller initialisiert und auf dem Eventbus registriert
     *
     * @param eventBus     der Eventbus
     * @param id           die ID
     * @param lobbyName    der Lobbyname
     * @param loggedInUser der aktuelle Benutzer
     * @param chatService  der ChatService
     * @param lobbyService der LobbyService
     * @param userService  der UserService
     * @param injector     der Injector
     * @author Keno O.
     * @since Sprint3
     */
    public GameManagement(EventBus eventBus, UUID id, String lobbyName, User loggedInUser, ChatService chatService, LobbyService lobbyService, UserService userService, Injector injector) {
        this.id = id;
        this.loggedInUser = loggedInUser;
        this.injector = injector;
        this.primaryStage = new Stage();
        this.lobbyName = lobbyName;
        this.eventBus = eventBus;

        this.chatViewPresenter = new ChatViewPresenter(lobbyName, id, loggedInUser, ChatViewPresenter.THEME.Light, chatService, injector, this);
        this.gameViewPresenter = new GameViewPresenter(loggedInUser, id, chatService, chatViewPresenter, lobbyService, userService, injector, this);
        this.lobbyPresenter = new LobbyPresenter(loggedInUser, lobbyName, id, chatService, chatViewPresenter, lobbyService, userService, injector, this, eventBus);

        eventBus.register(chatViewPresenter);
        eventBus.register(lobbyPresenter);
        eventBus.register(gameViewPresenter);
    }

    /**
     * Schließt das Fenster, wenn der aktuelle Benutzer diese Lobby verlassen hat
     *
     * @param msg die UserLeftLobbyMessage
     * @author Keno O.
     * @since Sprint3
     */
    @Subscribe
    private void userLeft(UserLeftLobbyMessage msg) {
        if (msg.getLobbyID().equals(id) && msg.getUser().getUsername().equals(loggedInUser.getUsername())) {
            close();
        }
    }

    /**
     * Schließt das Fenster, wenn sich der aktuelle Benutzer ausgeloggt hat
     *
     * @param msg die UserLoggedOutMessage
     * @author Keno O.
     * @since Sprint3
     */
    @Subscribe
    private void userLoggedOut(UserLoggedOutMessage msg) {
        if (msg.getUsername().equals(loggedInUser.getUsername())) {
            close();
        }
    }

    /**
     * Aktualisiert den loggedInUser, wenn dieser seine Daten geändert hat
     *
     * @param message die UpdatedUserMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
    }

    /**
     * Überprüft ob sich die aktuelle Stage im Vordergrund befindet
     *
     * @return
     * @author Keno O.
     * @since Sprint3
     */
    public boolean hasFocus() {
        return primaryStage.isFocused();
    }

    /**
     * Methode zum Anzeigen der LobbyView
     *
     * @author Keno O.
     * @since Sprint3
     */
    public void showLobbyView() {
        initLobbyView();
        showScene(lobbyScene, lobbyName);
    }

    /**
     * Methode zum Anzeigen der GameView
     *
     * @author Keno O.
     * @since Sprint3
     */
    public void showGameView() {
        initGameView();
        showScene(gameScene, lobbyName);
    }

    /**
     * Methode zum Schließen der aktuellen Stage
     *
     * @author Keno O.
     * @since Sprint3
     */
    public void close() {
        Platform.runLater(() -> primaryStage.close());
    }

    /**
     * Initialisieren der GameView
     *
     * @author Keno O.
     * @since Sprint3
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
     * @author Keno O.
     * @since Sprint3
     */
    private void initLobbyView() {
        if (lobbyScene == null) {
            Parent rootPane = initPresenter(lobbyPresenter, LobbyPresenter.fxml);
            lobbyScene = new Scene(rootPane, 900, 750);
            lobbyScene.getStylesheets().add(styleSheet);
        }
    }

    /**
     * initPresenter für Lobbies, hier wird dann der jeweilige lobbyPresenter als Controller gesetzt
     *
     * @param presenter der Presenter
     * @param fxml die zum Presenter gehörige fxml
     * @return
     * @author Keno O.
     * @since Sprint3
     */
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

    /**
     * Setzt die Szene der primaryStage auf die übergebene Szene und aktualisiert den Titel
     *
     * @param scene die Szene
     * @param title der Titel der Stage
     * @author Julia, Keno O.
     * @since Sprint3
     */
    private void showScene(final Scene scene, final String title) {
        Platform.runLater(() -> {
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            //User wird aus der Lobby ausgeloggt, wenn er das Lobbyfenster schließt
            primaryStage.setOnCloseRequest(windowEvent -> {
                if (primaryStage.getScene().equals(lobbyScene)) {
                    lobbyPresenter.getLobbyService().leaveLobby(lobbyName, new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()), id);
                }
            });
            primaryStage.show();
            new SoundMediaPlayer(SoundMediaPlayer.Sound.Window_Opened, SoundMediaPlayer.Type.Sound).play();
        });
    }
}
