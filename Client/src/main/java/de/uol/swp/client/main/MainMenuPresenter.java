package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MainMenuPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/MainMenuView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();

    private ObservableList<String> users;
    private ObservableList<Lobby> lobbies;

    private User loggedInUser;

    @FXML
    private ListView<String> usersView;
    @FXML
    private TextField lobbyName;

    @FXML
    private TableView<Lobby> lobbiesView;
    @FXML
    private TableColumn<Lobby, String> name = new TableColumn<>("Name");
    @FXML
    private TableColumn<Lobby, String> host = new TableColumn<>("Host");
    @FXML
    private TableColumn<Lobby, String> players = new TableColumn<>("Spieler");

    /**
     * Initialisiert Lobby-Tabelle
     */
    @FXML
    private void initialize() {
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        host.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOwner().getUsername()));
        players.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsers().size() + "/4"));
        lobbiesView.getColumns().addAll(name, host, players);
        name.setResizable(false);
        host.setResizable(false);
        players.setResizable(false);
        name.setPrefWidth(110);
        host.setPrefWidth(90);
    }

    @Subscribe
    public void loginSuccessful(LoginSuccessfulMessage message) {
        this.loggedInUser = message.getUser();
        userService.retrieveAllUsers();
        lobbyService.retrieveAllLobbies();
    }

    @Subscribe
    public void newUser(UserLoggedInMessage message) {
        LOG.debug("New user " + message.getUsername() + " logged in");
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.equals(message.getUsername()))
                users.add(message.getUsername());
        });
    }

    @Subscribe
    public void userLeft(UserLoggedOutMessage message) {
        LOG.debug("User " + message.getUsername() + " logged out");
        Platform.runLater(() -> users.remove(message.getUsername()));
    }

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
     * Aktualisiert Lobby-Tabelle, kann über lobbyService.retrieveAllLobbies() aufgerufen werden
     *
     * @param allLobbiesResponse
     */
    @Subscribe
    public void lobbyList(AllOnlineLobbiesResponse allLobbiesResponse) {
        LOG.debug("Update of lobbies list" + allLobbiesResponse.getLobbies());
        updateLobbiesList(allLobbiesResponse.getLobbies());
    }

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
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        if (lobbyName.getText().equals("")) {

            showAlert(Alert.AlertType.WARNING, "Bitte geben Sie einen Lobby Namen ein! ", "Fehler");
        } else {
            CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), loggedInUser);
            eventBus.post(msg);
            LOG.info("Request wurde gesendet.");
        }

    }


}
