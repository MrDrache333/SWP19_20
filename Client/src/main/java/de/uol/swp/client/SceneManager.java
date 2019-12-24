package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import de.uol.swp.client.auth.LoginPresenter;
import de.uol.swp.client.auth.events.ShowLoginViewEvent;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.game.GameManagement;
import de.uol.swp.client.game.event.GameQuitEvent;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.client.register.RegistrationPresenter;
import de.uol.swp.client.register.event.RegistrationCanceledEvent;
import de.uol.swp.client.register.event.RegistrationErrorEvent;
import de.uol.swp.client.register.event.ShowRegistrationViewEvent;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SceneManager {

    static final Logger LOG = LogManager.getLogger(SceneManager.class);
    static final String styleSheet = "css/global.css";

    final private Stage primaryStage;
    final private EventBus eventBus;
    final private UserService userService;
    final private LobbyService lobbyService;
    final private ChatService chatService;
    private final Injector injector;
    private Scene loginScene;
    private String lastTitle;
    private Scene registrationScene;
    private Scene mainScene;
    private Scene gameScene;
    private Scene lastScene = null;
    private Scene currentScene = null;


    private User currentUser;
    private Map<UUID, Scene> lobbyScenes = new HashMap<>();
    private Map<UUID, GameManagement> games = new HashMap<>();
    private Map<UUID, Stage> lobbyStages = new HashMap<>();


    @Inject
    public SceneManager(EventBus eventBus, UserService userService, LobbyService lobbyService, Injector injected, @Assisted Stage primaryStage, ChatService chatService) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
        this.userService = userService;
        this.primaryStage = primaryStage;
        this.injector = injected;
        this.chatService = chatService;
        this.lobbyService = lobbyService;

        initViews();
    }


    @Subscribe
    public void onShowRegistrationViewEvent(ShowRegistrationViewEvent event) {
        showRegistrationScreen();
    }

    @Subscribe
    public void onShowLoginViewEvent(ShowLoginViewEvent event) {

        showLoginScreen();
    }

    @Subscribe
    public void onRegistrationCanceledEvent(RegistrationCanceledEvent event) {
        showScene(lastScene, lastTitle);
    }

    @Subscribe
    public void onRegistrationErrorEvent(RegistrationErrorEvent event) {
        showError(event.getMessage());
    }

    private void initViews() {
        initLoginView();
        initMainView();
        initRegistrationView();
    }

    private Parent initPresenter(String fxmlFile) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(fxmlFile);
            LOG.debug("Loading " + url);
            loader.setLocation(url);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load View!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private void initMainView() {
        if (mainScene == null) {
            Parent rootPane = initPresenter(MainMenuPresenter.fxml);
            mainScene = new Scene(rootPane, 1280, 750);
            mainScene.getStylesheets().add(styleSheet);
            mainScene.getStylesheets().add(MainMenuPresenter.css);
        }
    }

    private void initLoginView() {
        if (loginScene == null) {
            Parent rootPane = initPresenter(LoginPresenter.fxml);
            loginScene = new Scene(rootPane, 1280, 750);
            loginScene.getStylesheets().add(styleSheet);
            loginScene.getStylesheets().add(LoginPresenter.css);
        }
    }

    private void initRegistrationView() {
        if (registrationScene == null) {
            Parent rootPane = initPresenter(RegistrationPresenter.fxml);
            registrationScene = new Scene(rootPane, 1280, 750);
            registrationScene.getStylesheets().add(styleSheet);
            registrationScene.getStylesheets().add(RegistrationPresenter.css);
        }
    }


    @Subscribe
    public void onGameQuitEvent(GameQuitEvent event) {
        showScene(mainScene, "test");
    }


    public void showError(String message, String e) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR, message + e);
            a.showAndWait();
        });
    }

    public void showServerError(String e) {
        showError("Server returned an error:\n", e);
    }

    public void showError(String e) {
        showError("Error:\n", e);
    }

    private void showScene(final Scene scene, final String title) {
        this.lastScene = currentScene;
        this.lastTitle = primaryStage.getTitle();
        this.currentScene = scene;
        Platform.runLater(() -> {
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            new SoundMediaPlayer(SoundMediaPlayer.Sound.Window_Opened, SoundMediaPlayer.Type.Sound).play();
        });
    }

    public void showLoginErrorScreen() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error logging in to server");
            alert.showAndWait();
            showLoginScreen();
        });
    }

    public void showMainScreen(User currentUser) {
        showScene(mainScene, "Welcome " + currentUser.getUsername());
    }


    public void showLoginScreen() {
        showScene(loginScene, "Login");
    }

    public void showRegistrationScreen() {
        showScene(registrationScene, "Registration");
    }

    /**
     * Es wird eine neue Stage mit der lobbyScene angezeigt und mit dem Attribut geöffnet.
     *
     * @param title   der Übergebene Titel aus dem MainMenuPresenter
     * @param lobbyID die übergebene LobbyID aus der empfangenen Message in der ClientApp
     * @author Paula, Haschem, Ferit, Anna
     * @version 0.2
     * @since Sprint3
     */
    public void showLobbyScreen(User currentUser, String title, UUID lobbyID) {
        Platform.runLater(() -> {
            //LobbyPresenter neue Instanz mit (name, id) wird erstellt
            GameManagement gameManagement = new GameManagement(eventBus, lobbyID, title, currentUser, chatService, lobbyService, userService, injector);

            eventBus.register(gameManagement);

            //LobbyPresenter und lobbyStage in die jeweilige Map packen, mit lobbyID als Schlüssel
            games.put(lobbyID, gameManagement);
            gameManagement.showLobbyView();
        });

    }

    public boolean hasFocus() {
        return primaryStage.isFocused();
    }

    /**
     * Gibt das zur übergebenen lobbyID gehörige GameManagement zurück
     *
     * @param lobbyID
     * @return GameManagement
     * @author Julia, Paula
     * @since Sprint3
     */
    public GameManagement getGameManagement(UUID lobbyID) {
        return games.get(lobbyID);
    }

    /**
     * Schließt alle GameManagement Stages
     *
     * @author Julia, Paula
     * @since Sprint3
     */
    public void closeAllStages() {
        Platform.runLater(() -> games.values().forEach(GameManagement::close));
    }

}
