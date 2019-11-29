package de.uol.swp.client.main;

import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.chat.ChatViewPresenter;
import de.uol.swp.common.chat.message.NewChatMessage;
import de.uol.swp.common.chat.response.ChatResponseMessage;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.response.AllOnlineLobbiesResponse;
import de.uol.swp.common.user.dto.UserDTO;
import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.response.AllOnlineUsersResponse;
import de.uol.swp.common.user.response.LoginSuccessfulMessage;
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
import java.util.List;

/**
 * The type Main menu presenter.
 */
public class MainMenuPresenter extends AbstractPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/MainMenuView.fxml";
    private static final Logger LOG = LogManager.getLogger(MainMenuPresenter.class);
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

    private ObservableList<String> users;
    private ChatViewPresenter chatViewPresenter;
    private ObservableList<Lobby> lobbies;

    /**
     * Initialize.
     *
     * @throws IOException the io exception
     */
    @FXML
    public void initialize() throws IOException {
        //Neue Instanz einer ChatViewPresenter-Controller-Klasse erstellen und nötige Parameter uebergeben
        chatViewPresenter = new ChatViewPresenter("allgemeiner", ChatViewPresenter.THEME.Light, chatService);
        chatViewPresenter.setChatId("global");
        //FXML laden
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ChatViewPresenter.fxml));
        //Controller der FXML setzen (Nicht in der FXML festlegen, da es immer eine eigene Instanz davon sein muss)
        loader.setController(chatViewPresenter);
        //Den ChatView in die chatView-Pane dieses Controllers laden
        chatView.getChildren().add(loader.load());

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
        joinLobby.setPrefWidth(110);
    }

    /**
     * Hilfsmethode zum Erstellen des Buttons zum Betreten einer Lobby
     */
    private void addJoinLobbyButton() {
        Callback<TableColumn<Lobby, Void>, TableCell<Lobby, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Lobby, Void> call(final TableColumn<Lobby, Void> param) {
                final TableCell<Lobby, Void> cell = new TableCell<>() {
                    final Button joinLobbyButton = new Button("Lobby beitreten");
                    {
                        joinLobbyButton.setOnAction((ActionEvent event) -> {
                            Lobby lobby = getTableView().getItems().get(getIndex());
                            if(lobby.getPlayers() == 4) {
                                showAlert(Alert.AlertType.WARNING, "Diese Lobby ist voll!", "Fehler");
                            }
                            else if(lobby.getUsers().contains(loggedInUser)) {
                                showAlert(Alert.AlertType.WARNING, "Du bist dieser Lobby schon beigetreten!", "Fehler");
                            }
                            else {
                                lobbyService.joinLobby(lobby.getName(), loggedInUser, lobby.getLobbyID());
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) { setGraphic(null); }
                        else { setGraphic(joinLobbyButton); }
                    }
                };

                return cell;
            }
        };

        joinLobby.setCellFactory(cellFactory);
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------

    /**
     * On chat response message.
     *
     * @param msg the msg
     */
    @Subscribe
    public void onChatResponseMessage(ChatResponseMessage msg) {
        chatViewPresenter.onChatResponseMessage(msg);
    }

    /**
     * On new chat message.
     *
     * @param msg the msg
     */
    @Subscribe
    public void onNewChatMessage(NewChatMessage msg) {
        chatViewPresenter.onNewChatMessage(msg);
    }

    /**
     * Login successful.
     *
     * @param message the message
     */
    @Subscribe
    public void loginSuccessful(LoginSuccessfulMessage message) {
        loggedInUser = message.getUser();
        chatViewPresenter.setloggedInUser(loggedInUser);
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
            if (users != null && loggedInUser != null && !loggedInUser.equals(message.getUsername()))
                users.add(message.getUsername());
            chatViewPresenter.userJoined(message.getUsername());
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
     * User list.
     *
     * @param allUsersResponse the all users response
     */
    @Subscribe
    public void userList(AllOnlineUsersResponse allUsersResponse) {
        LOG.debug("Update of user list " + allUsersResponse.getUsers());
        updateUsersList(allUsersResponse.getUsers());
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

    /**
     * Erstes Erstellen der Lobbytabelle beim Login und Aktualisierung
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
            lobbyList.forEach(l -> lobbies.add(l));
        });
    }


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
     * Die Methode fängt den Button-Klick ab und prüft, ob der LobbyName gültig ist.
     * Falls nein: Wird eine Fehlermeldung rausgegeben.
     * Falls ja: Wir eine CreateLobbyRequest mit dem eingegeben LobbyNamen und dem eingeloggten User auf den
     * Eventbus gepackt.
     *
     * @author Paula, Haschem, Ferit, Julia
     * @version 0.1
     * @since Sprint2
     */
    @FXML
    public void OnCreateLobbyButtonPressed(ActionEvent event) {
        boolean invalidLobbyName = false;
        boolean onlyWhitespaces = true;
        for (Lobby lobby : lobbies) {
            if (lobby.getName().equalsIgnoreCase(lobbyName.getText())) {
                invalidLobbyName = true;
                break;
            }
        }
        for(char c : lobbyName.getText().toCharArray()) {
            if(!Character.isWhitespace(c)) {
                onlyWhitespaces = false;
                break;
            }
        }
        if (lobbyName.getText().equals("")) {
            showAlert(Alert.AlertType.WARNING, "Bitte geben Sie einen Lobbynamen ein! ", "Fehler");
            lobbyName.requestFocus();
        }
        else if(invalidLobbyName) {
            showAlert(Alert.AlertType.WARNING, "Diese Lobby existiert bereits!", "Fehler");
            lobbyName.requestFocus();
        }
        else if(onlyWhitespaces) {
            showAlert(Alert.AlertType.WARNING, "Der Lobbyname darf nicht nur Leerzeichen enthalten!", "Fehler");
            lobbyName.requestFocus();
        }
        else {
            CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName.getText(), loggedInUser);
            eventBus.post(msg);
            LOG.info("Request wurde gesendet.");
        }

        lobbyName.clear();
    }

    @FXML
    public void onLogoutButtonPressed(ActionEvent actionEvent) {
        for(Lobby lobby : lobbies) {
            if(lobby.getUsers().contains(loggedInUser)) {
                if(lobby.getUsers().size() > 1) {
                    lobbyService.leaveLobby(lobby.getName(), loggedInUser, lobby.getLobbyID());
                }
                else {
                    //TODO Lobby löschen
                }
            }
        }
        userService.logout(loggedInUser);
    }

}