package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.user.dto.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * The type Main menu presenter.
 */
public class MainMenuPresenter extends AbstractPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/MainMenuView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    @FXML
    private TextField lobbyName;
    @FXML
    private ListView<String> usersView;
    @FXML
    private TableView<Lobby> lobbiesView;
    @FXML
    private TableColumn<Lobby, String> name = new TableColumn<>("Name");
    @FXML
    private TableColumn<Lobby, String> host = new TableColumn<>("Host");
    @FXML
    private TableColumn<Lobby, Integer> players = new TableColumn<>("Spieler");
    @FXML
    private Pane chatView;

    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    private ObservableList<Lobby> lobbies;

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("allgemeiner", ChatViewPresenter.THEME.Dark, chatService);
        chatViewPresenter.setChatId("global");
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());

        //Initialisieren der Lobby
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        host.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOwner().getUsername()));
        players.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPlayers()).asObject());
        lobbiesView.getColumns().addAll(name, host, players);
        name.setResizable(false);
        host.setResizable(false);
        players.setResizable(false);
        name.setPrefWidth(110);
        host.setPrefWidth(90);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * On chat response message.
     *
     * @param msg the msg
     */
    @Subscribe
    public void onChatResponseMessage(ChatResponseMessage msg) {
        chatViewPresenter.onChatResponseMessage(msg);
    }

    /**
     * On new chat message.
     *
     * @param msg the msg
     */
    @Subscribe
    public void onNewChatMessage(NewChatMessage msg) {
        chatViewPresenter.onNewChatMessage(msg);
    }

    /**
     * Login successful.
     *
     * @param message the message
     */
    @Subscribe
    public void loginSuccessful(LoginSuccessfulMessage message) {
        loggedInUser = message.getUser();
        chatViewPresenter.setloggedInUser(loggedInUser);
        LOG.debug("Logged in user: " + loggedInUser.getUsername());
        userService.retrieveAllUsers();
        lobbyService.retrieveAllLobbies();
    }

    /**
     * New user.
     *
     * @param message the message
     */
    @Subscribe
    public void newUser(UserLoggedInMessage message) {
        LOG.debug("New user " + message.getUsername() + " logged in");
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.equals(message.getUsername()))
                users.add(message.getUsername());
            chatViewPresenter.userJoined(message.getUsername());
        });
    }

    /**
     * User left.
     *
     * @param message the message
     */
    @Subscribe
    public void userLeft(UserLoggedOutMessage message) {
        LOG.debug("User " + message.getUsername() + " logged out");
        Platform.runLater(() -> {
            if (users.contains(message.getUsername())) {
                users.remove(message.getUsername());
                chatViewPresenter.userLeft(message.getUsername());
            }
        });
    }

    /**
     * User list.
     *
     * @param allUsersResponse the all users response
     */
    @Subscribe
    public void userList(AllOnlineUsersResponse allUsersResponse) {
        LOG.debug("Update of user list " + allUsersResponse.getUsers());
        updateUsersList(allUsersResponse.getUsers());
    }

    private void updateUsersList(List<UserDTO> userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (users == null) {
                users = FXCollections.observableArrayList();
                usersView.setItems(users);
            }
            users.clear();
            userList.forEach(u -> users.add(u.getUsername()));
        });
    }

    /**
     * Erstes erstellen der Lobbytabelle beim Login und Aktualisierung
     * @author Julia
     * @param allLobbiesResponse
     */
    @Subscribe
    public void lobbyList(AllOnlineLobbiesResponse allLobbiesResponse) {
        LOG.debug("Updating of lobbies list" + allLobbiesResponse.getLobbies());
        updateLobbiesList(allLobbiesResponse.getLobbies());
    }

    /**
     * updatet die lobbyList, wenn ein User eine Lobby betritt
     * @author Julia
     */
    private void updateLobbiesList(List<LobbyDTO> lobbyList) {
        Platform.runLater(() -> {
            if (lobbies == null) {
                lobbies = FXCollections.observableArrayList();
                lobbiesView.setItems(lobbies);
            }
            lobbies.clear();
            lobbyList.forEach(l -> lobbies.add(l));
        });
    }


    /**
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * Fängt den Button ab und sendet den Request zur Erstellung der Lobby an den Server.
     */

    public static void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(type, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        alert.show();
    }

    /**
     * Die Methode fängt den Button-Klick ab und prüft, ob der LobbyName leer ist.
     * Falls ja: Wird eine Fehlermeldung rausgegeben.
     * Falls nein: Wir eine CreateLobbyRequest mit dem eingegeben LobbyNamen und dem eingeloggten User auf den
     * Eventbus gepackt.
     *
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * @since Sprint2
     */
    @FXML

    //TODO : was machen, wenn nur Leerzeichen angegeben für Lobbynamen (möglich oder abfangen? )
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        boolean validLobbyName = true;
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equals(lobbyName.getText())) {
                validLobbyName = false;
            }
        }
        if (lobbyName.getText().equals("")) {
            showAlert(Alert.AlertType.WARNING, "Bitte geben Sie einen Lobby Namen ein! ", "Fehler");
            lobbyName.requestFocus();
        }
        else if(!validLobbyName) {
            showAlert(Alert.AlertType.WARNING, "Diese Lobby existiert bereits!", "Fehler");
            lobbyName.requestFocus();
        }
        else {
            CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), loggedInUser);
            eventBus.post(msg);
            LOG.info("Request wurde gesendet.");
        }

        lobbyName.clear();
    }

    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

}
