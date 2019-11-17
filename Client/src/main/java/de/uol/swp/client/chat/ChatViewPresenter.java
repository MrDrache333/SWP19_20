package de.uol.swp.client.chat;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ChatViewPresenter extends AbstractPresenter {
    public static final String fxml = "/fxml/ChatView.fxml";

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private ObservableList<String> messages;

    private ChatMessage currentMessage;

    @FXML
    private ListView<String> chatView;

    @Subscribe
    public void receiveChat (NewChatMessage message) {
        LOG.debug("Update of chat ");
        messages.add(message.getMessage().getMessage());
    }

    private void updateUsersList(List<ChatMessage> messageList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (messages == null) {
                messages = FXCollections.observableArrayList();
                chatView.setItems(messages);
            }
            messages.clear();
            messageList.forEach(u -> messages.add(u.getMessage()));
        });
    }


}
