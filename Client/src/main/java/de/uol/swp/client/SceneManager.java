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
import de.uol.swp.client.game.GameService;
import de.uol.swp.client.lobby.CreateLobbyPresenter;
import de.uol.swp.client.lobby.JoinLobbyPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.lobby.event.CloseCreateLobbyEvent;
import de.uol.swp.client.lobby.event.CloseJoinLobbyEvent;
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
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Optional;
import java.util.UUID;

/**
 * Der SceneManager
 *
 * @author Marco
 * @since Start
 */
@SuppressWarnings("UnstableApiUsage, unused")
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
    private CreateLobbyPresenter createLobbyPresenter;
    private JoinLobbyPresenter joinLobbyPresenter;
    private Stage joinLobbyStage;
    private Scene joinLobbyScene;
    private Stage createLobbyStage;
    private Scene createLobbyScene;
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

    /**
     * Der Konstruktor des SceneManagers
     *
     * @param eventBus     der Eventbus
     * @param userService  der UserService
     * @param lobbyService der LobbyService
     * @param injected     der Injektor
     * @param primaryStage die PrimaryStage
     * @param chatService  der Chatservice
     * @param gameService  der Gameservice
     * @author Marco, Anna, Haschem, Keno O.
     * @since Sprint 1
     */
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
            Text text = new Text(message);
            text.setWrappingWidth(390);
            alert.getDialogPane().setMaxWidth(400);
            alert.getDialogPane().setContent(text);
            alert.getDialogPane().setPadding(new Insets(10, 10, 10, 10));
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

    @Subscribe
    public void onCloseCreateLobbyEvent(CloseCreateLobbyEvent event) {
        closeCreateLobby();
    }

    @Subscribe
    public void onCloseJoinLobbyEvent(CloseJoinLobbyEvent event) {
        closeJoinLobby();
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

    public void showError(String message, String e) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR, message + e);
            a.showAndWait();
        });
    }

    public void showServerError(String e) {
        showError("Der Server gab einen Fehler zurück:\n", e);
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
            Alert alert = new Alert(Alert.AlertType.ERROR, "Fehler beim Einloggen auf den Server!");
            alert.showAndWait();
            showLoginScreen();
        });
    }

    public void showMainScreen(User currentUser) {
        showScene(primaryScene, "Willkommen " + currentUser.getUsername());
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
            settingsPresenter = new SettingsPresenter(loggedInUser, lobbyService, userService, injector, eventBus);
            initSettingsView(settingsPresenter);
            settingsStage = new Stage();
            settingsStage.setTitle("Einstellungen");
            settingsStage.setScene(settingsScene);
            settingsStage.setResizable(false);
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.show();
            eventBus.register(settingsPresenter);
        });
    }

    /**
     * Öffnet das Lobby-erstellen Fenster
     *
     * @param loggedInUser der eingeloggte User
     * @author Paula
     * @since Sprint7
     */
    public void showCreateLobbyScreen(User loggedInUser) {
        Platform.runLater(() -> {
            createLobbyPresenter = new CreateLobbyPresenter(loggedInUser, lobbyService, userService, eventBus);
            initCreateLobbyView(createLobbyPresenter);
            createLobbyStage = new Stage();
            createLobbyStage.setTitle("Lobby");
            createLobbyStage.setScene(createLobbyScene);
            createLobbyStage.setResizable(false);
            createLobbyStage.initModality(Modality.APPLICATION_MODAL);
            createLobbyStage.show();
            eventBus.register(createLobbyPresenter);
        });
    }

    /**
     * Öffnet das Lobby beitreten Fenster,falls man aufgefordert wird sein Passwort anzugeben
     *
     * @param loggedInUser Der angemeldete Nutzer
     * @author Paula
     * @since Sprint7
     */
    public void showJoinLobbyScreen(User loggedInUser, Lobby lobby) {
        Platform.runLater(() -> {
            joinLobbyPresenter = new JoinLobbyPresenter(loggedInUser, lobbyService, userService, eventBus, lobby);
            initJoinLobbyView(joinLobbyPresenter);
            joinLobbyStage = new Stage();
            joinLobbyStage.setTitle("Lobby: " + lobby.getName()+ " beitreten ");
            joinLobbyStage.setScene(joinLobbyScene);
            joinLobbyStage.setResizable(false);
            joinLobbyStage.initModality(Modality.APPLICATION_MODAL);
            joinLobbyStage.show();
            eventBus.register(joinLobbyPresenter);
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

    public void closeCreateLobby() {
        Platform.runLater(() -> createLobbyStage.close());
    }

    public void closeJoinLobby() {
        if (joinLobbyStage != null)
        Platform.runLater(() -> joinLobbyStage.close());
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
            LOG.debug("Lade " + url);
            loader.setLocation(url);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Konnte View nicht laden!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private Parent initPresenter(String fxmlFile, PrimaryPresenter presenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(fxmlFile);
            LOG.debug("Lade " + url);
            loader.setLocation(url);
            loader.setController(presenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Konnte View nicht laden!" + e.getMessage(), e);
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
            primaryStage.setOnCloseRequest(event -> userService.logout(currentUser));
            eventBus.register(primaryPresenter);
            primaryScene.setOnKeyPressed(hotkeyEventHandler);
        }
    }

    private Parent initSettingsPresenter(SettingsPresenter settingsPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(SettingsPresenter.fxml);
            LOG.debug("Lade " + url);
            loader.setLocation(url);
            loader.setController(settingsPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Konnte View nicht laden!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private Parent initCreateLobbyPresenter(CreateLobbyPresenter createLobbyPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(CreateLobbyPresenter.fxml);
            LOG.debug("Lade " + url);
            loader.setLocation(url);
            loader.setController(createLobbyPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Konnte View nicht laden!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private Parent initJoinLobbyPresenter(JoinLobbyPresenter joinLobbyPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(JoinLobbyPresenter.fxml);
            LOG.debug("Lade " + url);
            loader.setLocation(url);
            loader.setController(joinLobbyPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Konnte View nicht laden!" + e.getMessage(), e);
        }
        return rootPane;
    }

    private Parent initDeleteAccountPresenter(DeleteAccountPresenter deleteAccountPresenter) {
        Parent rootPane;
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        try {
            URL url = getClass().getResource(DeleteAccountPresenter.fxml);
            LOG.debug("Lade " + url);
            loader.setLocation(url);
            loader.setController(deleteAccountPresenter);
            rootPane = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Konnte View nicht laden!" + e.getMessage(), e);
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
            settingsScene = new Scene(rootPane, 400, 420);
            settingsScene.getStylesheets().add(SettingsPresenter.css);
        }
    }

    private void initDeleteAccountView() {
        if (deleteAccountScene == null) {
            Parent rootPane = initDeleteAccountPresenter(new DeleteAccountPresenter(currentUser, lobbyService, userService, eventBus));
            deleteAccountScene = new Scene(rootPane, 450, 250);
            deleteAccountScene.getStylesheets().add(SettingsPresenter.css);

        }
    }

    private void initCreateLobbyView(CreateLobbyPresenter createLobbyPresenter) {
        if (createLobbyScene == null) {
            Parent rootPane = initCreateLobbyPresenter(createLobbyPresenter);
            createLobbyScene = new Scene(rootPane, 350, 250);
            createLobbyScene.getStylesheets().add(CreateLobbyPresenter.css);
        }
    }

    private void initJoinLobbyView(JoinLobbyPresenter joinLobbyPresenter) {
        Parent rootPane = initJoinLobbyPresenter(joinLobbyPresenter);
        joinLobbyScene = new Scene(rootPane, 350, 250);
        joinLobbyScene.getStylesheets().add(JoinLobbyPresenter.css);

    }


    /**
     * EventHandler für Hotkeys während eines Spiels.
     * Bestätigungsfenster (Derzeit nur bei GiveUp) kann bei Bedarf auch auf weitere Hotkeys leicht erweitert werden.
     *
     * Momentane Hotkeys:
     * Strg + S: SkipPhase
     * Strg + G: GiveUp
     * Strg + L: CreateLobby
     *
     * @author Marvin
     * @since Sprint7
     */

    private final EventHandler<KeyEvent> hotkeyEventHandler = new EventHandler<>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.isControlDown()) {
                String focusedTab = primaryPresenter.getFocusedTab();
                if (focusedTab.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")) {
                    GameManagement gameManagement = primaryPresenter.getGameManagement(UUID.fromString(focusedTab));
                    User user = gameManagement.getLoggedInUser();
                    UUID lobbyID = gameManagement.getID();
                    switch (event.getCode()) {
                        case S:
                            LOG.debug("Skip Phase Hotkey pressed");
                            gameService.skipPhase(user, lobbyID);
                            break;
                        case G:
                            LOG.debug("Give Up Hotkey pressed");
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setResizable(false);
                            alert.initModality(Modality.APPLICATION_MODAL);
                            alert.getDialogPane().setHeaderText("Möchtest du wirklich aufgeben?");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                gameService.giveUp(lobbyID, (UserDTO) user);
                            }
                            break;
                    }
                    event.consume();
                } else if (focusedTab.equals("Menu")) {
                    if (event.getCode() == KeyCode.L) {
                        LOG.debug("Create Lobby Hotkey pressed");
                        showCreateLobbyScreen(primaryPresenter.getUser());
                    }
                    event.consume();
                }
            }
        }
    };
}