package de.uol.swp.client.chat;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.user.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatViewPresenter extends AbstractPresenter {
    public static final String fxml = "/fxml/ChatView.fxml";

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    @FXML
    private TextField chatTextField;

    private static ChatService chatService;
    private static UserService userService;

    public ChatViewPresenter(){}

    @FXML
    public void onSendChatButtonPressed() {
        String message;

        message = (String) chatTextField.getText();

        if (message != "") {
            ChatMessage newMsg = new ChatMessage(loggedInUser,"");
            chatService.sendMessage(newMsg);
            LOG.debug("Sending message as User: "+loggedInUser.getUsername());
            ChatMessage newChatMessage = new ChatMessage(loggedInUser, message);

            LOG.debug("new Message to send: "+ message);

            chatTextField.clear();

            chatService.sendMessage(newChatMessage);
        }
    }


    /**
     * Sets new userService.
     *
     * @param newUserService New value of userService.
     */
    public static void setUserService(UserService newUserService) {
        userService = newUserService;
    }

    /**
     * Sets new chatService.
     *
     * @param newChatService New value of chatService.
     */
    public static void setChatService(ChatService newChatService) {
        chatService = newChatService;
    }
}
