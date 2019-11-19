package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
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
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class MainMenuPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/MainMenuView.fxml";

    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);

    private ObservableList<String> users;

    private User loggedInUser;

    @FXML
    private ListView<String> usersView;
    // Textfeld, welches den eingegebenen Lobbynamen enthält.
    @FXML
    private TextField lobbyName;


    @FXML
    private Pane chatView;

    @FXML
    public void initialize() throws IOException {
        Pane newChatView = FXMLLoader.load(getClass().getResource(ChatViewPresenter.fxml));
        chatView.getChildren().add(newChatView);
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

    @Subscribe
    public void loginSuccessful(LoginSuccessfulMessage message) {
        loggedInUser = message.getUser();
        ChatViewPresenter.setloggedInUser(loggedInUser);
        ChatViewPresenter.setChatService(chatService);
        //TODO Implementiere ChatHistory-Update
        //chatService.getChatHistory(loggedInUser);
        LOG.debug("Logged in user: " + loggedInUser.getUsername());
        userService.retrieveAllUsers();
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
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * Fängt den Button ab und sendet den Request zur Erstellung der Lobby an den Server.
     */
    @FXML
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        if (!lobbyName.getText().equals("")) {
            CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), loggedInUser);
            eventBus.post(msg);
            LOG.info("Request wurde gesendet.");
        } else {
            LOG.info("Leerer Lobbyname wurde abgeschickt! :(");
            // TODO: Implementierung eines Popups, dass Lobbyname nicht leer sein darf oder rotes Feld markieren?
            //  Je nachdem was einfacher ist.
        }
    }
    @Subscribe
    public void onNewChatMessage(NewChatMessage msg) {
        ChatViewPresenter.onNewChatMessage(msg);
    }

    @Subscribe
    public void onChatResponseMessage(ChatResponseMessage msg) {
        if (msg.getChat().getChatId().equals("global") && msg.getSender().equals(loggedInUser.getUsername())) {
            ChatViewPresenter.updateChat(msg.getChat().getMessages());
        }
    }


}
