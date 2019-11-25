package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.response.AllLobbyUsersResponse;
import de.uol.swp.common.user.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GameViewPresenter extends AbstractPresenter {
    public static final String fxml = "/fxml/GameView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    @FXML
    private ListView<String> usersView;
    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    private Lobby lobby;

    /**
     * Sobald ein neuer User der Lobby beitritt wird er zur Liste users hinzugefügt und der ChatViewPresenter geupdatet.
     * Bis auf die Lobby-Überprüfung & Message-Typ quasi äquivalent zu MainMenuPresenter.userList.
     *
     * @author Marvin
     */
    @Subscribe
    public void newUser(UserJoinedLobbyMessage userJoinedLobbyMessage) {
        if (userJoinedLobbyMessage.getName().equals(lobby.getName())) {
            LOG.debug("New user " + userJoinedLobbyMessage.getUser().getUsername() + " logged in");
            Platform.runLater(() -> {
                if (users != null && loggedInUser != null && !loggedInUser.equals(userJoinedLobbyMessage.getUser()))
                    users.add(userJoinedLobbyMessage.getUser().getUsername());
                chatViewPresenter.userJoined(userJoinedLobbyMessage.getUser().getUsername());
            });
        }
    }

    /**
     * Bei einer AllLobbyUsersResponse wird updateUsersList ausgeführt, wenn es diese Lobby betrifft.
     * Bis auf die Lobby-Überprüfung & Response-Typ quasi äquivalent zu MainMenuPresenter.userList.
     *
     * @author Marvin
     */
    @Subscribe
    public void userList(AllLobbyUsersResponse allLobbyUsersResponse) {
        if (allLobbyUsersResponse.getName().equals(lobby.getName())) {
            LOG.debug("Update of user list " + allLobbyUsersResponse.getUsers());
            updateUsersList(allLobbyUsersResponse.getUsers());
        }
    }

    /**
     * Die usersView Liste wird geupdatet.
     * Äquivalent zu MainMenuPresenter.updateUsersList.
     *
     * @author Marvin
     */
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
}
