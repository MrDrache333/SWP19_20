package de.uol.swp.client.main;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.game.GameManagement;
import de.uol.swp.client.game.GameService;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.user.UserService;
import de.uol.swp.common.lobby.message.KickUserMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.OpenSettingsRequest;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class PrimaryPresenter extends AbstractPresenter {

    /**
     * Die Konstante fxml.
     */
    public static final String fxml = "/fxml/PrimaryView.fxml";
    /**
     * Die Konstante css.
     */
    public static final String css = "css/PrimaryPresenter.css";

    private final Logger LOG = LogManager.getLogger(PrimaryPresenter.class);
    private final Map<UUID, GameManagement> games = new HashMap<>();
    private Injector injector = null;
    private ChatService chatService;
    private LobbyService lobbyService;
    private GameService gameService;

    @FXML
    private TabPane TabView;

    /**
     * Initialisierung des Injektors
     *
     * @param eventBus     Der Eventbus
     * @param loggedInUser Der eingeloggte Nutzer
     * @param chatService  Der Chatservice
     * @param lobbyService Der Lobbyservice
     * @param userService  Der Userservice
     * @param injector     Der Injector
     * @author Fenja Oelrichs Garcia
     * @version 1.0
     * @since Sprint4
     */
    public void initialise(EventBus eventBus, User loggedInUser, ChatService chatService, LobbyService lobbyService, UserService userService, Injector injector, GameService gameService) {
        this.loggedInUser = loggedInUser;
        this.injector = injector;
        this.eventBus = eventBus;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.gameService = gameService;
    }

    /**
     * Login war erfolgreich. Der User tritt dem globalen Chat bei. Lobbys werden aktualisiert
     *
     * @param message Die LoginSuccessfulResponse
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void loginSuccessful(LoginSuccessfulResponse message) {
        loggedInUser = message.getUser();
    }

    /**
     * Schließt alle Tabs, wenn der eingeloggte User der übergebene User ist
     *
     * @param msg Die UserDroppedMessage
     * @author Fenja, Keno O.
     * @since Sprint7
     */
    private void userDroppedAccount(UserDroppedMessage msg) {
        if (loggedInUser.getUsername().equals(msg.getUser().getUsername())) {
            closeAllTabs();
        }
    }

    /**
     * Fügt eine Tab, der Tabview hinzu
     *
     * @param tab Der zu hinzufügende Tab
     * @author Fenja, Keno O.
     * @since Sprint7
     */
    public void addTab(Tab tab) {
        TabView.getTabs().add(tab);
    }

    /**
     * Zeigt einen Tab an
     *
     * @param id die ID des Tabs
     * @author Fenja, Keno O.
     * @since Sprint7
     */
    public void showTab(UUID id) {
        TabView.getTabs().forEach(t -> {
            if (t.getId().equals(id.toString())) {
                TabView.getSelectionModel().select(t);
            }
        });
    }

    /**
     * Methode fängt Button-Klick ab, User verlässt alle Lobbies, in denen er angemeldet ist und wird ausgeloggt
     *
     * @param actionEvent Das Actionevent
     * @author Julia, Paula
     * @version 1.0
     * @since Sprint3
     */
    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

    /**
     * Gibt den fokussierten Tab als ID zurück
     *
     * @return Die ID des Tabs
     * @author Fenja, Keno O.
     * @since Sprint7
     */
    public String getFocusedTab() {
        return TabView.getSelectionModel().getSelectedItem().getId();
    }

    /**
     * Entfernt den Tab aus der TabView
     *
     * @param id Die Tab UUID
     * @author Fenja, Keno O.
     * @since Sprint7
     */
    private void removeTab(UUID id) {
        TabView.getTabs().forEach(t -> {
            if (t.getId().equals(id.toString())) {
                Platform.runLater(() -> TabView.getTabs().remove(t));
            }
        });
    }

    /**
     * Beim Drücken des instructions Button
     *
     * @param actionEvent das Actionevent
     * @author Keno Oelrichs Garcia
     * @version 1.0
     * @since Sprint3
     */
    @FXML
    public void onInstructionsButtonPressed(ActionEvent actionEvent) {
        try {
            this.lobbyService.startWebView();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Schließt das Fenster, wenn der aktuelle Benutzer diese Lobby verlassen hat
     *
     * @param msg Die UserLeftLobbyMessage
     * @author Keno O.
     * @since Sprint3
     */
    @Subscribe
    private void userLeft(UserLeftLobbyMessage msg) {
        if (games.containsKey(msg.getLobbyID()) && msg.getUser().getUsername().equals(loggedInUser.getUsername())) {
            closeTab(msg.getLobbyID(), false);
        }
    }

    /**
     * Die Methode postet ein Request auf den Bus, wenn der Einstellungen-Button gedrückt wird
     *
     * @param actionEvent Das ActionEvent
     * @author Anna
     * @since Sprint4
     */
    @FXML
    public void onSettingsButtonPressed(ActionEvent actionEvent) {
        OpenSettingsRequest request = new OpenSettingsRequest(loggedInUser);
        eventBus.post(request);
    }

    /**
     * Es wird eine neue Stage mit der lobbyScene angezeigt und mit dem Attribut geöffnet.
     *
     * @param currentUser Der aktuelle Nutzer
     * @param title       Der Übergebene Titel aus dem MainMenuPresenter
     * @param lobbyID     Die übergebene LobbyID aus der empfangenen Message in der ClientApp
     * @author Paula, Haschem, Ferit, Anna
     * @version 1.0
     * @since Sprint3
     */
    public void createLobby(User currentUser, String title, UUID lobbyID, UserDTO gameOwner) {
        Platform.runLater(() -> {
            //LobbyPresenter neue Instanz mit (name, id) wird erstellt
            GameManagement gameManagement = new GameManagement(eventBus, lobbyID, title, currentUser, chatService, lobbyService, userService, injector, gameOwner, gameService, this);

            eventBus.register(gameManagement);

            //LobbyPresenter und lobbyStage in die jeweilige Map packen, mit lobbyID als Schlüssel
            games.put(lobbyID, gameManagement);
            gameManagement.showLobbyView();

            //Neuen Tab initialisieren, Pane vom GameManagement übernehmen und der TabView hinzufügen

            //Auf Schließung des Tabs reagieren
            gameManagement.getPrimaryTab().setOnCloseRequest(event -> {
                games.remove(gameManagement.getID());
                lobbyService.leaveLobby(gameManagement.getID(), (UserDTO) loggedInUser);
                TabView.getTabs().remove(gameManagement.getPrimaryTab());
                lobbyService.retrieveAllLobbies();
            });
            TabView.getTabs().add(gameManagement.getPrimaryTab());
            TabView.getSelectionModel().select(gameManagement.getPrimaryTab());
        });

    }

    /**
     * Bei Erhalt einer UserLeftLobbyMessage wird der Tab geschlossen und eine Log Info ausgegeben.
     *
     * @param msg Die UserLeftLobbyMessage
     * @author Fenja, Keno O., Timo
     * @since Sprint8
     */
    @Subscribe
    private void onUserLeftLobby(UserLeftLobbyMessage msg) {
        if (games.containsKey(msg.getLobbyID()) && loggedInUser.getUsername().equals(msg.getUser().getUsername())) {
            closeTab(msg.getLobbyID(), false);
            LOG.info("User " + msg.getUser().getUsername() + " verließ die Lobby erfolgreich.");
        }
    }

    /**
     * Sorgt dafür, dass die Lobby clientseitig geschlossen wird und diese ggf. verlassen wird
     *
     * @param uuid  Die UUID der Lobby
     * @param leave Ob die Lobby Serverseitig noch verlassen werden muss
     */
    public void closeTab(UUID uuid, boolean leave) {
        if (games.containsKey(uuid)) {
            removeTab(uuid);
            if (leave)
                lobbyService.leaveLobby(games.get(uuid).getID(), (UserDTO) loggedInUser);
            games.remove(uuid);
        }
    }

    /**
     * Gibt das zur übergebenen lobbyID gehörige GameManagement zurück
     *
     * @param lobbyID Die LobbyID
     * @return GameManagement game management
     * @author Julia, Paula
     * @since Sprint3
     */
    public GameManagement getGameManagement(UUID lobbyID) {
        GameManagement gameManagement;
        try {
            gameManagement = games.get(lobbyID);
        } catch (NullPointerException e) {
            gameManagement = null;
        }
        return gameManagement;
    }

    /**
     * Wenn die Nachricht abgefangen wird und man der gekickte Benutzer ist und in der Lobby ist wird das Lobbyfenster
     * geschlossen
     *
     * @author Darian, Marvin
     * @since Sprint4
     */
    @Subscribe
    private void onKickUserMessage(KickUserMessage msg) {
        if (games.containsKey(msg.getLobbyID()) && msg.getUser().getUsername().equals(loggedInUser.getUsername())) {
            closeTab(msg.getLobbyID(), true);
        }
    }

    /**
     * Schließt das Fenster, wenn sich der aktuelle Benutzer ausgeloggt hat
     *
     * @param msg Die UserLoggedOutMessage
     * @author Keno O.
     * @since Sprint3
     */
    @Subscribe
    private void userLoggedOut(UserLoggedOutMessage msg) {
        if (loggedInUser != null && msg.getUsername().equals(loggedInUser.getUsername())) {
            closeAllTabs();
        }
    }

    /**
     * Aktualisiert den eingeloggten User
     *
     * @param message Die UpdatedUserMessage
     * @author Keno O., Paula
     * @since Sprint3
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser != null && loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
    }

    /**
     * Schließt alle GameManagement Stages
     *
     * @author Julia, Paula
     * @version 1.0
     * @since Sprint3
     */
    public void closeAllTabs() {
        Platform.runLater(() -> games.values().forEach(e -> {
            try {
                closeTab(e.getID(), true);
            } catch (ConcurrentModificationException ignored) {
            }
        }));
    }

    /**
     * Gibt den eingeloggten User zurück
     *
     * @return Gibt den eingeloggten User zurück
     * @author Marvin
     * @since Sprint8
     */
    public User getUser() {
        return loggedInUser;
    }

}
