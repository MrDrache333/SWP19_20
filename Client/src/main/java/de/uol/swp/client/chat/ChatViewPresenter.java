package de.uol.swp.client.chat;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private static final String styleSheet = "css/ChatViewPresenter.css";
    /**
     * The constant styleSheet_dark.
     */
    private static final String styleSheet_dark = "css/ChatViewPresenter-Dark.css";
    /**
     * The constant styleSheet_light.
     */
    private static final String styleSheet_light = "css/ChatViewPresenter-Light.css";
    private final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    //Maximum ChatMessages to store localy and in ListView
    private final int MAXCHATMESSAGEHISTORY = 100;
    //Color of the Message Label (OWN MESSAGES) (Background and Text) in both Themes
    private final String CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME_LIGHT = "#1C6FEE";
    private final String CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME_DARK = "#1C6FEE";
    private final String CHATMESSAGEBUBBLETEXTCOLOR_ME_LIGHT = "white";
    private final String CHATMESSAGEBUBBLETEXTCOLOR_ME_DARK = "white";
    //Color of the Message Label (OTHER MESSAGES) (Background and Text) in both Themes
    private final String CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER_LIGHT = "#DFDEE5";
    private final String CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER_DARK = "#4D4C4F";
    private final String CHATMESSAGEBUBBLETEXTCOLOR_OTHER_LIGHT = "black";
    private final String CHATMESSAGEBUBBLETEXTCOLOR_OTHER_DARK = "white";
    //Color of the Send-Button (Background and Text) in both Themes
    private final String CHATMESSAGESENDBUTTONBACKGROUNDCOLOR_LIGHT = "#1C6FEE";
    private final String CHATMESSAGESENDBUTTONBACKGROUNDCOLOR_DARK = "#1C6FEE";
    private final String CHATMESSAGESENDBUTTONTEXTCOLOR_LIGHT = "white";
    private final String CHATMESSAGESENDBUTTONTEXTCOLOR_DARK = "white";
    //Max Width for one Message (Will be calculated automaticly)
    private int maxChatMessageWidth;
    //The THeme to use
    private THEME CHATTHEME;
    //ID of the Chat (FOr filtering Messages)
    private String chatId;
    //Name of the Chat (FOr the Title Label)
    private String chatTitle;

    private ChatMessage lastMessage;

    private Injector injector;
    //Colors in the actual Theme
    private String CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME;
    private String CHATMESSAGEBUBBLETEXTCOLOR_ME;
    private String CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER;
    private String CHATMESSAGEBUBBLETEXTCOLOR_OTHER;
    private String CHATMESSAGESENDBUTTONBACKGROUNDCOLOR;
    private String CHATMESSAGESENDBUTTONTEXTCOLOR;

    //FXML GUI Elements
    @FXML
    private Label titleLabel;
    @FXML
    private AnchorPane chatViewAnchorPane;
    @FXML
    private TextField chatTextField;
    @FXML
    private ListView<VBox> messageView;

    private ChatService chatService;
    //Liste mit formatierten Chatnachrichten
    private ObservableList<VBox> chatMessages = FXCollections.observableArrayList();
    private List<ChatMessage> chatMessageHistory = new ArrayList<>();
    //Wenn bei der Benutzung des TextFeldes eine Taste gedrueckt wird
    private EventHandler<KeyEvent> onKeyPressedinchatTextFieldEvent = event -> {
        //Abschicken der Nachricht, wenn die ENTER-Taste gedrueckt wurde
        if (event.getCode() == KeyCode.ENTER) {
            onSendChatButtonPressed();
        }
    };

    /**
     * Instantiates a new Chat view presenter.
     *
     * @param chatTitle   the name
     * @param theme       the theme
     * @param chatService the chat service
     */
    public ChatViewPresenter(String chatTitle, UUID chatId, User currentUser, THEME theme, ChatService chatService, Injector injector) {
        this.chatTitle = chatTitle;
        this.CHATTHEME = theme;
        this.chatService = chatService;
        this.chatId = chatId.toString();
        this.loggedInUser = currentUser;
        this.injector = injector;

        setTheme(CHATTHEME);
    }

    /**
     * Instantiates a new Chat view presenter.
     *
     * @param chatTitle   the name
     * @param theme       the theme
     * @param chatService the chat service
     */
    public ChatViewPresenter(String chatTitle, String chatId, User currentUser, THEME theme, ChatService chatService) {
        this.chatTitle = chatTitle;
        this.CHATTHEME = theme;
        this.chatService = chatService;
        this.chatId = chatId;
        this.loggedInUser = currentUser;

        setTheme(CHATTHEME);
    }

    private void setTheme(THEME theme) {
        //Set the right Colors for the choosen Theme
        if (theme.equals(THEME.Light)) {
            CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME = CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME_LIGHT;
            CHATMESSAGEBUBBLETEXTCOLOR_ME = CHATMESSAGEBUBBLETEXTCOLOR_ME_LIGHT;
            CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER = CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER_LIGHT;
            CHATMESSAGEBUBBLETEXTCOLOR_OTHER = CHATMESSAGEBUBBLETEXTCOLOR_OTHER_LIGHT;
            CHATMESSAGESENDBUTTONBACKGROUNDCOLOR = CHATMESSAGESENDBUTTONBACKGROUNDCOLOR_LIGHT;
            CHATMESSAGESENDBUTTONTEXTCOLOR = CHATMESSAGESENDBUTTONTEXTCOLOR_LIGHT;
        } else {
            CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME = CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME_DARK;
            CHATMESSAGEBUBBLETEXTCOLOR_ME = CHATMESSAGEBUBBLETEXTCOLOR_ME_DARK;
            CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER = CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER_DARK;
            CHATMESSAGEBUBBLETEXTCOLOR_OTHER = CHATMESSAGEBUBBLETEXTCOLOR_OTHER_DARK;
            CHATMESSAGESENDBUTTONBACKGROUNDCOLOR = CHATMESSAGESENDBUTTONBACKGROUNDCOLOR_DARK;
            CHATMESSAGESENDBUTTONTEXTCOLOR = CHATMESSAGESENDBUTTONTEXTCOLOR_DARK;
        }
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

        //Set the choosen Chat Name in the Title
        titleLabel.setText(chatTitle.toUpperCase() + " CHAT");

        //Nötige Styles laden und uebernehmen
        chatViewAnchorPane.getStylesheets().add(styleSheet);
        if (CHATTHEME.equals(ChatViewPresenter.THEME.Light)) {
            LOG.debug("Loading Light Theme");
            chatViewAnchorPane.getStylesheets().add(styleSheet_light);
            //chatViewAnchorPane.setStyle("-fx-background-color: white");
            //titleLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
        } else if (CHATTHEME.equals(THEME.Dark)) {
            LOG.debug("Loading Dark Theme");
            chatViewAnchorPane.getStylesheets().add(styleSheet_dark);
        }
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * On new chat message.
     *
     * @param msg the msg
     */
    @Subscribe
    private void onNewChatMessage(NewChatMessage msg) {
        if (!chatId.equals("") && msg.getChatId().equals(chatId) && (lastMessage == null || !(msg.getMessage().getSender().getUsername().equals("server") && lastMessage.getSender().getUsername().equals("server") && msg.getMessage().getMessage().equals(lastMessage.getMessage())))) {
            Platform.runLater(() -> {
                //Loesche alte Nachrichten bei bedarf
                if (chatMessages.size() > MAXCHATMESSAGEHISTORY) {
                    chatMessages.remove(0);
                    chatMessageHistory.remove(0);
                }
                //Fuege neue ChatNachricht hinzu
                chatMessageHistory.add(msg.getMessage());
                chatMessages.add(chatMessagetoBox(msg.getMessage()));
            });
            lastMessage = msg.getMessage();
        }
    }

    /**
     * On chat response message.
     *
     * @param msg the msg
     */
    @Subscribe
    private void onChatResponseMessage(ChatResponseMessage msg) {
        if (msg.getChat().getChatId().equals(chatId) && msg.getSender().equals(loggedInUser.getUsername())) {
            updateChat(msg.getChat().getMessages());
        }
    }

    //--------------------------------------
    // METHODS
    //--------------------------------------


    /**
     * User joined.
     *
     * @param username the username
     */
//Display a Message when a User joined the Chat
    public void userJoined(String username) {
        if (!chatId.equals(""))
            onNewChatMessage(new NewChatMessage(chatId, new ChatMessage(new UserDTO("server", "", ""), username + " ist " + (chatId.equals("global") ? "dem Chat" : "der Lobby") + " beigereten")));
    }

    /**
     * User left.
     *
     * @param username the username
     */
//Display a Message when a User left the Chat
    public void userLeft(String username) {
        if (!chatId.equals(""))
            onNewChatMessage(new NewChatMessage(chatId, new ChatMessage(new UserDTO("server", "", ""), username + " hat " + (chatId.equals("global") ? "den Chat" : "die Lobby") + " verlassen")));
    }

    /**
     * On send chat button pressed.
     */
    @FXML
    private void onSendChatButtonPressed() {
        if (chatId.equals("")) return;
        String message;

        message = chatTextField.getText();
        //Pruefe auf leere Nachricht
        if (!message.equals("")) {
            LOG.debug("Sending new Chat message: User= " + loggedInUser.getUsername() + " Msg= " + message + " ChatID= " + chatId);
            ChatMessage newChatMessage = new ChatMessage(loggedInUser, message);

            LOG.debug("new Message to send: " + message);

            chatTextField.clear();
            this.chatService.sendMessage(chatId, newChatMessage);
        }
    }

    /**
     * Creates a HBox with Labels from a given ChatMessage
     *
     * @param msg the ChatMessage
     * @return a HBox with Labels
     */
    private VBox chatMessagetoBox(ChatMessage msg) {
        String plainMessage = msg.getMessage();
        //Inhalt der HBox festlegen und mit passenden Styles versehen
        Label sender = new Label(msg.getSender().getUsername());
        Label message = new Label(msg.getMessage());
        message.setWrapText(true);
        message.setMaxWidth(maxChatMessageWidth);
        //Als Tooltip der Nachricht die Eingangs-Zeit anzeigen
        message.setTooltip(new Tooltip(new SimpleDateFormat("HH:mm:ss").format(msg.getTimeStamp())));
        sender.setStyle("-fx-text-fill: black; -fx-font-size: 12");

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
                message.setStyle("-fx-background-radius: " + (plainMessage.length() > message.getMaxWidth() / 10 ? "15" : "90") + ";-fx-background-color: " + CHATMESSAGEBUBBLEBACKGROUNDCOLOR_ME + ";-fx-text-fill: " + CHATMESSAGEBUBBLETEXTCOLOR_ME + "; -fx-font-size: 16");
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
                message.setStyle("-fx-background-radius: " + (plainMessage.length() > message.getMaxWidth() / 10 ? "15" : "90") + ";-fx-background-color: " + CHATMESSAGEBUBBLEBACKGROUNDCOLOR_OTHER + ";-fx-text-fill: " + CHATMESSAGEBUBBLETEXTCOLOR_OTHER + "; -fx-font-size: 16");
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
            message.setStyle("-fx-text-fill: black; -fx-background-color: transparent; -fx-font-size: 14");
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(message);
        }
        box.getChildren().add(hbox);
        box.setPadding(new Insets(0, 0, 0, 0));
        box.setSpacing(5);
        return box;
    }

    //Tauscht eine Nachricht im Chat durch eine andere aus
    private void replaceChatMessage(int index, VBox box) {
        chatMessages.remove(index);
        chatMessages.add(index, box);
    }

    /**
     * Update chat.
     *
     * @param chatMessageList the chat message list
     */
    private void updateChat(List<ChatMessage> chatMessageList) {
        Platform.runLater(() -> {
            chatMessageList.forEach(msg -> chatMessages.add(chatMessagetoBox(msg)));
            chatMessageHistory.addAll(chatMessageList);
        });
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

    /**
     * Setlogged in user.
     *
     * @param user the user
     */
    public void setloggedInUser(User user) {
        loggedInUser = user;
    }

    //--------------------------------------
    // GETTER UND SETTER
    //--------------------------------------

    /**
     * Set chat id.
     *
     * @param chatId the chat id
     */
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    /**
     * The enum Theme.
     */
    public enum THEME {
        /**
         * Light theme.
         */
        Light,
        /**
         * Dark theme.
         */
        Dark
    }
}