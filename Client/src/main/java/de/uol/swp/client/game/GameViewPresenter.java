package de.uol.swp.client.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.client.game.event.GameQuitEvent;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.request.RetrieveAllLobbyUsersRequest;
import de.uol.swp.common.lobby.response.AllLobbyUsersResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZonedDateTime;
import java.util.List;
import javax.swing.*;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author fenja, hashem, marvin
 *
 */

public class GameViewPresenter extends AbstractPresenter {

    private UUID lobbyID;

    private User loggedInUser;
    public static final String fxml = "/fxml/GameView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private static final GameQuitEvent gameQuitMessage = new GameQuitEvent();
    private static SceneManager sceneManager;

    @FXML
    private Pane chatView;
    @FXML
    private ListView<String> usersView;
    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;

    public GameViewPresenter() {

    }

    /*
        showAlert Methode, um Alert Box zu erstellen
         */



    public void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            eventBus.post(gameQuitMessage);
        } else {

        }

    }



    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        ChatViewPresenter chatViewPresenter = new ChatViewPresenter("Game", ChatViewPresenter.THEME.Light, chatService);
        //chatID setzen

        LOG.debug("Got ChatID from Server: " + lobbyID.toString());
        chatViewPresenter.setChatId(lobbyID.toString());
        chatViewPresenter.setloggedInUser(loggedInUser);
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());

    }

    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

    @FXML
    public void onGiveUpButtonPressed(ActionEvent actionEvent) {

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
        RetrieveAllLobbyUsersRequest msg = new RetrieveAllLobbyUsersRequest(userJoinedLobbyMessage.getLobbyName());
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
        RetrieveAllLobbyUsersRequest msg = new RetrieveAllLobbyUsersRequest(createLobbyMessage.getLobbyName());
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
