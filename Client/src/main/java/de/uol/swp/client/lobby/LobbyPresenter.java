package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.GameManagement;
import de.uol.swp.common.chat.ChatService;
import de.uol.swp.common.game.card.parser.JsonCardParser;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.lobby.exception.LobbyExceptionMessage;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.request.AddBotRequest;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.lobby.response.SetChosenCardsResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class LobbyPresenter extends AbstractPresenter {

    public static final String fxml = "/fxml/LobbyViewWIP.fxml";
    private static final Logger LOG = LogManager.getLogger(ChatViewPresenter.class);

    private final ChatViewPresenter chatViewPresenter;
    private final GameManagement gameManagement;
    private final UUID lobbyID;
    private final String lobbyName;
    private final CardPack cardpack;
    private final EventBus eventBus;
    private final Injector injector;

    private Map<String, HBox> readyUserList = new TreeMap<>();
    private ObservableList<HBox> userHBoxes;
    private User loggedInUser;
    private UserDTO loggedInUserDTO;
    private UserDTO gameOwner;
    private boolean gameSettingsOpen;
    private boolean ownReadyStatus = false;

    @FXML
    private ChoiceBox<Integer> chooseMaxPlayer;
    @FXML
    private Pane lobbyViewWIP;
    @FXML
    private ListView<HBox> usersView;
    @FXML
    private Pane chatView;
    @FXML
    private Button readyButton;
    @FXML
    private Button gamesettingsButton;
    @FXML
    private Button createBotButton;
    @FXML
    private HBox lobbyHBox;
    @FXML
    private Label settingOwner;
    @FXML
    private Label maxSettingOwner;
    @FXML
    private VBox gameSettingsVBox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TilePane chosenCards;
    @FXML
    private TextFlow textFlow;
    @FXML
    private Button sendCards;
    @FXML
    private TilePane choosableCards;
    @FXML
    private ImageView bigCardImage;
    @FXML
    private ImageView bigCard;

    private final ImageView crownView = new ImageView("images/crown.png");



    /**
     * Instanziiert einen neuen LobbyPresenter.
     *
     * @param loggedInUser      der eingeloggte Nutzer
     * @param name              der Name
     * @param lobbyID           die LobbyID
     * @param chatService       der ChatService
     * @param chatViewPresenter der ChatViewPresenter
     * @param lobbyService      der LobbyService
     * @param userService       der UserService
     * @param injector          der Injector
     * @param gameManagement    das GameManagement
     * @author Julia, Keno O, Anna, Darian, Keno S.
     * @since Sprint 2
     */
    public LobbyPresenter(User loggedInUser, String name, UUID lobbyID, ChatService chatService, ChatViewPresenter chatViewPresenter, LobbyService lobbyService, UserService userService, Injector injector, UserDTO gameOwner, GameManagement gameManagement, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyName = name;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.chatViewPresenter = chatViewPresenter;
        this.injector = injector;
        this.gameOwner = gameOwner;
        this.gameManagement = gameManagement;
        this.eventBus = eventBus;
        this.loggedInUserDTO = new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail());
        this.cardpack = new JsonCardParser().loadPack("Basispack");
        this.gameSettingsOpen = false;
    }

    //--------------------------------------
    // FXML METHODS
    //--------------------------------------

    /**
     * Wird aufgerufen wenn der Lobby verlassen Button gedrückt wird.
     *
     * @param actionEvent Das ActionEvent
     * @author Julia, Keno S., Marvin
     * @since Sprint 3
     */
    @FXML
    public void onLeaveLobbyButtonPressed(ActionEvent actionEvent) {
        lobbyService.leaveLobby(lobbyID, loggedInUserDTO);
    }

    /**
     * Initialisieren des Chats - FXML laden, Controller setzen (muss immer eine eigene Instanz sein)
     * und chatView ind die chatView-Pane dieses Controllers laden.
     * Der eingeloggte User wird zur Userliste hinzugefügt und diese wird aktualisiert.
     *
     * @throws IOException Die IO-Exception
     * @author Keno O, Darian, Timo, Ferit
     * @since Sprint 2
     */
    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader = injector.getInstance(FXMLLoader.class);
        loader.setLocation(getClass().getResource(ChatViewPresenter.fxml));
        loader.setController(chatViewPresenter);
        chatView.getChildren().add(loader.load());
        ((Pane) chatView.getChildren().get(0)).setPrefHeight(chatView.getPrefHeight());
        ((Pane) chatView.getChildren().get(0)).setPrefWidth(chatView.getPrefWidth());
        chatViewPresenter.userJoined(loggedInUser.getUsername());

        lobbyService.retrieveAllUsersInLobby(lobbyID);
        readyUserList.put(loggedInUser.getUsername(), getHboxFromReadyUser(loggedInUser, false));
        updateUsersList();

        if (gameOwner.equals(loggedInUser)) {
            gamesettingsButton.setVisible(true);
            createBotButton.setVisible(true);

            chooseMaxPlayer.setDisable(false);
            chooseMaxPlayer.setValue(4);
        } else {
            gamesettingsButton.setVisible(false);
            createBotButton.setVisible(false);
            chooseMaxPlayer.setDisable(true);
            chooseMaxPlayer.setVisible(false);
            settingOwner.setVisible(false);
            maxSettingOwner.setVisible(false);
        }

    }
/*
    /**
     * Wird aufgerufen wenn der Logout-Button gedrückt wird.
     *
     * @param actionEvent
     * @author Keno S, Keno O.
     * @since Sprint 3

    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        lobbyService.leaveAllLobbiesOnLogout(loggedInUserDTO);
        userService.logout(loggedInUser);
    } */

    /**
     * Wird aufgerufen wenn der Bereit-Button gedrückt wird.
     * Der Text auf dem Button und der ownReadyStatus werden dabei jeweils geändert.
     *
     * @param actionEvent Das ActionEvent
     * @author Darian, Keno S, Keno O.
     * @since Sprint 3
     */
    @FXML
    public void onReadyButtonPressed(ActionEvent actionEvent) {
        if (ownReadyStatus) {
            readyButton.setText("Bereit");
            ownReadyStatus = false;
        } else {
            readyButton.setText("Nicht mehr Bereit");
            ownReadyStatus = true;
        }
        LOG.debug("Setze eigenen Bereitstatus in der Lobby " + lobbyID + " zu " + (ownReadyStatus ? "Bereit" : "Nicht bereit"));
        lobbyService.setLobbyUserStatus(lobbyID, loggedInUserDTO, ownReadyStatus);
    }


    /**
     * Wird aufgerufen wenn der Wert in der max. Spieler-Box geändert wird.
     *
     * @param actionEvent Das ActionEvent
     * @author Timo, Rike
     * @since Sprint 3
     */
    @FXML
    public void onMaxPlayerSelected(ActionEvent actionEvent) {
        if (gameOwner.equals(loggedInUser)) {
            lobbyService.setMaxPlayer(this.getLobbyID(), this.loggedInUser, chooseMaxPlayer.getValue());
        }
    }

    /**
     * Wenn der BotButton gepresst wird.
     *
     * @param actionEvent
     */
    @FXML
    public void onCreateBotButtonPressed(ActionEvent actionEvent) {
        AddBotRequest request = new AddBotRequest(lobbyID);
        eventBus.post(request);
    }

    /**
     * Wird aufgerufen, wenn der Button für die Spieleinstellungen betätigt wird.
     *
     * @param actionEvent Das ActionEvent
     * @author Fenja, Anna
     * @since Sprint 7
     */
    @FXML
    public void onGamesettingsButtonPressed(ActionEvent actionEvent) {
        if (!gameSettingsOpen) {
            gamesettingsButton.setText("Spieleinstellungen schließen");
            gameSettingsOpen = true;
            Platform.runLater(() -> {
                String pfad1 = "file:Client/src/main/resources/cards/images/card_back.png";
                Image picture1 = new Image(pfad1);
                bigCard = new ImageView(picture1);
                bigCard.setPreserveRatio(true);
                bigCard.setFitWidth(250);
                bigCard.setLayoutX(400);
                bigCard.setLayoutY(100);
                bigCard.setVisible(false);
                lobbyViewWIP.setOnMouseClicked(mouseEvent -> {
                    if (bigCard.isVisible()) {
                        bigCard.setVisible(false);
                    }
                });
                lobbyViewWIP.getChildren().add(bigCard);

                VBox gameSettingsVBox = new VBox();
                gameSettingsVBox.setSpacing(20);
                gameSettingsVBox.setPrefSize(450, 630);
                gameSettingsVBox.setId("gameSettingsVBox");

                //Ausgewählte Karten anzeigen
                TilePane chosenCards = new TilePane();
                chosenCards.setPrefSize(400, 160);
                chosenCards.setStyle("-fx-background-color: #3D3D3D");
                chosenCards.setOpacity(0.5);
                chosenCards.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                TextFlow textFlow = new TextFlow();
                textFlow.setPrefSize(200, 50);
                textFlow.setTextAlignment(TextAlignment.CENTER);
                Text text = new Text("Wähle Karten aus...");
                text.setFill(Paint.valueOf("white"));
                text.setStyle("-fx-font-size: 24");
                textFlow.getChildren().add(text);
                chosenCards.getChildren().add(textFlow);

                //Button zum Abschicken der Nachricht für die Karten
                Button sendCards = new Button();
                sendCards.setText("Auswahl abschicken");
                sendCards.setPrefSize(450, 31);
                sendCards.setVisible(false);
                sendCards.setOnAction(e -> {
                    if (chosenCards.getChildren().size() > 0) {
                        ArrayList<Short> chosenCardIDs = new ArrayList<>();
                        for (Node n : chosenCards.getChildren()) {
                            chosenCardIDs.add(Short.valueOf(n.getId()));
                        }
                        lobbyService.sendChosenCards(lobbyID, chosenCardIDs);
                    }
                });

                //auswählbare Karten initilaisieren
                TilePane tilePane = new TilePane();
                tilePane.setPrefHeight(500);
                tilePane.setPrefWidth(500);
                tilePane.setMaxWidth(500);
                tilePane.setVgap(10);
                tilePane.setHgap(10);
                tilePane.setStyle("-fx-background-color: #3D3D3D");
                for (int i = 0; i < cardpack.getCards().getActionCards().size(); i++) {
                    short cardID = cardpack.getCards().getActionCards().get(i).getId();
                    String pfad = "cards/images/" + cardID + "_sm.png";
                    Image picture = new Image(pfad);
                    ImageView card = new ImageView(picture);
                    card.setPreserveRatio(true);
                    card.setFitWidth(100);
                    choosableCards.getChildren().add(card);
                    card.setOnMouseClicked(event ->
                    {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            if (chosenCards.getChildren().size() < 10) {
                                chosenCards.getChildren().remove(textFlow);
                                choosableCards.getChildren().remove(card);
                                ImageView chosenCard = new ImageView(picture);
                                chosenCard.setPreserveRatio(true);
                                chosenCard.setFitWidth(80);
                                chosenCard.setId(String.valueOf(cardID));
                                chosenCards.getChildren().add(chosenCard);
                                sendCards.setVisible(true);
                                chosenCard.setOnMouseClicked(event2 -> {
                                    if (event2.getButton() == MouseButton.PRIMARY) {
                                        chosenCards.getChildren().remove(chosenCard);
                                        choosableCards.getChildren().add(0, card);
                                        if (chosenCards.getChildren().size() == 0) {
                                            chosenCards.getChildren().add(textFlow);
                                            sendCards.setVisible(false);
                                        }
                                    } else {
                                        showBigCardImage(cardID);
                                    }
                                });
                            }
                        } else {
                            showBigCardImage(cardID);
                        }
                    });
                }
            });
        } else {
            gameSettingsVBox.setVisible(false);
            gamesettingsButton.setText("Spieleinstellungen");
        }
    }

    /**
     * Methode für den Klick des Buttons Auswahl-abschicken
     *
     * @param event
     * @author Anna
     * @since Sprint 8
     */
    @FXML
    public void sendChosenCards(ActionEvent event) {
        if (chosenCards.getChildren().size() > 0) {
            ArrayList<Short> chosenCardIDs = new ArrayList<>();
            for (Node n : chosenCards.getChildren()) {
                chosenCardIDs.add(Short.valueOf(n.getId()));
            }
            lobbyService.sendChosenCards(lobbyID, chosenCardIDs);
        }
    }

    /**
     * Hilfsmethode, um die Karte groß anzuzeigen
     *
     * @param cardID Die CardID
     * @author Fenja, Anna
     * @since Sprint 7
     */
    public void showBigCardImage(short cardID) {
        Platform.runLater(() -> {
            String pfad = "cards/images/" + cardID + ".png";
            Image picture = new Image(pfad);
            bigCardImage.setImage(picture);
            bigCardImage.setVisible(true);
        });
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * Ruft Methode auf, die die ausgewählten Karten sendet.
     *
     * @param message Die SetChosenCardsResponse
     * @author Fenja, Anna
     * @since Sprint 7
     */
    @Subscribe
    public void onSendChosenCardsMessage(SetChosenCardsResponse message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        if (message.isSuccess()) {
            lobbyHBox.getChildren().forEach(t -> {
                if (t.getId().equals("gameSettingsVBox")) {
                    Platform.runLater(() -> lobbyHBox.getChildren().remove(t));
                    gameSettingsOpen = false;
                    Platform.runLater(() -> gamesettingsButton.setText("Spieleinstellungen"));
                }
            });
        }
    }

    /**
     * Ruft Methode auf, die den Status des Nutzers ändert, nachdem der Bereit-Status des Nutzers serverseitig geändert wurde.
     *
     * @param message Die UpdatedLobbyReadyStatusMessage
     * @author Darian, Keno O.
     * @since Sprint 3
     */
    @Subscribe
    public void onUpdatedLobbyReadyStatusMessage(UpdatedLobbyReadyStatusMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        if (readyUserList.containsKey(message.getUser().getUsername())) {
            LOG.debug("User " + message.getUser().getUsername() + " änderte seinen Status zu " + (message.isReady() ? "Bereit" : "Nicht bereit") + " in Lobby " + getLobbyName());
            updateReadyUser(message.getUser(), message.isReady());
        }
    }

    /**
     * Reaktion auf die AllOnlineUsersInLobbyResponse vom Server.
     * Die Userliste wird aktualisiert.
     *
     * @param response Die AllOnlineUsersInLobbyResponse
     * @author Keno O.
     * @since Sprint 3
     */
    @Subscribe
    private void onReceiveAllUsersInLobby(AllOnlineUsersInLobbyResponse response) {
        if (response.getLobbyID().equals(lobbyID)) {
            readyUserList = new TreeMap<>();
            response.getUsers().forEach(user -> readyUserList.put(user.getUsername(), getHboxFromReadyUser(user, response.getStatus(user))));
            updateUsersList();
        }
    }

    /**
     * Aktualisiert den loggedInUser sowie die Userliste.
     *
     * @param message Die UpdatedUserMessage
     * @author Julia, Anna
     * @since Sprint 4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
            loggedInUserDTO = (UserDTO) message.getUser();
            LOG.debug("User " + message.getOldUser().getUsername() + " änderte seinen Namen zu " + message.getUser().getUsername());
        }
        Platform.runLater(() -> {
            if (readyUserList.containsKey(message.getOldUser().getUsername())) {
                userLeftLobby(message.getOldUser().getUsername(), false);
                readyUserList.put(message.getUser().getUsername(), getHboxFromReadyUser(message.getUser(), false));
                updateUsersList();
                chatViewPresenter.userJoined(message.getUser().getUsername());
            }
        });
    }

    /**
     * Deaktivieren der Max. Spieler ChoiceBox, sofern der eingeloggte Nutzer nicht der Lobbyowner ist.
     *
     * @param msg Die SetMaxPlayerMessage
     * @author Timo, Rike, Darian
     * @since Sprint 3
     */
    @Subscribe
    public void onSetMaxPlayerMessage(SetMaxPlayerMessage msg) {
        Platform.runLater(() -> {
            if (msg.getOwner().equals(loggedInUser) && lobbyID == msg.getLobbyID()) {
                chooseMaxPlayer.setDisable(false);
                chooseMaxPlayer.setValue(msg.getMaxPlayer());
                LOG.info("Max. Spieler der Lobby: " + msg.getLobby().getName() + " erfolgreich auf " + msg.getMaxPlayer() + " gesetzt.");
            }
        });
    }

    /**
     * GameView wird aufgerufen.
     *
     * @param message Die StartGameMessage
     * @author Darian, Keno O.
     * @since Sprint 3
     */
    @Subscribe
    public void onGameStartMessage(StartGameMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("Spieler in der Lobby mit der ID" + message.getLobbyID() + " startet.");
        gameManagement.showGameView();
    }

    /**
     * Nachdem der Nutzer sich ausgeloggt hat, wird er auch aus der Lobbyliste gelöscht.
     *
     * @param message Die UserLoggedOutMessage
     * @author Darian
     * @since Sprint 3
     */
//    @Subscribe
//    public void onUserLoggedOutMessage(UserLoggedOutMessage message) {
//        userLeftLobby(message.getUsername(), false);
//    }
//
//    Überflüssig, da man beim ausloggen inzwischen schon jede Lobby verlässt. (Siehe LeaveAllLobbiesOnLogoutRequest)

    /**
     * User wird aus der Liste entfernt, wenn er seinen Account gelöscht hat
     *
     * @param message Die UserDroppedMessage
     * @author Julia
     * @since Sprint 4
     */
    @Subscribe
    public void onUserDroppedMessage(UserDroppedMessage message) {
        userLeftLobby(message.getUser().getUsername(), false);
    }

    /**
     * Ein neuer Nutzer tritt der Lobby bei, die Userliste der Lobby wird aktualisiert und eine Nachricht im Chat angezeigt.
     *
     * @param message Die UserJoinedLobbyMessage
     * @author Darian, Keno O., Marvin
     * @since Sprint 3
     */
    @Subscribe
    public void onUserJoinedLobbyMessage(UserJoinedLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        Platform.runLater(() -> {
            if (readyUserList != null && loggedInUser != null) {
                gameOwner = message.getGameOwner();
                readyUserList.put(message.getUser().getUsername(), getHboxFromReadyUser(message.getUser(), false));
                updateUsersList();
                chatViewPresenter.userJoined(message.getUser().getUsername());
            }
        });
    }

    /**
     * Ein Nutzer verlässt die Lobby, die Userliste wird aktualisiert.
     *
     * @param message Die UserLeftLobbyMessage
     * @author Darian, Keno O, Julia
     * @since Sprint 3
     */
    @Subscribe
    public void onUserLeftLobbyMessage(UserLeftLobbyMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("User " + message.getUser().getUsername() + " hat die Lobby verlassen!");
        if (message.getLobby() != null) {
            gameOwner = message.getGameOwner();
            userLeftLobby(message.getUser().getUsername(), false);
            if (gameOwner.getUsername().equals(loggedInUser.getUsername())) {
                gamesettingsButton.setVisible(true);
            }
        }
    }

    /**
     * Wenn die Nachrticht eingeht dass ein Spieler gekickt wird, wird dieser aus der UserListe enntfernt. Dies wird
     * Ebenfalls im Chat angezeigt.
     *
     * @param message Die eingehende Nachricht vom Server
     * @author Darian, Marvin
     * @since Sprint 4
     */
    @Subscribe
    public void onKickUserMessage(KickUserMessage message) {
        if (!message.getLobbyID().equals(lobbyID)) return;
        LOG.debug("User " + message.getLobby().getName() + " wurde aus der Lobby gekickt!");
        userLeftLobby(message.getUser().getUsername(), true);
    }

    /**
     * Hier wird die LobbyExceptionMessage abgefangen und die Nachricht in einem neuem Fenster angezeigt
     *
     * @param msg die Nachricht
     * @author Darian
     * @since Sprint 8
     */
    @Subscribe
    public void LobbyExceptionMessage(LobbyExceptionMessage msg) {
        SceneManager.showAlert(Alert.AlertType.ERROR, msg.getMessage(), "Lobby");
    }

    //--------------------------------------
    // PRIVATE METHODS
    //--------------------------------------

    /**
     * Wenn der Benutzer aus der Lobby gegangen/gekickt ist wird das im Chat angezeigt und er wird aus der UserListe
     * entfernt.
     *
     * @param username Benutzername des Benutzers der gegangen ist
     * @param kicked   True, wenn der Benutzer aus der Lobby gekickt wurde
     * @author Darian
     * @since Sprint 4
     */
    private void userLeftLobby(String username, boolean kicked) {
        if (readyUserList.get(username) != null) {
            Platform.runLater(() -> {
                readyUserList.remove(username);
                readyUserList.replace(gameOwner.getUsername(), crownCheck(readyUserList.get(gameOwner.getUsername()), gameOwner));
                updateUsersList();
                //Je nachdem ob der Benutzer gekickt wurde oder freiwillig aus der Lobby gegangen ist wird es auch so angezeigt
                if (kicked) {
                    chatViewPresenter.userKicked(username);
                } else {
                    chatViewPresenter.userLeft(username);
                }
                if (readyUserList.containsKey(username)) {
                    readyUserList.remove(username);
                    updateUsersList();
                }
            });
        }
    }

    /**
     * Geänderte Userliste wird angezeigt.
     *
     * @author Darian, Keno O.
     * @since Sprint 3
     */
    private void updateUsersList() {
        Platform.runLater(() -> {
            if (userHBoxes == null) {
                userHBoxes = FXCollections.observableArrayList();
                usersView.setItems(userHBoxes);
            }
            userHBoxes.clear();
            userHBoxes.addAll(getAllHBoxes());
        });
    }

    /**
     * Es wird eine HBox erstellt in der man den Benutzernamen sieht und den Bereit-Status. Wenn man der Besitzer der
     * Lobby ist kann man mit einem Button daneben die Spieler aus der Lobby entfernen
     *
     * @param user   Der User
     * @param status Der aktualisierte Status
     * @return The generated HBox
     * @author Darian
     * @since Sprint 3
     */
    private HBox getHboxFromReadyUser(User user, boolean status) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(5);
        Circle circle = new Circle(12.0f, status ? Paint.valueOf("green") : Paint.valueOf("red"));
        Label usernameLabel = new Label(user.getUsername());
        box.getChildren().add(circle);
        box.getChildren().add(usernameLabel);
        //Es wird geprüft ob man der Besitzer der Lobby ist und ob der Button neben einem selber auftaucht
        if (loggedInUser.getUsername().equals(gameOwner.getUsername()) && !user.getUsername().equals(gameOwner.getUsername())) {
            Button button = new Button("Spieler entfernen");
            box.getChildren().add(button);
            //Wenn der Button gedrückt wird der Spieler entfernt.
            button.setOnAction(e -> lobbyService.kickUser(lobbyID, (UserDTO) loggedInUser, (UserDTO) user));
        }
        crownCheck(box, user);
        return box;
    }

    /**
     * Ausgelagerte Methode aus getHboxFromReadyUser da es als Teilfunktion benötigt wird.
     *
     * @param box  Die HBox des Users
     * @param user Der User
     * @return Die HBox mit oder ohne Krone
     * @author Marvin
     * @since Sprint 8
     */

    private HBox crownCheck(HBox box, User user) {
        if (!box.getChildren().contains(crownView) && user.getUsername().equals(gameOwner.getUsername())) {
            crownView.setFitHeight(15);
            crownView.setFitWidth(15);
            Platform.runLater(() -> {
                if(!box.getChildren().contains(crownView))
                    box.getChildren().add(crownView);
            });
        }
        return box;
    }

    /**
     * Nutzer wird erst aus der Userliste gelöscht und dann mit seinem neuen Status wieder hinzugefügt.
     *
     * @param user   Der User
     * @param status Der aktuelle Bereit-Status
     * @author Darian
     * @since Sprint 3
     */
    private void updateReadyUser(User user, boolean status) {
        if (readyUserList.containsKey(user.getUsername())) {
            readyUserList.remove(user.getUsername());
            readyUserList.put(user.getUsername(), getHboxFromReadyUser(user, status));
            updateUsersList();
        }
    }

    /**
     * Konvertiert die HBox Map in eine ArrayList.
     *
     * @return Alle HBoxes als ArrayList
     * @author Darian, Keno S.
     * @since Sprint 3
     */
    private ArrayList<HBox> getAllHBoxes() {
        return new ArrayList<>(readyUserList.values());
    }

    //--------------------------------------
    // GETTER AND SETTER
    //--------------------------------------

    /**
     * Gibt die LobbyID zurück.
     *
     * @return Die LobbyID
     * @author Darian
     * @since Sprint 3
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Gibt den Lobbynamen zurück.
     *
     * @return Den Lobbynamen
     * @author Darian
     * @since Sprint 3
     */
    public String getLobbyName() {
        return lobbyName;
    }

    /**
     * Gibt den LobbyService zurück.
     *
     * @return Den LobbyService
     * @author Ferit
     * @since Sprint 3
     */
    public LobbyService getLobbyService() {
        return lobbyService;
    }

    /**
     * Ändert den Text des Buttons auf Bereit und den Status auf false.
     *
     * @param loggedInUser Der aktuelle User
     * @author Anna
     * @since Sprint 6
     */
    public void setButtonReady(UserDTO loggedInUser) {
        if (loggedInUser.equals(this.loggedInUserDTO)) {
            readyButton.setText("Bereit");
            ownReadyStatus = false;
        }
    }
}