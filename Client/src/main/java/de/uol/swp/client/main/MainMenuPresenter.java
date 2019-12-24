package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The type Main menu presenter.
 */
public class MainMenuPresenter extends AbstractPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/MainMenuView.fxml";
    public static final String css = "css/MainMenuPresenter.css";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    private ObservableList<Lobby> lobbies;

    @FXML
    private TextField lobbyName;
    @FXML
    private ListView<String> usersView;
    @FXML
    private TableView<Lobby> lobbiesView;
    @FXML
    private TableColumn<Lobby, String> name = new TableColumn<>("Name");
    @FXML
    private TableColumn<Lobby, String> host = new TableColumn<>("Host");
    @FXML
    private TableColumn<Lobby, Integer> players = new TableColumn<>("Spieler");
    @FXML
    private TableColumn<Lobby, Void> joinLobby = new TableColumn<>();
    @FXML
    private Pane chatView;
    @FXML
    private Button createLobbyButton, logoutButton;

    /**
     * @author Paula, Haschem, Ferit
     * @version 0.1
     * Fängt den Button ab und sendet den Request zur Erstellung der Lobby an den Server.
     */

    public static void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(type, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        alert.show();
    }

    /**
     * Methode fängt ButtonKlick ab, User verlässt alle Lobbies, in denen er angemeldet ist und wird ausgeloggt
     *
     * @param actionEvent
     * @author Julia, Paula
     * @since sprint3
     */

    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Pressed, SoundMediaPlayer.Type.Sound).play();
        lobbyService.leaveAllLobbiesOnLogout(new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()));
        userService.logout(loggedInUser);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("globaler", "global", loggedInUser, ChatViewPresenter.THEME.Light, chatService);
        chatViewPresenter.setChatId("global");
        eventBus.register(chatViewPresenter);
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());
        //Fenstergroesse uebernehmen
        ((Pane) chatView.getChildren().get(0)).setMinHeight(chatView.getMinHeight());
        ((Pane) chatView.getChildren().get(0)).setMinWidth(chatView.getMinWidth());

        //Initialisieren der Lobbytabelle
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        host.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOwner().getUsername()));
        players.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPlayers()).asObject());
        addJoinLobbyButton();
        lobbiesView.getColumns().addAll(name, host, players, joinLobby);
        lobbiesView.setPlaceholder(new Label("Keine Lobbies vorhanden"));
        name.setResizable(false);
        host.setResizable(false);
        players.setResizable(false);
        joinLobby.setResizable(false);
        name.setStyle("-fx-alignment: CENTER-LEFT;");
        host.setStyle("-fx-alignment: CENTER-LEFT;");
        players.setStyle("-fx-alignment: CENTER-LEFT;");
        name.setPrefWidth(235);
        host.setPrefWidth(135);
        joinLobby.setPrefWidth(90);

        createLobbyButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        logoutButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());

    }

    /**
     * Die Methode fängt den Button-Klick ab und prüft, ob der LobbyName gültig ist.
     * Falls nein: Wird eine Fehlermeldung rausgegeben.
     * Falls ja: Wir eine CreateLobbyRequest mit dem eingegeben LobbyNamen und dem eingeloggten User auf den
     * Eventbus gepackt.
     *
     * @author Paula, Haschem, Ferit, Julia, Keno O.
     * @version 0.2
     * @since Sprint2
     */
    @FXML
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Pressed, SoundMediaPlayer.Type.Sound).play();
        List<String> lobbyNames = new ArrayList<>();
        lobbies.forEach(lobby -> lobbyNames.add(lobby.getName()));
        if (lobbyNames.contains(lobbyName.getText())) {
            showAlert(Alert.AlertType.WARNING, "Dieser Name ist bereits vergeben", "Fehler");
            lobbyName.requestFocus();
        } else if (Pattern.matches("([a-zA-Z]|[0-9])+(([a-zA-Z]|[0-9])+([a-zA-Z]|[0-9]| )*([a-zA-Z]|[0-9])+)*", lobbyName.getText())) {
            CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()));
            eventBus.post(msg);
            LOG.info("Request wurde gesendet.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Bitte geben Sie einen gültigen Lobby Namen ein!\n\nDieser darf aus Buchstaben, Zahlen und Leerzeichen bestehen, aber nicht mit einem Leerzeichen beginnen oder enden", "Fehler");
            lobbyName.requestFocus();
        }
        lobbyName.clear();
    }


    /**
     * Login successful.
     *
     * @param message the message
     */
    @Subscribe
    public void loginSuccessful(LoginSuccessfulResponse message) {
        loggedInUser = message.getUser();
        chatViewPresenter.setloggedInUser(loggedInUser);
        chatViewPresenter.userJoined(loggedInUser.getUsername());
        LOG.debug("Logged in user: " + loggedInUser.getUsername());
        userService.retrieveAllUsers();
        lobbyService.retrieveAllLobbies();
    }

    /**
     * New user.
     *
     * @param message the message
     */
    @Subscribe
    public void newUser(UserLoggedInMessage message) {
        LOG.debug("New user " + message.getUsername() + " logged in");
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.getUsername().equals(message.getUsername())) {
                chatViewPresenter.userJoined(message.getUsername());
                users.add(message.getUsername());
            }
        });
    }

    /**
     * User left.
     *
     * @param message the message
     */
    @Subscribe
    public void userLeft(UserLoggedOutMessage message) {
        LOG.debug("User " + message.getUsername() + " logged out");
        Platform.runLater(() -> {
            if (users.contains(message.getUsername())) {
                users.remove(message.getUsername());
                chatViewPresenter.userLeft(message.getUsername());
            }
        });

    }

    /**
     * Fügt eine neu erstellte Lobby zur Tabelle hinzu
     *
     * @param message
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void newLobbyCreated(CreateLobbyMessage message) {
        if(lobbies != null) {
            Platform.runLater(() -> {
                lobbies.add(0, message.getLobby());
            });
        }
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem ein User einer Lobby beigetreten ist
     *
     * @param message
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void userJoinedLobby(UserJoinedLobbyMessage message) {
        if(lobbies != null) {
            Platform.runLater(() -> {
                lobbies.removeIf(lobby -> lobby.getName().equals(message.getLobbyName()));
                lobbies.add(0, message.getLobby());
            });
        }
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem ein User eine Lobby verlassen hat
     *
     * @param message
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void userLeftLobby(UserLeftLobbyMessage message) {
        if(lobbies != null) {
            Platform.runLater(() -> {
                lobbies.removeIf(lobby -> lobby.getName().equals(message.getLobbyName()));
                if(message.getLobby() != null) {
                    lobbies.add(0, message.getLobby());
                }
            });
        }
    }

    /**
     * User list.
     *
     * @param allUsersResponse the all users response
     */
    @Subscribe
    public void userList(AllOnlineUsersResponse allUsersResponse) {
        LOG.debug("Update of user list " + allUsersResponse.getUsers());
        updateUsersList(allUsersResponse.getUsers());
    }


    /**
     * Erstes Erstellen der Lobbytabelle beim Login
     *
     * @param allLobbiesResponse
     * @author Julia
     * @since Sprint2
     */
    @Subscribe
    public void lobbyTable(AllOnlineLobbiesResponse allLobbiesResponse) {
        LOG.debug("Updating of lobbies list" + allLobbiesResponse.getLobbies());
        updateLobbiesTable(allLobbiesResponse.getLobbies());
    }

    //-----------------
    // Help methods
    //-----------------

    /**
     * updatet die lobbytabelle
     *
     * @author Julia
     * @since Sprint2
     */
    private void updateLobbiesTable(List<LobbyDTO> lobbyList) {
        Platform.runLater(() -> {
            if (lobbies == null) {
                lobbies = FXCollections.observableArrayList();
                lobbiesView.setItems(lobbies);
            }
            lobbies.clear();
            lobbies.addAll(lobbyList);
        });
    }


    /**
     * Hilfsmethode zum Erstellen des Buttons zum Betreten einer Lobby
     */
    private void addJoinLobbyButton() {
        Callback<TableColumn<Lobby, Void>, TableCell<Lobby, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Lobby, Void> call(final TableColumn<Lobby, Void> param) {

                return new TableCell<>() {
                    final Button joinLobbyButton = new Button("Beitreten");

                    {
                        joinLobbyButton.setOnAction((ActionEvent event) -> {
                            Lobby lobby = getTableView().getItems().get(getIndex());
                            if (lobby.getPlayers() == 4) {
                                showAlert(Alert.AlertType.WARNING, "Diese Lobby ist voll!", "Fehler");
                            } else if (lobby.getUsers().contains(loggedInUser)) {
                                showAlert(Alert.AlertType.WARNING, "Du bist dieser Lobby schon beigetreten!", "Fehler");
                            } else {
                                lobbyService.joinLobby(lobby.getName(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()), lobby.getLobbyID());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(joinLobbyButton);
                        }
                    }
                };
            }
        };

        joinLobby.setCellFactory(cellFactory);
    }

    private void updateUsersList(List<UserDTO> userList) {
        // Attention: This must be done on the FX Thread!
        Platform.runLater(() -> {
            if (users == null) {
                users = FXCollections.observableArrayList();
                usersView.setItems(users);
            }
            users.clear();
            userList.forEach(u -> users.add(u.getUsername()));
        });
    }


}