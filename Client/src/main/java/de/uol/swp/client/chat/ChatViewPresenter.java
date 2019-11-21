package de.uol.swp.client.chat;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    /**
     * The constant styleSheet.
     */
    public static final String styleSheet = "css/ChatViewPresenter.css";

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    @FXML
    private TextField chatTextField;
    @FXML
    private ListView messageView;
    private static int maxChatMessageWidth;


    //Liste mit formatierten Chatnachrichten
    private static ObservableList<VBox> chatMessages = FXCollections.observableArrayList();
    private static List<ChatMessage> chatMessageHistory = new ArrayList<>();
    @FXML
    private AnchorPane chatViewAnchorPane;

    //Services
    private static ChatService chatService;
    private static UserService userService;

    /**
     * Instantiates a new Chat view presenter.
     */
    public ChatViewPresenter(){}

    //--------------------------------------
    // FXML
    //--------------------------------------
    private EventHandler<KeyEvent> onKeyPressedinchatTextFieldEvent = event -> {
        if (event.getCode() == KeyCode.ENTER) {
            onSendChatButtonPressed();
        }
    };

    /**
     * On new chat message.
     *
     * @param msg the msg
     */
    public static void onNewChatMessage(NewChatMessage msg) {
        Platform.runLater(() -> {
            chatMessageHistory.add(msg.getMessage());
            chatMessages.add(chatMessagetoBox(msg.getMessage()));
        });
    }

    /**
     * On send chat button pressed.
     */
    @FXML
    public void onSendChatButtonPressed() {
        String message;

        message = chatTextField.getText();

        if (message != "") {
            LOG.debug("Sending message as User: "+loggedInUser.getUsername());
            ChatMessage newChatMessage = new ChatMessage(loggedInUser, message);

            LOG.debug("new Message to send: "+ message);

            chatTextField.clear();

            chatService.sendMessage(newChatMessage);
        }
    }

    //--------------------------------------
    // STATIC METHODS
    //--------------------------------------

    /**
     * Creates a HBox with Labels from a given ChatMessage
     *
     * @param msg the ChatMessage
     * @return a HBox with Labels
     */
    private static VBox chatMessagetoBox(ChatMessage msg) {
        String plainMessage = msg.getMessage();
        //Inhalt der HBox festlegen und mit passenden Styles versehen
        Label sender = new Label(msg.getSender().getUsername());
        Label message = new Label(msg.getMessage());
        message.setWrapText(true);
        message.setMaxWidth(maxChatMessageWidth);
        sender.setStyle("-fx-text-fill: lightgrey; -fx-font-size: 12");

        //Je nachdem wer die Nachriht gesendet hat, diese auf der richtigen Seite darstellen
        VBox box = new VBox();
        HBox hbox = new HBox();
        ImageView pb = new ImageView();
        pb.setImage(new Image("/images/pb_template.png"));
        pb.setFitHeight(25);
        pb.setFitWidth(25);

        if (msg.getSender().getUsername().equals(loggedInUser.getUsername())) {
            //Wenn die Nachricht mehrere Zeilen umfasst, dann aendere den Radius der Ecken
            message.setStyle("-fx-background-radius: " + (plainMessage.length() > message.getMaxWidth() / 10 ? "15" : "90") + ";-fx-background-color: #1C6FEE;-fx-text-fill: white; -fx-font-size: 16");
            sender.setText("Du");
            sender.setAlignment(Pos.BOTTOM_RIGHT);
            message.setAlignment(Pos.BOTTOM_RIGHT);
            message.setPadding(new Insets(5, 5, 5, 5));
            /*
            SVGPath indicator = new SVGPath();
            indicator.setContent("M 0 0 c 5 15 20 20 20 20 c -12.8 0 -20 -10 -20.0 -10");
            indicator.setStyle("-fx-padding: 0,0,0,-20; -fx-fill: #1C6FEE; -fx-fit-to-height: 10; -fx-max-height: 10");

             */

            hbox.getChildren().add(message);
            //hbox.getChildren().add(indicator);
            box.alignmentProperty().setValue(Pos.BOTTOM_RIGHT);
            hbox.alignmentProperty().setValue(Pos.BOTTOM_RIGHT);


        } else {
            //Wenn die Nachricht mehrere Zeilen umfasst, dann aendere den Radius der Ecken
            message.setStyle("-fx-background-radius: " + (plainMessage.length() > message.getMaxWidth() / 10 ? "15" : "90") + ";-fx-background-color: #4D4C4F;-fx-text-fill: white; -fx-font-size: 16");
            sender.setAlignment(Pos.BOTTOM_LEFT);
            sender.setPadding(new Insets(0, 0, 0, 40));
            message.setAlignment(Pos.BOTTOM_LEFT);
            message.setPadding(new Insets(8, 8, 8, 8));
            /*
            SVGPath indicator = new SVGPath();
            indicator.setContent("m 10.0 0 c -2.5 7.5 -10.0 10.0 -10.0 10.0 c 6.4 0 10.0 -5.0 10.0 -5.0");
            indicator.setStyle("-fx-padding: 0,-20,0,0; -fx-fill: #4D4C4F;");

             */
            hbox.setSpacing(5);

            //Vorrangegangene Eintraege ggf. bearbeiten
            if (chatMessageHistory.size() > 1) {
                String lastsender = chatMessageHistory.get(chatMessageHistory.size() - 1).getSender().getUsername();
                if (msg.getSender().getUsername().equals(lastsender) && !loggedInUser.getUsername().equals(msg.getSender().getUsername())) {
                    //Letzte Box abändern oder löschen?
                    if (chatMessages.size() >= 1) {
                        VBox tempVBox = chatMessages.get(chatMessages.size() - 1);
                        HBox tempHBox = (HBox) tempVBox.getChildren().get(tempVBox.getChildren().size() - 1);
                        if (tempHBox.getChildren().size() >= 2) {
                            tempHBox.getChildren().remove(0);
                            ImageView tempImageView = new ImageView();
                            tempImageView.setFitWidth(25);
                            tempHBox.getChildren().add(0, tempImageView);
                            tempVBox.getChildren().remove(tempVBox.getChildren().size() - 1);
                            tempVBox.getChildren().add(tempHBox);
                            replaceChatMessage(chatMessages.size() - 1, tempVBox);
                        } else
                            box.getChildren().add(sender);
                    } else
                        box.getChildren().add(sender);
                } else {
                    box.getChildren().add(sender);
                }
            } else
                box.getChildren().add(sender);

            hbox.getChildren().add(pb);
            //hbox.getChildren().add(indicator);
            hbox.getChildren().add(message);
            box.alignmentProperty().setValue(Pos.BOTTOM_LEFT);
            hbox.alignmentProperty().setValue(Pos.BOTTOM_LEFT);
        }
        box.getChildren().add(hbox);
        box.setPadding(new Insets(0, 0, 0, 0));
        box.setSpacing(5);
        return box;
    }

    private static void replaceChatMessage(int index, VBox box) {
        chatMessages.remove(index);
        chatMessages.add(index, box);
    }

    /**
     * Update chat.
     *
     * @param chatMessageList the chat message list
     */
    public static void updateChat(List<ChatMessage> chatMessageList) {
        Platform.runLater(() -> {
            chatMessageList.forEach(msg -> chatMessages.add(chatMessagetoBox(msg)));
            chatMessageHistory.addAll(chatMessageList);
        });
    }

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        updateChatMessages(new ArrayList<>());
        messageView.setItems(chatMessages);
        maxChatMessageWidth = (int) chatViewAnchorPane.getPrefWidth() - 50;
        chatTextField.setOnKeyPressed(onKeyPressedinchatTextFieldEvent);
        chatMessages.addListener((ListChangeListener<VBox>) change -> {
            Platform.runLater(() -> {
                messageView.scrollTo(messageView.getItems().size() - 1);
            });
        });

    }

    //--------------------------------------
    // METHODS
    //--------------------------------------

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


    //--------------------------------------
    // GETTER UND SETTER
    //--------------------------------------

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

    private void updateChatMessages(List<ChatMessage> chatMessageList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (chatMessages == null) {
                chatMessages = FXCollections.observableArrayList();
                messageView.setItems(chatMessages);
            }
            chatMessages.clear();
            chatMessageHistory.clear();
            chatMessageHistory.addAll(chatMessageList);
            chatMessageList.forEach(msg -> chatMessages.add(chatMessagetoBox(msg)));
        });
    }


}
