package de.uol.swp.client.lobby;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    //private static final Logger LOG = LogManager.getLogger(LobbyPresenter.class);

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();
    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private String chatID;
    private ChatViewPresenter chatViewPresenter;

    private Map<String, HBox> readyUserList = new TreeMap<>();

    private UUID lobbyID;
    private String lobbyName;
    private User loggedInUser;

    //Eigener Status in der Lobby
    private boolean ownReadyStatus = false;

    @FXML
    private ListView<HBox> usersView;
    @FXML
    private Pane chatView;
    @FXML
    private Button readyButton;

    private ObservableList<HBox> users;

    /**
     * Instantiates a new Lobby presenter.
     *
     * @param loggedInUser the logged in user
     * @param name         the name
     * @param lobbyID      the lobby id
     * @param chatService  the chat service
     */
    public LobbyPresenter(User loggedInUser, String name, UUID lobbyID, ChatService chatService, LobbyService lobbyService, UserService userService) {
        this.loggedInUser = loggedInUser;
        this.lobbyName = name;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    //--------------------------------------
    // FXML METHODS
    //--------------------------------------

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("Lobby", ChatViewPresenter.THEME.Light, chatService);
        //chatID setzen
        chatID = lobbyID.toString();
        LOG.debug("Got ChatID from Server: " + chatID);
        chatViewPresenter.setChatId(chatID);
        chatViewPresenter.setloggedInUser(loggedInUser);
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());

        readyUserList.put(loggedInUser.getUsername(), getHboxFromReadyUser(loggedInUser.getUsername(), false));
        updateUsersList();
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
        lobbyService.setLobbyUserStatus(lobbyName, loggedInUser, ownReadyStatus);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * On new lobby created.
     *
     * @param msg the msg
     */
    @Subscribe
    public void onNewLobbyCreated(CreateLobbyMessage msg) {
        chatID = msg.getChatID().toString();
        LOG.debug("Got ChatID from Server: " + chatID);
        chatViewPresenter.setChatId(chatID);
    }

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
        LOG.debug("Receiving message as User: " + loggedInUser.getUsername() + " for Chat " + msg.getChatId());
        chatViewPresenter.onNewChatMessage(msg);
    }

    /**
     * New user.
     *
     * @param message the message
     */
    @Subscribe
    public void newUser(UserLoggedInMessage message) {
        Platform.runLater(() -> {
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
        Platform.runLater(() -> {
            chatViewPresenter.userLeft(message.getUsername());
        });
    }

    /**
     * On updated lobby ready status message.
     *
     * @param message the message
     */
    @Subscribe
    public void onUpdatedLobbyReadyStatusMessage(UpdatedLobbyReadyStatusMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        if (readyUserList.containsKey(message.getUser().getUsername())) {
            updateReadyUser(message.getUser().getUsername(), message.isReady());
        }
    }

    /**
     * New user.
     *
     * @param message the message
     */
    @Subscribe
    public void newUser(UserJoinedLobbyMessage message) {
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
    public void userLeft(UserLeftLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("User " + message.getLobbyName() + " left the Lobby");
        Platform.runLater(() -> {
            users.remove(message.getLobbyName());
            updateUsersList();
            chatViewPresenter.userLeft(message.getUser().getUsername());
            if (readyUserList.containsKey(message.getUser().getUsername())) {
                readyUserList.remove(message.getUser().getUsername());
                updateUsersList();
            }
        });
    }

    @Subscribe
    public void onGameStartMessage(StartGameMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("Game in lobby " + message.getLobbyName() + " starts.");
    }

    //--------------------------------------
    // PRIVATE METHODS
    //--------------------------------------

    private void updateUsersList() {//List<LobbyUser> userList) {
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
}