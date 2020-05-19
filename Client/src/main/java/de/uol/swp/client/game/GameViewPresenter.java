package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Der Presenter für die Spielansicht.
 *
 * @author fenja, hashem, marvin
 * @since Sprint 3
 */
public class GameViewPresenter extends AbstractPresenter {

    /**
     * Die Konstante fxml.
     */
    public static final String fxml = "/fxml/GameViewWIP.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private final UUID lobbyID;
    private User loggedInUser;

    private int usableMoney;

    // @FXML
    // private Pane gameView;

    @FXML
    private Pane gameViewWIP;
    @FXML
    private Pane chatView;
    @FXML
    private Pane shopTeppich;
    @FXML
    private ListView<String> usersView;
    @FXML
    private StackPane deckPane;
    @FXML
    private StackPane discardPilePane;
    @FXML
    private ImageView cardPlaceholder1;
    @FXML
    private ImageView cardPlaceholder2;
    @FXML
    private ImageView cardPlaceholder3;
    @FXML
    private ImageView cardPlaceholder4;
    @FXML
    private ImageView cardPlaceholder5;
    @FXML
    private ImageView cardPlaceholder6;
    @FXML
    private ImageView cardPlaceholder7;
    @FXML
    private ImageView cardPlaceholder8;
    @FXML
    private ImageView cardPlaceholder9;
    @FXML
    private ImageView cardPlaceholder10;
    @FXML
    private StackPane countDeckPane;
    @FXML
    private Label countDeckLabel;
    @FXML
    private Label numberOfAction;
    @FXML
    private Label numberOfMoney;
    @FXML
    private Label numberOfBuy;
    @FXML
    private Label infoActualPhase;
    @FXML
    private Button playAllMoneyCardsButton;

    private final HandcardsLayoutContainer handcards;
    private final PlayedCardLayoutContainer playedCardLayoutContainer;

    private ObservableList<String> users;
    private final GameService gameService;
    private MouseEvent mouseEvent;
    private final ChatViewPresenter chatViewPresenter;
    private final Injector injector;
    private final GameManagement gameManagement;

    private ArrayList<Short> handCardIDs;

    private final EventHandler<MouseEvent> handCardEventHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            ImageView card = (ImageView) event.getSource();
            playChoosenCard(lobbyID, loggedInUser, card.getImage().getUrl(), Short.valueOf(card.getId()), card, (MouseEvent) event);
        }
    };

    /**
     * Instantiiert einen neuen GameView Presenter.
     *
     * @param loggedInUser      der angemeldete Benutzer
     * @param lobbyID           die Lobby ID
     * @param chatService       der Chat Service
     * @param chatViewPresenter der Chat View Presenter
     * @param lobbyService      der Lobby Service
     * @param userService       der User Service
     * @param injector          der Injector
     * @param gameManagement    das Game Management
     */
    public GameViewPresenter(User loggedInUser, UUID lobbyID, ChatService chatService, ChatViewPresenter chatViewPresenter, LobbyService lobbyService, UserService userService, Injector injector, GameManagement gameManagement, GameService gameService) {
        this.loggedInUser = loggedInUser;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.chatViewPresenter = chatViewPresenter;
        this.injector = injector;
        this.gameManagement = gameManagement;
        handcards = new HandcardsLayoutContainer(460, 618, 160, 650);
        playedCardLayoutContainer = new PlayedCardLayoutContainer(500, 500, 160, 100);
        this.gameService = gameService;
        initializeUserList();
    }

    /**
     * Show Alert für den Aufgeben Button
     *
     * @param type    der Typ
     * @param message die Nachricht
     * @param title   der Titel
     * @author M.Haschem
     * @since Sprint 3
     */

    public void showGiveUpAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            gameManagement.getGameService().giveUp(lobbyID, (UserDTO) loggedInUser);
        }
    }

    public void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(type, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        Optional<ButtonType> result = alert.showAndWait();
    }

    /**
     * Initialisieren.
     *
     * @throws IOException die io Ausnahme
     * @author Fenja
     * @since Sprint 3
     */
    @FXML
    public void initialize() throws IOException {
        //FXML laden
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        loader.setLocation(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());
        gameViewWIP.getChildren().add(playedCardLayoutContainer);
        gameViewWIP.getChildren().add(handcards);
    }

    /**
     * Die Aktionskarten werden erstellt und auf dem Spielfeld angezeigt.
     *
     * @param theList die IDs der Aktionskarten
     * @author Ferit, Fenja, Anna
     * @since Sprint 7
     */
    private void initalizeCardFieldImages(ArrayList<Short> theList) {
        ArrayList<ImageView> allImageViews = new ArrayList<>(Arrays.asList(cardPlaceholder1, cardPlaceholder2, cardPlaceholder3, cardPlaceholder4, cardPlaceholder5, cardPlaceholder6, cardPlaceholder7, cardPlaceholder8, cardPlaceholder9, cardPlaceholder10));
        int index = 0;
        for (ImageView imageView : allImageViews) {
            String theIdInString = String.valueOf(theList.get(index));
            String imageUrl = "/cards/images/" + theIdInString + "_sm.png";
            Image theImage = new Image(imageUrl);
            imageView.setImage(theImage);
            imageView.setId(theIdInString);
            index++;
        }
        theList = null;
        allImageViews = null;
    }

    /**
     * Aufgeben Button gedrückt Ereignis.
     *
     * @param actionEvent das Ereignis der Aktion.
     * @author Haschem
     * @since Sprint 3
     */
    @FXML
    public void onGiveUpButtonPressed(ActionEvent actionEvent) {
        showGiveUpAlert(Alert.AlertType.CONFIRMATION, " ", "Möchtest du wirklich aufgeben?");
    }

    /**
     * Ereignis das ausgeführt wird, wenn der User den Button "Alle Geldkarten spielen" drückt
     *
     * @param actionEvent
     * @author Rike
     * @since Sprint 7
     */
    @FXML
    public void onPlayAllMoneyCardsButtonPressed(ActionEvent actionEvent) {
        playAllMoneyCardsOnHand();
    }

    /**
     * Ereignis das ausgeführt wird, wenn eine Karte im Shop angeklickt wird.
     * ruft die chosenBuyableCard()-Methode auf
     *
     * @param mouseEvent
     * @author Rike
     * @since Sprint 5
     */
    @FXML
    public void onBuyableCardClicked(MouseEvent mouseEvent) {
        chosenBuyableCard(mouseEvent);
    }

    /**
     * Wird bei Erstellung aufgerufen und initialisiert UserList.
     *
     * @author Marvin
     * @since Sprint3
     */
    public void initializeUserList() {
        lobbyService.retrieveAllUsersInLobby(lobbyID);
    }

    /**
     * Die IDs der gesendeten Aktionskarten werden initilaisiert
     *
     * @param msg die Nachricht mit den IDs und der jeweiligen Azahl der Spielkarten
     * @author Anna, Fenja
     * @since Sprint 7
     */
    @Subscribe
    public void onSendCardFieldMessage(SendCardFieldMessage msg) {
        ArrayList<Short> list = new ArrayList<>();
        for (Short key : msg.getCardField().keySet()) {
            if (key > 6) {
                list.add(key);
            }
        }
        initalizeCardFieldImages(list);
    }

    /**
     * Sobald ein neuer User der Lobby beitritt, wird eine RetrieveAllLobbyUsersRequest gesendet.
     *
     * @param userJoinedLobbyMessage Die Nachricht wenn ein User die Lobby beitritt
     * @author Marvin
     * @since Sprint3
     */
    @Subscribe
    public void newUser(UserJoinedLobbyMessage userJoinedLobbyMessage) {
        if (userJoinedLobbyMessage.getLobbyID().equals(this.lobbyID)) {
            lobbyService.retrieveAllUsersInLobby(lobbyID);
            LOG.debug("Neuer User in der Lobby, LobbyService empfängt Nutzer");
        }
    }

    /**
     * Wenn die StartClearPhaseMessage ankommt, werden die Karten auf der Hand zum Ablagestapel bewegt
     * Die Anzeige für die jeweilige Phase wird für den Spieler aktualisiert
     *
     * @param msg Die Nachricht
     * @author Darian, Rike
     * @since Sprint7
     */
    @Subscribe
    public void onStartClearPhase(StartClearPhaseMessage msg){
        if (msg.getGameID().equals(this.lobbyID) && msg.getUser().equals(loggedInUser)) {
            synchronized (handcards){
                moveCardsToDiscardPile(handcards.getChildren(), false);
            }
            moveCardsToDiscardPile(playedCardLayoutContainer.getChildren(), true);
        }
        onStartPhase(msg.getGameID(), msg.getUser(), msg);
    }

    /**
     * Aktualisiert den loggedInUser sowie die Liste, falls sich der Username geändert hat
     *
     * @param message
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
        if (users != null && users.contains(message.getOldUser().getUsername())) {
            users.remove(message.getOldUser().getUsername());
            users.add(message.getUser().getUsername());
        }
    }

    /**
     * Bei einer AllLobbyUsersResponse wird updateUsersList ausgeführt, wenn es diese Lobby betrifft.
     * Bis auf die Lobby-Überprüfung & Response-Typ quasi äquivalent zu MainMenuPresenter.userList.
     *
     * @param allOnlineUsersInLobbyResponse die Antwort aller Lobby-Benutzer
     * @author Marvin
     * @since Sprint3
     */
    @Subscribe
    public void userList(AllOnlineUsersInLobbyResponse allOnlineUsersInLobbyResponse) {
        if (allOnlineUsersInLobbyResponse.getLobbyID().equals(this.lobbyID)) {
            LOG.debug("Aktualisieren der Userliste mit" + allOnlineUsersInLobbyResponse.getUsers());
            updateUsersList(allOnlineUsersInLobbyResponse.getUsers());
        }
    }

    /**
     * Die Nachricht die gesendet wird, wenn der Kauf einer Karte erfolgreich war
     * die Karte, sowie die gebrauchten Geldkarten wandert auf den Ablagestapel (Animation)
     * Überprüft ob der Spieler noch Karten der gekauften Art kaufen kann und fügt ggf. das ImageView (kleines Bild) wieder hinzu
     *
     * @param msg die Nachricht
     * @author Devin, Anna, Rike
     * @since Sprint 5
     */
    // TODO: Karte wenn sie gekauft wird, von der richtigen Postition einfliegen lassen. ( Weiter nach rechts)
    @Subscribe
    public void onBuyCardMessage(BuyCardMessage msg) {
        if (msg.getLobbyID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            ImageView selectedCard = (ImageView) mouseEvent.getSource();
            String pfad = "file:Client/src/main/resources/cards/images/" + msg.getCardID().toString() + ".png";
            Image picture = new Image(pfad);
            ImageView newCardImage = new ImageView(picture);
            LOG.debug("Der Spieler " + msg.getCurrentUser() + " hat die Karte " + msg.getCardID() + " gekauft.");
            // fügt ein "neues" Bild an der Stelle des alten Bildes im Shop hinzu
            newCardImage.setPreserveRatio(true);
            newCardImage.setFitHeight(107);
            newCardImage.setFitWidth(Math.round(newCardImage.getBoundsInLocal().getWidth()));
            newCardImage.setLayoutX(selectedCard.getLayoutX());
            newCardImage.setLayoutY(selectedCard.getLayoutY());
            newCardImage.setId(String.valueOf(msg.getCardID()));
            Platform.runLater(() -> {
                gameViewWIP.getChildren().add(newCardImage);
                AnimationManagement.buyCard(newCardImage);
                gameViewWIP.getChildren().remove(newCardImage);
                discardPilePane.getChildren().add(newCardImage);
            });
            if (msg.getCounterCard() < 1) {
                ColorAdjust makeImageDarker = new ColorAdjust();
                makeImageDarker.setBrightness(-0.7);
                selectedCard.setEffect(makeImageDarker);
            }
            // entfernt die genutzen Geldkarten aus der Aktionszone (wichtig, wenn der User mehr als 1 Kauf hat)
            Platform.runLater(() -> {
                int money = 0;
                int playedCardLayoutContainerSize = playedCardLayoutContainer.getChildren().size();
                ObservableList<Node> removeMoneyCardList = FXCollections.observableArrayList();
                for (int i = 0; i < playedCardLayoutContainerSize; i++) {
                    Node removeCards = playedCardLayoutContainer.getChildren().get(i);
                    if (removeCards.getId().equals("1") || removeCards.getId().equals("2") || removeCards.getId().equals("3")) {
                        money += Integer.parseInt(removeCards.getId());
                        removeMoneyCardList.add(removeCards);
                        playedCardLayoutContainer.getChildren().remove(i);
                        if (money >= msg.getCostCard()) {
                            break;
                        }
                    }
                }
                moveCardsToDiscardPile(removeMoneyCardList, false);
            });
        }
    }

    /**
     * Die Nachricht die angibt ob die Karte gespielt werden konnte
     *
     * @param msg die Nachricht
     * @author Devin
     * @since Sprint 6
     */
    @FXML
    @Subscribe
    public void onPlayCardMessage(PlayCardMessage msg) {
        ImageView card = (ImageView) mouseEvent.getTarget();
        if (msg.getGameID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            if (msg.getIsPlayed()) {
                Platform.runLater(() -> {
                    if (handcards.getChildren().contains(card)) {
                        AnimationManagement.playCard(card, playedCardLayoutContainer.getChildren().size());
                        handcards.getChildren().remove(card);
                        playedCardLayoutContainer.getChildren().add(card);
                        card.removeEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                    }
                });
                //TODO: Wenn Aktionen implementiert sind, prüfen ob showAlert noch notwendig ist oder ob Serverseitig bereits bei Scheitern eine Message gesendet wird
            } else {
                showAlert(Alert.AlertType.WARNING, "Du kannst die Karte nicht spielen!", "Fehler");
                LOG.debug("Das Spielen der Karte " + msg.getHandCardID() + " von " + msg.getCurrentUser() + " ist fehlgeschlagen");
            }
        }
    }

    /**
     * Zeigt die Karten auf der Hand in der GameView an
     * setzt den Zustand des "Alle Geldkarten spielen" auf anklickbar, wenn der User Geldkarten auf die Hand bekommt
     *
     * @author Devin S., Anna, Rike
     * @since Sprint5
     */
    @FXML
    @Subscribe
    public void onDrawHandMessage(DrawHandMessage message) {
        Platform.runLater(() -> {
            if (lobbyID.equals(message.getTheLobbyID())) {
                handCardIDs = message.getCardsOnHand();
                handCardIDs.forEach((n) -> {
                    String pfad = "file:Client/src/main/resources/cards/images/" + n + ".png";
                    Image picture = new Image(pfad);
                    ImageView card = new ImageView(picture);
                    card.setFitHeight(107);
                    card.setPreserveRatio(true);
                    card.setId(n.toString());
                    card.setFitWidth(Math.round(card.getBoundsInLocal().getWidth()));
                    deckPane.getChildren().add(card);
                    synchronized (handcards) {
                        AnimationManagement.addToHand(card, handcards.getChildren().size());
                        deckPane.getChildren().remove(card);
                        handcards.getChildren().add(card);
                    }
                    card.addEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                    if (playAllMoneyCardsButton.isDisable() && (n == 1 || n == 2 || n == 3)) {
                        playAllMoneyCardsButton.setDisable(false);
                    }
                });
            }
        });
    }

    /**
     * Hier wird die GameExceptionMessage abgefangen und die Nachricht in einem neuem Fenster angezeigt
     *
     * @param msg die Nachricht
     * @author Anna
     * @since Sprint 7
     */
    @Subscribe
    public void onGameExceptionMessage(GameExceptionMessage msg) {
        if (msg.getGameID().equals(lobbyID)) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, msg.getMessage());
                DialogPane root = alert.getDialogPane();
                Stage dialogStage = new Stage(StageStyle.UTILITY);
                for (ButtonType buttonType : root.getButtonTypes()) {
                    ButtonBase button = (ButtonBase) root.lookupButton(buttonType);
                    button.setOnAction(evt -> {
                        dialogStage.close();
                    });
                }
                root.getScene().setRoot(new Group());
                root.setPadding(new Insets(10, 0, 10, 0));
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setResizable(false);
                dialogStage.showAndWait();
            });
        }
    }

    /**
     * Die Anzahl der Karten auf Deck wird aktualisiert und eventuell die Karten vom Ablegestapel entfernt.
     *
     * @param msg die Nachricht
     * @author Fenja, Anna
     * @since Sprint 7
     */
    @Subscribe
    public void onCardsDeckSizeMessage(CardsDeckSizeMessage msg) {
        if (msg.getGameID().equals(lobbyID)) {
            if (msg.getPlayer().equals(loggedInUser)) {
                Platform.runLater(() -> {
                    countDeckLabel.setText(String.valueOf(msg.getCardsDeckSize()));
                    if (msg.getDiscardPileWasCleared()) {
                        discardPilePane.getChildren().clear();
                    }
                });
            }
        }
    }

    /**
     * Impelemntiert das Verhalten bei Erhalten einer StartActionPhaseMessage und StartBuyPhaseMessage
     * die onStartPhase Methode wird aufgerufen
     *
     * @param msg
     * @author Rike
     * @since Sprint 7
     */

    @Subscribe
    public void onStartActionPhaseMessage(StartActionPhaseMessage msg) {
        onStartPhase(msg.getGameID(), msg.getUser(), msg);
    }

    @Subscribe
    public void onStartBuyPhaseMessage(StartBuyPhaseMessage msg) {
        onStartPhase(msg.getGameID(), msg.getUser(), msg);
    }

    /**
     * Aktualisiert die Anzeige für Aktion, Kauf und Geld
     *
     * @param msg
     * @author Rike
     * @since Sprint 7
     */
    @Subscribe
    public void onInfoPlayDisplayMessage(InfoPlayDisplayMessage msg) {
        if (msg.getLobbyID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            Platform.runLater(() -> {
                if (msg.getAvailableBuy() == 1){ numberOfBuy.setText(msg.getAvailableBuy() + " Kauf"); }
                if (msg.getAvailableBuy() != 1) { numberOfBuy.setText(msg.getAvailableBuy() + " Käufe"); }
                if (msg.getAvailableAction() == 1){ numberOfAction.setText(msg.getAvailableAction() + " Aktion"); }
                if (msg.getAvailableAction() != 1){ numberOfAction.setText(msg.getAvailableAction() + " Aktionen"); }
                if (msg.getSourceMessage() == Phase.Type.ActionPhase || msg.getSourceMessage() == Phase.Type.Clearphase) {
                    usableMoney = msg.getAdditionalMoney();
                }
                if (msg.getSourceMessage() == Phase.Type.Buyphase) {
                    if (playAllMoneyCardsButton.isDisable() && playAllMoneyCardsButton.isVisible()) {
                        usableMoney = msg.getAdditionalMoney() + msg.getMoneyOnHand();
                    }
                }
                numberOfMoney.setText(usableMoney + " Geld");
            });
        }
    }

    /**
     * Die usersView Liste wird geupdatet.
     * Äquivalent zu MainMenuPresenter.updateUsersList.
     *
     * @param userList die neue Userliste
     * @author Marvin
     * @since Sprint3
     */
    private void updateUsersList(Set<User> userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (usersView != null) {
                if (users == null) {
                    users = FXCollections.observableArrayList();
                    usersView.setItems(users);
                }
                users.clear();
                userList.forEach(u -> users.add(u.getUsername()));
            }
        });
    }

    /**
     * Skipt die aktuelle Phase des Spielers zur nächsten.
     *
     * @author Devin S.
     * @since Sprint6
     */
    @FXML
    public void onSkipPhaseButtonPressed(ActionEvent actionEvent) {
        gameManagement.getGameService().skipPhase(loggedInUser, lobbyID);
    }

    /**
     * Methode, die beim Anklicken einer Handkarte ausgeführt wird.
     *
     * @param gameID       Die ID des Spiels
     * @param loggedInUser der User der gerade eingelogt im Spiel ist und die Karte ausgewählt hat.
     * @param pfad         Der Pfad zum entsprechendem Vollbild
     * @param id           Die ID der Karte
     * @param card         Die ImageView der ausgewählten Karte
     * @param e            Das MouseEvent, das zum anlicken der Karte zuständig ist.
     * @author Devin
     * @since Sprint 6
     */
    private void playChoosenCard(UUID gameID, User loggedInUser, String pfad, Short id, ImageView card, MouseEvent e) {
        ImageView bigCardImage = new ImageView(new Image(pfad));
        bigCardImage.setFitHeight(225.0);
        bigCardImage.setFitWidth(150.0);
        bigCardImage.toFront();
        bigCardImage.setLayoutX(425.0);
        bigCardImage.setLayoutY(155.0);
        gameViewWIP.getChildren().add(bigCardImage);
        if (id > 6) {
            Button play = new Button("Auspielen");
            Button back = new Button("Zurück");
            play.setLayoutX(432.0);
            play.setLayoutY(385.0);
            back.setLayoutX(516.0);
            back.setLayoutY(385.0);
            back.setMinWidth(52.0);
            gameViewWIP.getChildren().add(play);
            gameViewWIP.getChildren().add(back);
            play.setOnAction(event -> {
                gameViewWIP.getChildren().remove(play);
                gameViewWIP.getChildren().remove(back);
                gameViewWIP.getChildren().remove(bigCardImage);
                for (Node a : handcards.getChildren()) {
                    ImageView b = (ImageView) a;
                    if (b.equals(card)) {
                        mouseEvent = e;
                        gameManagement.getGameService().playCard(gameID, loggedInUser, id);
                    }
                }
            });
            // Aktion hinter dem Zurück Button -> Buttons und das große Bild werden entfernt
            back.setOnAction(event -> {
                gameViewWIP.getChildren().remove(play);
                gameViewWIP.getChildren().remove(back);
                gameViewWIP.getChildren().remove(bigCardImage);
            });
        } else {
            Button back = new Button("Zurück");
            back.setLayoutX(516.0);
            back.setLayoutY(385.0);
            back.setMinWidth(52.0);
            gameViewWIP.getChildren().add(back);
            // Aktion hinter dem Zurück Button -> Buttons und das große Bild werden entfernt
            back.setOnAction(event -> {
                gameViewWIP.getChildren().remove(back);
                gameViewWIP.getChildren().remove(bigCardImage);
            });
        }
    }

    /**
     * Die Karten werden zum Ablagestapel bewegt
     *
     * @param children Das children von dem Karten Stapel
     * @param achtionCards true wenn die Karten in der Aktionszone liegen
     * @author Darian
     * @since Sprint7
     */
    private void moveCardsToDiscardPile(ObservableList<Node> children, boolean achtionCards){
        for (Node c : children) {
            Platform.runLater(() -> {
                ImageView card = (ImageView) c;
                String pfad = "file:Client/src/main/resources/cards/images/" + card.getId() + ".png";
                Image picture = new Image(pfad);
                ImageView newCardImage = new ImageView(picture);
                newCardImage.setPreserveRatio(true);
                newCardImage.setFitHeight(107);
                newCardImage.setFitWidth(Math.round(newCardImage.getBoundsInLocal().getWidth()));
                newCardImage.setLayoutX(450 + c.getLayoutX());
                if (achtionCards) {
                    newCardImage.setLayoutY(493);
                }
                else{
                    newCardImage.setLayoutY(610);
                }
                newCardImage.setId(String.valueOf(c));
                children.remove(c);
                gameViewWIP.getChildren().add(newCardImage);
                PathTransition pathTransition = AnimationManagement.clearCards(newCardImage);
                pathTransition.setOnFinished(actionEvent -> {
                    gameViewWIP.getChildren().remove(newCardImage);
                    ImageView iv = new ImageView(picture);
                    iv.setPreserveRatio(true);
                    iv.setFitHeight(107);
                    discardPilePane.getChildren().add(iv);
                });
            });
        }
    }

    /**
     * Hilfsmethode für onBuyableCardClicked() und onBuyCardMessage()
     * Großes Bild der Karte wird angezeigt.
     * Es werden zwei Buttons("kaufen"/"zurück") hinzugefügt.
     * kauf-Button -> BuyCardRequest wird gestellt
     * zurück-Button -> Buttons und große Ansicht der Karte werden entfernt
     * Request wird erst gesendet, wenn der User die Geldkarten ausgespielt hat & dran ist (abfrage über Zustand des "Geldkarten spielen"-Buttons
     * ansonst gibt es einen Alert
     *
     * @param mouseEvent das Event
     * @author Fenja, Anna, Rike
     * @since Sprint 5
     */
    private void chosenBuyableCard(MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getSceneX();
        double mouseY = mouseEvent.getSceneY();
        ImageView cardImage = (ImageView) mouseEvent.getSource();
        // Überprüfung ob sich die angeklickte Karte innerhalb des Shops befindet und nicht bereits auf dem Ablagestapel
        //    if (mouseX > shopTeppich.getLayoutX() && mouseX < (shopTeppich.getLayoutX() + shopTeppich.getWidth()) &&
        //          mouseY > shopTeppich.getLayoutY() && mouseY < (shopTeppich.getLayoutY() + shopTeppich.getHeight()) && cardImage.getEffect() == null) {
        // Karte befindet sich im Shop
        //Karte hat noch keinen Effekt gesetzt bekommen, ist also noch im Shop vorhanden
        if (cardImage.getEffect() != null) {
            return;
        }
        String cardID = cardImage.getId();
        String PathCardLargeView = "file:Client/src/main/resources/cards/images/" + cardID + ".png";
        // Ein großes Bild der Karte wird hinzugefügt
        ImageView bigCardImage = new ImageView(new Image(PathCardLargeView));
        // Setzt die Größe und die Position des Bildes. Das Bild ist im Vordergrund. Bild wird hinzugefügt
        bigCardImage.setFitHeight(240.0);
        bigCardImage.setFitWidth(150.0);
        bigCardImage.toFront();
        bigCardImage.setLayoutX(325.0);
        bigCardImage.setLayoutY(20.0);
        gameViewWIP.getChildren().add(bigCardImage);
        // Es werden zwei Buttons hinzugefügt (Zurück und Kaufen)
        Button buy = new Button("Kaufen");
        Button back = new Button("Zurück");
        gameViewWIP.getChildren().add(buy);
        gameViewWIP.getChildren().add(back);
        // Position der Buttons wird gesetzt
        buy.setLayoutX(325.0);
        buy.setLayoutY(255.0);
        buy.setMinWidth(70.0);
        back.setLayoutX(405.0);
        back.setLayoutY(255.0);
        back.setMinWidth(70.0);
        // Aktion hinter dem Kauf-Button
        buy.setOnAction(event -> {
            if (playAllMoneyCardsButton.isDisable() && playAllMoneyCardsButton.isVisible()) {
                buy.setVisible(false);
                back.setVisible(false);
                bigCardImage.setVisible(false);
                BuyCardRequest req = new BuyCardRequest(lobbyID, loggedInUser, Short.valueOf(cardID));
                gameService.buyCard(req);
                this.mouseEvent = mouseEvent;
            } else {
                buy.setVisible(false);
                back.setVisible(false);
                bigCardImage.setVisible(false);
                if (!playAllMoneyCardsButton.isVisible()) {
                    showAlert(Alert.AlertType.INFORMATION, "Du bist nicht dran!", "Fehler");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Du musst erst deine Geldkarten ausspielen!", "Fehler");
                }
            }
        });
        // Aktion hinter dem Zurück-Button -> Buttons und das große Bild werden entfernt
        back.setOnAction(event -> {
            buy.setVisible(false);
            back.setVisible(false);
            bigCardImage.setVisible(false);
        });
    }

    /**
     * Hier werden alle Geldkarten, die sich auf der Hand befinden, ausgespielt
     * der playAllMoneyCardsButton wird anschließend auf nicht anklickbar gesetzt
     *
     * @author Anna, Rike
     * @since Sprint 7
     */
    private void playAllMoneyCardsOnHand() {
        synchronized (handcards) {
            for (Node c : handcards.getChildren()) {
                ImageView card = (ImageView) c;
                if (card.getId().equals("1") || card.getId().equals("2") || card.getId().equals("3")) {
                    usableMoney += Integer.parseInt(card.getId());
                    Platform.runLater(() -> {
                        AnimationManagement.playCard(card, playedCardLayoutContainer.getChildren().size());
                        handcards.getChildren().remove(c);
                        playedCardLayoutContainer.getChildren().add(card);
                    });
                }
            }
            numberOfMoney.setText(usableMoney + " Geld");
            playAllMoneyCardsButton.setDisable(true);
        }
    }

    /**
     * Hilfsmethode für die onStartActionPhaseMessage, onStartBuyPhaseMessage und onStartClearPhase
     *
     * @param gameID die ID des Spieles
     * @param user   der User
     * @param msg    die Message
     * @author Rike
     * @since Sprint 7
     */
    private void onStartPhase(UUID gameID, User user, AbstractServerMessage msg) {
        if (gameID.equals(lobbyID)) {
            Platform.runLater(() -> {
                if (user.equals(loggedInUser)) {
                    playAllMoneyCardsButton.setVisible(true);
                    if (msg instanceof StartActionPhaseMessage) {
                        infoActualPhase.setText("Du darfst Aktionen spielen.");
                    }
                    if (msg instanceof StartBuyPhaseMessage) {
                        infoActualPhase.setText("Du darfst Karten kaufen.");
                    }
                    if (msg instanceof StartClearPhaseMessage) {
                        infoActualPhase.setText("Clearphase");
                    }
                } else {
                    playAllMoneyCardsButton.setVisible(false);
                    infoActualPhase.setText("Du bist nicht dran.");
                }
            });
        }
    }
}