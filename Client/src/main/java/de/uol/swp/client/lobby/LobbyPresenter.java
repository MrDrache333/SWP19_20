package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.chat.Chat;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Paula, Haschem, Ferit, Anna
 * @version 0.2
 */

public class LobbyPresenter extends AbstractPresenter {

    @FXML
    private Pane chatView;

    public static final String fxml = "/fxml/LobbyView.fxml";
    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();
    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private String chatID;
    private ChatViewPresenter chatViewPresenter;

    private UUID lobbyID;
    private String name;


    public LobbyPresenter(String name, UUID lobbyID, ChatService chatService){
        this.name = name;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
    }

    public UUID getLobbyID(){
        return lobbyID;
    }

    public String getName(){
        return name;
    }

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
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /*@Subscribe
    public void onNewLobbyCreated(CreateLobbyMessage msg) {
        chatID = msg.getChatID().toString();
        LOG.debug("Got ChatID from Server: " + chatID);
        chatViewPresenter.setChatId(chatID);
    }*/

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