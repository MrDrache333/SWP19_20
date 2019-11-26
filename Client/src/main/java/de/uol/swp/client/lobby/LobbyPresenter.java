package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.user.dto.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.server.lobby.LobbyManagement;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Paula, Haschem, Ferit
 * @version 0.1
 */

public class LobbyPresenter extends AbstractPresenter {

    @FXML
    private Pane chatView;

    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final String url = "https://confluence.swl.informatik.uni-oldenburg.de/display/SWP2019B/Spielanleitung?preview=/126746667/126746668/Dominion%20-%20Anleitung%20-%20V1.pdf";
    //private static final Logger LOG = LogManager.getLogger(LobbyPresenter.class);

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();
    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private String chatID;
    private ChatViewPresenter chatViewPresenter;

    @FXML
    private ListView<String> usersView;

    private ObservableList<String> users;

    public LobbyPresenter() {
    }

    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("Lobby", ChatViewPresenter.THEME.Light, chatService);
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
    }

    public void onLogoutButtonPressed(ActionEvent actionEvent) {

        userService.logout(loggedInUser);
    }

    public void onInstructionsButtonPressed(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * New user.
     *
     * @param message the message
     */
    @Subscribe
    public void newUser(UserJoinedLobbyMessage message) {
        LOG.debug("New user " + message.getUser() + " logged in");
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.toString().equals(message.getName()))
                users.add(message.getName());
            //chatViewPresenter.userJoined(message.getUsername());
        });
    }

    /**
     * User left.
     *
     * @param message the message
     */
    @Subscribe
    public void userLeft(UserLeftLobbyMessage message) {
        LOG.debug("User " + message.getName() + " logged out");
        Platform.runLater(() -> {
            if (users.contains(message.getName())) {
                users.remove(message.getName());
                //chatViewPresenter.userLeft(message.getUsername());
            }
        });
    }

    /**
     * User list.
     *
     * @param allUsersResponse the all users response
     */
    @Subscribe
    public void userList(AllOnlineUsersInLobbyResponse allUsersResponse) {
        LOG.debug("Update of user list " + allUsersResponse.getUsers());
        updateUsersList(allUsersResponse.getUsers());
    }

    private void updateUsersList(LobbyDTO userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (users == null) {
                users = FXCollections.observableArrayList();
                usersView.setItems(users);
            }
            users.clear();
            userList.getUsers().forEach(u -> users.add(u.getUsername()));
        });
    }

}



    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    @Subscribe
    public void onNewLobbyCreated(CreateLobbyMessage msg) {
        chatID = msg.getChatID().toString();
        LOG.debug("Got ChatID from Server: " + chatID);
        chatViewPresenter.setChatId(chatID);
    }

    @Subscribe
    public void onChatResponseMessage(ChatResponseMessage msg) {
        chatViewPresenter.onChatResponseMessage(msg);
    }

    @Subscribe
    public void onNewChatMessage(NewChatMessage msg) {
        LOG.debug("Sending message as User: " + loggedInUser.getUsername() + " from LobbyChat.");
        chatViewPresenter.onNewChatMessage(msg);

    }

    @Subscribe
    public void newUser(UserLoggedInMessage message) {
        Platform.runLater(() -> {
            chatViewPresenter.userJoined(message.getUsername());
        });
    }

    @Subscribe
    public void userLeft(UserLoggedOutMessage message) {
        Platform.runLater(() -> {
            chatViewPresenter.userLeft(message.getUsername());
        });
    }
}