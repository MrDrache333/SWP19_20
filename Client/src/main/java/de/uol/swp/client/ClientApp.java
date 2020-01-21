package de.uol.swp.client;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.uol.swp.client.di.ClientModule;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.exception.RegistrationExceptionMessage;
import de.uol.swp.common.user.message.UpdateUserFailedMessage;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.OpenSettingsRequest;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import de.uol.swp.common.user.response.RegistrationSuccessfulResponse;
import io.netty.channel.Channel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ClientApp extends Application implements ConnectionListener {

    private static final Logger LOG = LogManager.getLogger(ClientApp.class);
    private static SceneManager sceneManager;
    private String host;
    private int port;
    private UserService userService;
    private LobbyService lobbyService;
    private User user;
    private ClientConnection clientConnection;
    private EventBus eventBus;

    // -----------------------------------------------------
    // Java FX Methods
    // ----------------------------------------------------

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Getter Methode um einen SceneManager zu erhalten.
     *
     * @return Einen SceneManager
     */
    public static SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Diese Methode setzt die Verbindungsdaten zum Server.
     * host: localhost - Der Server auf der eigenen Maschine.
     * port: 8889 - Der Port auf dem der Server läuft.
     */
    @Override
    public void init() {
        Parameters p = getParameters();
        List<String> args = p.getRaw();

        if (args.size() != 2) {
            host = "localhost";
            port = 8889;
            System.err.println("Usage: " + ClientConnection.class.getSimpleName() + " host port");
            System.err.println("Using default port " + port + " on " + host);
        } else {
            host = args.get(0);
            port = Integer.parseInt(args.get(1));
        }

        // do not establish connection here
        // if connection is established in this stage, no GUI is shown and
        // exceptions are only visible in console!
    }

    /**
     * Diese Methode startet die ClientApp und initialisiert alle benötigten Elemente zum starten der ClientApp.
     *
     * @param primaryStage Die Stage zu der zugehörigen ClientApp gehört.
     */
    @Override
    public void start(Stage primaryStage) {

        // Client app is created by java, so injection must
        // be handled here manually
        Injector injector = Guice.createInjector(new ClientModule());

        // get user service from guice, is needed for logout
        this.userService = injector.getInstance(UserService.class);
        this.lobbyService = injector.getInstance(LobbyService.class);

        // get event bus from guice
        eventBus = injector.getInstance(EventBus.class);
        // Register this class for de.uol.swp.client.events (e.g. for exceptions)
        eventBus.register(this);

        // Client app is created by java, so injection must
        // be handled here manually
        SceneManagerFactory sceneManagerFactory = injector.getInstance(SceneManagerFactory.class);
        sceneManager = sceneManagerFactory.create(primaryStage);
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Intro, SoundMediaPlayer.Type.Music).play();

        //  close request calls method to close all windows
        primaryStage.setOnCloseRequest(event -> closeAllWindows());

        ClientConnectionFactory connectionFactory = injector.getInstance(ClientConnectionFactory.class);
        clientConnection = connectionFactory.create(host, port);
        clientConnection.addConnectionListener(this);
        // JavaFX Thread should not be blocked to long!
        Thread t = new Thread(() -> {
            try {
                clientConnection.start();
            } catch (Exception e) {
                exceptionOccurred(e.getMessage());
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Zeigt den LoginScreen, wenn eine Verbindung zum Server besteht.
     *
     * @param ch Der Übergeben Channel für die Verbindung zum Server.
     */
    @Override
    public void connectionEstablished(Channel ch) {
        sceneManager.showLoginScreen();
    }

    /**
     * Stoppt die ClientApp des Users und loggt den zugehörigen User aus.
     */
    @Override
    public void stop() {
        if (userService != null && user != null) {
            userService.logout(user);
            user = null;
        }
        eventBus.unregister(this);
        // Important: Close connection so connection thread can terminate
        // else client application will not stop
        LOG.trace("Trying to shutting down client ...");
        if (clientConnection != null) {
            clientConnection.close();
        }
        LOG.info("ClientConnection shutdown");
    }

    //----------------
    // EVENTBUS
    //----------------

    @Override
    public void exceptionOccurred(String e) {
        sceneManager.showServerError(e);
    }

    /**
     * Diese Methode zeigt den MainScreen für den übergebenen User der message an.
     *
     * @param message Eine message vom Server, dass der User eingeloggt worden ist.
     */
    @Subscribe
    public void userLoggedIn(LoginSuccessfulResponse message) {
        LOG.debug("User logged in successfully " + message.getUser().getUsername());
        this.user = message.getUser();
        sceneManager.showMainScreen(user);
    }

    @Subscribe
    public void onRegistrationExceptionMessage(RegistrationExceptionMessage message) {
        sceneManager.showServerError("Registration error " + message);
        LOG.error("Registration error " + message);
    }

    /**
     * Diese Methode zeigt den LoginScreen für den übergebenen User der message an.
     *
     * @param message Eine message vom Server, dass der User registriert worden ist.
     */
    @Subscribe
    public void onRegistrationSuccessfulMessage(RegistrationSuccessfulResponse message) {
        LOG.info("Registration successful.");
        sceneManager.showLoginScreen();
    }

    @Subscribe
    private void handleEventBusError(DeadEvent deadEvent) {
        LOG.error("DeadEvent detected " + deadEvent);
    }

    /**
     * Empfängt vom Server die Message, dass die Lobby erstellt worden ist und öffnet im SceneManager
     * somit die Lobby. Überprüft außerdem ob der Ersteller mit dem eingeloggten User übereinstimmt, damit
     * nur dem ersteller ein neu erstelltes Lobbyfenster angezeigt wird.
     *
     * @param message CreateLobbyMessage vom Server, dass die Lobby erstellt worden ist.
     * @author Paula, Haschem, Ferit, Anna, Darian
     * @version 0.2
     * @since Sprint3
     */
    @Subscribe
    public void onCreateLobbyMessage(CreateLobbyMessage message) {
        if (message.getUser().getUsername().equals(user.getUsername())) {
            sceneManager.showLobbyScreen(message.getUser(), message.getLobbyName(), message.getChatID(), message.getUser());
            LOG.debug("CreateLobbyMessage vom Server erfolgreich angekommen");
        }
    }

    /**
     * Empfängt vom Server die Message, dass der User der Lobby beigetreten ist.
     * Lobbys in Hauptmenü werden aktualisiert.
     *
     * @param message UserJoinedLobbyMessage Das ein User der Lobby beigetreten ist.
     * @author Paula, Julia
     * @since Sprint3
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        if (message.getUser().getUsername().equals(user.getUsername())) {
            if (sceneManager.getGameManagement(message.getLobbyID()) != null) {
                sceneManager.getGameManagement(message.getLobbyID()).showLobbyView();
            } else {
                sceneManager.showLobbyScreen(message.getUser(), message.getLobbyName(), message.getLobbyID(), message.getGameOwner());
            }
            LOG.info("User " + message.getUser().getUsername() + " joined lobby successfully");
        }
    }

    /**
     * Empfängt vom Server die Message, dass User Lobby verlassen hat.
     * Lobby wird geschlossen. User wird aus Lobby gelöscht.
     *
     * @param message UserLeftLobbyMessage
     * @author Julia, Paula
     * @since Sprint3
     */
    @Subscribe
    public void onUserLeftLobbyMessage(UserLeftLobbyMessage message) {
        if (message.getUser().getUsername().equals(user.getUsername())) {
            sceneManager.showMainScreen(user);
            LOG.info("User " + message.getUser().getUsername() + " left lobby successfully");
            sceneManager.getGameManagement(message.getLobbyID()).close();
        }
    }

    /**
     * Empfängt die Nachricht (vom MainMenuPresenter), dass das Einstellungsfenster geöffnet werden soll.
     * Öffnet das Einstellungsfenster.
     *
     * @param message Die Anfrage zum öffnen des Fensters
     * @author Anna
     * @since Sprint4
     */
    @Subscribe
    public void onOpenSettingsRequest(OpenSettingsRequest message) {
        if (message.getUser().getUsername().equals(user.getUsername())) {
            sceneManager.showSettingsScreen(message.getUser());
        }
    }

    /**
     * Aktualisiert den User und schließt das Einstellungsfenster.
     *
     * @param message Nachricht um die neuen Daten des Users anzuzeigen und zu setzen.
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void onUpdatedUserMessage(UpdatedUserMessage message) {
        if (user.getUsername().equals(message.getOldUser().getUsername())) {
            user = message.getUser();
            sceneManager.closeSettings();
            sceneManager.showMainScreen(user);
            LOG.info("User " + message.getOldUser().getUsername() + " updated his data");
        }
    }

    /**
     * Zeigt den Fehler beim Updaten der Daten an.
     *
     * @param message UpdateUserFailedMessage
     */
    @Subscribe
    public void onUpdateUserFailedMessage(UpdateUserFailedMessage message) {
        if (user.getUsername().equals(message.getUser().getUsername())) {
            sceneManager.showError(message.getMessage());
        }
    }


    /**
     * Empfängt vom Server die Message, dass ein User gekickt wurde. Bei diesem User wird das Lobbyfenster
     * geschlossen und das MainMenu wird angezeigt
     *
     * @param message KickUserMessage
     * @author Darian
     * @since Sprint 4
     */
    @Subscribe
    public void onKickUserMessage(KickUserMessage message) {
        if (message.getUser().getUsername().equals(user.getUsername())) {
            sceneManager.showMainScreen(user);
            LOG.info("User " + message.getUser().getUsername() + " is kicked from the lobby successfully");
            sceneManager.getGameManagement(message.getLobbyID()).close();
            SceneManager.showAlert(Alert.AlertType.ERROR,"Sie wurden aus der Lobby entfernt","Lobby verlassen");
        }
        lobbyService.retrieveAllLobbies();
    }

    // -----------------------------------------------------
    // JavFX Help methods
    // -----------------------------------------------------

    /**
     * Empfängt vom Server die Message, dass sich der Nutzer ausgeloggt hat. Der Nutzer wird aus allen Lobbys gelöscht.
     * Lobbys im Hauptmenü werden aktualisiert, alle Stages werden geschlossen und das Loginfenster wird geöffnet
     *
     * @param message UserLoggedOutMessage
     * @author Julia, Paula
     * @since Sprint3
     */
    @Subscribe
    public void onUserLoggedOutMessage(UserLoggedOutMessage message) {
        LOG.info("Logout and leaving of all lobbies successful.");
        if (message.getUsername().equals(user.getUsername())) {
            sceneManager.closeAllStages();
            sceneManager.showLoginScreen();
        }
        lobbyService.retrieveAllLobbies();
    }


    // -----------------------------------------------------
    // JavFX Help methods
    // -----------------------------------------------------

    /**
     * Aktualisiert die Max Player Anzahl der Lobbys wenn eine SetMaxPlayerMessage eingeht.
     *
     * @author Timo, Rike
     * @since Sprint 3
     */
    @Subscribe
    public void onSetMaxPlayerMessage(SetMaxPlayerMessage msg) {
        if (msg.isSetMaxPlayerSet()) {
            LOG.info("Max. Spieler der Lobby: " + msg.getLobbyID() + " erfolgreich auf " + msg.getMaxPlayer() + " gesetzt.");
            lobbyService.retrieveAllLobbies();
        } else {
            LOG.info("Max. Spieler der Lobby: " + msg.getLobbyID() + " nicht gesetzt. User ist nicht der Lobbyowner!");
        }
    }

    /**
     * Nachdem der Account gelöscht wurde, werden alle Fenster geschlossen und der Login-Screen angezeigt
     *
     * @author Anna
     * @since Sprint4
     */
    @Subscribe
    public void onUserDroppedMessage(UserDroppedMessage message) {
        LOG.info("Deleting Account and leaving of all lobbies successful.");
        if (message.getUser().getUsername().equals(user.getUsername())) {
            sceneManager.closeAllStages();
            sceneManager.showLoginScreen();
        }
        lobbyService.retrieveAllLobbies();
    }

    /**
     * Schließen aller Fenster, wenn die Methode aufgerufen wird.
     *
     * @author Julia, Paula
     * @since Sprint3
     */
    public void closeAllWindows() {
        Platform.exit();
    }
}
