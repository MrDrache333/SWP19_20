package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.LobbyCreatedMessage;
import de.uol.swp.server.lobby.LobbyManagement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.TreeModelEvent;
import java.io.IOException;

/**
 * @author Paula, Haschem, Ferit
 * @version 0.1
 */

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyView.fxml";

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);


    private String chatID;

    @FXML
    private Pane chatView;

    public LobbyPresenter() {
    }

    @FXML
    public void initialize() throws IOException {
        Pane newChatView = FXMLLoader.load(getClass().getResource(ChatViewPresenter.fxml));
        chatView.getChildren().add(newChatView);
    }

    @Inject
    public LobbyPresenter(EventBus eventBus, LobbyManagement lobbyManagement) {
        setEventBus(eventBus);
    }

    @Subscribe
    public void onNewLobbyCreated(CreateLobbyMessage msg) {
        chatID = msg.getChatID().toString();
        LOG.info("Got em ChatID vong Server");
    }
    @Subscribe
    public void onNewChatMessage(NewChatMessage msg) {
        if(msg.getChatId().equals(chatID)) {
            ChatViewPresenter.onNewChatMessage(msg);
        }
    }

    @Subscribe
    public void onChatResponseMessage(ChatResponseMessage msg) {
        if (msg.getChat().getChatId().equals(chatID) && msg.getSender().equals(loggedInUser.getUsername())) {
            ChatViewPresenter.updateChat(msg.getChat().getMessages());
        }
    }
}




