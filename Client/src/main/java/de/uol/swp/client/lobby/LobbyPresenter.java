package de.uol.swp.client.lobby;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.UpdatedLobbyReadyStatusMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.user.dto.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

/**
 * @author Paula, Haschem, Ferit
 * @version 0.1
 */

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final String url = "https://confluence.swl.informatik.uni-oldenburg.de/display/SWP2019B/Spielanleitung?preview=/126746667/126746668/Dominion%20-%20Anleitung%20-%20V1.pdf";
    //private static final Logger LOG = LogManager.getLogger(LobbyPresenter.class);

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();
    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private String chatID;
    private ChatViewPresenter chatViewPresenter;

    private UUID lobbyID;
    private String lobbyName;

    //Eigener Status in der Lobby
    private boolean ownReadyStatus = false;

    @FXML
    private ListView<HBox> usersView;
    @FXML
    private Pane chatView;
    @FXML
    private Button readyButton;
    @FXML
    private Circle circle;
    @FXML
    private Label usernameLabel;

    //TODO Liste in eine HBox verwandeln. Ähnlich wie beim Chat. Warum? Damit Der Name und ein Icon mit Farbe platz drin findet :)
    private ObservableList<HBox> users;

    public LobbyPresenter() {
    }

    public LobbyPresenter(String lobbyName, UUID lobbyID){
        this.chatService = chatService;
        this.lobbyName = lobbyName;
        this.lobbyID = lobbyID;
    }

    public UUID getLobbyID(){
        return lobbyID;
    }

    public String getLobbyName(){
        return lobbyName;
    }

    //--------------------------------------
    // FXML METHODS
    //--------------------------------------

    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("Lobby", ChatViewPresenter.THEME.Light, chatService);
        //chatID setzen
        chatID = lobbyID.toString();
        LOG.debug("Got ChatID from Server: " + chatID);
        chatViewPresenter.setChatId(chatID);
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());

        usernameLabel = new Label("BEISPIELTEXT");//loggedInUser.getUsername());
        circle = new Circle(12.0f, Paint.valueOf("red"));
        HBox box = new HBox(circle, usernameLabel);
        users = FXCollections.observableArrayList();
        users.add(box);    //loggedInUser.getUsername());
        updateUsersList();
    }

    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        userService.logout(loggedInUser);
    }

    @FXML
    public void onInstructionsButtonPressed(ActionEvent actionEvent) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    @FXML
    public void onReadyButtonPressed(ActionEvent actionEvent){
        if (ownReadyStatus == true){
            readyButton.setText("Bereit");
            ownReadyStatus = false;
        }
        else{
            readyButton.setText("Nicht mehr Bereit");
            ownReadyStatus = true;
        }
        lobbyService.setLobbyUserStatus(lobbyName, loggedInUser, ownReadyStatus);
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

    @Subscribe
    public void onUpdatedLobbyReadyStatusMessage(UpdatedLobbyReadyStatusMessage message) {
        if(message.getLobbyID()==lobbyID && users.contains(message.getName())){

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
            if (users != null && loggedInUser != null && !loggedInUser.toString().equals(message.getName())){
                //users.add(message.getName());
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
        LOG.debug("User " + message.getName() + " left the Lobby");
        Platform.runLater(() -> {
            users.remove(message.getName());
            updateUsersList();
            chatViewPresenter.userLeft(message.getUser().getUsername());
        });
    }

    //--------------------------------------
    // PRIVATE METHODS
    //--------------------------------------

    private void updateUsersList(){//List<LobbyUser> userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            usersView.setItems(users);
        });
    }
}