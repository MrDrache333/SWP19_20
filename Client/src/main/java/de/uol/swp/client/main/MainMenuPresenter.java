package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.client.lobby.OpenJoinLobbyRequest;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.*;
import de.uol.swp.common.lobby.request.OpenLobbyCreateRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import de.uol.swp.common.user.message.UserDroppedMessage;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulResponse;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


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
    private TableColumn<Lobby, Circle> inGame = new TableColumn<>("im Spiel");
    @FXML
    private TableColumn<Lobby, Void> joinLobby = new TableColumn<>();
    @FXML
    private Pane chatView;
    @FXML
    private Button createLobbyButton;


    //--------------------------------------
    // FXML METHODS
    //--------------------------------------

    /**
     * Hauptmenü initialisiert
     *
     * @throws IOException mögliche Fehlermeldung
     * @author Marco, Keno O., Julia
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
        players.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlayers() + " / " + c.getValue().getMaxPlayer()));
        inGame.setCellValueFactory(c -> new SimpleObjectProperty<>(new Circle(6.0f, c.getValue().getInGame() ? Paint.valueOf("green") : Paint.valueOf("red"))));
        addJoinLobbyButton();
        lobbiesView.getColumns().addAll(name, host, players, inGame, joinLobby);
        lobbiesView.setPlaceholder(new Label("Keine Lobbies vorhanden"));
        name.setResizable(false);
        host.setResizable(false);
        players.setResizable(false);
        inGame.setResizable(false);
        joinLobby.setResizable(false);
        name.setStyle("-fx-alignment: CENTER-LEFT;");
        host.setStyle("-fx-alignment: CENTER-LEFT;");
        players.setStyle("-fx-alignment: CENTER-LEFT;");
        inGame.setStyle("-fx-alignment: CENTER;");
        joinLobby.setStyle("-fx-alignment: CENTER;");
        name.setPrefWidth(208);
        host.setPrefWidth(122);
        players.setPrefWidth(66);
        inGame.setPrefWidth(60);
        joinLobby.setPrefWidth(85);
    }

    /**
     * Request für Lobby erstellen Fenster
     *
     * @param actionEvent
     */
    @FXML
    public void onOpenCreateLobbyView(ActionEvent actionEvent) {
        OpenLobbyCreateRequest request = new OpenLobbyCreateRequest(loggedInUser);
        eventBus.post(request);
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
        LOG.debug("Neuer User " + message.getUsername() + " hat sich eingeloggt");
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
        LOG.debug("User " + message.getUser().getUsername() + " löschte seinen Account");
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
        LOG.debug("Neue Lobby " + message.getLobbyName() + " erstellt");
        if (message.getLobbyName() != null && lobbies != null) {
            Platform.runLater(() -> {
                lobbies.add(0, message.getLobby());
            });
        }
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
        LOG.debug("User " + message.getUser().getUsername() + " trat der Lobby " + message.getLobby().getName() + " bei.");
        if (lobbies != null)
            Platform.runLater(() -> {
                lobbies.removeIf(lobby -> lobby.getLobbyID().equals(message.getLobbyID()));
                lobbies.add(0, message.getLobby());
            });
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem ein User eine Lobby verlassen hat
     *
     * @param message die UserLeftLobbyMessage
     * @author Julia, Darian
     * @since Sprint4
     */
    @Subscribe
    public void userLeftLobby(UserLeftLobbyMessage message) {
        if (lobbies != null) {
            if (message.getLobby() != null) {
                LOG.debug("User " + message.getUser().getUsername() + " verließ Lobby  " + message.getLobby().getName());
                Platform.runLater(() -> {
                    lobbies.removeIf(lobby -> lobby.getLobbyID().equals(message.getLobbyID()));
                    lobbies.add(0, message.getLobby());
                });
            } else {
                lobbies.removeIf(lobby -> lobby.getLobbyID().equals(message.getLobbyID()));
            }
        }
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem sich ein User ausgeloggt oder seinen Account gelöscht hat
     *
     * @param message die UserLeftAllLobbiesMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void userLeftAllLobbies(UserLeftAllLobbiesMessage message) {
        Platform.runLater(() -> {
            if (lobbies != null) {
                lobbies.clear();
                lobbies.addAll(message.getLobbies());
            }
        });
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem ein User aus einer Lobby gekickt wurde
     *
     * @param message die KickUserMessage
     * @author Julia, Marvin, Darian
     * @since Sprint4
     */
    @Subscribe
    public void userKicked(KickUserMessage message) {
        LOG.debug("User " + message.getUser().getUsername() + " wurde von der Lobby " + message.getLobby().getName() + " gekickt.");
        if (lobbies != null)
            Platform.runLater(() -> {
                lobbies.removeIf(lobby -> lobby.getLobbyID().equals(message.getLobbyID()));
                lobbies.add(0, message.getLobby());
            });
    }

    /**
     * Aktualisiert die Lobbytabelle, nachdem die max. Spielerzahl einer Lobby gesetzt wurde
     *
     * @param message die SetMaxPlayerMessage
     * @author Julia
     * @since Sprint4
     */
    @Subscribe
    public void maxPlayerSet(SetMaxPlayerMessage message) {
        if (message.isSetMaxPlayerSet() && lobbies != null) {
            Platform.runLater(() -> {
                lobbies.removeIf(lobby -> lobby.getLobbyID().equals(message.getLobby().getLobbyID()));
                lobbies.add(0, message.getLobby());
            });
        }
    }

    /**
     * Aktualisiert den Status einer Lobby in der Tabelle, wenn ein Spiel in ihr begonnen hat
     *
     * @param message die StartGameMessage
     * @author Julia
     * @since Sprint6
     */
    @Subscribe
    public void onGameStart(StartGameMessage message) {
        if (lobbies != null)
            Platform.runLater(() -> {
                Lobby updatedLobby = null;
                for (Lobby lobby : lobbies) {
                    if (lobby.getLobbyID().equals(message.getLobbyID())) {
                        updatedLobby = lobby;
                        break;
                    }
                }
                if (updatedLobby == null) return;
                lobbies.remove(updatedLobby);
                updatedLobby.setInGame(true);
                lobbies.add(0, updatedLobby);
                lobbiesView.refresh();
            });
    }

    /**
     * Aktualisiert den Status einer Lobby in der Tabelle, wenn das Spiel in ihr zu Ende ist
     *
     * @param message die UpdatedInGameMessage
     * @author Julia
     * @since Sprint6
     */
    @Subscribe
    public void onGameEnd(UpdatedInGameMessage message) {
        if (lobbies != null)
            Platform.runLater(() -> {
                Lobby updatedLobby = null;
                for (Lobby lobby : lobbies) {
                    if (lobby.getLobbyID().equals(message.getLobbyID())) {
                        updatedLobby = lobby;
                        break;
                    }
                }
                lobbies.remove(updatedLobby);
                updatedLobby.setInGame(false);
                lobbies.add(0, updatedLobby);
                lobbiesView.refresh();
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
        LOG.debug("User " + message.getOldUser().getUsername() + " hat seine Daten aktualisiert. Aktualisierung von Lobby-Tabelle und Benutzerliste");
        if (loggedInUser != null) {
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
                        Lobby lobbyToUpdate = new LobbyDTO(lobby.getName(), updatedOwner, lobby.getLobbyID(), lobby.getLobbyPassword(), newUsers, lobby.getPlayers(), lobby.getMaxPlayer(), lobby.getInGame());
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
        LOG.debug("Aktualisierung der Benutzerliste " + allUsersResponse.getUsers());
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
        LOG.debug("Aktualisierung der Lobbyliste " + allLobbiesResponse.getLobbies());
        updateLobbiesTable(allLobbiesResponse.getLobbies());
    }


    //-----------------
    // PRIVATE METHODEN
    //-----------------

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
     * @author Rike, Julia, Paula, Marvin
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
                                SceneManager.showAlert(Alert.AlertType.WARNING, "Diese Lobby ist voll!", "Fehler");
                            } else if (lobby.getUsers().contains(loggedInUser)) {
                                SceneManager.showAlert(Alert.AlertType.WARNING, "Du bist dieser Lobby schon beigetreten!", "Fehler");
                            } else if (lobby.getInGame()) {
                                SceneManager.showAlert(Alert.AlertType.WARNING, "Du kannst einer Lobby nicht beitreten,\nwenn in ihr ein Spiel läuft!", "Fehler");
                            } else {
                                if (lobby.getLobbyPassword().isEmpty()) {
                                    lobbyService.joinLobby(lobby.getLobbyID(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()));
                                } else if (!lobby.getLobbyPassword().isEmpty()) {
                                    OpenJoinLobbyRequest request = new OpenJoinLobbyRequest(loggedInUser, lobby);
                                    eventBus.post(request);
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
            if (users != null && chatViewPresenter != null && users.contains(username)) {
                users.remove(username);
                chatViewPresenter.userLeft(username);
            }
        });
    }
}