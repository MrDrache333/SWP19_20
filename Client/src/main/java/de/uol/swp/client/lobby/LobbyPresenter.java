package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.GameManagement;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.lobby.LobbyService;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.OpenSettingsRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final String url = "https://confluence.swl.informatik.uni-oldenburg.de/display/SWP2019B/Spielanleitung?preview=/126746667/126746668/Dominion%20-%20Anleitung%20-%20V1.pdf";

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private ChatViewPresenter chatViewPresenter;

    private Map<String, HBox> readyUserList = new TreeMap<>();

    private UUID lobbyID;
    private String lobbyName;
    private User loggedInUser;
    private UserDTO loggedInUserDTO;
    private EventBus eventBus;
    private Injector injector;

    private boolean ownReadyStatus = false;

    @FXML
    private ListView<HBox> usersView;
    @FXML
    private Pane chatView;
    @FXML
    private Button readyButton;
    @FXML
    ChoiceBox<Integer> chooseMaxPlayer;

    private ObservableList<HBox> userHBoxes;

    private GameManagement gameManagement;

    /**
     * Instanziiert einen neuen LobbyPresenter.
     *
     * @param loggedInUser      der eingeloggte Nutzer
     * @param name              der Name
     * @param lobbyID           die LobbyID
     * @param chatService       der ChatService
     * @param chatViewPresenter der ChatViewPresenter
     * @param lobbyService      der LobbyService
     * @param userService       der UserService
     * @param injector          der Injector
     * @param gameManagement    das GameManagement
     *
     * @author Julia, Keno O, Anna, Darian, Keno S.
     * @since Sprint2
     */
    public LobbyPresenter(User loggedInUser, String name, UUID lobbyID, ChatService chatService, ChatViewPresenter chatViewPresenter, LobbyService lobbyService, UserService userService, Injector injector, GameManagement gameManagement, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyName = name;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.chatViewPresenter = chatViewPresenter;
        this.injector = injector;
        this.gameManagement = gameManagement;
        this.eventBus = eventBus;
        this.loggedInUserDTO = new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail());
    }

    //--------------------------------------
    // FXML METHODS
    //--------------------------------------
    /**
     * Wird aufgerufen wenn der Lobby verlassen Button gedrückt wird.
     *
     * @param event
     * @author Julia, Keno S.
     * @since Sprint3
     */
    @FXML
    public void onLeaveLobbyButtonPressed(ActionEvent event) {
        lobbyService.leaveLobby(lobbyName, loggedInUserDTO, lobbyID);
    }

    /**
     * Die Methode postet ein Request auf den Bus, wenn der Einstellungen-Button gedrückt wird.
     *
     * @param actionEvent
     * @author Anna
     * @since Sprint4
     */
    @FXML
    public void onSettingsButtonPressed(ActionEvent actionEvent) {
        OpenSettingsRequest request = new OpenSettingsRequest(loggedInUser);
        eventBus.post(request);
    }

    /**
     * Intitialisieren des Chats - FXML laden, Controller setzen (muss immer eine eigene Instanz sein)
     * und chatView ind die chatView-Pane dieses Controllers laden.
     * Der eingeloggte User wird zur Userliste hinzugefügt und diese wird aktualisiert.
     * chooserMaxPlayer wird auf den Default Wert (4) gesetzt.
     *
     * @throws IOException die IO-Exception
     * @author Ferit, Keno O, Darian, Timo
     * @since Sprint2
     */
    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        loader.setLocation(getClass().getResource(ChatViewPresenter.fxml));
        loader.setController(chatViewPresenter);
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());
        chatViewPresenter.userJoined(loggedInUser.getUsername());

        lobbyService.retrieveAllUsersInLobby(lobbyID);
        readyUserList.put(loggedInUser.getUsername(), getHboxFromReadyUser(loggedInUser.getUsername(), false));
        updateUsersList();

        chooseMaxPlayer.setValue(4);
    }

    /**
     * Wird aufgerufen wenn der Logout-Button gedrückt wird.
     *
     * @param actionEvent
     * @author Keno S, Keno O.
     * @since Sprint3
     */
    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        lobbyService.leaveAllLobbiesOnLogout(loggedInUserDTO);
        userService.logout(loggedInUser);
    }

    /**
     * Wird aufgerufen wenn der Spielanleitung-Button gedrückt wird.
     *
     * @param actionEvent
     * @author Keno S, Keno O.
     * @since Sprint3
     */
    @FXML
    public void onInstructionsButtonPressed(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Wird aufgerufen wenn der Bereit-Button gedrückt wird.
     * Der Text auf dem Button und der ownReadyStatus werden dabei jeweils geändert.
     *
     * @param actionEvent
     * @author Darian, Keno S, Keno O.
     * @since Sprint3
     */
    @FXML
    public void onReadyButtonPressed(ActionEvent actionEvent) {
        if (ownReadyStatus) {
            readyButton.setText("Bereit");
            ownReadyStatus = false;
        } else {
            readyButton.setText("Nicht mehr Bereit");
            ownReadyStatus = true;
        }
        LOG.debug("Set own ReadyStauts in Lobby " + lobbyID + " to " + (ownReadyStatus ? "Ready" : "Not Ready"));
        lobbyService.setLobbyUserStatus(lobbyName, loggedInUserDTO, ownReadyStatus);
    }

    /**
     * Wird aufgerufen wenn der Wert in der max. Spieler-Box geändert wird.
     *
     * @param actionEvent
     * @author Timo, Rike
     * @since Sprint 3
     */
    @FXML
    public void onMaxPlayerSelected(ActionEvent actionEvent)
    {
        lobbyService.setMaxPlayer(chooseMaxPlayer.getValue(), this.getLobbyID(), this.loggedInUser);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * Ruft Methode auf, die den Status des Nutzers ändert, nachdem der Bereit-Status des Nutzers serverseitig geändert wurde.
     *
     * @param message die UpdatedLobbyReadyStatusMessage
     * @author Darian, Keno O.
     * @since Sprint3
     */
    @Subscribe
    public void onUpdatedLobbyReadyStatusMessage(UpdatedLobbyReadyStatusMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        if (readyUserList.containsKey(message.getUser().getUsername())) {
            LOG.debug("User " + message.getUser().getUsername() + " changed his status to " + (message.isReady() ? "Ready" : "Not Ready") + " in Lobby " + lobbyID);
            updateReadyUser(message.getUser().getUsername(), message.isReady());
        }
    }

    /**
     * Reaktion auf die AllOnlineUsersInLobbyResponse vom Server.
     * Die Userliste wird aktualisiert.
     *
     * @param response die AllOnlineUsersInLobbyResponse
     * @author Keno O.
     * @since Sprint3
     */

    @Subscribe
    private void onReceiveAllUsersInLobby(AllOnlineUsersInLobbyResponse response) {
        if (response.getLobbyID().equals(lobbyID)) {
            readyUserList = new TreeMap<>();
            response.getUsers().forEach(e -> {
                readyUserList.put(e.getUsername(), getHboxFromReadyUser(e.getUsername(), response.getStatus(e)));
            });
            updateUsersList();
        }
    }

    /**
     * Aktualisiert den loggedInUser sowie die Userliste.
     *
     * @param message die UpdatedUserMessage
     * @author Julia, Anna
     * @since Sprint4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
            loggedInUserDTO = (UserDTO) message.getUser();
            LOG.debug("User " + message.getOldUser().getUsername() + " changed his name to " + message.getUser().getUsername());
        }
        Platform.runLater(() -> {
            if (readyUserList.containsKey(message.getOldUser().getUsername())){
                userLeftLobby(message.getOldUser().getUsername());
                readyUserList.put(message.getUser().getUsername(), getHboxFromReadyUser(message.getUser().getUsername(), false));
                updateUsersList();
                chatViewPresenter.userJoined(message.getUser().getUsername());
            }
        });
    }

    /**
     * Deaktivieren der Max. Spieler ChoiceBox, sofern der eingeloggte Nutzer nicht der Lobbyowner ist.
     *
     * @param msg die SetMaxPlayerMessage
     * @author Timo, Rike
     * @since Sprint 3
     */
    @Subscribe
    public void onSetMaxPlayerMessage(SetMaxPlayerMessage msg) {
        Platform.runLater(() -> {
            if (!chooseMaxPlayer.getValue().equals(msg.getMaxPlayer())){
                chooseMaxPlayer.setValue(msg.getMaxPlayer());
            }
            if (!msg.getOwner().equals(loggedInUser)) {
                chooseMaxPlayer.setDisable(true);
            } else {
                chooseMaxPlayer.setDisable(false);
            }
        });
    }

    /**
     * GameView wird aufgerufen.
     *
     * @param message die StartGameMessage
     * @author Darian, Keno O.
     * @since Sprint3
     */
    @Subscribe
    public void onGameStartMessage(StartGameMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("Game in lobby " + message.getLobbyName() + " starts.");
        gameManagement.showGameView();
    }

    /**
     * Nachdem der Nutzer sich ausgeloggt hat, wird er auch aus der Lobbyliste gelöscht.
     *
     * @param message die UserLoggedOutMessage
     * @author Darian
     * @since Sprint3
     */
    @Subscribe
    public void onUserLoggedOutMessage(UserLoggedOutMessage message) {
        userLeftLobby(message.getUsername());
    }

    /**
     * User wird aus der Liste entfernt, wenn er seinen Account gelöscht hat
     *
     * @param message die UserDroppedMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void onUserDroppedMessage(UserDroppedMessage message) {
        userLeftLobby(message.getUser().getUsername());
    }

    /**
     * Ein neuer Nutzer tritt der Lobby bei, die Userliste der Lobby wird aktualisiert und eine Nachricht im Chat angezeigt.
     *
     * @param message die UserJoinedLobbyMessage
     * @author Darian, Keno O.
     * @since Sprint3
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("New user " + message.getUser() + " logged in");
        Platform.runLater(() -> {
            if (readyUserList != null && loggedInUser != null && !loggedInUser.toString().equals(message.getLobbyName())) {
                readyUserList.put(message.getUser().getUsername(), getHboxFromReadyUser(message.getUser().getUsername(), false));
                updateUsersList();
                chatViewPresenter.userJoined(message.getUser().getUsername());
            }
        });
    }

    /**
     * Ein Nutzer verlässt die Lobby, die Userliste wird aktualisiert.
     *
     * @param message die UserLeftLobbyMessage
     * @author Darian, Keno O, Julia
     * @since Sprint3
     */
    @Subscribe
    public void onUserLeftLobbyMessage(UserLeftLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("User " + message.getUser().getUsername() + " left the Lobby");
        userLeftLobby(message.getUser().getUsername());
    }

    //--------------------------------------
    // PRIVATE METHODS
    //--------------------------------------

    /**
     * Nutzer wird aus der Userliste der Lobby gelöscht und eine Nachricht im Chat angezeigt.
     *
     * @param username der Username
     * @author Darian, Keno O.
     * @since Sprint3
     */
    private void userLeftLobby(String username) {
        if (readyUserList.get(username) != null) {
            Platform.runLater(() -> {
                readyUserList.remove(username);
                updateUsersList();
                chatViewPresenter.userLeft(username);
                if (readyUserList.containsKey(username)) {
                    readyUserList.remove(username);
                    updateUsersList();
                }
            });
        }
    }

    /**
     * Geänderte Userliste wird angezeigt.
     *
     *@author Darian, Keno O.
     *@since Sprint3
     */

    private void updateUsersList() {
        Platform.runLater(() -> {
            if (userHBoxes == null) {
                userHBoxes = FXCollections.observableArrayList();
                usersView.setItems(userHBoxes);
            }
            userHBoxes.clear();
            userHBoxes.addAll(getAllHBoxes());
        });
    }


    /**
     * Erstellt eine neue HBox für einen Nutzer.
     *
     * @param username der Nutzer
     * @param status   der aktuelle Bereit-Status
     * @return die generierte HBox
     * @author Darian
     * @since Sprint3
     */
    private HBox getHboxFromReadyUser(String username, boolean status) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(5);
        Circle circle = new Circle(12.0f, status ? Paint.valueOf("green") : Paint.valueOf("red"));
        Label usernameLabel = new Label(username);
        box.getChildren().add(circle);
        box.getChildren().add(usernameLabel);
        return box;
    }

    /**
     * Nutzer wird erst aus der Userliste gelöscht und dann mit seinem neuen Status wieder hinzugefügt.
     *
     * @param userName der Username
     * @param status der aktuelle Bereit-Status
     * @author Darian
     * @since Sprint3
     */
    private void updateReadyUser(String userName, boolean status) {
        if (readyUserList.containsKey(userName)) {
            readyUserList.remove(userName);
            readyUserList.put(userName, getHboxFromReadyUser(userName, status));
            updateUsersList();
        }
    }

    /**
     * Konvertiert die HBox Map in eine ArrayList.
     *
     * @return alle HBoxes als ArrayList
     * @author Darian, Keno S.
     * @since Sprint3
     */
    private ArrayList<HBox> getAllHBoxes() {
        return new ArrayList<>(readyUserList.values());
    }

    //--------------------------------------
    // GETTER AND SETTER
    //--------------------------------------

    /**
     * Gibt die LobyID zurück.
     *
     * @return die LobbyID
     * @author Darian
     * @since Sprint3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gibt den Lobbynamen zurück.
     *
     * @return den Lobbynamen
     * @author Darian
     * @since Sprint3
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Gibt den LobbyService zurück.
     *
     * @return den LobbyService
     * @author Ferit
     * @since Sprint3
     */
    public LobbyService getLobbyService() {
        return lobbyService;
    }
}