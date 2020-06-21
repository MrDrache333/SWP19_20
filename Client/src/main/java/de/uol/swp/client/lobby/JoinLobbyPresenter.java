package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AlertBox;
import de.uol.swp.client.lobby.event.CloseJoinLobbyEvent;
import de.uol.swp.client.main.MainMenuPresenter;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("UnstableApiUsage, unused")
public class JoinLobbyPresenter {

    /**
     * FXML
     */
    public static final String fxml = "/fxml/JoinLobbyView.fxml";
    public static final String css = "css/JoinLobbyView.css";
    private static final Logger LOG = LogManager.getLogger(JoinLobbyPresenter.class);

    private User loggedInUser;
    private final LobbyService lobbyService;
    private MainMenuPresenter mainMenuPresenter;
    private final UserService userService;
    private final EventBus eventBus;
    private final Lobby lobby;

    @FXML
    private Button cancelButton;
    @FXML
    private Button joinButton;
    @FXML
    private PasswordField passwordField;

    /**
     * Konstruktor des JoinLobbyPresenters
     *
     * @param loggedInUser Der eingeloggte User
     * @param lobbyService Der LobbyService
     * @param userService Der UserService
     * @param eventBus Der EventBus
     * @param lobby Die Lobby
     * @author Paula
     * @since Sprint 7
     */
    public JoinLobbyPresenter(User loggedInUser, LobbyService lobbyService, UserService userService, EventBus eventBus, Lobby lobby) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.eventBus = eventBus;
        this.lobby = lobby;
    }

    /**
     * Nach Eingabe des Passworts der Lobby wird hier das Passwort überprüft, ist es
     * gleich, wird die lobbyJoinUserRequest verschickt.
     * Ist es falsch, wird man zur erneuten Eingabe aufgefordert.
     *
     * @param actionEvent Das ActionEvent
     * @author Paula
     * @since Sprint 7
     */
    @FXML
    public void onJoinButtonPressed(javafx.event.ActionEvent actionEvent) {
        // Passwörter stimmen überein
        if (lobby.getLobbyPassword().equals(String.valueOf(passwordField.getText())) || lobby.getLobbyPassword() == null) {
            lobbyService.joinLobby(lobby.getLobbyID(), new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()), false);
        }
        //Passwörter stimmen nicht überein
        else {
            new AlertBox(Alert.AlertType.ERROR, "Das eingegebene Passwort ist falsch.", "Fehler");
        }
        passwordField.clear();
        passwordField.requestFocus();


    }

    /**
     * Beim Drücken auf den Abbrechen Button schließt sich das Fenster.
     *
     * @param actionEvent Das ActionEvent
     * @since Sprint 7
     * @author Paula
     */
    @FXML
    public void onCancelButtonPressed(javafx.event.ActionEvent actionEvent) {
        eventBus.post(new CloseJoinLobbyEvent());
        passwordField.clear();
    }

    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if (loggedInUser != null && loggedInUser.getUsername().equals(message.getOldUser().getUsername())) {
            loggedInUser = message.getUser();
        }
    }

}




