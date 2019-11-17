package de.uol.swp.client.chat;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.message.NewChatMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatViewPresenter extends AbstractPresenter {
    public static final String fxml = "/fxml/ChatView.fxml";

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    @FXML
    private TextField chatTextField;

    @FXML
    public void onSendChatButtonPressed(ActionEvent actionEvent) {
        String message;

        message = chatTextField.getText();

        if (message != "") {
            ChatMessage newMsg = new ChatMessage(loggedInUser);
            chatService.sendMessage(newMsg);
        }
    }
}
