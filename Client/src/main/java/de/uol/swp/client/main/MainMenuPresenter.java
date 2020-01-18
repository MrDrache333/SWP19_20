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
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.request.OpenSettingsRequest;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import javafx.application.Platform;
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

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;


public class MainMenuPresenter extends AbstractPresenter {


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
    private TableColumn<Lobby, String> players = new TableColumn<>("Spieler");
    @FXML
    private TableColumn<Lobby, Void> joinLobby = new TableColumn<>();
    @FXML
    private Pane chatView;
    @FXML
    private Button createLobbyButton, logoutButton;


    //--------------------------------------
    // FXML METHODS
    //--------------------------------------

    /**
     * Hauptmenü initialisiert
     *
     * @throws IOException mögliche Fehlermeldung
     * @author Marco, Julia, Keno O.
     * @since Start
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
        name.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName() + (c.getValue().getLobbyPassword().equals("") ? " (offen)" : " (privat)")));
        host.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOwner().getUsername()));
        addJoinLobbyButton();
        lobbiesView.getColumns().addAll(name, host, players, joinLobby);
        lobbiesView.setPlaceholder(new Label("Keine Lobbies vorhanden"));
        players.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlayers() + " / " + c.getValue().getMaxPlayer()));
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
     * Sobald der Lobby erstellen Button gedrückt wird, öffnet sich ein Dialog. Hier wird man aufgefordert einen Namen für die Lobby anzugeben. Das Passwortfeld ist optional
     * auszufüllen. Bleibt das Passwortfeld leer, wird die Lobby offen. Wird ein Passwort angegegben, wird dieses gespeicherrt und die Lobby wird privat
     *
     * @param event das Event
     * @author Rike, Paula
     * @since Sprint4
     */
    @FXML
    public void onShowLobbyDialogButtonPressed(ActionEvent event) {
        // Erzeugung Dialog
        JDialog createLobbyDialoge = new JDialog();
        createLobbyDialoge.setResizable(false);
        createLobbyDialoge.setTitle("Lobby erstellen");
        createLobbyDialoge.setSize(400, 150);
        JPanel panel = new JPanel();

        // Textfeld für Name wird erstellt und Panel hinzugefügt
        // Text und Spaltenanzahl werden dabei direkt gesetzt
        JLabel lname = new JLabel("Lobbyname: ");
        JTextField lName_input = new JTextField("", 20);
        lname.setSize(60, 60);
        panel.add(lname);
        panel.add(lName_input);

        // Textfeld für Passwort wird erstellt und Panel hinzugefügt´
        JLabel lobbyPassword = new JLabel("Passwort (optional): ");
        JPasswordField lPassword_input = new JPasswordField("", 20);
        lPassword_input.setEchoChar('*');
        panel.add(lobbyPassword);
        panel.add(lPassword_input);

        //Lobby erstellen Button + Action
        JButton createLobby = new JButton("Lobby erstellen");
        ActionListener onCreateLobbyButtonPressed = new ActionListener() {


            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                List<String> lobbyNames = new ArrayList<>();
                //Eingaben des Nutzers als String speichern
                String lobbyName = lName_input.getText();
                String lobbyPassword = String.valueOf(lPassword_input.getPassword());
                //Lobbys werden durchgegangen
                lobbies.forEach(lobby -> lobbyNames.add(lobby.getName()));
                // wenn Name vorhanden: Alert + Moeglichkeit neuen Namen anzugeben
                if (lobbyNames.contains(lName_input.getText())) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.WARNING, "Dieser Name ist bereits vergeben", "Fehler");
                        lName_input.setText("");
                        createLobbyDialoge.requestFocus();
                    });
                }
                // Wenn Name noch nicht vorhanden: Erstellen neuer Lobby
                else if (Pattern.matches("([a-zA-Z]|[0-9])+(([a-zA-Z]|[0-9])+([a-zA-Z]|[0-9]| )*([a-zA-Z]|[0-9])+)*", lobbyName)) {
                    CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName, lobbyPassword, new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()));
                    eventBus.post(msg);
                    LOG.info("Request wurde gesendet.");

                    //  Dialog wird geschlossen
                    createLobbyDialoge.setVisible(false);
                } else {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.WARNING, "Bitte geben Sie einen gültigen Lobby Namen ein!\n\nDieser darf aus Buchstaben, Zahlen und Leerzeichen bestehen, aber nicht mit einem Leerzeichen beginnen oder enden", "Fehler");
                        lName_input.setText("");
                        createLobbyDialoge.requestFocus();
                    });
                }
            }
        };
        createLobby.addActionListener(onCreateLobbyButtonPressed);
        panel.add(createLobby);
        createLobbyDialoge.add(panel);
        createLobbyDialoge.setVisible(true);

    }


    /**
     * Die Methode postet ein Request auf den Bus, wenn der Einstellungen-Button gedrückt wird
     *
     * @param actionEvent das ActionEvent
     * @author Anna
     * @since Sprint4
     */
    @FXML
    public void onSettingsButtonPressed(ActionEvent actionEvent) {
        OpenSettingsRequest request = new OpenSettingsRequest(loggedInUser);
        eventBus.post(request);
    }

    /**
     * Drückt man auf den LogoutButton, wird der User aus allen Lobbys, in denen er angemeldet ist, entfernt
     * Zudem wird der User ausgeloggt.
     *
     * @param actionEvent das ActionEvent
     * @author Julia, Paula
     * @since Sprint3
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
     * Login war erfolgreich. Der User tritt dem globalen Chat bei. Lobbys werden aktualisiert
     *
     * @param message Die Nachricht
     * @author Marco
     * @since Start
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
     * Neuer User loggt sich sein
     *
     * @param message Die UserLoggedInMessage
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void newUser(UserLoggedInMessage message) {
        LOG.debug("New user " + message.getUsername() + " logged in");
        Platform.runLater(() -> {
            if (users != null && loggedInUser != null && !loggedInUser.getUsername().equals(message.getUsername())) {
                chatViewPresenter.userJoined(message.getUsername());
                if (!users.contains(message.getUsername())) {
                    users.add(message.getUsername());
                }
            }
        });
    }

    /**
     * User wird aus der Liste entfernt, wenn er sich asuegloggt hat
     *
     * @param message die UserLoggedOutMessage
     * @author Marco, Keno O., Julia
     * @since Start
     */
    @Subscribe
    public void userLoggedOut(UserLoggedOutMessage message) {
        LOG.debug("User " + message.getUsername() + " logged out");
        userLeft(message.getUsername());
    }

    /**
     * User wird aus der Liste entfernt, wenn er seinen Account gelöscht hat
     *
     * @param message die UserDroppedMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void userDropped(UserDroppedMessage message) {
        LOG.debug("User " + message.getUser().getUsername() + " deleted his account");
        userLeft(message.getUser().getUsername());
    }

    /**
     * Fügt eine neu erstellte Lobby zur Tabelle hinzu
     *
     * @param message die CreateLobbyMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void newLobbyCreated(CreateLobbyMessage message) {
        LOG.debug("New lobby " + message.getLobbyName() + " created");
        Platform.runLater(() -> {
            lobbies.add(0, message.getLobby());
        });
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem ein User einer Lobby beigetreten ist
     *
     * @param message die UserJoinedLobbyMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void userJoinedLobby(UserJoinedLobbyMessage message) {
        LOG.debug("User " + message.getUser().getUsername() + " joined lobby " + message.getLobbyName());
        Platform.runLater(() -> {
            lobbies.removeIf(lobby -> lobby.getName().equals(message.getLobbyName()));
            lobbies.add(0, message.getLobby());
        });
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem ein User eine Lobby verlassen hat
     *
     * @param message die UserLeftLobbyMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void userLeftLobby(UserLeftLobbyMessage message) {
        LOG.debug("User " + message.getUser().getUsername() + " left lobby " + message.getLobbyName());
        Platform.runLater(() -> {
            lobbies.removeIf(lobby -> lobby.getName().equals(message.getLobbyName()));
            if (message.getLobby() != null) {
                lobbies.add(0, message.getLobby());
            }
        });
    }

    /**
     * Aktualisiert den loggedInUser und die Lobbytabelle sowie die Userliste, falls sich der Username geändert hat
     *
     * @param message die UpdatedUserMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        LOG.debug("User " + message.getOldUser().getUsername() + " updated his data. Updating lobby table and user list");
        if (loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
        Platform.runLater(() -> {
            List<Lobby> toRemove = new ArrayList<>();
            List<Lobby> toAdd = new ArrayList<>();
            for (Lobby lobby : lobbies) {
                if (lobby.getUsers().contains(message.getOldUser())) {
                    User updatedOwner = lobby.getOwner();
                    //ggf. Owner aktualisieren
                    if (lobby.getOwner().getUsername().equals(message.getOldUser().getUsername())) {
                        updatedOwner = message.getUser();
                    }
                    //Userliste der Lobby aktualisieren
                    List<User> updatedUsers = new ArrayList<>(lobby.getUsers());
                    updatedUsers.remove(message.getOldUser());
                    updatedUsers.add(message.getUser());
                    Set<User> newUsers = new TreeSet<>(updatedUsers);
                    Lobby lobbyToUpdate = new LobbyDTO(lobby.getName(), updatedOwner, lobby.getLobbyID(), lobby.getLobbyPassword(), newUsers, lobby.getPlayers(), lobby.getMaxPlayer());
                    toRemove.add(lobby);
                    toAdd.add(lobbyToUpdate);
                }
            }

            //Lobbytabelle aktualisieren
            lobbies.removeAll(toRemove);
            lobbies.addAll(toAdd);

            //Userliste nur aktualisieren, wenn sich der Username geändert hat
            if (!message.getUser().getUsername().equals(message.getOldUser().getUsername())) {
                users.removeIf(user -> user.equals(message.getOldUser().getUsername()));
                users.add(message.getUser().getUsername());
            }
        });
    }

    /**
     * User Liste wird aktualisiert
     *
     * @param allUsersResponse die AllOnlineUsersResoinse
     * @author Marco
     * @since Start
     */
    @Subscribe
    public void userList(AllOnlineUsersResponse allUsersResponse) {
        LOG.debug("Update of user list " + allUsersResponse.getUsers());
        updateUsersList(allUsersResponse.getUsers());
    }


    /**
     * Erstes Erstellen der Lobbytabelle beim Login
     *
     * @param allLobbiesResponse Die AllOnlineLobbiesResponse
     * @author Julia
     * @since Sprint2
     */
    @Subscribe
    public void lobbyTable(AllOnlineLobbiesResponse allLobbiesResponse) {
        LOG.debug("Updating of lobbies list" + allLobbiesResponse.getLobbies());
        updateLobbiesTable(allLobbiesResponse.getLobbies());
    }


    //--------------------------------------
    // PUBLIC METHODS
    //--------------------------------------


    /**
     * Alert wird erstellt
     *
     * @param type    der Alert-Typ
     * @param message die Message
     * @param title   der Titel des Alerts
     * @author Paula, Haschem, Ferit
     * @since Sprint1
     */
    public static void showAlert(Alert.AlertType type, String message, String title) {
        Alert alert = new Alert(type, "");
        alert.setResizable(false);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText(title);
        alert.show();
    }

    //--------------------------------------
    // PRIVATE METHODS
    //--------------------------------------

    /**
     * Updatet die lobbytabelle
     *
     * @param lobbyList die LobbyListe, die alle Lobbys enthält
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
     * beim join wird ebenfalls überprüft ob die Lobby ein lobbyPassword besitzt und ggf. dieses abgefragt
     *
     * @author Rike, Julia, Paula
     * @since Sprint3
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
                            if (lobby.getPlayers() == lobby.getMaxPlayer()) {
                                showAlert(Alert.AlertType.WARNING, "Diese Lobby ist voll!", "Fehler");
                            } else if (lobby.getUsers().contains(loggedInUser)) {
                                showAlert(Alert.AlertType.WARNING, "Du bist dieser Lobby schon beigetreten!", "Fehler");
                            } else {
                                if (lobby.getLobbyPassword().isEmpty()) {
                                    lobbyService.joinLobby(lobby.getName(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()), lobby.getLobbyID());
                                } else if (!lobby.getLobbyPassword().isEmpty()) {
                                    // Es soll ein Dialogfenster geöffnet werden, wenn für die Lobby ein lobbyPassword existiert
                                    JDialog joinLobbyDialog = new JDialog();
                                    joinLobbyDialog.setResizable(false);
                                    joinLobbyDialog.setTitle("Lobby beitreten");
                                    joinLobbyDialog.setSize(400, 150);

                                    JPanel panel = new JPanel();
                                    // Textfeld für Passwort wird erstellt und Panel hinzugefügt
                                    JLabel enteredPassword = new JLabel("Passwort: ");
                                    JPasswordField ePassword = new JPasswordField("", 15);
                                    panel.add(enteredPassword);
                                    panel.add(ePassword);

                                    // enteredPassword mit dem lobbyPassword vergleichen, wenn "Beitreten"-Button gedrückt
                                    JButton joinLobby = new JButton("Lobby beitreten");
                                    ActionListener onEnteredPasswordPressed = new ActionListener() {
                                        @Override
                                        public void actionPerformed(java.awt.event.ActionEvent e) {
                                            // Passwort ist nicht gleich, Fehlermeldung erscheint
                                            if (!lobby.getLobbyPassword().equals(String.valueOf(ePassword.getPassword()))) {
                                                Platform.runLater(() -> {
                                                    showAlert(Alert.AlertType.ERROR, "Das eingegebene Passwort ist falsch.", "Fehler");
                                                    ePassword.setText("");
                                                });
                                            }
                                            // Passwort ist gleich, man wird zur Lobby hinzugefügt
                                            else if (lobby.getLobbyPassword().equals(String.valueOf(ePassword.getPassword()))) {
                                                lobbyService.joinLobby(lobby.getName(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()), lobby.getLobbyID());
                                                // Dialog wird nicht mehr angezeigt
                                                joinLobbyDialog.setVisible(false);
                                            }
                                        }
                                    };


                                    joinLobby.addActionListener(onEnteredPasswordPressed);
                                    panel.add(joinLobby);
                                    joinLobbyDialog.add(panel);
                                    joinLobbyDialog.setVisible(true);
                                }
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

    /**
     * Updatet die Liste mit den angemeldeten Usern
     *
     * @param userList Die Liste, die, die eingeloggten User enthält
     * @author Marco
     * @since Start
     */
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

    /**
     * User wird aus der Userliste entfernt und im Chat wird angezeigt, dass er das Spiel verlassen hat
     *
     * @param username der Name des Users
     * @author Julia
     * @since Sprint4
     */
    private void userLeft(String username) {
        Platform.runLater(() -> {
            if (users.contains(username)) {
                users.remove(username);
                chatViewPresenter.userLeft(username);
            }
        });
    }
}