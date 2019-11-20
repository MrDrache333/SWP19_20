package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.event.ShowLobbyViewEvent;
import de.uol.swp.server.lobby.LobbyManagement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import javax.swing.event.TreeModelEvent;
import java.io.IOException;

/**
 * @author Paula, Haschem, Ferit
 * @version 0.1
 */

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyView.fxml";

    private static final ShowLobbyViewEvent showLobbyViewMessage = new ShowLobbyViewEvent();

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
}




