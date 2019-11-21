package de.uol.swp.client.chat;

import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.user.User;
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
     * Pfad zur zu verwendenen FXML.
     */
    public static final String fxml = "/fxml/ChatView.fxml";
    /**
     * Pfad zum zu verwendenen Stylesheet.
     */
    public static final String styleSheet = "css/ChatViewPresenter.css";
    public static final String styleSheet_dark = "css/ChatViewPresenter-Dark.css";
    public static final String styleSheet_light = "css/ChatViewPresenter-Light.css";

    //Festlegen der Maximalen Chat-Historie
    private static final int MAXCHATMESSAGEHISTORY = 100;
    private static int maxChatMessageWidth;

    public static THEME CHATTHEME = THEME.Light;
    @FXML
    private Label titleLabel;

    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    //FXML elemente
    @FXML
    private AnchorPane chatViewAnchorPane;
    @FXML
    private TextField chatTextField;
    @FXML
    private ListView<VBox> messageView;

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
        if (!msg.getSender().getUsername().equals("server")) {
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
        } else {
            //Wenn die empfangene Nachricht eine ServerMessage ist
            message.setStyle("-fx-text-fill: grey; -fx-background-color: transparent; -fx-font-size: 14");
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(message);
        }
        box.getChildren().add(hbox);
        box.setPadding(new Insets(0, 0, 0, 0));
        box.setSpacing(5);
        return box;
    }


    //Liste mit formatierten Chatnachrichten
    private static ObservableList<VBox> chatMessages = FXCollections.observableArrayList();
    private static List<ChatMessage> chatMessageHistory = new ArrayList<>();


    //Services
    private static ChatService chatService;

    /**
     * Instantiates a new Chat view presenter.
     */
    public ChatViewPresenter() {
    }

    //--------------------------------------
    // FXML
    //--------------------------------------

    //Wenn abei der Benutzung des TextFeldes eine Taste gedrueckt wird
    private EventHandler<KeyEvent> onKeyPressedinchatTextFieldEvent = event -> {
        //Abschicken der Nachricht, wenn die ENTER-Taste gedrueckt wurde
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
            //Loesche alte Nachrichten bei bedarf
            if (chatMessages.size() >= MAXCHATMESSAGEHISTORY) {
                chatMessages.remove(0);
                chatMessageHistory.remove(0);
            }
            //Fuege neue ChatNachricht hinzu
            chatMessageHistory.add(msg.getMessage());
            chatMessages.add(chatMessagetoBox(msg.getMessage()));
        });
    }

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        //Erstellt eine neue Chat-Historie und uebergibt die Liste an die ListView
        updateChatMessages(new ArrayList<>());
        messageView.setItems(chatMessages);
        //Berechnet die maximale Nachrichtenbreite
        maxChatMessageWidth = (int) chatViewAnchorPane.getPrefWidth() - 70;
        //Nachrichten mit der ENTER-Taste abschicken
        chatTextField.setOnKeyPressed(onKeyPressedinchatTextFieldEvent);
        //Automatisches Scrollen zur neuesten Nachricht
        chatMessages.addListener((ListChangeListener<VBox>) change -> Platform.runLater(() -> messageView.scrollTo(messageView.getItems().size() - 1)));

        if (CHATTHEME.equals(THEME.Light)) {
            chatViewAnchorPane.setStyle("-fx-background-color: white");
            titleLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
            messageView.setStyle("-fx-background-color: white;");
        }
    }

    //--------------------------------------
    // STATIC METHODS
    //--------------------------------------

    //Tauscht eine Nachricht im Chat durch eine andere aus
    private static void replaceChatMessage(int index, VBox box) {
        chatMessages.remove(index);
        chatMessages.add(index, box);
    }

    /**
     * On send chat button pressed.
     */
    @FXML
    public void onSendChatButtonPressed() {
        String message;

        message = chatTextField.getText();
        //Pruefe auf leere Nachricht
        if (!message.equals("")) {
            LOG.debug("Sending message as User: " + loggedInUser.getUsername());
            ChatMessage newChatMessage = new ChatMessage(loggedInUser, message);

            LOG.debug("new Message to send: " + message);

            chatTextField.clear();
            chatService.sendMessage(newChatMessage);
        }
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

    //
    public enum THEME {
        Light,
        Dark
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

    //Aktualisiert die ListView indem alle übergebenen Nahrichten dieser hinzugefuegt werden
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
