package de.uol.swp.client;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.Assisted;
import de.uol.swp.client.auth.LoginPresenter;
import de.uol.swp.client.auth.events.ShowLoginViewEvent;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.game.GameService;
import de.uol.swp.client.game.event.GameQuitEvent;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.PrimaryPresenter;
import de.uol.swp.client.register.RegistrationPresenter;
import de.uol.swp.client.register.event.RegistrationCanceledEvent;
import de.uol.swp.client.register.event.RegistrationErrorEvent;
import de.uol.swp.client.register.event.ShowRegistrationViewEvent;
import de.uol.swp.client.settings.DeleteAccountPresenter;
import de.uol.swp.client.settings.SettingsPresenter;
import de.uol.swp.client.settings.event.CloseDeleteAccountEvent;
import de.uol.swp.client.settings.event.CloseSettingsEvent;
import de.uol.swp.client.settings.event.DeleteAccountEvent;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.client.user.UserService;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.UUID;

/**
 * Der SceneManager
 *
 * @author Marco
 * @since Start
 */
public class SceneManager {

    static final Logger LOG = LogManager.getLogger(SceneManager.class);
    static final String styleSheet = "css/global.css";

    final private Stage primaryStage;
    final private EventBus eventBus;
    final private UserService userService;
    final private LobbyService lobbyService;
    final private GameService gameService;
    final private ChatService chatService;
    private final Injector injector;
    private SettingsPresenter settingsPresenter;
    private Stage settingsStage;
    private Stage deleteAccountStage;
    private Scene loginScene;
    private String lastTitle;
    private Scene registrationScene;
    private Scene mainScene;
    private Scene settingsScene;
    private Scene deleteAccountScene;
    private Scene lastScene = null;
    private Scene currentScene = null;
    private User currentUser;
    private PrimaryPresenter primaryPresenter;
    private Scene primaryScene;


    @Inject
    public SceneManager(EventBus eventBus, UserService userService, LobbyService lobbyService, Injector injected, @Assisted Stage primaryStage, ChatService chatService, GameService gameService) {
        this.eventBus = eventBus;
        this.eventBus.register(this);
        this.userService = userService;
        this.primaryStage = primaryStage;
        this.injector = injected;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.gameService = gameService;
        initViews();
    }

    /**
     * Alert wird erstellt
     *
     * @param type    der Alert-Typ
     * @param message die Message
     * @param title   der Titel des Alerts
     * @author Paula, Haschem, Ferit
     * @since Sprint1
     */
    public static void showAlert(Alert.AlertType type, String message, String title) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, "");
            alert.setResizable(false);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.getDialogPane().setContentText(message);
            alert.getDialogPane().setHeaderText(title);
            alert.show();
        });
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

    @Subscribe
    public void onCloseSettingsEvent(CloseSettingsEvent event) {
        closeSettings();
    }

    @Subscribe
    public void onCloseDeleteAccountEvent(CloseDeleteAccountEvent event) {
        closeDeleteAccount();
    }


    /**
     * Wenn in den Einstellungen auf den Button "Account löschen" geklickt wird, wird ein neues Fenster geöffnet,
     * in dem nachgefragt wird, ob man seinen Account wirklich löschen will.
     *
     * @param event das Event
     * @author Anna
     * @since Sprint4
     */
    @Subscribe
    public void onDeleteAccountEvent(DeleteAccountEvent event) {
        Platform.runLater(() -> {
            currentUser = event.getUser();
            initDeleteAccountView();
            deleteAccountStage = new Stage();
            deleteAccountStage.setTitle("Account löschen");
            deleteAccountStage.setScene(deleteAccountScene);
            deleteAccountStage.setResizable(false);
            deleteAccountStage.show();
        });
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

    public void closeDeleteAccount() {
        Platform.runLater(() -> deleteAccountStage.close());
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
        showScene(primaryScene, "Welcome " + currentUser.getUsername());
    }


    public void showLoginScreen() {
        showScene(loginScene, "Login");
    }

    public void showRegistrationScreen() {
        showScene(registrationScene, "Registration");
    }

    /**
     * Es wird eine neue Stage mit der lobbyScene angezeigt und mit dem Parametern geöffnet.
     *
     * @param title   der übergebene Titel aus dem MainMenuPresenter
     * @param lobbyID die übergebene LobbyID aus der empfangenen Message in der ClientApp
     * @author Paula, Haschem, Ferit, Anna, Darian
     * @version 0.2
     * @since Sprint3
     */
    public void showLobbyScreen(User currentUser, String title, UUID lobbyID, UserDTO gameOwner) {
        Platform.runLater(() -> {
            //neue Instanz des LobbyPresenter mit (name, id) wird erstellt
            primaryPresenter.createLobby(currentUser, title, lobbyID, gameOwner);
        });
    }

    public boolean hasFocus() {
        return primaryStage.isFocused();
    }

    /**
     * Öffnet das Einstellungsfenster, indem eine neue Stage mit der settingsScene erstellt wird.
     *
     * @param loggedInUser der eingeloggte User
     * @author Anna, Julia
     * @since Sprint4
     */
    public void showSettingsScreen(User loggedInUser) {
        Platform.runLater(() -> {
            settingsPresenter = new SettingsPresenter(loggedInUser, lobbyService, userService, eventBus);
            initSettingsView(settingsPresenter);
            settingsStage = new Stage();
            settingsStage.setTitle("Einstellungen");
            settingsStage.setScene(settingsScene);
            settingsStage.setResizable(false);
            settingsStage.show();
            eventBus.register(settingsPresenter);
        });
    }

    /**
     * Schließt alle Stages
     *
     * @author Julia, Paula
     * @since Sprint3
     */
    public void closeAllStages() {
        Platform.runLater(() -> {
            if (settingsStage != null) {
                settingsStage.close();
            }
            if (deleteAccountStage != null) {
                deleteAccountStage.close();
            }
        });
    }

    public void closeSettings() {
        Platform.runLater(() -> settingsStage.close());
    }

    //-----------------
    // PRIVATE METHODS
    //-----------------

    private void initViews() {
        initLoginView();
        initRegistrationView();
        initPrimaryView();
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

    private Parent initPresenter(String fxmlFile, PrimaryPresenter presenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(fxmlFile);
            LOG.debug("Loading " + url);
            loader.setLocation(url);
            loader.setController(presenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load View!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private void initPrimaryView() {
        if (primaryScene == null) {
            primaryPresenter = new PrimaryPresenter();
            primaryPresenter.initialise(eventBus, currentUser, chatService, lobbyService, userService, injector, gameService);
            Parent rootPane = initPresenter(PrimaryPresenter.fxml, primaryPresenter);
            primaryScene = new Scene(rootPane, 1400, 790);
            primaryScene.getStylesheets().add(styleSheet);
            primaryScene.getStylesheets().add(PrimaryPresenter.css);
            primaryStage.setOnCloseRequest(event -> {
                userService.logout(currentUser);
            });
            eventBus.register(primaryPresenter);
        }
    }

    private Parent initSettingsPresenter(SettingsPresenter settingsPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(SettingsPresenter.fxml);
            LOG.debug("Loading " + url);
            loader.setLocation(url);
            loader.setController(settingsPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load View!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private Parent initDeleteAccountPresenter(DeleteAccountPresenter deleteAccountPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(DeleteAccountPresenter.fxml);
            LOG.debug("Loading " + url);
            loader.setLocation(url);
            loader.setController(deleteAccountPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Could not load View!" + e.getMessage(), e);
        }
        return rootPane;
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
        }
    }

    private void initSettingsView(SettingsPresenter settingsPresenter) {
        if (settingsScene == null) {
            Parent rootPane = initSettingsPresenter(settingsPresenter);
            settingsScene = new Scene(rootPane, 400, 255);
            settingsScene.getStylesheets().add(SettingsPresenter.css);
        }
    }

    private void initDeleteAccountView() {
        if (deleteAccountScene == null) {
            Parent rootPane = initDeleteAccountPresenter(new DeleteAccountPresenter(currentUser, lobbyService, userService, eventBus));
            deleteAccountScene = new Scene(rootPane, 250, 100);
            deleteAccountScene.getStylesheets().add(SettingsPresenter.css);

        }
    }
}