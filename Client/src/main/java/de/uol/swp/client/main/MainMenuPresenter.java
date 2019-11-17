package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.CreateLobbyRequest;
import de.uol.swp.common.lobby.message.LobbyCreatedMessage;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MainMenuPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/MainMenuView.fxml";

    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);

    private ObservableList<String> users;
    private ObservableList<String> lobbies;

    private User loggedInUser;

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();
    @FXML
    private ListView<String> usersView;
    // Textfeld, welches den eingegebenen Lobbynamen enthält.
    @FXML
    private TextField lobbyName;
    @FXML
    private ListView<String> lobbiesView;


    @Subscribe
    public void loginSuccessful(LoginSuccessfulMessage message) {
        this.loggedInUser = message.getUser();
        userService.retrieveAllUsers();
    }

    @Subscribe
    public void newLobbyCreated(LobbyCreatedMessage message) {
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

    @Subscribe
    public void lobbyList(AllOnlineLobbiesResponse allLobbiesResponse) {
        LOG.debug("Update of lobbies list" + allLobbiesResponse.getLobbies());
        updateLobbiesList(allLobbiesResponse.getLobbies());
    }

    private void updateLobbiesList(List<LobbyDTO> lobbyList) {
        Platform.runLater(() -> {
            if(lobbies == null) {
                lobbies = FXCollections.observableArrayList();
                lobbiesView.setItems(lobbies);
            }
            lobbies.clear();
            lobbyList.forEach(l -> lobbies.add(l.getName()));
        });
    }


    /**
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * Fängt den Button ab und sendet den Request zur Erstellung der Lobby an den Server.
     */
    @FXML
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), loggedInUser);
        eventBus.post(msg);
        LOG.info("Request wurde gesendet.");
    }


}
