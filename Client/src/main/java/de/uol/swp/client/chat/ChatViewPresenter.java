package de.uol.swp.client.chat;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Chat view presenter.
 */
public class ChatViewPresenter extends AbstractPresenter {
    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/ChatView.fxml";

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    @FXML
    private TextField chatTextField;

    private static ObservableList<String> chatMessages;
    @FXML
    private ListView messageView;

    private static ChatService chatService;
    private static UserService userService;

    /**
     * Instantiates a new Chat view presenter.
     */
    public ChatViewPresenter(){}

    public static void onNewChatMessage(NewChatMessage msg) {
        Platform.runLater(() -> chatMessages.add(msg.getMessage().getMessage()));
    }

    /**
     * Setlogged in user.
     *
     * @param user the user
     */
    public static void setloggedInUser(User user) {
        loggedInUser = user;
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

    /**
     * Sets logged in user.
     *
     * @param user the user
     */
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    @FXML
    public void initialize() {
        updateChatMessages(new ArrayList<>());
    }

    /**
     * On send chat button pressed.
     */
    @FXML
    public void onSendChatButtonPressed() {
        String message;

        message = chatTextField.getText();

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

    private void updateChatMessages(List<ChatMessage> chatMessageList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (chatMessages == null) {
                chatMessages = FXCollections.observableArrayList();
                messageView.setItems(chatMessages);
            }
            chatMessages.clear();
            chatMessageList.forEach(msg -> chatMessages.add(msg.getMessage()));
        });
    }


}
