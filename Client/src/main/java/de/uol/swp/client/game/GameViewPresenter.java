package de.uol.swp.client.game;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatService;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.game.event.GameQuitEvent;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.game.ShowCardRequest;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionListener;
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
    private ListView<String> usersView;

    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    private Injector injector;
    private GameManagement gameManagement;

    /**
    //TODO: Nach Anpassung Abfragen (Zeile 196 - 215) entfernen -> Zeile 67-69
    private boolean istDran = true;
    private boolean inKaufphase = true;
    private boolean genuegendGeld = true;
     */

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
     * @param type der Typ
     * @param message die Nachricht
     * @param title der Titel
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
     * Ereignis das ausgeführt wird, wenn auf eine Karte im Shop klickt (Karte die man kaufen kann
     *
     * @param mouseEvent
     * @author Rike
     * @since Sprint 5
     */
    @FXML
    public void onBuyableCardClicked (MouseEvent mouseEvent) {
        ImageView cardImage = (ImageView) mouseEvent.getSource();
        String cardName = cardImage.getId();

        ShowCardRequest request = new ShowCardRequest(loggedInUser, cardName);
        eventBus.post(request);
        /**
        ImageView cardImage = (ImageView) mouseEvent.getSource();
        String cardName = cardImage.getId();

        //Erzeuge Dialog mit Namen der Karte
        JDialog buyCardDialog = new JDialog();
        buyCardDialog.setResizable(false);
        buyCardDialog.setTitle(cardName);
        buyCardDialog.setSize(400,400);
        JPanel panel = new JPanel();

        //TODO: Bild der Karte wird angezeigt -> Groß-Ansicht

        //Karte kaufen + Action
        JButton buyCard = new JButton("kaufen");
        ActionListener onBuyCardButtonPressed = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (istDran && inKaufphase && genuegendGeld){
                    //TODO: Karte muss übergeben werden; Bewegung ausführen: AnimationManagement.buyCard(cardImage);
                    System.out.println("Karte wurde gekauft");
                }
                else if (!istDran){
                    //TODO: Durch Abfrage ob Spieler dran ist ersetzen
                    Platform.runLater(()->{
                        SceneManager.showAlert(Alert.AlertType.WARNING, "Du bist nicht dran!", "Fehler");
                        buyCardDialog.requestFocus();
                    });
                }
                else if (istDran && !inKaufphase){
                    //TODO: Durch Abfrage ob Spieler in Kaufphase ersetzen
                    Platform.runLater(()->{
                        SceneManager.showAlert(Alert.AlertType.WARNING, "Du bist nicht in der Kaufphase!", "Fehler");
                        buyCardDialog.requestFocus();
                    });
                }
                else if (istDran && inKaufphase && !genuegendGeld){
                    //TODO: Durch Abfrage ob Spieler genügend Geld auf der Hand hat ersetzen
                    Platform.runLater(()->{
                        SceneManager.showAlert(Alert.AlertType.WARNING, "Du hast nicht genügend Geld!", "Fehler");
                        buyCardDialog.requestFocus();
                    });
                }
            }
        };
        buyCard.addActionListener(onBuyCardButtonPressed);
        panel.add(buyCard);

        //Fenster verlassen + Action
        JButton leaveBuyCard = new JButton("zurück");
        ActionListener onLeaveBuyCardButtonPressed = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                buyCardDialog.setVisible(false);
            }
        };
        leaveBuyCard.addActionListener(onLeaveBuyCardButtonPressed);
        panel.add(leaveBuyCard);

        buyCardDialog.add(panel);
        buyCardDialog.setVisible(true);
         **/
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
        if (users.contains(message.getOldUser().getUsername())){
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
