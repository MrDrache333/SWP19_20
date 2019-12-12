package de.uol.swp.client.lobby;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.GameManagement;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.lobby.LobbyService;
import de.uol.swp.common.lobby.message.StartGameMessage;
import de.uol.swp.common.lobby.message.UpdatedLobbyReadyStatusMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * The type Lobby presenter.
 *
 * @author Paula, Haschem, Ferit, Anna
 * @version 0.2
 */
public class LobbyPresenter extends AbstractPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final String url = "https://confluence.swl.informatik.uni-oldenburg.de/display/SWP2019B/Spielanleitung?preview=/126746667/126746668/Dominion%20-%20Anleitung%20-%20V1.pdf";

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();
    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private ChatViewPresenter chatViewPresenter;

    private Map<String, HBox> readyUserList = new TreeMap<>();

    private UUID lobbyID;
    private String lobbyName;
    private User loggedInUser;
    private Injector injector;


    //Eigener Status in der Lobby
    private boolean ownReadyStatus = false;

    @FXML
    private ListView<HBox> usersView;
    @FXML
    private Pane chatView;
    @FXML
    private Button readyButton;

    /**
     * @author Timo, Rike
     * @since Sprint 3
     * @implNote Anlegen des Chosebox-Objektes
     */
    @FXML
    ChoiceBox<Integer> choseMaxPlayer;

    private ObservableList<HBox> users;

    private GameManagement gameManagement;

    /**
     * Instantiates a new Lobby presenter.
     *
     * @param loggedInUser      the logged in user
     * @param name              the name
     * @param lobbyID           the lobby id
     * @param chatService       the chat service
     * @param chatViewPresenter the chat view presenter
     * @param lobbyService      the lobby service
     * @param userService       the user service
     * @param injector          the injector
     * @param gameManagement    the game management
     */
    public LobbyPresenter(User loggedInUser, String name, UUID lobbyID, ChatService chatService, ChatViewPresenter chatViewPresenter, LobbyService lobbyService, UserService userService, Injector injector, GameManagement gameManagement) {
        this.loggedInUser = loggedInUser;
        this.lobbyName = name;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.chatViewPresenter = chatViewPresenter;
        this.injector = injector;
        this.gameManagement = gameManagement;
    }

    //--------------------------------------
    // FXML METHODS
    //--------------------------------------

    @FXML
    public void onLeaveLobbyButtonPressed(ActionEvent event) {
        lobbyService.leaveLobby(lobbyName, loggedInUser, lobbyID);
    }

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    public void initialize() throws IOException {
        //FXML laden
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        loader.setLocation(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());
        chatViewPresenter.userJoined(loggedInUser.getUsername());

        readyUserList.put(loggedInUser.getUsername(), getHboxFromReadyUser(loggedInUser.getUsername(), false));
        updateUsersList();
        //Setzt choseMaxPlayer auf den Default-Wert
        choseMaxPlayer.setValue(4);
    }

    /**
     * On logout button pressed.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

    /**
     * On instructions button pressed.
     *
     * @param actionEvent the action event
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
     * On ready button pressed.
     *
     * @param actionEvent the action event
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
        lobbyService.setLobbyUserStatus(lobbyName, loggedInUser, ownReadyStatus);
    }

    /**
     * @author Timo, Rike
     * @Since Sprint 3
     * @param actionEvent the action event
     */
    @FXML
    public void onMaxPlayerSelected(ActionEvent actionEvent)
    {
        lobbyService.setMaxPlayer(choseMaxPlayer.getValue(), this.getLobbyID(), this.loggedInUser);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * On updated lobby ready status message.
     *
     * @param message the message
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
     * On game start message.
     *
     * @param message the message
     */
    @Subscribe
    public void onGameStartMessage(StartGameMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("Game in lobby " + message.getLobbyName() + " starts.");
        gameManagement.showGameView();
    }

    /**
     * User left.
     *
     * @param message the message
     */
    @Subscribe
    public void onUserLoggedOutMessage(UserLoggedOutMessage message) {
        userLeftLobby(message.getUsername());
    }

    /**
     * New user.
     *
     * @param message the message
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("New user " + message.getUser() + " logged in");
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.toString().equals(message.getLobbyName())) {
                readyUserList.put(message.getUser().getUsername(), getHboxFromReadyUser(message.getUser().getUsername(), false));
                users.add(readyUserList.get(message.getUser().getUsername()));
                updateUsersList();
                chatViewPresenter.userJoined(message.getUser().getUsername());
            }
        });
    }

    /**
     * User left.
     *
     * @param message the message
     */
    @Subscribe
    public void onUserLeftLobbyMessage(UserLeftLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("User " + message.getLobbyName() + " left the Lobby");
        userLeftLobby(message.getUser().getUsername());
        chatViewPresenter.userLeft(message.getUser().getUsername());
    }

    //--------------------------------------
    // PRIVATE METHODS
    //--------------------------------------

    private void userLeftLobby(String username) {
        if (users.contains(username)) {
            Platform.runLater(() -> {
                users.remove(username);
                updateUsersList();
                chatViewPresenter.userLeft(username);
                if (readyUserList.containsKey(username)) {
                    readyUserList.remove(username);
                    updateUsersList();
                }
            });
        }
    }

    private void updateUsersList() {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (users == null) {
                users = FXCollections.observableArrayList();
                usersView.setItems(users);
            }
            users.clear();
            users.addAll(getAllHBoxes());
        });
    }


    /**
     * Creates a new HBox for a User
     *
     * @param username The User
     * @param status   The actual Status
     * @return The generated HBox
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

    private void updateReadyUser(String userName, boolean status) {
        if (readyUserList.containsKey(userName)) {
            readyUserList.remove(userName);
            readyUserList.put(userName, getHboxFromReadyUser(userName, status));
            updateUsersList();
        }
    }

    /**
     * Converts the HBox Map to a ArrayList
     *
     * @return All HBoxes as ArrayList
     */
    private ArrayList<HBox> getAllHBoxes() {
        ArrayList<HBox> list = new ArrayList<>(readyUserList.values());
        return list;
    }

    //--------------------------------------
    // GETTER AND SETTER
    //--------------------------------------

    /**
     * Gets lobby id.
     *
     * @return the lobby id
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gets lobby name.
     *
     * @return the lobby name
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Gets lobby service
     *
     * @return the lobby service
     */
    public LobbyService getLobbyService() { return lobbyService; }
}