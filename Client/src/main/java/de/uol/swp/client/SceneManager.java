package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import de.uol.swp.client.auth.LoginPresenter;
import de.uol.swp.client.auth.events.ShowLoginViewEvent;
import de.uol.swp.client.game.GameViewPresenter;
import de.uol.swp.client.lobby.*;
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

public class SceneManager {

    static final Logger LOG = LogManager.getLogger(SceneManager.class);
    static final String styleSheet = "css/swp.css";

    final private Stage primaryStage;
    final private Stage lobbyStage;
    final private EventBus eventBus;
    final private UserService userService;
    private Scene loginScene;
    private String lastTitle;
    private Scene registrationScene;
    private Scene mainScene;
    private Scene gameScene;
    private Scene lastScene = null;
    private Scene currentScene = null;
    private Scene lobbyScene;

    private User currentUser;

    private Injector injector;


    @Inject
    public SceneManager(EventBus eventBus, UserService userService, Injector injected, @Assisted Stage primaryStage, Stage lobbyStage) {
        this.eventBus = eventBus;
        this.lobbyStage = lobbyStage;
        this.eventBus.register(this);
        this.userService = userService;
        this.primaryStage = primaryStage;
        this.injector = injected;

        initViews();
    }

    private void initViews() {
        initLoginView();
        initMainView();
        initRegistrationView();
        initLobbyView();
        initGameView();
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

    private void initMainView() {
        if (mainScene == null) {
            Parent rootPane = initPresenter(MainMenuPresenter.fxml);
            mainScene = new Scene(rootPane, 1280, 750);
            mainScene.getStylesheets().add(styleSheet);
        }
    }

    private void initGameView() {
        if (gameScene == null) {
            Parent rootPane = initPresenter(GameViewPresenter.fxml);
            gameScene = new Scene(rootPane, 1280, 750);
            gameScene.getStylesheets().add(styleSheet);
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
    private void initLobbyView() {

        if (lobbyScene == null) {
            Parent rootPane = initPresenter(LobbyPresenter.fxml);
            lobbyScene = new Scene(rootPane, 800, 600);
            lobbyScene.getStylesheets().add(styleSheet);
        }
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

    public void showGameScreen() {
        showScene(gameScene, "Login");
    }

    public void showRegistrationScreen() {
        showScene(registrationScene, "Registration");
    }

    /**
     * Es wird eine neue Stage mit der lobbyScene angezeigt und mit dem Attribut geöffnet.
     *
     * @param title der Übergebene Titel aus dem MainMenuPresenter
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * @since Sprint2
     */

    //TODO: LobbyScreen bzw Stage schließen, wenn Hauptmenü geschlossen wird
    public void showLobbyScreen(String title) {
        Platform.runLater(() -> {
            lobbyStage.setTitle(title);
            lobbyStage.setScene(lobbyScene);
            lobbyStage.setX(primaryStage.getX() + 200);
            lobbyStage.setY(primaryStage.getY() + 100);
            lobbyStage.show();
        });

    }

}