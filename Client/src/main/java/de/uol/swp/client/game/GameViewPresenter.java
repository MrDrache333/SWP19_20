package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.container.GeneralLayoutContainer;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.card.parser.components.CardAction.request.ChooseCardRequest;
import de.uol.swp.common.game.card.parser.components.CardAction.request.OptionalActionRequest;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private Short numberOfPlayersInGame;

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
    private Label player1_label;
    @FXML
    private Label player2_label;
    @FXML
    private Label player3_label;
    @FXML
    private ImageView avatar_icon_top;
    @FXML
    private ImageView avatar_icon_left;
    @FXML
    private ImageView avatar_icon_right;
    @FXML
    private StackPane countDeckPane;
    @FXML
    private Label countDeckLabel;
    @FXML
    private VBox bigCardImageBox;
    @FXML
    private ImageView bigCardImage;
    @FXML
    private Button buyCardButton;
    @FXML
    private Label countEstateCardLabel;
    @FXML
    private Label countDuchiesCardLabel;
    @FXML
    private Label countProvinceCardLabel;
    @FXML
    private Label countCurseCardLabel;
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
    @FXML
    private Pane valueCardsBox;
    @FXML
    private Button skipPhaseButton;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;
    @FXML
    private Button selectButton;

    private final GeneralLayoutContainer handcards;
    private final GeneralLayoutContainer firstEnemyHand;
    private final GeneralLayoutContainer secondEnemyHand;
    private final GeneralLayoutContainer thirdEnemyHand;
    private final GeneralLayoutContainer myPCLC;
    private final GeneralLayoutContainer firstEnemyPCLC;
    private final GeneralLayoutContainer secondEnemyPCLC;
    private final GeneralLayoutContainer thirdEnemyPCLC;
    private final GeneralLayoutContainer myDPLC;
    private final GeneralLayoutContainer firstEnemyDPLC;
    private final GeneralLayoutContainer secondEnemyDPLC;
    private final GeneralLayoutContainer thirdEnemyDPLC;
    private final GeneralLayoutContainer myDLC;
    private final GeneralLayoutContainer firstEnemyDLC;
    private final GeneralLayoutContainer secondEnemyDLC;
    private final GeneralLayoutContainer thirdEnemyDLC;

    private ObservableList<String> users;
    private final GameService gameService;
    private MouseEvent mouseEvent;
    private final ChatViewPresenter chatViewPresenter;
    private final Injector injector;
    private final GameManagement gameManagement;
    private ArrayList<Short> handCardIDs;
    private Map<Short, Label> valuecardLabels = new HashMap<>();
    private ColorAdjust makeImageDarker = new ColorAdjust();
    private boolean chooseCardBecauseOfActionCard = false;
    private ColorAdjust notChosenCard = new ColorAdjust();
    private boolean directHand;
    private ArrayList<Short> choosenCardsId = new ArrayList<>();
    private ArrayList<ImageView> choosenCards = new ArrayList<>();
    private int numberOfCardsToChoose;
    private String currentInfoText;

    /**
     * Das Event das den Handkarten gegeben wird, wenn sie ausspielbar sein sollen.
     * @author Devin
     * @since Sprint 5
     */
    private final EventHandler<MouseEvent> handCardEventHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            ImageView card = (ImageView) event.getSource();
            playChoosenCard(lobbyID, loggedInUser, card.getImage().getUrl(), Short.valueOf(card.getId()), card, (MouseEvent) event);
        }
    };

    /**
     * Das Event das den Handkarten gegeben wird, wenn sie auswählbar gemacht werden sollen.
     * @author Devin
     * @since Sprint 8
     */
    private final EventHandler<MouseEvent> discardCardEventHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            ImageView card = (ImageView) event.getSource();
            discardChoosenCard(lobbyID, loggedInUser, card.getImage().getUrl(), Short.valueOf(card.getId()), card, (MouseEvent) event);
        }
    };

    /**
     * Das Event für den "Auswahl senden"-Button, er sendet eine ChooseCardResponse an den Server über den GameService
     * @author Devin
     * @since Sprint 8
     */
    @FXML
    private final EventHandler<ActionEvent> sendChoosenCardResponse = new EventHandler() {
        @Override
        public void handle(Event event) {
            selectButton.setVisible(false);
            playAllMoneyCardsButton.setVisible(true);
            for (ImageView card : choosenCards) {
                choosenCardsId.add(Short.parseShort(card.getId()));
            }
            gameService.chooseCardResponse( lobbyID, loggedInUser, choosenCardsId, directHand);
            handcards.getChildren().forEach((n) -> {
                n.removeEventHandler(MouseEvent.MOUSE_CLICKED, discardCardEventHandler);
                n.addEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                n.setEffect(null);
            });
            Platform.runLater(() -> {
                skipPhaseButton.setDisable(false);
                Platform.runLater(() -> {
                    infoActualPhase.setText(currentInfoText);
                });
            });
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
        this.gameService = gameService;
        makeImageDarker.setBrightness(-0.7);
        // Die Hände für jeden Spieler
        handcards = new GeneralLayoutContainer(575, 630, 110, 420, "My.HCLC");
        firstEnemyHand = new GeneralLayoutContainer(700, 110, 110, 215, "1.HCLC");
        secondEnemyHand = new GeneralLayoutContainer(300, 308, 105, 215, "2.HCLC");
        thirdEnemyHand = new GeneralLayoutContainer(1070, 308, 105, 215, "3.HCLC");
        // Die Aktion-Zonen für jeden Spieler
        myPCLC = new GeneralLayoutContainer(960, 480, 100, 200, "My.PCLC");
        firstEnemyPCLC = new GeneralLayoutContainer(700, 150, 120, 240, "1.PCLC");
        secondEnemyPCLC = new GeneralLayoutContainer(360, 308, 120, 160, "2.PCLC");
        thirdEnemyPCLC = new GeneralLayoutContainer(1062, 308, 120, 160, "3.PCLC");
        // Die Abwurf-Zonen für jeden Spieler
        myDPLC = new GeneralLayoutContainer(1050, 630, 110, 60, "My.DPLC");
        firstEnemyDPLC = new GeneralLayoutContainer(640, 0, 110, 60, "1.DPLC");
        secondEnemyDPLC = new GeneralLayoutContainer(328, 447, 104, 60, "2.DPLC");
        thirdEnemyDPLC = new GeneralLayoutContainer(1198, 169, 106, 60, "3.DPLC");
        // Die Decks für jeden Spieler
        //myDLC = new GeneralLayoutContainer(513,630,110,60,"My.DLC");
        myDLC = new GeneralLayoutContainer(350, 630, 110, 60, "My.DLC");
        firstEnemyDLC = new GeneralLayoutContainer(915, 0, 110, 60, "1.DLC");
        secondEnemyDLC = new GeneralLayoutContainer(328, 169, 104, 60, "2.DLC");
        thirdEnemyDLC = new GeneralLayoutContainer(1198, 446, 106, 60, "3.DLC");
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
        // Die Bestandteile des Spiels wie Hände, Aktionzonen usw. werden dem GameViewWIP hinzugefügt
        gameViewWIP.getChildren().add(handcards);
        gameViewWIP.getChildren().add(firstEnemyHand);
        gameViewWIP.getChildren().add(secondEnemyHand);
        gameViewWIP.getChildren().add(thirdEnemyHand);
        gameViewWIP.getChildren().add(myPCLC);
        gameViewWIP.getChildren().add(firstEnemyPCLC);
        gameViewWIP.getChildren().add(secondEnemyPCLC);
        gameViewWIP.getChildren().add(thirdEnemyPCLC);
        gameViewWIP.getChildren().add(myDPLC);
        gameViewWIP.getChildren().add(firstEnemyDPLC);
        gameViewWIP.getChildren().add(secondEnemyDPLC);
        gameViewWIP.getChildren().add(thirdEnemyDPLC);
        gameViewWIP.getChildren().add(myDLC);
        gameViewWIP.getChildren().add(firstEnemyDLC);
        gameViewWIP.getChildren().add(secondEnemyDLC);
        gameViewWIP.getChildren().add(thirdEnemyDLC);
        selectButton.setVisible(false);
        selectButton.setOnAction(sendChoosenCardResponse);
        gameViewWIP.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                bigCardImageBox.setVisible(false);
            }
        });
    }

    /**
     * Die Aktionskarten werden erstellt und auf dem Spielfeld angezeigt.
     * Die Anzahl der Wertkarten wird angezeigt.
     *
     * @param theList    die IDs der Aktionskarten
     * @param valueCards Die Anzahl der Wertkarten, mit der ID der Karte als Schlüssel
     * @author Ferit, Fenja, Anna
     * @since Sprint 7
     */
    private void initalizeCardFieldImages(ArrayList<Short> theList, Map<Short, Integer> valueCards) {
        ArrayList<ImageView> allImageViews = new ArrayList<>(Arrays.asList(cardPlaceholder1, cardPlaceholder2, cardPlaceholder3, cardPlaceholder4, cardPlaceholder5, cardPlaceholder6, cardPlaceholder7, cardPlaceholder8, cardPlaceholder9, cardPlaceholder10));
        int index = 0;
        valuecardLabels.put((short) 4, countEstateCardLabel);
        valuecardLabels.put((short) 5, countDuchiesCardLabel);
        valuecardLabels.put((short) 6, countProvinceCardLabel);
        valuecardLabels.put((short) 38, countCurseCardLabel);
        //Initialisieren der Aktionskarten
        for (ImageView imageView : allImageViews) {
            String theIdInString = String.valueOf(theList.get(index));
            String imageUrl = "cards/images/" + theIdInString + "_sm.png";
            Image theImage = new Image(imageUrl);
            imageView.setImage(theImage);
            imageView.setId(theIdInString);
            index++;
        }
        //Initialiseren der Anzahl der Wertkarten
        Platform.runLater(() -> {
            for (Short key : valuecardLabels.keySet()) {
                Label l = valuecardLabels.get(key);
                l.setText(String.valueOf(valueCards.get(key)));
            }
        });
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
     * @param actionEvent das Action-Event
     * @author Rike
     * @since Sprint 7
     */
    @FXML
    public void onPlayAllMoneyCardsButtonPressed(ActionEvent actionEvent) {
        playAllMoneyCardsOnHand();
    }

    /**
     * Ereignis wenn der Spieler den "Nein"-Button drückt. Es wird true an den Server zurückgegeben
     *
     * @param actionEvent das ActionEvent
     * @author Darian
     * @since Sprint 8
     */
    @FXML
    public void onYesButtonPressed(ActionEvent actionEvent) {
        optionalAction(true);
    }

    /**
     * Ereignis wenn der Spieler den "Nein"-Button drückt. Es wird false an den Server zurückgegeben
     *
     * @param actionEvent das ActionEvent
     * @author Darian
     * @since Sprint 8
     */
    @FXML
    public void onNoButtonPressed(ActionEvent actionEvent) {
        optionalAction(false);
    }

    /**
     * Ereignis das ausgeführt wird, wenn eine Karte im Shop angeklickt wird.
     * ruft die chosenBuyableCard()-Methode auf
     *
     * @param mouseEvent das Maus-Event
     * @author Rike
     * @since Sprint 5
     */
    @FXML
    public void onBuyableCardClicked(MouseEvent mouseEvent) {
        chosenBuyableCard(mouseEvent);
    }

    /**
     * Die IDs der gesendeten Aktionskarten werden initilaisiert.
     * Die Anzahl der Wertkarten wird in einer Map gespeichert, mit der ID der jeweiligen Karte als Schlüssel.
     *
     * @param msg die Nachricht mit den IDs und der jeweiligen Azahl der Spielkarten
     * @author Anna, Fenja
     * @since Sprint 7
     */
    @Subscribe
    public void onSendCardFieldMessage(SendCardFieldMessage msg) {
        ArrayList<Short> list = new ArrayList<>();
        Map<Short, Integer> valuecards = new HashMap<>();
        for (Short key : msg.getCardField().keySet()) {
            if (key > 6 && key != 38) { //Aktionskarten, ohne Fluchkarte
                list.add(key);
            } else if (key <= 6 && key > 3 || key == 38) { //Wertkarten und Fluchkarte
                valuecards.put(key, msg.getCardField().get(key));
            }
        }
        initalizeCardFieldImages(list, valuecards);
    }

    /**
     * Sobald ein neuer User der Lobby beitritt, wird eine RetrieveAllLobbyUsersRequest gesendet.
     *
     * @param userJoinedLobbyMessage Die Nachricht wenn ein User die Lobby beitritt
     * @author Marvin
     * @since Sprint 3
     */
    @Subscribe
    public void newUser(UserJoinedLobbyMessage userJoinedLobbyMessage) {
        if (userJoinedLobbyMessage.getLobbyID().equals(this.lobbyID)) {
            getInGameUserList(this.lobbyID);
            LOG.debug("Neuer User in der Lobby, LobbyService empfängt Nutzer");
        }
    }

    /**
     * Aktualisiert den loggedInUser sowie die Liste, falls sich der Username geändert hat
     *
     * @param message die Nachricht
     * @author Julia
     * @since Sprint 4
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
        getInGameUserList(this.lobbyID);
    }

    /**
     * Aktualisiert den loggedInUser sowie die Liste, wenn ein Spieler die Lobby (also das Spiel) verlässt.
     *
     * @param message die Nachricht
     * @author Alex
     * @since Sprint 7
     */
    @Subscribe
    public void onUserLeftLobbyMessage(UserLeftLobbyMessage message) {
        if (message.getLobbyID().equals(this.lobbyID)) {
            getInGameUserList(this.lobbyID);
            LOG.debug("A User left the Lobby. Updating Users now.");
        }
    }

    /**
     * Wird bei Erstellung in GameManagement aufgerufen und startet eine Abfrage an den Server für alle User in der Lobby.
     *
     * @author Marvin, Alex
     * @since Sprint 3
     */
    public void getInGameUserList(UUID id) {
        lobbyService.retrieveAllUsersInLobby(id);
    }

    /**
     * Wird aufgerufen, wenn eine AllOnlineUsersInLobbyResponse empfangen wird. Prüft auch, ob aktuell das GameView angezeigt wird.
     *
     * @param response die Antwort aller Lobby-Benutzer
     * @author Marvin, Alex
     * @since Sprint 3
     */
    @Subscribe
    private void onReceiveAllUsersInLobby(AllOnlineUsersInLobbyResponse response) {
        if (response.getLobbyID().equals(this.lobbyID)) {
            LOG.debug("Aktualisieren der Userliste mit " + response.getUsers());

            response.getUsers().forEach(user -> {
                LOG.debug("Füge den folgenden Nutzer der Liste hinzu: " + user.getUsername());
            });

            updateUsersInGame(response.getUsers());
        } else {
            LOG.debug("AllOnlineUsersInLobbyResponse empfangen. Für eigene Lobby aber nicht relevant.");
        }
    }

    /**
     * Die Nachricht die gesendet wird, wenn der Kauf einer Karte erfolgreich war
     * die Karte, sowie die gebrauchten Geldkarten wandert auf den Ablagestapel (Animation)
     * Überprüft ob der Spieler noch Karten der gekauften Art kaufen kann und fügt ggf. das ImageView (kleines Bild) wieder hinzu
     * Wenn die gekauft Karte eine Wertkarte war, wird dessen Anzahl aktualisiert.
     *
     * @param msg die Nachricht
     * @author Devin, Anna, Rike
     * @since Sprint 5
     */
    // TODO: Karte wenn sie gekauft wird, von der richtigen Postition einfliegen lassen. ( Weiter nach rechts)
    @Subscribe
    public void onBuyCardMessage(BuyCardMessage msg) {
        if (msg.getGameID().equals(lobbyID)) {
            if (valuecardLabels.containsKey(msg.getCardID())) {
                Platform.runLater(() -> {
                    valuecardLabels.get(msg.getCardID()).setText(String.valueOf(msg.getCounterCard()));
                });
            }
            ImageView selectedCard = (ImageView) mouseEvent.getSource();
            if (msg.getCounterCard() < 1) {
                selectedCard.setEffect(makeImageDarker);
            }
            if (msg.getCurrentUser().equals(loggedInUser)) {
                String pfad = "cards/images/" + msg.getCardID().toString() + ".png";
                Image picture = new Image(pfad);
                ImageView newCardImage = new ImageView(picture);
                LOG.debug("Der Spieler " + msg.getCurrentUser() + " hat die Karte " + msg.getCardID() + " gekauft.");
                // fügt ein "neues" Bild an der Stelle des alten Bildes im Shop hinzu
                newCardImage.setPreserveRatio(true);
                newCardImage.setFitHeight(107);
                newCardImage.setFitWidth(Math.round(newCardImage.getBoundsInLocal().getWidth()));
                newCardImage.setLayoutX(selectedCard.getLayoutX());
                if (Short.parseShort(selectedCard.getId()) < 7 ||Short.parseShort(selectedCard.getId()) == 38 ) {
                    newCardImage.setLayoutX(selectedCard.getLayoutX() + 450);
                }
                newCardImage.setLayoutY(selectedCard.getLayoutY());
                newCardImage.setId(String.valueOf(msg.getCardID()));
                Platform.runLater(() -> {
                    gameViewWIP.getChildren().add(newCardImage);
                    AnimationManagement.buyCard(newCardImage);
                    gameViewWIP.getChildren().remove(newCardImage);
                    myDPLC.getChildren().add(newCardImage);
                });
                // entfernt die genutzen Geldkarten aus der Aktionszone (wichtig, wenn der User mehr als 1 Kauf hat)
                Platform.runLater(() -> {
                    int money = 0;
                    int GeneralLayoutContainerSize = myPCLC.getChildren().size() - 1;
                    ObservableList<Node> removeMoneyCardList = FXCollections.observableArrayList();
                    for (int i = GeneralLayoutContainerSize; i >= 0; i--) {
                        Node removeCards = myPCLC.getChildren().get(i);
                        if (removeCards.getId().equals("1") || removeCards.getId().equals("2") || removeCards.getId().equals("3")) {
                            money += Integer.parseInt(removeCards.getId());
                            removeMoneyCardList.add(removeCards);
                            myPCLC.getChildren().remove(i);
                            if (money >= msg.getCostCard()) {
                                break;
                            }
                        }
                    }
                    moveCardsToDiscardPile(removeMoneyCardList, false);
                });
            }
        }
    }

    @Subscribe
    public void onOptionalActionRequest(OptionalActionRequest msg){
        if (msg.getGameID().equals(lobbyID) && msg.getPlayer().equals(loggedInUser)){
            Platform.runLater(() -> {
                yesButton.setVisible(true);
                noButton.setVisible(true);
                playAllMoneyCardsButton.setVisible(false);
                infoActualPhase.setText(msg.getTextMessage());
                infoActualPhase.setStyle("-fx-font-size: 12;");
            });
        }
    }

    /**
     * Die Nachricht die angibt ob die Karte gespielt werden konnte.
     * Wenn currentPlayer eine Karte ausspielt, wird die ausgewählte Karte auf das Ausspielfeld gelegt.
     * Wenn ein anderer Spieler eine Karte ausspielt, bekommen wird das für dei anderen Spieler angezeigt.
     *
     * @param msg die Nachricht die vom server gesendet wird, wenn ein Spieler eine Karte ausspielz.
     * @author Devin
     * @since Sprint 6
     */
    @FXML
    @Subscribe
    public void onPlayCardMessage(PlayCardMessage msg) {
        // Falls diese Message an den currentPlayer geschickt wird, wird das ausspielen der Karte angezeigt.
        if (msg.getGameID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            ImageView card = (ImageView) mouseEvent.getTarget();
            if (msg.getIsPlayed()) {
                Platform.runLater(() -> {
                    if (handcards.getChildren().contains(card)) {
                        card.setEffect(null);
                        if (!msg.isRemoveCardAfter()) {
                            AnimationManagement.playCard(card, myPCLC.getChildren().size(), myPCLC);
                            myPCLC.getChildren().add(card);
                            handcards.getChildren().remove(card);
                        } else {
                            AnimationManagement.deleteCard(card);
                        }
                    }
                    if (msg.getHandCardIdAsString().equals("1") || msg.getHandCardIdAsString().equals("2") || msg.getHandCardIdAsString().equals("3")) {
                        usableMoney += Integer.parseInt(msg.getHandCardIdAsString());
                        numberOfMoney.setText(usableMoney + " Geld");
                    }
                });
            } else {
                showAlert(Alert.AlertType.WARNING, "Du kannst die Karte nicht spielen!", "Fehler");
                LOG.debug("Das Spielen der Karte " + msg.getHandCardIdAsString() + " von " + msg.getCurrentUser() + " ist fehlgeschlagen");
            }
        }
        // Falls die Message bei anderen Spielern ankommt, wird ihnen angezeigt, dass ihr Gegner eine Karte spiet.
        if (msg.getGameID().equals(lobbyID) && !msg.getCurrentUser().equals(loggedInUser)) {
            if (msg.getIsPlayed()) {
                List<Short> playerIndexNumbers = new ArrayList<>();
                playerIndexNumbers.add((short) 0);
                playerIndexNumbers.add((short) 1);
                playerIndexNumbers.add((short) 2);
                playerIndexNumbers.add((short) 3);
                if (msg.getGameID().equals(lobbyID) && !msg.getCurrentUser().equals(loggedInUser)) {
                    playerIndexNumbers.remove(msg.getUserPlaceNumber());
                    Card card = new Card(msg.getHandCardIdAsString(), firstEnemyPCLC.getLayoutX(), firstEnemyPCLC.getLayoutY(), firstEnemyPCLC.getHeight());
                    if (playerIndexNumbers.get(0).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            AnimationManagement.playCard((ImageView) firstEnemyHand.getChildren().get(0), firstEnemyPCLC.getChildren().size(), firstEnemyPCLC);
                            firstEnemyHand.getChildren().remove(0);
                            firstEnemyPCLC.getChildren().add(card);
                        });
                    }

                    if (playerIndexNumbers.get(1).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            AnimationManagement.playCard((ImageView) secondEnemyHand.getChildren().get(0), secondEnemyPCLC.getChildren().size(), secondEnemyPCLC);
                            secondEnemyHand.getChildren().remove(0);
                            secondEnemyPCLC.getChildren().add(card);
                        });
                    }
                    if (playerIndexNumbers.get(2).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            AnimationManagement.playCard((ImageView) thirdEnemyHand.getChildren().get(0), thirdEnemyPCLC.getChildren().size(), thirdEnemyPCLC);
                            thirdEnemyHand.getChildren().remove(0);
                            thirdEnemyPCLC.getChildren().add(card);
                        });
                    }
                }
                //TODO: Wenn Aktionen implementiert sind, prüfen ob showAlert noch notwendig ist oder ob Serverseitig bereits bei Scheitern eine Message gesendet wird
            } else {
                showAlert(Alert.AlertType.WARNING, "Du kannst die Karte nicht spielen!", "Fehler");
                LOG.debug("Das Spielen der Karte " + msg.getHandCardIdAsString() + " von " + msg.getCurrentUser() + " ist fehlgeschlagen");
            }
        }
    }

    /**
     * Wenn ein anderer Spieler eine Karte von der Hand entsorgt, wird dies den anderen Spielern angezeigt.
     * Wenn der Spieler eine Karte oder mehrere Karten auswählen darf, werden alle nicht auswählbaren verdunkelt.
     *
     * @param req       Die Request, die vom server gesendet wird, wenn der jeweilige Spieler eine Karte entsorgt.
     * @author Devin, Fenja, Anna
     * @since Sprint 7
     */
    @FXML
    @Subscribe
    public void onChooseCardRequest (ChooseCardRequest req) {
        if (req.getGameID().equals(lobbyID) && req.getPlayer().equals(loggedInUser)) {
            choosenCardsId.clear();
            choosenCards.clear();
            ImageView card = (ImageView) mouseEvent.getTarget();
            numberOfCardsToChoose = req.getCount();
            directHand = req.getDirectHand();
            currentInfoText = infoActualPhase.getText();
            skipPhaseButton.setDisable(true);
            if (req.getSource() == AbstractPlayground.ZoneType.HAND) {
                for (Node n : handcards.getChildren()) {
                    n.setEffect(null);
                }
                selectButton.setVisible(true);
                playAllMoneyCardsButton.setVisible(false);
                Platform.runLater(() -> {
                    handcards.getChildren().forEach((n) -> {
                        if (!card.equals(n)) {
                            n.removeEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                            if (req.getCards().contains(Short.parseShort(n.getId()))) {
                                n.addEventHandler(MouseEvent.MOUSE_CLICKED, discardCardEventHandler);
                            }
                        }
                    });
                });
                Platform.runLater(() -> {
                    if (numberOfCardsToChoose != 255) {
                        infoActualPhase.setText(numberOfCardsToChoose + " Karte(n) entsorgen");
                    } else {
                        infoActualPhase.setText("Lege beliebig viele Karten ab ");
                    }
                });
            }
            if (req.getSource().equals(AbstractPlayground.ZoneType.BUY)) {
                notChosenCard.setBrightness(-0.7);
                for (int i = 0; i < 10; i++) {
                    ImageView iv = (ImageView) shopTeppich.getChildren().get(i);
                    if (!req.getCards().contains(Short.valueOf(iv.getId()))) {
                        iv.setEffect(notChosenCard);
                    }
                }
                for (int i = 0; i < 7; i++) {
                    ImageView iv = (ImageView) valueCardsBox.getChildren().get(i);
                    if (!req.getCards().contains(Short.valueOf(iv.getId()))) {
                        iv.setEffect(notChosenCard);
                    }
                }
                Platform.runLater(() -> {
                    infoActualPhase.setText("Nimm dir eine Karte vom Spielfeld.");
                    infoActualPhase.setStyle("-fx-font-size: 15");
                });
                chooseCardBecauseOfActionCard = true;
            }
        }
    }

    /**
     * Wenn ein anderer Spieler sich in der ClearPhase befindet, wird das Entsorgen dessen Handkarten und ausgespielten Karten den anderen Spielern angezeigt
     *
     * @param msg Die Message die vom server gesendet wird, wenn ein anderer Spieler eine Karte sich in der ClearPhase befindet.
     * @author Devin
     * @since Sprint 7
     */
    @FXML
    @Subscribe
    public void onStartClearPhaseMessage(StartClearPhaseMessage msg) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Wenn ein anderer Spieler eine ClearPhaseMessage erhählt wird dies den anderen Spielern
                // angezeigt, indem deren Repräsentation des Spieler seine Handkarten und ausgespielten Karten auf den Ablagestapel legt.
                if (msg.getGameID().equals(lobbyID) && !msg.getCurrentUser().equals(loggedInUser)) {
                    List<Short> playerIndexNumbers = new ArrayList<>();
                    playerIndexNumbers.add((short) 0);
                    playerIndexNumbers.add((short) 1);
                    playerIndexNumbers.add((short) 2);
                    playerIndexNumbers.add((short) 3);
                    playerIndexNumbers.remove(msg.getUserPlaceNumber());
                    if (playerIndexNumbers.get(0).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            firstEnemyHand.getChildren().clear();
                            firstEnemyPCLC.getChildren().clear();
                        });
                        for (int i = 0; i < 5; i++) {
                            Card card = new Card("card_back", firstEnemyHand.getLayoutX(), firstEnemyHand.getLayoutY(), 80);
                            Platform.runLater(() -> {
                                firstEnemyHand.getChildren().add(card);
                            });
                        }
                    }
                    if (playerIndexNumbers.get(1).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            secondEnemyHand.getChildren().clear();
                            secondEnemyPCLC.getChildren().clear();
                        });
                        for (int i = 0; i < 5; i++) {
                            Platform.runLater(() -> {
                                Card card = new Card("card_back", secondEnemyHand.getLayoutX(), secondEnemyHand.getLayoutY(), 80);
                                secondEnemyHand.getChildren().add(card);
                            });
                        }
                    }
                    if (playerIndexNumbers.get(2).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            thirdEnemyHand.getChildren().clear();
                            thirdEnemyPCLC.getChildren().clear();
                        });
                        for (int i = 0; i < 5; i++) {
                            Platform.runLater(() -> {
                                Card card = new Card("card_back", thirdEnemyHand.getLayoutX(), thirdEnemyHand.getLayoutY(), 80);
                                thirdEnemyHand.getChildren().add(card);
                            });
                        }
                    }
                }
                return null;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Kümmert sich nur um die eigene Animation (von der Hand zum Discard Pile)
     *
     * @param msg die Message
     */
    @Subscribe
    public void onStartClearPhaseMessageOwnHand(StartClearPhaseMessage msg) {
        onStartPhase(msg.getGameID(), msg.getCurrentUser(), msg);
        if (msg.getGameID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            Platform.runLater(() -> {
                moveCardsToDiscardPile(handcards.getChildren(), false);
                moveCardsToDiscardPile(myPCLC.getChildren(), true);
            });
        }
    }

    /**
     * Fügt die Karte aus der DiscardPileLastCardMessage dem Ablagestapel hinzu.
     *
     * @param msg Die Nachricht
     * @author Timo
     * @since Sprint 6
     */
    @Subscribe
    public void onDiscardPileLastCardMessage(DiscardPileLastCardMessage msg) {
        Platform.runLater(() -> {
            if (msg.getGameID().equals(this.gameManagement.getID()) && msg.getUser().equals(this.loggedInUser)) {
                String pfad = "cards/images/" + msg.getCardID() + ".png";
                Image picture = new Image(pfad);
                ImageView card = new ImageView(picture);
                card.setFitHeight(107);
                card.setPreserveRatio(true);
                card.setFitWidth(Math.round(card.getBoundsInLocal().getWidth()));
                myDPLC.getChildren().add(card);
            }
        });
    }

    /**
     * Zeigt die Karten auf der Hand in der GameView an
     * setzt den Zustand des "Alle Geldkarten spielen" auf anklickbar, wenn der User Geldkarten auf die Hand bekommt
     *
     * @author Devin S., Anna, Rike
     * @since Sprint 5
     */
    @FXML
    @Subscribe
    public void onDrawHandMessage(DrawHandMessage message) {
        if (message.getUser().equals(this.loggedInUser)) {
            numberOfPlayersInGame = message.getNumberOfPlayers();
            Platform.runLater(() -> {
                if (lobbyID.equals(message.getTheLobbyID())) {
                    handCardIDs = message.getCardsOnHand();
                    handCardIDs.forEach((n) -> {
                        String pfad = "cards/images/" + n + ".png";
                        Image picture = new Image(pfad);
                        ImageView card = new ImageView(picture);
                        card.setFitHeight(107);
                        card.setPreserveRatio(true);
                        card.setId(n.toString());
                        card.setFitWidth(Math.round(card.getBoundsInLocal().getWidth()));
                        myDLC.getChildren().add(card);
                        synchronized (handcards) {
                            AnimationManagement.addToHand(card, handcards.getChildren().size());
                            myDLC.getChildren().remove(card);
                            handcards.getChildren().add(card);
                        }
                        card.addEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                    });
                    if (numberOfPlayersInGame == 1) {
                        return;
                    }
                    if (message.isInitialHand()) {
                        String pfad = "cards/images/card_back.png";
                        Image picture = new Image(pfad);
                        for (int i = 0; i < 5; i++) {
                            ImageView card = new ImageView(picture);
                            ImageView card2 = new ImageView(picture);
                            ImageView card3 = new ImageView(picture);
                            card.setFitHeight(80);
                            card.setPreserveRatio(true);
                            card.setId("back");
                            card.setFitWidth(Math.round(card.getBoundsInLocal().getWidth()));
                            card2.setFitHeight(card.getFitWidth());
                            card2.setPreserveRatio(true);
                            card2.setId("back");
                            card2.setFitWidth(card.getFitHeight());
                            card3.setFitHeight(card.getFitWidth());
                            card3.setPreserveRatio(true);
                            card3.setId("back");
                            card3.setFitWidth(card.getFitHeight());
                            firstEnemyHand.getChildren().add(card);
                            if (numberOfPlayersInGame >= 3) {
                                secondEnemyHand.getChildren().add(card2);
                                if (numberOfPlayersInGame == 4) {
                                    thirdEnemyHand.getChildren().add(card3);
                                }
                            }
                        }
                    }

                }
            });
        }
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
                        myDPLC.getChildren().clear();
                    }
                });
            }
        }
    }

    /**
     * Implementiert das Verhalten bei Erhalten einer StartActionPhaseMessage
     * die onStartPhase Methode wird aufgerufen
     *
     * @param msg die Nachricht
     * @author Rike
     * @since Sprint 7
     */
    @Subscribe
    public void onStartActionPhaseMessage(StartActionPhaseMessage msg) {
        onStartPhase(msg.getGameID(), msg.getUser(), msg);
    }

    /**
     * Impelementiert das Verhalten bei Erhalten einer StartBuyPhaseMessage
     * die onStartPhase Methode wird aufgerufen
     *
     * @param msg die Nachricht
     * @author Rike
     * @since Sprint 7
     */
    @Subscribe
    public void onStartBuyPhaseMessage(StartBuyPhaseMessage msg) {
        onStartPhase(msg.getGameID(), msg.getUser(), msg);
    }

    /**
     * Aktualisiert die Anzeige für Aktion, Kauf und Geld
     *
     * @param msg die Nachricht
     * @author Rike
     * @since Sprint 7
     */
    @Subscribe
    public void onInfoPlayDisplayMessage(InfoPlayDisplayMessage msg) {
        if (msg.getLobbyID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            Platform.runLater(() -> {
                if (msg.getAvailableBuy() == 1) {
                    numberOfBuy.setText(msg.getAvailableBuy() + " Kauf");
                }
                if (msg.getAvailableBuy() != 1) {
                    numberOfBuy.setText(msg.getAvailableBuy() + " Käufe");
                }
                if (msg.getAvailableAction() == 1) {
                    numberOfAction.setText(msg.getAvailableAction() + " Aktion");
                }
                if (msg.getAvailableAction() != 1) {
                    numberOfAction.setText(msg.getAvailableAction() + " Aktionen");
                }
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
     * Die Karten werden von einem Ort zum Anderen bewegt.
     * Beim Ablagetsapel und der Hand können die Karten direkt aus dem Container geholt und bewegt werden, bei den
     * anderen Zonen müssen die Karten erst erstellt werden.
     *
     * @param msg die MoveCardMessage
     * @author Anna
     * @since Sprint 9
     */
    @Subscribe
    public void onMoveCardMessage(MoveCardMessage msg) {
        if (msg.getGameID().equals(lobbyID) && msg.getPlayer().equals(loggedInUser)) {
            AbstractPlayground.ZoneType source = msg.getMove().getCardSource();
            AbstractPlayground.ZoneType destination = msg.getMove().getCardDestination();
            switch (source) {
                case HAND:
                case DISCARD:
                    ArrayList<ImageView> cardsToMove = new ArrayList<>();
                    for (de.uol.swp.common.game.card.Card c : msg.getMove().getCardsToMove()) {
                        int i = 0;
                        ImageView iv = getImageViewFromRegion(getRegionFromZoneType(source, c.getId()), c.getId(), i);
                        while (cardsToMove.contains(iv)) {
                            i++;
                            iv = getImageViewFromRegion(getRegionFromZoneType(source, c.getId()), c.getId(), i);
                        }
                        iv.setLayoutY(107);
                        cardsToMove.add(iv);
                        ImageView finalIv = iv;
                        Platform.runLater(() -> {
                            playAnimation(destination, finalIv, source);
                        });
                    }
                    break;
                case BUY:
                    for (de.uol.swp.common.game.card.Card c : msg.getMove().getCardsToMove()) {
                        ImageView card = getImageViewFromRegion(getRegionFromZoneType(source, c.getId()), c.getId());
                        ImageView card2 = new Card(card.getId(), card.getLayoutX(), card.getLayoutY(), 107);
                        if (c.getId() < 7 || c.getId() == 38 ) {
                            card2.setLayoutX(card.getLayoutX() + 450);
                        }
                        Platform.runLater(() -> {
                            gameViewWIP.getChildren().add(card2);
                            playAnimation(destination, card2, source);
                        });
                    }
                    break;
                case DRAW:
                    for (de.uol.swp.common.game.card.Card c : msg.getMove().getCardsToMove()) {
                        ImageView card2 = new Card(String.valueOf(c.getId()), 0, 107, 107);
                        Platform.runLater(() -> {
                            myDLC.getChildren().add(card2);
                            playAnimation(destination, card2, source);
                        });
                    }
                    break;
                case TRASH:
                    for (de.uol.swp.common.game.card.Card c : msg.getMove().getCardsToMove()) {
                        ImageView card2 = new Card(String.valueOf(c.getId()), 300, 0, 107);
                        Platform.runLater(() -> {
                            gameViewWIP.getChildren().add(card2);
                            playAnimation(destination, card2, source);
                        });
                    }
            }
        }
    }

    /**
     * Die usersView Liste wird geupdatet.
     * Äquivalent zu MainMenuPresenter.updateUsersList.
     *
     * @param userList die neue Userliste
     * @author Marvin
     * @since Sprint 3
     */
    private void updateUsersList(Set<User> userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (usersView != null) {
                if (users == null) {
                    users = FXCollections.observableArrayList();
                    usersView.setItems(users);
                } else {
                    LOG.debug("Keine User in der Lobby.");
                }
                users.clear();
                userList.forEach(u -> users.add(u.getUsername()));
            }
        });
    }

    /**
     * Aktualisiert die Spieler auf dem Spielfeld.
     * Geht von der Reihenfolge der AllOnlineUsersInLobbyResponse aus.
     * Setzt die Sichtbarkeit der Elemente auf true oder false, je nachdem wie viele Spieler noch im Spiel sind.
     * Die Methode versteckt auch Spielerplätze wieder, falls ein Spieler das Spiel verlässt.
     *
     * @param usersList Die Liste der Spieler im Spiel bzw. in der Lobby.
     * @author Alex
     * @since Sprint 7
     */
    private void updateEnemiesOnBoard(Set<User> usersList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            int enemyCounter = 0;
            for (User u : usersList) {
                if (loggedInUser != null && u.getUsername().equals(loggedInUser.getUsername())) {
                    //skip self
                } else {
                    enemyCounter++;
                    if (enemyCounter == 1) {
                        player1_label.setText(u.getUsername());
                        player1_label.setVisible(true);
                        avatar_icon_top.setVisible(true);
                    } else if (enemyCounter == 2) {
                        player2_label.setText(u.getUsername());
                        player2_label.setVisible(true);
                        avatar_icon_left.setVisible(true);
                    } else if (enemyCounter == 3) {
                        player3_label.setText(u.getUsername());
                        player3_label.setVisible(true);
                        avatar_icon_right.setVisible(true);
                    }
                }
            }
            if (enemyCounter == 1) {
                player2_label.setVisible(false);
                player3_label.setVisible(false);
                avatar_icon_left.setVisible(false);
                avatar_icon_right.setVisible(false);
            } else if (enemyCounter == 2) {
                player3_label.setVisible(false);
                avatar_icon_right.setVisible(false);
            }
        });
    }

    /**
     * Fasst Funktionen zusammen, welche die Spielerlisten/Spielernamen aktualisieren.
     *
     * @param usersList Die User Liste als Set
     * @author Alex
     * @since Sprint 7
     */
    private void updateUsersInGame(Set<User> usersList) {
        if (usersList != null && !usersList.isEmpty()) {
            updateEnemiesOnBoard(usersList);
            updateUsersList(usersList);
            LOG.debug("Spielerliste erfolgreich aktualisiert - updateUsersInGame Methode");
        } else {
            LOG.error("Aktualisierung der Spielerliste fehlgeschlagen - updateUsersInGame Methode");
        }
    }

    /**
     * Skipt die aktuelle Phase des Spielers zur nächsten.
     *
     * @author Devin S.
     * @since Sprint 6
     */
    @FXML
    public void onSkipPhaseButtonPressed(ActionEvent actionEvent) {
        gameManagement.getGameService().skipPhase(loggedInUser, lobbyID);
    }

    /**
     * Methode, die beim Anklicken einer Handkarte ausgeführt wird.
     * Rechtsklick -> großes Bild
     * Linksklick -> playCardRequest wird gestellt
     *
     * @param gameID       Die ID des Spiels
     * @param loggedInUser der User der gerade eingelogt im Spiel ist und die Karte ausgewählt hat.
     * @param pfad         Der Pfad zum entsprechendem Vollbild
     * @param id           Die ID der Karte
     * @param card         Die ImageView der ausgewählten Karte
     * @param e            Das MouseEvent, das zum anlicken der Karte zuständig ist.
     * @author Devin, Fenja, Anna
     * @since Sprint 6
     */
    private void playChoosenCard(UUID gameID, User loggedInUser, String pfad, Short id, ImageView card, MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) {
            bigCardImage.setImage(new Image(pfad));
            buyCardButton.setVisible(false);
            bigCardImageBox.setVisible(true);
        } else {
            if (id > 6 && id != 38 && card.getParent() == handcards) { //nur Aktionskarten, ohne Fluchkarte & nur Karten, welche noch in der Hand zone sind.
                bigCardImageBox.setVisible(false);
                for (Node a : handcards.getChildren()) {
                    ImageView b = (ImageView) a;
                    if (b.equals(card)) {
                        mouseEvent = e;
                        gameManagement.getGameService().playCard(gameID, loggedInUser, id);
                    }
                }
            }
        }
    }

    /**
     * Methode, die beim Anklicken einer Handkarte ausgeführt wird.
     * Rechtsklick -> großes Bild
     * Linksklick -> Karte auf den Ablagestapel legen wird gestellt
     *
     * @param gameID       Die ID des Spiels
     * @param loggedInUser der User der gerade eingelogt im Spiel ist und die Karte ausgewählt hat.
     * @param pfad         Der Pfad zum entsprechendem Vollbild
     * @param id           Die ID der Karte
     * @param card         Die ImageView der ausgewählten Karte
     * @param e            Das MouseEvent, das zum anlicken der Karte zuständig ist.
     * @author Devin
     * @since Sprint 7
     */
    private void discardChoosenCard(UUID gameID, User loggedInUser, String pfad, Short id, ImageView card, MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY) {
            bigCardImage.setImage(new Image(pfad));
            buyCardButton.setVisible(false);
            bigCardImageBox.setVisible(true);
        } else {
            if (!choosenCards.contains(card)) {
                choosenCards.add(card);
                card.setEffect(makeImageDarker);
                bigCardImageBox.setVisible(false);
                if(numberOfCardsToChoose != 255) {
                    numberOfCardsToChoose -= 1;
                    Platform.runLater(() -> {
                        infoActualPhase.setText(numberOfCardsToChoose + " Karten entsorgen");
                    });
                }
            } else {
                choosenCards.remove(card);
                card.setEffect(null);
                if(numberOfCardsToChoose != 255) {
                    numberOfCardsToChoose += 1;
                    Platform.runLater(() -> {
                        infoActualPhase.setText(numberOfCardsToChoose + " Karten entsorgen");
                    });
                }
            }
        }
        if(numberOfCardsToChoose == 0) {
            for (ImageView card2 : choosenCards) {
                choosenCardsId.add(Short.parseShort(card2.getId()));
            }
            handcards.getChildren().forEach((n) -> {
                n.removeEventHandler(MouseEvent.MOUSE_CLICKED, discardCardEventHandler);
                n.addEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                n.setEffect(null);
            });
            gameService.chooseCardResponse(gameID, loggedInUser, choosenCardsId, directHand);
            selectButton.setVisible(false);
            playAllMoneyCardsButton.setVisible(true);
            skipPhaseButton.setDisable(false);
            Platform.runLater(() -> {
                infoActualPhase.setText(currentInfoText);
            });
        }
    }

    /**
     * Die Karten werden zum Ablagestapel bewegt
     *
     * @param children    Das children von dem Karten Stapel
     * @param actionCards true wenn die Karten in der Aktionszone liegen
     * @author Darian
     * @since Sprint 7
     */
    private void moveCardsToDiscardPile(ObservableList<Node> children, boolean actionCards) {
        for (Node c : children) {
            Platform.runLater(() -> {
                ImageView card = (ImageView) c;
                String pfad = "cards/images/" + card.getId() + ".png";
                if (actionCards) {
                    card.setLayoutX(c.getLayoutX() - 400);
                    card.setLayoutY(0);//433);
                } else {
                    card.setLayoutX(c.getLayoutX() - 845);
                    card.setLayoutY(145);
                }
                card.setId(String.valueOf(c));
                myDPLC.getChildren().add(card);
                AnimationManagement.clearCards(card, myDPLC);
                children.remove(c);
            });
        }
    }

    /**
     * Hilfsmethode für onBuyableCardClicked() und onBuyCardMessage()
     * Rechtsklick auf Karte -> Großes Bild der Karte wird angezeigt.
     * kauf-Button wird hinzugefügt -> BuyCardRequest wird gestellt
     * Linksklick auf Karte -> BuyCardRequest wird gestellt
     *
     * @param mouseEvent das Event
     * @author Fenja, Anna, Rike
     * @since Sprint 5
     */
    private void chosenBuyableCard(MouseEvent mouseEvent) {
        ImageView cardImage = (ImageView) mouseEvent.getSource();
        //Karte hat noch keinen Effekt gesetzt bekommen, ist also noch im Shop vorhanden
        if (cardImage.getEffect() != null) {
            return;
        }
        if (mouseEvent.getButton() != MouseButton.PRIMARY) {
            String cardID = cardImage.getId();
            String PathCardLargeView = "cards/images/" + cardID + ".png";
            bigCardImage.setImage(new Image(PathCardLargeView));
            // Aktion hinter dem Kauf-Button
            buyCardButton.setVisible(true);
            buyCardButton.setOnAction(event -> {
                bigCardImageBox.setVisible(false);
                if (playAllMoneyCardsButton.isDisable() && playAllMoneyCardsButton.isVisible()) {
                    BuyCardRequest req = new BuyCardRequest(lobbyID, loggedInUser, Short.valueOf(cardID));
                    gameService.buyCard(req);
                    this.mouseEvent = mouseEvent;
                } else {
                    if (!playAllMoneyCardsButton.isVisible()) {
                        showAlert(Alert.AlertType.INFORMATION, "Du bist nicht dran!", "Fehler");
                    } else {
                        //TODO: ggf. anpassen, wenn man auch Karten kaufen kann ohne sein Geld vorher gespielt zu haben
                        showAlert(Alert.AlertType.INFORMATION, "Du musst erst deine Geldkarten ausspielen!", "Fehler");
                    }
                }
            });
            bigCardImageBox.setVisible(true);
            bigCardImageBox.toFront();
        } else {
            String cardID = cardImage.getId();
            bigCardImageBox.setVisible(false);
            if (playAllMoneyCardsButton.isVisible() && playAllMoneyCardsButton.isDisable()) {
                if (chooseCardBecauseOfActionCard) {
                    gameService.chooseCardResponse(lobbyID, loggedInUser, new ArrayList<>(Collections.singletonList(Short.valueOf(cardID))), directHand);
                    for (int i = 0; i < 10; i++) {
                        ImageView iv = (ImageView) shopTeppich.getChildren().get(i);
                        if (iv.getEffect() == notChosenCard) {
                            iv.setEffect(null);
                        }
                    }
                    for (int i = 0; i < 7; i++) {
                        ImageView iv = (ImageView) valueCardsBox.getChildren().get(i);
                        if (iv.getEffect() == notChosenCard) {
                            iv.setEffect(null);
                        }
                    }
                    chooseCardBecauseOfActionCard = false;
                    skipPhaseButton.setDisable(false);
                    Platform.runLater(() -> {
                        infoActualPhase.setText(currentInfoText);
                        infoActualPhase.setStyle("-fx-font-weight: bold; -fx-font-size: 18");
                    });
                    return;
                }
                BuyCardRequest req = new BuyCardRequest(lobbyID, loggedInUser, Short.valueOf(cardID));
                gameService.buyCard(req);
                this.mouseEvent = mouseEvent;
            } else {
                if (!playAllMoneyCardsButton.isVisible()) {
                    showAlert(Alert.AlertType.INFORMATION, "Du bist nicht dran!", "Fehler");
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Du musst erst deine Geldkarten ausspielen!", "Fehler");
                }
            }
        }
    }

    /**
     * Hier werden alle Geldkarten, die sich auf der Hand befinden, ausgespielt.
     * Der playAllMoneyCardsButton wird anschließend auf nicht anklickbar gesetzt
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
                        AnimationManagement.playCard(card, myPCLC.getChildren().size(), myPCLC);
                        handcards.getChildren().remove(c);
                        myPCLC.getChildren().add(card);
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
     * @author Rike, Anna, Paula
     * @since Sprint 7
     */
    private void onStartPhase(UUID gameID, User user, AbstractServerMessage msg) {
        if (gameID.equals(lobbyID)) {
            Platform.runLater(() -> {
                if (user.equals(loggedInUser)) {
                    playAllMoneyCardsButton.setVisible(true);
                    if (msg instanceof StartActionPhaseMessage) {
                        playAllMoneyCardsButton.setDisable(true);
                        skipPhaseButton.setDisable(false);
                        infoActualPhase.setText("Du darfst Aktionen spielen.");
                        for (Node n : handcards.getChildren()) {
                            if (Integer.parseInt(n.getId()) < 4) {
                                n.setEffect(makeImageDarker);
                            }
                        }
                    }
                    if (msg instanceof StartBuyPhaseMessage) {
                        playAllMoneyCardsButton.setDisable(false);
                        skipPhaseButton.setDisable(false);
                        infoActualPhase.setText("Du darfst Karten kaufen.");
                        for (Node n : handcards.getChildren()) {
                            if (Integer.parseInt(n.getId()) > 6) {
                                n.setEffect(makeImageDarker);
                            }
                            if (Integer.parseInt(n.getId()) < 4) {
                                n.setEffect(null);
                            }
                        }
                    }
                    if (msg instanceof StartClearPhaseMessage) {
                        playAllMoneyCardsButton.setDisable(true);
                        skipPhaseButton.setDisable(false);
                        infoActualPhase.setText("Clearphase");
                        for (Node n : handcards.getChildren()) {
                            if (Integer.parseInt(n.getId()) > 6) {
                                n.setEffect(null);
                            }
                        }
                    }
                } else {
                    playAllMoneyCardsButton.setVisible(false);
                    skipPhaseButton.setDisable(true);
                    infoActualPhase.setText("Du bist nicht dran.");
                }
            });
        }
    }

    /**
     * Die Region, die zu der übergebenen Zone gehört, wird zurückgegeben.
     *
     * @param zoneType die Zone
     * @param cardID   id der Karte in der Region
     * @author Anna
     * @since Sprint 9
     */
    public Region getRegionFromZoneType(AbstractPlayground.ZoneType zoneType, short cardID) {
        switch (zoneType) {
            case TRASH:
                break;
            case HAND:
                return handcards;
            case BUY:
                if (cardID > 6 && cardID != 38) {
                    return shopTeppich;
                } else {
                    return valueCardsBox;
                }
            case DRAW:
                return myDLC;
            case DISCARD:
                return myDPLC;
        }
        return null;
    }

    /**
     * Die zur Zielzone passende Animation wird ausgeführt und die Karte wird auch zur neuen Zone hinzugefügt und aus
     * der alten Zone entfernt
     *
     * @param destination die Zielzone
     * @param card        die zu bewegende Karte
     * @param source      die ursprüngliche Zone
     * @author Anna
     * @since Sprint 9
     */
    public void playAnimation(AbstractPlayground.ZoneType destination, ImageView card, AbstractPlayground.ZoneType source) {
        switch (destination) {
            case TRASH:
                AnimationManagement.deleteCard(card);
                return;
            case HAND:
                AnimationManagement.addToHand(card, handcards.getChildren().size());
                handcards.getChildren().add(card);
                break;
            case DISCARD:
                AnimationManagement.buyCard(card);
                myDPLC.getChildren().add(card);
                break;
            default:
                LOG.debug("Die Bewegung zur Zone " + destination + " wurde noch nicht implementiert");
        }
        switch (source) {
            case TRASH:
            case BUY:
                gameViewWIP.getChildren().remove(card);
                break;
            case DRAW:
                myDLC.getChildren().remove(card);
                break;
            case DISCARD:
                myDPLC.getChildren().remove(card);
                break;
            case HAND:
                handcards.getChildren().remove(card);
                break;
        }
    }

    /**
     * Die erste Karte mit passender ID, die aus der übergebenen Region stammt, wird zurückgegeben.
     *
     * @param region die Region, in der die Karte sich befindet
     * @param id     die ID der Karte
     * @author Anna
     * @since Sprint 9
     */
    public ImageView getImageViewFromRegion(Region region, short id) {
        return (ImageView) region.getChildrenUnmodifiable().stream().filter(c -> Short.parseShort(c.getId()) == id).findFirst().get();
    }

    /**
     * Die erste Karte mit passender ID und Index, die aus der übergebenen Region stammt, wird zurückgegeben.
     *
     * @param region die Region, in der die Karte sich befindet
     * @param id     die ID der Karte
     * @param i      der Index
     * @author Anna
     * @since Sprint 9
     */
    public ImageView getImageViewFromRegion(Region region, short id, int i) {
        return (ImageView) region.getChildrenUnmodifiable().stream().filter(c -> Short.parseShort(c.getId()) == id).toArray()[i];
    }

    /**
     * Die Antwort auf die OptionalActionRequest wird zum Server gesendet und die Buttons verschwinden wieder.
     *
     * @param answer Die Entscheidung des Spielers
     * @author Darian
     * @since Sprint 8
     */
    private void optionalAction(boolean answer) {
        gameManagement.getGameService().optionalAction(loggedInUser, lobbyID, answer);
        Platform.runLater(() -> {
            yesButton.setVisible(false);
            noButton.setVisible(false);
            playAllMoneyCardsButton.setVisible(true);
            infoActualPhase.setStyle("-fx-font-size: 17; -fx-font-weight: bold;");
        });
    }
}