package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.event.GameQuitEvent;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.response.AllOnlineUsersInLobbyResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
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
    public static final String fxml = "/fxml/GameView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private UUID lobbyID;
    private User loggedInUser;
    @FXML
    private Pane chatView;
    @FXML
    private Pane gameView;
    @FXML
    private ListView<String> usersView;

    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    private Injector injector;
    private GameManagement gameManagement;


    //TODO: Nach Anpassung Abfragen (Zeile 194 - 215) entfernen -> Zeile 70-74
    private boolean istDran = true;
    private boolean inKaufphase = true;
    private boolean genuegendGeld = true;
    private int anzahlKarte = 2;

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
    public GameViewPresenter(User loggedInUser, UUID lobbyID, ChatService chatService, ChatViewPresenter chatViewPresenter, LobbyService lobbyService, UserService userService, Injector injector, GameManagement gameManagement) {
        this.loggedInUser = loggedInUser;
        this.lobbyID = lobbyID;
        this.chatService = chatService;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.chatViewPresenter = chatViewPresenter;
        this.injector = injector;
        this.gameManagement = gameManagement;
        initializeUserList();
    }

    /*
        showAlert Methode, um Alert Box zu erstellen
         */


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
            eventBus.post(new GameQuitEvent());
        }//so funktioniert das nicht
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

    }

    /**
     * Logout Button gedrückt Ereignis.
     *
     * @param actionEvent das Ereignis der Aktion.
     * @author Fenja
     * @since Sprint 3
     */
    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        lobbyService.leaveAllLobbiesOnLogout(new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()));
        userService.logout(loggedInUser);
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
     *
     * Großes Bild der Karte wird angezeigt.
     * Es wird ein Button ("zurück"), wenn der Spieler die Karte nicht kaufen kann bzw. zwei Buttons("kaufen"/"zurück"), wenn er es kann hinzugefügt.
     *
     *
     * @param mouseEvent
     * @author Rike
     * @since Sprint 5
     */
    @FXML
    public void onBuyableCardClicked (MouseEvent mouseEvent) {
        ImageView cardImage = (ImageView) mouseEvent.getSource();
        //Speichert Informationen zur ausgewählten Karte zwischen
        Image chosenCardImage = cardImage.getImage();
        double chosenCardFitHeight = cardImage.getFitHeight();
        double chosenCardLayoutX = cardImage.getLayoutX();
        double chosenCardLayoutY = cardImage.getLayoutY();
        String chosenCardID = cardImage.getId();

        // ein großes Bild der Karte wird hinzugefügt
        // TODO: das große Bild muss zur ausgewählten Karte passen -> wie sind die Bilder gespeichert
        ImageView newCardImage = new ImageView(new Image(new File(getClass().getResource("/images/karte_gross.png").toExternalForm().replace("file:", "")).toURI().toString()));
        // setzt die Größe und die Position des Bildes. Das Bild ist im Vordergrund. Bild wird hinzugefügt
        newCardImage.setFitHeight(150.0);
        newCardImage.toFront();
        newCardImage.setLayoutX(458.0);
        newCardImage.setLayoutY(207.0);
        gameView.getChildren().add(newCardImage);

        //TODO: temporäre Lösung zur Abfragung (istDran, inKaufphase, genuegendGeld) -> anpassen
        if (istDran && inKaufphase && genuegendGeld){
            // wenn der Spieler eine Karte kaufen kann, werden zwei Buttons eingefügt (über den Handkarten)
            Button buy = new Button ("kaufen");
            Button back1 = new Button ("zurück");
            gameView.getChildren().add(buy);
            gameView.getChildren().add(back1);
            // Position der Buttons wird gesetzt
            buy.setLayoutX(432.0);
            buy.setLayoutY(375.0);
            back1.setLayoutX(516.0);
            back1.setLayoutY(375.0);
            back1.setMinWidth(52.0);
            // Aktion hinter dem Kauf-Button
            buy.setOnAction(event -> {
                System.out.println("Karte wurde gekauft.");
                buy.setVisible(false);
                back1.setVisible(false);
                newCardImage.setVisible(false);
                anzahlKarte--;
                if(anzahlKarte > 0){
                    ImageView chosenCard = new ImageView(chosenCardImage);
                    chosenCard.setFitHeight(chosenCardFitHeight);
                    chosenCard.setLayoutX(chosenCardLayoutX);
                    chosenCard.setLayoutY(chosenCardLayoutY);
                    chosenCard.setId(chosenCardID);
                }
                //TODO: Der Pfad passt noch nicht -> AnimationManagement anpassen
                AnimationManagement.buyCard(cardImage);
            });
            // Aktion hinter dem Zurück Button -> Kauf-/Zurück-Button und das große Bild werden entfernt
            back1.setOnAction(event -> {
                buy.setVisible(false);
                back1.setVisible(false);
                newCardImage.setVisible(false);
            });
        }
        else{
            // kann der Spieler keine Karte kaufen, wird nur ein Button hinzugefügt (über den Handkarten)
            Button back2 = new Button ("zurück");
            gameView.getChildren().add(back2);
            // Position des Buttons
            back2.setLayoutX(472.0);
            back2.setLayoutY(375.0);
            // Aktion hinter dem Zurück Button -> Zurück-Button und das große Bild werden entfernt
            back2.setOnAction(event -> {
                back2.setVisible(false);
                newCardImage.setVisible(false);
            });
        }
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
            LOG.debug("New user in Lobby, LobbyService is retrieving users");
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
        if (users.contains(message.getOldUser().getUsername())) {
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
            LOG.debug("Update of user list with" + allOnlineUsersInLobbyResponse.getUsers());
            updateUsersList(allOnlineUsersInLobbyResponse.getUsers());
        }
    }

    /**
     * Die usersView Liste wird geupdatet.
     * Äquivalent zu MainMenuPresenter.updateUsersList.
     *
     * @param userList
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
}
