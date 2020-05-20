package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.container.DeckLayoutContainer;
import de.uol.swp.client.game.container.DiscardPileLayoutContainer;
import de.uol.swp.client.game.container.HandcardsLayoutContainer;
import de.uol.swp.client.game.container.PlayedCardLayoutContainer;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.game.messages.*;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
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
    private Short numberOfPlayersInGame;

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

    private final HandcardsLayoutContainer handcards;
    private final HandcardsLayoutContainer firstEnemyHand;
    private final HandcardsLayoutContainer secondEnemyHand;
    private final HandcardsLayoutContainer thirdEnemyHand;
    private final PlayedCardLayoutContainer myPCLC;
    private final PlayedCardLayoutContainer firstEnemyPCLC;
    private final PlayedCardLayoutContainer secondEnemyPCLC;
    private final PlayedCardLayoutContainer thirdEnemyPCLC;
    private final DiscardPileLayoutContainer myDPLC;
    private final DiscardPileLayoutContainer firstEnemyDPLC;
    private final DiscardPileLayoutContainer secondEnemyDPLC;
    private final DiscardPileLayoutContainer thirdEnemyDPLC;
    private final DeckLayoutContainer myDLC;
    private final DeckLayoutContainer firstEnemyDLC;
    private final DeckLayoutContainer secondEnemyDLC;
    private final DeckLayoutContainer thirdEnemyDLC;

    private ObservableList<String> users;
    private final GameService gameService;
    private MouseEvent mouseEvent;
    private final ChatViewPresenter chatViewPresenter;
    private final Injector injector;
    private final GameManagement gameManagement;

    private PathTransition pathTransition;
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
        // Die Hände für jeden Spieler
        handcards = new HandcardsLayoutContainer(575, 630, 110, 420, "My.HCLC");
        firstEnemyHand = new HandcardsLayoutContainer(575, 110, 110, 420,"1.HCLC");
        secondEnemyHand = new HandcardsLayoutContainer(300, 308, 105, 215,"2.HCLC");
        thirdEnemyHand = new HandcardsLayoutContainer(1070, 308, 105, 215,"3.HCLC");
        // Die Aktion-Zonen für jeden Spieler
        myPCLC = new PlayedCardLayoutContainer(960, 480, 100, 200, "My.PCLC");
        firstEnemyPCLC = new PlayedCardLayoutContainer(700, 150,100, 200,"1.PCLC");
        secondEnemyPCLC = new PlayedCardLayoutContainer(360, 308,107, 215, "2.PCLC");
        thirdEnemyPCLC = new PlayedCardLayoutContainer(1012, 308, 105, 215,"3.PCLC");
        // Die Abwerf-Zonen für jeden Spieler
        myDPLC = new DiscardPileLayoutContainer(997, 630, 110,60, "My.DPLC");
        firstEnemyDPLC = new DiscardPileLayoutContainer(513,0,110,60,"1.DPLC");
        secondEnemyDPLC = new DiscardPileLayoutContainer(328,447,104,60,"2.DPLC");
        thirdEnemyDPLC = new DiscardPileLayoutContainer(1198,169,106,60,"3.DPLC");
        // Die Decks für jeden Spieler
        //myDLC = new DeckLayoutContainer(513,630,110,60,"My.DLC");
        myDLC = new DeckLayoutContainer(0,630,110,60,"My.DLC");
        firstEnemyDLC = new DeckLayoutContainer(997,0,110,60,"1.DLC");
        secondEnemyDLC = new DeckLayoutContainer(328,169,104,60,"2.DLC");
        thirdEnemyDLC = new DeckLayoutContainer(1198,446,106,60,"3.DLC");

        this.gameService = gameService;
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

    public void showAlert(Alert.AlertType type, String message, String title) {
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
        showAlert(Alert.AlertType.CONFIRMATION, " ", "Möchtest du wirklich aufgeben?");
    }

    /**
     * Ereignis das ausgeführt wird, wenn auf eine Karte im Shop angeklickt wird.
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
            getInGameUserList(this.lobbyID);
            LOG.debug("Neuer User in der Lobby, LobbyService empfängt Nutzer");
        }
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
        getInGameUserList(this.lobbyID);
    }

    /**
     * Aktualisiert den loggedInUser sowie die Liste, wenn ein Spieler die Lobby (also das Spiel) verlässt.
     *
     * @param message
     * @author Alex
     * @since Sprint7
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
     * @since Sprint3
     */
    public void getInGameUserList(UUID id) {
        lobbyService.retrieveAllUsersInLobby(id);
    }


    /**
    *
    * Wird aufgerufen, wenn eine AllOnlineUsersInLobbyResponse empfangen wird. Prüft auch, ob aktuell das GameView angezeigt wird.
    *
    * @param response die Antwort aller Lobby-Benutzer
    * @author Marvin, Alex
    * @since Sprint3
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
     * Die Nachricht die angibt, ob der Kauf einer Karte erfolgreich war oder nicht.
     * War der Kauf erfolgreich wandert die Karte auf den Ablagestapel (Animation)
     * Überprüft ob die Spieler noch Karten der gekauften Art kaufen können und fügt ggf. das ImageView (kleines Bild) wieder hinzu
     *
     * @param msg die Nachricht
     * @author Rike, Devin, Anna
     * @since Sprint 5
     */
    // TODO: Karte wenn sie gekauft wird, von der richtigen Postition einfliegen lassen. ( Weiter nach rechts)
    @Subscribe
    public void onBuyCardMessage(BuyCardMessage msg) {
        if (msg.getLobbyID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            if (msg.isBuyCard()) {
                ImageView selectedCard = (ImageView) mouseEvent.getSource();
                String pfad = "cards/images/" + msg.getCardID().toString() + ".png";
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
                    myDPLC.getChildren().add(newCardImage);
                });
                if (msg.getCounterCard() < 1) {
                    ColorAdjust makeImageDarker = new ColorAdjust();
                    makeImageDarker.setBrightness(-0.7);
                    selectedCard.setEffect(makeImageDarker);
                }
                playAllMoneyCardsOnHand();
            } else {
                showAlert(Alert.AlertType.WARNING, "Du kannst die Karte nicht kaufen!", "Fehler");
                LOG.debug("Der Kauf der Karte " + msg.getCardID() + " von " + msg.getCurrentUser() + " ist fehlgeschlagen");
            }
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
                        AnimationManagement.playCard(card, myPCLC.getChildren().size(), myPCLC);
                        handcards.getChildren().remove(card);
                        myPCLC.getChildren().add(card);
                        card.removeEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                    }
                });
            } else {
                showAlert(Alert.AlertType.WARNING, "Du kannst die Karte nicht spielen!", "Fehler");
                LOG.debug("Das Spielen der Karte " + msg.getHandCardID() + " von " + msg.getCurrentUser() + " ist fehlgeschlagen");
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
                    Card card = new Card(msg.getHandCardID(), firstEnemyPCLC.getLayoutX(), firstEnemyPCLC.getLayoutY(), firstEnemyPCLC.getHeight());
                    if (playerIndexNumbers.get(0).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            AnimationManagement.playCard((ImageView)firstEnemyHand.getChildren().get(0),  firstEnemyPCLC.getChildren().size(), firstEnemyPCLC);
                            firstEnemyHand.getChildren().remove(0);
                            firstEnemyPCLC.getChildren().add(card);
                            return;
                        });
                    }

                    if (playerIndexNumbers.get(1).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            AnimationManagement.playCard((ImageView)secondEnemyHand.getChildren().get(0),  secondEnemyPCLC.getChildren().size(), secondEnemyPCLC);
                            secondEnemyHand.getChildren().remove(0);
                            secondEnemyPCLC.getChildren().add(card);
                            return;
                        });
                    }
                    if (playerIndexNumbers.get(2).equals(msg.getEnemyPlaceNumber())) {
                        Platform.runLater(() -> {
                            AnimationManagement.playCard((ImageView)thirdEnemyHand.getChildren().get(0),  thirdEnemyPCLC.getChildren().size(), thirdEnemyPCLC);
                            thirdEnemyHand.getChildren().remove(0);
                            thirdEnemyPCLC.getChildren().add(card);
                            return;
                        });
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Du kannst die Karte nicht spielen!", "Fehler");
                LOG.debug("Das Spielen der Karte " + msg.getHandCardID() + " von " + msg.getCurrentUser() + " ist fehlgeschlagen");
            }
        }
    }

    /**
     * Wenn ein anderer Spieler eine Karte von der Hand entsorgt, wird dies den anderen Spielern angezeigt.
     *
     * @param msg       Die Message die vom server gesendet wird, wenn ein anderer Spieler eine Karte entsorgt.
     * @author Devin
     * @since Sprint 7
     */
    @FXML
    @Subscribe
    public void onDiscardCardMessage (DiscardCardMessage msg) {
        if (msg.getGameID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            //TODO: Aussuchverfahren zum abwerfen von Karten implementieren
        }
        List<Short> playerIndexNumbers = new ArrayList<>(); playerIndexNumbers.add((short) 0); playerIndexNumbers.add((short) 1); playerIndexNumbers.add((short) 2); playerIndexNumbers.add((short) 3);
        if (msg.getGameID().equals(lobbyID) && !msg.getCurrentUser().equals(loggedInUser)) {
            playerIndexNumbers.remove(msg.getUserPlaceNumber());
            if (playerIndexNumbers.get(0).equals(msg.getEnemyPlaceNumber())) {
                int numberOfCardsInHand = firstEnemyHand.getChildren().size();
                for (Short id: msg.getCardID()) {
                    Card card = new Card (id.toString(),328,447,104);
                    if(numberOfCardsInHand==0) {
                        LOG.debug("Die Hand hat keine Karten mehr zum entsorgen");
                        return;
                    }
                    Platform.runLater(() -> {
                    firstEnemyHand.getChildren().remove(0);
                    // TODO: Animation Management zum entsorgen einer Karte
                    firstEnemyDPLC.getChildren().add(card);
                    });
                }
                return;
            }
            if (playerIndexNumbers.get(1).equals(msg.getEnemyPlaceNumber())) {
                int numberOfCardsInHand = secondEnemyHand.getChildren().size();
                for (Short id: msg.getCardID()) {
                    Card card = new Card (id.toString(),0,0,104);
                    if(numberOfCardsInHand==0) {
                        LOG.debug("Die Hand hat keine Karten mehr zum entsorgen");
                        return;
                    }
                    Platform.runLater(() -> {
                        secondEnemyHand.getChildren().remove(0);
                        // TODO: Animation Management zum entsorgen einer Karte
                        secondEnemyDPLC.getChildren().add(card);
                    });
                }
                return;
            }
            if (playerIndexNumbers.get(2).equals(msg.getEnemyPlaceNumber())) {
                int numberOfCardsInHand = thirdEnemyHand.getChildren().size();
                for (Short id: msg.getCardID()) {
                    Card card = new Card (id.toString(),328,447,104);
                    if(numberOfCardsInHand==0) {
                        LOG.debug("Die Hand hat keine Karten mehr zum entsorgen");
                        return;
                    }
                    Platform.runLater(() -> {
                        thirdEnemyHand.getChildren().remove(0);
                        // TODO: Animation Management zum entsorgen einer Karte
                        thirdEnemyDPLC.getChildren().add(card);
                    });
                }
                return;
            }
        }
    }

    /**
     * Wenn ein anderer Spieler sich in der ClearPhase befindet, wird das Entsorgen dessen Handkarten und ausgespielten Karten den anderen Spielern angezeigt
     *
     * @param msg       Die Message die vom server gesendet wird, wenn ein anderer Spieler eine Karte sich in der ClearPhase befindet.
     * @author Devin
     * @since Sprint 7
     */
    @FXML
    @Subscribe
    public void onStartClearPhaseMessage (StartClearPhaseMessage msg) {
        // Wenn die ClearMessage an den currentPlayer geht werden, seine Handkarten und
        // ausgespielten Karten auf den Ablagestapel getan und fünf neue Karten gezogen.
        if (msg.getGameID().equals(lobbyID) && msg.getCurrentUser().equals(loggedInUser)) {
            Platform.runLater(() -> {
                moveCardsToDiscardPile(handcards.getChildren(), false);
                moveCardsToDiscardPile(myPCLC.getChildren(), true);
            });
            ArrayList<Short> handCardID = msg.getCardsToDraw();
            handCardID.forEach((n) -> {
                Card card = new Card(n.toString(), handcards.getLayoutX(), handcards.getLayoutY(), handcards.getHeight());
                Platform.runLater(() -> {
                    myDLC.getChildren().add(card);
                    AnimationManagement.addToHand(card, msg.getCardsToDraw().size());
                    handcards.getChildren().add(card);
                    myDLC.getChildren().remove(card);
                    card.addEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                });
            });
        }
        // Wenn ein anderer Spieler eine ClearPhaseMessage erhählt wird dies den anderen Spielern
        // angezeigt, indem deren Repräsentation des Spieler seine Handkarten und ausgespielten Karten auf den Ablagestapel legt.
        if (msg.getGameID().equals(lobbyID) && !msg.getCurrentUser().equals(loggedInUser)) {
            List<Short> playerIndexNumbers = new ArrayList<>(); playerIndexNumbers.add((short) 0); playerIndexNumbers.add((short) 1); playerIndexNumbers.add((short) 2); playerIndexNumbers.add((short) 3);
            playerIndexNumbers.remove(msg.getUserPlaceNumber());
            if (playerIndexNumbers.get(0).equals(msg.getEnemyPlaceNumber())) {
                Platform.runLater(() -> {
                    firstEnemyHand.getChildren().clear();
                    firstEnemyPCLC.getChildren().clear();
                });
                for(int i=0; i<5; i++) {
                    Card card = new Card("card_back", firstEnemyHand.getLayoutX(), firstEnemyHand.getLayoutY(), firstEnemyHand.getHeight());
                    Platform.runLater(() -> {
                        firstEnemyHand.getChildren().add(card);
                    });
                }
                return;
            }
            if (playerIndexNumbers.get(1).equals(msg.getEnemyPlaceNumber())) {
                Platform.runLater(() -> {
                    secondEnemyHand.getChildren().clear();
                    secondEnemyPCLC.getChildren().clear();
                });
                for(int i=0; i<5; i++) {
                    Platform.runLater(() -> {
                        Card card = new Card("card_back", secondEnemyHand.getLayoutX(), secondEnemyHand.getLayoutY(), secondEnemyHand.getHeight());
                        secondEnemyHand.getChildren().add(card);
                    });
                }
                return;
            }
            if (playerIndexNumbers.get(2).equals(msg.getEnemyPlaceNumber())) {
                Platform.runLater(() -> {
                    thirdEnemyHand.getChildren().clear();
                    thirdEnemyPCLC.getChildren().clear();
                });
                for(int i=0; i<5; i++) {
                    Platform.runLater(() -> {
                        Card card = new Card("card_back", thirdEnemyHand.getLayoutX(), thirdEnemyHand.getLayoutY(), thirdEnemyHand.getHeight());
                        thirdEnemyHand.getChildren().add(card);
                    });
                }
                return;
            }
        }
    }


    /**
     * Fügt die Karte aus der DiscardPileLastCardMessage dem Ablagestapel hinzu.
     *
     * @param msg Die Nachricht
     * @author Timo
     * @Sprint 6
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
     *
     * @author Devin S., Anna
     * @since Sprint5
     */
    @FXML
    @Subscribe
    public void onDrawHandMessage(DrawHandMessage message) {
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
                        //AnimationManagement.addToHand(card, handcards.getChildren().size());
                        myDLC.getChildren().remove(card);
                        handcards.getChildren().add(card);
                    }
                    card.addEventHandler(MouseEvent.MOUSE_CLICKED, handCardEventHandler);
                });
                if (numberOfPlayersInGame == 1){
                    return;
                }
                String pfad = "cards/images/card_back.png";
                Image picture = new Image(pfad);
                for(int i=0; i<5; i++) {
                    ImageView card = new ImageView(picture);
                    ImageView card2 = new ImageView(picture);
                    ImageView card3 = new ImageView(picture);
                    card.setFitHeight(107);
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
                    firstEnemyDLC.getChildren().add(card);
                    // AnimationManagement.addToHand(card, firstEnemyHand.getChildren().size());
                    firstEnemyDLC.getChildren().remove(card);
                    firstEnemyHand.getChildren().add(card);
                    if(numberOfPlayersInGame >= 3) {
                        secondEnemyDLC.getChildren().add(card2);
                        // AnimationManagement.addToHand(card2, secondEnemyHand.getChildren().size());
                        secondEnemyDLC.getChildren().remove(card2);
                        secondEnemyHand.getChildren().add(card2);
                        if (numberOfPlayersInGame == 4){
                            thirdEnemyDLC.getChildren().add(card3);
                            // AnimationManagement.addToHand(card3, thirdEnemyHand.getChildren().size());
                            thirdEnemyDLC.getChildren().remove(card3);
                            thirdEnemyHand.getChildren().add(card3);
                        }
                    }
                }
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
                        myDPLC.getChildren().clear();
                    }
                });
            }
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
                } else {
                    LOG.debug("No users in Lobby.");
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
     * @since Sprint7
     */
    private void updateEnemiesOnBoard(Set<User> usersList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            int enemyCounter = 0;
            for( User u : usersList)
            {
                if (u.getUsername().equals(loggedInUser.getUsername())) {
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
     * @since Sprint7
     * @author Alex
     * @param usersList Die User Liste als Set
     */
    private void updateUsersInGame(Set<User> usersList) {
        updateEnemiesOnBoard(usersList);
        updateUsersList(usersList);
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
        bigCardImage.setLayoutX(725.0);
        bigCardImage.setLayoutY(205.0);
        gameViewWIP.getChildren().add(bigCardImage);
        if (id > 6) {
            Button play = new Button("auspielen");
            Button back = new Button("zurück");
            play.setLayoutX(732.0);
            play.setLayoutY(435.0);
            back.setLayoutX(816.0);
            back.setLayoutY(435.0);
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
            Button back = new Button("zurück");
            back.setLayoutX(816.0);
            back.setLayoutY(435.0);
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
     * @param actionCards true wenn die Karten in der Aktionszone liegen
     * @author Darian
     * @since Sprint7
     */
    private void moveCardsToDiscardPile(ObservableList<Node> children, boolean actionCards){
        for (Node c : children) {
            Platform.runLater(() -> {
                ImageView card = (ImageView) c;
                String pfad = "cards/images/" + card.getId() + ".png";
                if(actionCards) {
                    card.setLayoutX(c.getLayoutX()-400);
                    card.setLayoutY(0);//433);
                } else {
                    card.setLayoutX(c.getLayoutX()-845);
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
     * Großes Bild der Karte wird angezeigt.
     * Es werden zwei Buttons("kaufen"/"zurück") hinzugefügt.
     * kauf-Button -> BuyCardRequest wird gestellt
     * zurück-Button -> Buttons und große Ansicht der Karte werden entfernt
     *
     * @param mouseEvent das Event
     * @author Rike, Fenja, Anna
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
        // setzt die Größe und die Position des Bildes. Das Bild ist im Vordergrund. Bild wird hinzugefügt
        bigCardImage.setFitHeight(225.0);
        bigCardImage.setFitWidth(150.0);
        bigCardImage.toFront();
        bigCardImage.setLayoutX(725.0);
        bigCardImage.setLayoutY(205.0);
        gameViewWIP.getChildren().add(bigCardImage);
        // Es werden zwei Buttons hinzugefügt (Zurück und Kaufen)
        Button buy = new Button("Kaufen");
        Button back = new Button("Zurück");
        gameViewWIP.getChildren().add(buy);
        gameViewWIP.getChildren().add(back);
        // Position der Buttons wird gesetzt
        buy.setLayoutX(732.0);
        buy.setLayoutY(435.0);
        buy.setMinWidth(70.0);
        back.setLayoutX(816.0);
        back.setLayoutY(435.0);
        back.setMinWidth(70.0);
        // Aktion hinter dem Kauf-Button
        buy.setOnAction(event -> {
            buy.setVisible(false);
            back.setVisible(false);
            bigCardImage.setVisible(false);
            BuyCardRequest req = new BuyCardRequest(lobbyID, loggedInUser, Short.valueOf(cardID));
            gameService.buyCard(req);
            this.mouseEvent = mouseEvent;
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
     *
     * @author Anna
     * @since Sprint 7
     */
    public void playAllMoneyCardsOnHand() {
        for (Node c : handcards.getChildren()) {
            ImageView card = (ImageView) c;
            if (card.getId().equals("1") || card.getId().equals("2") || card.getId().equals("3")) {
                Platform.runLater(() -> {
                    AnimationManagement.playCard(card, myPCLC.getChildren().size(), myPCLC);
                    handcards.getChildren().remove(c);
                    myPCLC.getChildren().add(card);
                });
            }
        }
    }
}