package de.uol.swp.client.chat;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.client.chat.ChatService;
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
    public void onSendChatButtonPressed() {
        String message;

        message = (String) chatTextField.getText();

        if (message != "") {
            ChatMessage newChatMessage = new ChatMessage(loggedInUser, message);

            LOG.debug("new Message to send: "+ message);

            chatTextField.clear();

            chatService.sendMessage(newChatMessage);
        }
    }
}
