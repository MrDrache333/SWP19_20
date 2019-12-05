package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.request.RetrieveAllLobbyUsersRequest;
import de.uol.swp.common.lobby.response.AllLobbyUsersResponse;
import de.uol.swp.common.user.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class GameViewPresenter extends AbstractPresenter {
    public static final String fxml = "/fxml/GameView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private static SceneManager sceneManager;
    @FXML
    private ListView<String> usersView;
    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    Boolean aufgeben = false;

    /*
        showAlert Methode, um Alert Box zu erstellen
         */
    public static void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        alert.show();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            sceneManager.showMainScreen(loggedInUser);
        } else {

        }

    }


    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

    @FXML
    public void onGiveUpButtonPressed(ActionEvent actionEvent) {
        aufgeben = true;

        showAlert(Alert.AlertType.CONFIRMATION, " ", "Möchtest du wirklich aufgeben?");

    }

    /**
     * Sobald ein neuer User der Lobby beitritt wird eine RetrieveAllLobbyUsersRequest gesendet.
     *
     * @author Marvin
     * @since Sprint3
     */

    @Subscribe
    public void newUser(UserJoinedLobbyMessage userJoinedLobbyMessage) {
        RetrieveAllLobbyUsersRequest msg = new RetrieveAllLobbyUsersRequest(userJoinedLobbyMessage.getName());
        eventBus.post(msg);
        LOG.debug("newUser: RetrieveAllLobbyRequest gesendet");
    }

    /**
     * Sobald eine neue Lobby erstellt wird eine RetrieveAllLobbyUsersRequest gesendet.
     *
     * @author Marvin
     * @since Sprint3
     */

    @Subscribe
    public void newLobby(CreateLobbyMessage createLobbyMessage) throws InterruptedException {
        RetrieveAllLobbyUsersRequest msg = new RetrieveAllLobbyUsersRequest(createLobbyMessage.getName());
        eventBus.post(msg);
        LOG.debug("newLobby: RetrieveAllLobbyRequest gesendet");
    }

    /**
     * Bei einer AllLobbyUsersResponse wird updateUsersList ausgeführt, wenn es diese Lobby betrifft.
     * Bis auf die Lobby-Überprüfung & Response-Typ quasi äquivalent zu MainMenuPresenter.userList.
     *
     * @author Marvin
     * @since Sprint3
     */
    @Subscribe
    public void userList(AllLobbyUsersResponse allLobbyUsersResponse) {
        Optional<Lobby> lobby = allLobbyUsersResponse.getLobby();
        if (lobby.isPresent()) {
            if (lobby.get().getUsers().contains(loggedInUser)) {
                LOG.debug("Update of user list " + allLobbyUsersResponse.getUsers());
                updateUsersList(allLobbyUsersResponse.getUsers());
            }
        }
    }

    /**
     * Die usersView Liste wird geupdatet.
     * Äquivalent zu MainMenuPresenter.updateUsersList.
     *
     * @author Marvin
     * @since Sprint3
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
