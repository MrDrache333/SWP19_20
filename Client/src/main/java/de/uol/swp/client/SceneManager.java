package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import de.uol.swp.client.auth.LoginPresenter;
import de.uol.swp.client.auth.events.ShowLoginViewEvent;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.lobby.LobbyPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.client.register.RegistrationPresenter;
import de.uol.swp.client.register.event.RegistrationCanceledEvent;
import de.uol.swp.client.register.event.RegistrationErrorEvent;
import de.uol.swp.client.register.event.ShowRegistrationViewEvent;
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
    static final String styleSheet = "css/swp.css";

    final private Stage primaryStage;
    final private EventBus eventBus;
    final private UserService userService;
    final private ChatService chatService;
    final private LobbyService lobbyService;
    private Scene loginScene;
    private String lastTitle;
    private Scene registrationScene;
    private Scene mainScene;
    private Scene lastScene = null;
    private Scene currentScene = null;

    private User currentUser;

    private Injector injector;

    private Map<UUID, Scene> lobbyScenes = new HashMap<>();
    private Map<UUID, LobbyPresenter> lobbies = new HashMap<>();
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

    private void initViews() {
        initLoginView();
        initMainView();
        initRegistrationView();
        //initLobbyView();
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
//        AbstractPresenter presenter = loader.getController();
//        presenter.setEventBus(eventBus);
//        presenter.setUserService(userService);
        return rootPane;
    }

    //initPresenter für Lobbies, hier wird dann der jeweilige lobbyPresenter als Controller gesetzt
    private Parent initPresenter(LobbyPresenter lobbyPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(LobbyPresenter.fxml);
            LOG.debug("Loading " + url);
            loader.setLocation(url);
            //Controller wird gesetzt (Instanz der LobbyPresenter Klasse)
            loader.setController(lobbyPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load View!" + e.getMessage(), e);
        }
//        AbstractPresenter presenter = loader.getController();
//        presenter.setEventBus(eventBus);
//        presenter.setUserService(userService);
        return rootPane;
    }

    private void initMainView() {
        if (mainScene == null) {
            Parent rootPane = initPresenter(MainMenuPresenter.fxml);
            mainScene = new Scene(rootPane, 1280, 750);
            mainScene.getStylesheets().add(styleSheet);
        }
    }

    private void initLoginView() {
        if (loginScene == null) {
            Parent rootPane = initPresenter(LoginPresenter.fxml);
            loginScene = new Scene(rootPane, 1280, 750);
            loginScene.getStylesheets().add(styleSheet);
        }
    }

    private void initRegistrationView() {
        if (registrationScene == null) {
            Parent rootPane = initPresenter(RegistrationPresenter.fxml);
            registrationScene = new Scene(rootPane, 1280, 750);
            registrationScene.getStylesheets().add(styleSheet);
        }
    }

    // LobbyView wird initalisiert und deklariert.
    //neue Szene für die neue Lobby wird erstellt und gespeichert
    private void initLobbyView(LobbyPresenter lobbyPresenter) {
        //presenter als controller setzen
        Parent rootPane = initPresenter(lobbyPresenter);
        Scene newLobbyScene = new Scene(rootPane, 900, 750);
        newLobbyScene.getStylesheets().add(styleSheet);
        //scene in Map packen
        lobbyScenes.put(lobbyPresenter.getLobbyID(), newLobbyScene);
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
        this.currentUser = currentUser;
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

    //TODO: LobbyScreen bzw Stage schließen, wenn Hauptmenü geschlossen wird
    public void showLobbyScreen(User currentUser, String title, UUID lobbyID) {
        Platform.runLater(() -> {
            //LobbyPresenter neue Instanz mit (name, id) wird erstellt
            LobbyPresenter lobbyPresenter = new LobbyPresenter(currentUser, title, lobbyID, chatService, lobbyService, userService);
            eventBus.register(lobbyPresenter);
            //initLobbyView mit gerade erstelltem Presenter als Controller aufrufen -> Scene wird erstellt
            initLobbyView(lobbyPresenter);
            //neue Stage wird erstellt
            Stage newLobbyStage = new Stage();
            newLobbyStage.setTitle(title);
            //passende lobbyScene setzen
            newLobbyStage.setScene(lobbyScenes.get(lobbyID));
            newLobbyStage.setX(primaryStage.getX() + 200);
            newLobbyStage.setY(primaryStage.getY() + 100);
            newLobbyStage.show();
            //LobbyPresenter und lobbyStage in die jeweilige Map packen, mit lobbyID als Schlüssel
            lobbies.put(lobbyID, lobbyPresenter);
            lobbyStages.put(lobbyID, newLobbyStage);
        });

    }

}
