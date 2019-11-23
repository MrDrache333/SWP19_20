package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.message.CreateLobbyRequest;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
    private Pane chatView;
    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("allgemeiner", ChatViewPresenter.THEME.Light, chatService);
        chatViewPresenter.setChatId("global");
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
    }

    /**
     * On create lobby button pressed.
     *
     * @param event the event
     */
    @FXML
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), loggedInUser);
        eventBus.post(msg);
        LOG.debug("Request to create Lobby");
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
}
