package de.uol.swp.client.chat;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.ClientApp;
import de.uol.swp.client.Notifyer;
import de.uol.swp.client.game.GameManagement;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.chat.ChatMessage;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
 * Der ChatViewPresenter
 *
 * @author KenoO
 * @since Sprint 2
 */
public class ChatViewPresenter extends AbstractPresenter {
    /**
     * Pfad zu der verwendeten FXML.
     */
    public static final String fxml = "/fxml/ChatView.fxml";
    /**
     * Pfad zu dem verwendeten Stylesheet.
     */
    private static final String styleSheet = "css/ChatViewPresenter.css";
    /**
     * Der konstante styleSheet_dark.
     */
    private static final String styleSheet_dark = "css/ChatViewPresenter-Dark.css";
    /**
     * Der konstante styleSheet_light.
     */
    private static final String styleSheet_light = "css/ChatViewPresenter-Light.css";
    private final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    //Maximum der Chatnachrichten, die lokal in der ListView gespeichert werden
    private final int MAXCHATMESSAGEHISTORY = 100;

    //Farbe der Nachrichten Label (eigene Nachrichten) (Hintergrund und Text) bei beiden Themen
    private final String ChatMessageBubbleBackgroundColor_Me_LIGHT = "#3D3D3D";
    private final String ChatMessageBubbleBackgroundColor_Me_DARK = "#3D3D3D";
    private final String ChatMessageBubbleTextColor_Me_LIGHT = "white";
    private final String ChatMessageBubbleTextColor_Me_DARK = "white";

    //Farbe der Nachrichten Labell (von anderen Nachrichten) (Hintergrund und Text) bei beiden Themen
    private final String ChatMessageBubbleBackgroundColor_Other_LIGHT = "#DFDEE5";
    private final String ChatMessageBubbleBackgroundColor_Other_DARK = "#4D4C4F";
    private final String ChatMessageBubbleTextColor_Other_LIGHT = "black";
    private final String ChatMessageBubbleTextColor_Other_DARK = "white";

    //Farbe des Senden Buttons (Hintergrund und Text) in beiden Themen
    private final String ChatMessageSendButtonBackgroundColor_LIGHT = "#1C6FEE";
    private final String ChatMessageSendButtonBackgroundColor_DARK = "#1C6FEE";
    private final String ChatMessageSendButtonTextColor_LIGHT = "white";
    private final String ChatMessageSendButtonTextColor_DARK = "white";

    //maximale Breite einer Nachricht (wird automatisch kalkuliert)
    private int maxChatMessageWidth;

    //Das Thema, das benutzt wird
    private THEME CHATTHEME;

    //Die ChatID (zur Identifizierung der Chatnachricht)
    private String chatId;

    //Name des Chats (für das Label)
    private String chatTitle;

    private GameManagement gameManagement;

    private ChatMessage lastMessage;

    private Injector injector;

    // infoUser für Chatinformationen
    private final UserDTO infoUser = new UserDTO("infoUser", "", "");

    // serverUser für Chatnachrichten
    private final UserDTO serverUser = new UserDTO("server", "", "");


    //Farben des aktuellen Chats
    private String ChatMessageBubbleBackgroundColor_Me;
    private String ChatMessageBubbleTextColor_Me;
    private String ChatMessageBubbleBackgroundColor_Other;
    private String ChatMessageBubbleTextColor_Other;
    private String ChatMessageSendButtonBackgroundColor;
    private String ChatMessageSendButtonTextColor;

    //FXML GUI Elemente
    @FXML
    private Label titleLabel;
    @FXML
    private AnchorPane chatViewAnchorPane;
    @FXML
    private TextField chatTextField;
    @FXML
    private ListView<VBox> messageView;
    @FXML
    private Button sendButton;

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
     * Instatiziiere einen neuen ChatViewPresenter im Spiel.
     *
     * @param chatTitle   Name des Chats
     * @param theme       THEMA des Chats
     * @param chatService des Chatservices
     * @author Keno
     * @since Sprint 3
     */
    public ChatViewPresenter(String chatTitle, UUID chatId, User currentUser, THEME theme, ChatService chatService, Injector injector, GameManagement gameManagement) {
        this.chatTitle = chatTitle;
        this.CHATTHEME = theme;
        this.chatService = chatService;
        this.chatId = chatId.toString();
        this.loggedInUser = currentUser;
        this.injector = injector;
        this.gameManagement = gameManagement;

        setTheme(CHATTHEME);
    }

    /**
     * Instaziiere einen neuen ChatViewPresenter in der lobby.
     *
     * @param chatTitle   Name des Chats
     * @param theme       Thema des Chats
     * @param chatService des Chatservices
     * @author Keno O
     * @since Sprint 2
     */
    public ChatViewPresenter(String chatTitle, String chatId, User currentUser, THEME theme, ChatService chatService) {
        this.chatTitle = chatTitle;
        this.CHATTHEME = theme;
        this.chatService = chatService;
        this.chatId = chatId;
        this.loggedInUser = currentUser;

        setTheme(CHATTHEME);
    }

    /**
     * Die Farben des Chatthemas werden eingestellt
     *
     * @param theme Thema des Chats
     * @author Keno O
     * @since Sprint 3
     */
    private void setTheme(THEME theme) {

        if (theme.equals(THEME.Light)) {
            ChatMessageBubbleBackgroundColor_Me = ChatMessageBubbleBackgroundColor_Me_LIGHT;
            ChatMessageBubbleTextColor_Me = ChatMessageBubbleTextColor_Me_LIGHT;
            ChatMessageBubbleBackgroundColor_Other = ChatMessageBubbleBackgroundColor_Other_LIGHT;
            ChatMessageBubbleTextColor_Other = ChatMessageBubbleTextColor_Other_LIGHT;
            ChatMessageSendButtonBackgroundColor = ChatMessageSendButtonBackgroundColor_LIGHT;
            ChatMessageSendButtonTextColor = ChatMessageSendButtonTextColor_LIGHT;
        } else {
            ChatMessageBubbleBackgroundColor_Me = ChatMessageBubbleBackgroundColor_Me_DARK;
            ChatMessageBubbleTextColor_Me = ChatMessageBubbleTextColor_Me_DARK;
            ChatMessageBubbleBackgroundColor_Other = ChatMessageBubbleBackgroundColor_Other_DARK;
            ChatMessageBubbleTextColor_Other = ChatMessageBubbleTextColor_Other_DARK;
            ChatMessageSendButtonBackgroundColor = ChatMessageSendButtonBackgroundColor_DARK;
            ChatMessageSendButtonTextColor = ChatMessageSendButtonTextColor_DARK;
        }
    }

    /**
     * Initialisiere den Chat
     *
     * @author KenoO
     * @since Sprint 2
     */
    @FXML
    public void initialize() {

        //Erstellt eine neue Chat-Historie und übergibt die Liste an die ListView
        updateChatMessages(new ArrayList<>());
        try {
            messageView.setItems(chatMessages);
        } catch (Exception ignored) {
        }

        //Berechnet die maximale Nachrichtenbreite
        maxChatMessageWidth = (int) chatViewAnchorPane.getPrefWidth() - 70;

        //Nachrichten mit der ENTER-Taste abschicken
        chatTextField.setOnKeyPressed(onKeyPressedinchatTextFieldEvent);

        //Automatisches Scrollen zur neuesten Nachricht
        chatMessages.addListener((ListChangeListener<VBox>) change -> Platform.runLater(() -> messageView.scrollTo(messageView.getItems().size() - 1)));

        //Verwende den richtigen Namen im Label
        titleLabel.setText(chatTitle.toUpperCase() + " CHAT");
        if (!chatId.equals("global")) {
            titleLabel.setStyle("-fx-text-fill: white;");
        }

        //Nötige Styles laden und übernehmen
        chatViewAnchorPane.getStylesheets().add(styleSheet);
        if (CHATTHEME.equals(ChatViewPresenter.THEME.Light)) {
            LOG.debug("Lade Light Theme");
            chatViewAnchorPane.getStylesheets().add(styleSheet_light);
        } else if (CHATTHEME.equals(THEME.Dark)) {
            LOG.debug("Lade Dark Theme");
            chatViewAnchorPane.getStylesheets().add(styleSheet_dark);
        }

        sendButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * Die Methode onNewChatMessage
     *
     * @param msg die Nachricht
     * @author KenoO
     * @since Sprint 2
     */
    @Subscribe
    private void onNewChatMessage(NewChatMessage msg) {
        if (loggedInUser == null) return;
        if (!chatId.equals("") && msg.getChatId().equals(chatId) && (lastMessage == null || !(msg.getMessage().getSender().getUsername().equals("server") && lastMessage.getSender().getUsername().equals("server") && msg.getMessage().getMessage().equals(lastMessage.getMessage())))) {
            if (lastMessage != null && !loggedInUser.getUsername().equals(msg.getMessage().getSender().getUsername()) && ((gameManagement != null && !gameManagement.hasFocus()) || (gameManagement == null && !ClientApp.getSceneManager().hasFocus())))
                try {
                    new Notifyer().notify(Notifyer.MessageType.INFO, "Nachricht von " + msg.getMessage().getSender().getUsername() + " in " + (chatId.equals("global") ? "GLOBAL" : chatTitle), msg.getMessage().getMessage());
                } catch (Exception e) {
                    LOG.debug("Nachricht konnte nicht angezeigt werden");
                }
            if (!loggedInUser.getUsername().equals(msg.getMessage().getSender().getUsername()) && msg.getMessage().getSender().equals("server"))
                new SoundMediaPlayer(SoundMediaPlayer.Sound.Message_Receive, SoundMediaPlayer.Type.Sound).play();
            Platform.runLater(() -> {
                //Loesche alte Nachrichten bei bedarf
                if (chatMessages.size() > MAXCHATMESSAGEHISTORY) {
                    chatMessages.remove(0);
                    chatMessageHistory.remove(0);
                }
                //Fuege neue ChatNachricht hinzu
                chatMessages.add(chatMessagetoBox(msg.getMessage()));
                chatMessageHistory.add(msg.getMessage());

            });
            lastMessage = msg.getMessage();
        }
    }

    /**
     * Die Methode onChatResponseMessage.
     *
     * @param msg die Nachricht
     * @author KenoO
     * @since Sprint 2
     */
    @Subscribe
    private void onChatResponseMessage(ChatResponseMessage msg) {
        if (msg.getChat().getChatId().equals(chatId) && msg.getSender().equals(loggedInUser.getUsername())) {
            updateChat(msg.getChat().getMessages());
        }
    }

    /**
     * Aktualisiert den loggedInUser
     *
     * @param message
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser != null && loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
    }

    //--------------------------------------
    // METHODE
    //--------------------------------------


    /**
     * Die Methode userJoined gibt eine Nachricht, wenn ein User dem Chat beitritt.
     *
     * @param username the username
     * @author KenoO, Fenja, Timo
     * @since Sprint 2
     */

    public void userJoined(String username) {
        if (!chatId.equals(""))
            onNewChatMessage(new NewChatMessage(chatId, new ChatMessage(chatId.equals("global") ? serverUser : infoUser, username + " ist " + (chatId.equals("global") ? "dem Chat" : "der Lobby") + " beigereten")));
    }

    /**
     * User left.
     * Ausgabe einer Nachricht, wenn ein User einen Chat verlassen hat.
     *
     * @param username the username
     * @author KenoO, Fenja, Timo
     * @since Sprint 2
     */

    public void userLeft(String username) {
        if (!chatId.equals(""))
            onNewChatMessage(new NewChatMessage(chatId, new ChatMessage(chatId.equals("global") ? serverUser : infoUser, username + " hat " + (chatId.equals("global") ? "den Chat" : "die Lobby") + " verlassen!")));
    }

    /**
     * Nachricht, dass der Spieler gekickt wurde, wird im Chat angezeigt.
     *
     * @param username Benutzername des gekickten Spielers
     * @author Darian, Fenja, Timo
     * @since sprint4
     */
    public void userKicked(String username) {
        if (!chatId.equals(""))
            onNewChatMessage(new NewChatMessage(chatId, new ChatMessage(chatId.equals("global") ? serverUser : infoUser, username + " wurde aus der Lobby entfernt!")));
    }

    /**
     * Die Methode onSendChatButtonPressed.
     *
     * @author KenoO
     * @since Sprint 2
     */
    @FXML
    private void onSendChatButtonPressed() {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Message_Send, SoundMediaPlayer.Type.Sound).play();
        if (chatId.equals("")) return;
        String message;

        message = chatTextField.getText();
        //Prüfe auf leere Nachricht
        if (!message.equals("")) {
            LOG.debug("Sende neue Chatnachricht: User= " + loggedInUser.getUsername() + " Msg= " + message + " ChatID= " + chatId);
            ChatMessage newChatMessage = new ChatMessage(loggedInUser, message);

            LOG.debug("Neue Nachricht zum Senden: " + message);

            chatTextField.clear();
            this.chatService.sendMessage(chatId, newChatMessage);
        }
    }

    /**
     * Kreiert eine HBox mit dem Label einer gegeben ChatMessage
     *
     * @param msg die Chatnachricht
     * @return eine HBox mit Labels
     * @author KenoO
     * @since Sprint2
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
        if (!msg.getSender().getUsername().equals("server") && !msg.getSender().getUsername().equals("infoUser")) {
            if (msg.getSender().getUsername().equals(loggedInUser.getUsername())) {
                //Wenn die Nachricht mehrere Zeilen umfasst, dann aendere den Radius der Ecken
                message.setStyle("-fx-background-radius: " + (plainMessage.length() > message.getMaxWidth() / 10 ? "15" : "90") + ";-fx-background-color: " + ChatMessageBubbleBackgroundColor_Me + ";-fx-text-fill: " + ChatMessageBubbleTextColor_Me + "; -fx-font-size: 16" + ";-fx-opacity: 0.73");
                sender.setText("Du");
                sender.setAlignment(Pos.BOTTOM_RIGHT);
                message.setAlignment(Pos.BOTTOM_RIGHT);
                message.setPadding(new Insets(5, 5, 5, 5));


                hbox.getChildren().add(message);
                box.alignmentProperty().setValue(Pos.BOTTOM_RIGHT);
                hbox.alignmentProperty().setValue(Pos.BOTTOM_RIGHT);


            } else {
                //Wenn die Nachricht mehrere Zeilen umfasst, dann ändere den Radius der Ecken
                message.setStyle("-fx-background-radius: " + (plainMessage.length() > message.getMaxWidth() / 10 ? "15" : "90") + ";-fx-background-color: " + ChatMessageBubbleBackgroundColor_Other + ";-fx-text-fill: " + ChatMessageBubbleTextColor_Other + "; -fx-font-size: 16");
                sender.setAlignment(Pos.BOTTOM_LEFT);
                sender.setPadding(new Insets(0, 0, 0, 40));
                message.setAlignment(Pos.BOTTOM_LEFT);
                message.setPadding(new Insets(8, 8, 8, 8));

                hbox.setSpacing(5);

                //Vorrangegangene Einträge ggf. bearbeiten
                if (chatMessageHistory.size() > 1) {
                    String lastsender = chatMessageHistory.get(chatMessageHistory.size() - 1).getSender().getUsername();
                    if (msg.getSender().getUsername().equals(lastsender)) {
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
                hbox.getChildren().add(message);
                box.alignmentProperty().setValue(Pos.BOTTOM_LEFT);
                hbox.alignmentProperty().setValue(Pos.BOTTOM_LEFT);
            }
        } else if (msg.getSender().getUsername().equals("infoUser")) {
            // Wenn die empfangene Nachricht eine Info-Nachricht ist
            message.setStyle("-fx-text-fill: blue; -fx-background-color: transparent; -fx-font-size: 16");
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(message);
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
     * Aktualisieren des Chats
     *
     * @param chatMessageList die Chatnachricht
     * @author KenoO
     * @since Sprint2
     */
    private void updateChat(List<ChatMessage> chatMessageList) {
        Platform.runLater(() -> {
            chatMessageList.forEach(msg -> chatMessages.add(chatMessagetoBox(msg)));
            chatMessageHistory.addAll(chatMessageList);
        });
    }

    //Aktualisiert die ListView indem alle übergebenen Nachrichten dieser hinzugefügt werden
    private void updateChatMessages(List<ChatMessage> chatMessageList) {
        // Warnung: Das muss auf dem FX Thread passieren!
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
     * Setze den LoggedInUser
     *
     * @param user den User
     * @author KenoO
     * @since Sprint2
     */
    public void setloggedInUser(User user) {
        loggedInUser = user;
    }

    //--------------------------------------
    // GETTER UND SETTER
    //--------------------------------------

    /**
     * Setze Chat ID
     *
     * @param chatId die chat id
     * @author KenoO
     * @since Sprint2
     */
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    /**
     * Das Enum zum Thema
     */
    public enum THEME {
        /**
         * Helles Thema.
         */
        Light,
        /**
         * Dunkles Thema
         */
        Dark
    }
}