package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.AlertBox;
import de.uol.swp.client.lobby.event.CloseCreateLobbyEvent;
import de.uol.swp.client.sound.SoundMediaPlayer;
import de.uol.swp.common.lobby.request.CreateLobbyRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage, unused")
public class CreateLobbyPresenter extends AbstractPresenter {

    /**
     * Die FXML Konstante.
     */
    public static final String fxml = "/fxml/CreateLobbyView.fxml";
    public static final String css = "css/CreateLobbyView.css";
    private static final Logger LOG = LogManager.getLogger(CreateLobbyPresenter.class);

    private User loggedInUser;
    private final EventBus eventBus;

    @FXML
    private Button cancelButton;
    @FXML
    private Button createButton;
    @FXML
    private TextField lobbynameField;
    @FXML
    private PasswordField passwordField;

    /**
     * Erstellt einen Lobby Presenter
     *
     * @param loggedInUser der eingeloggte User
     * @param lobbyService der LobbyService
     * @param userService  der UserService
     * @param eventBus     der Eventbus
     * @author Paula
     * @since Sprint 7
     */
    public CreateLobbyPresenter(User loggedInUser, LobbyService lobbyService, UserService userService, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.eventBus = eventBus;
    }

    /**
     * Sobald der Lobby erstellen Button gedrückt wird, öffnet sich ein Dialog. Hier wird man aufgefordert einen Namen für die Lobby anzugeben. Das Passwortfeld ist optional
     * auszufüllen. Bleibt das Passwortfeld leer, wird die Lobby offen. Wird ein Passwort angegegben, wird dieses gespeicherrt und die Lobby wird privat
     *
     * @param actionEvent Das ActionEvent
     * @author Paula
     * @since Sprint 7
     */
    @FXML
    public void onCreateLobbyButtonPressed(ActionEvent actionEvent) {
        String lobbyName = lobbynameField.getText();
        String lobbyPassword = passwordField.getText();
       createButton.setOnMouseEntered(event -> new SoundMediaPlayer(SoundMediaPlayer.Sound.Button_Hover, SoundMediaPlayer.Type.Sound).play());
        if (Pattern.matches("([a-zA-Z]|[0-9])+(([a-zA-Z]|[0-9])+([a-zA-Z]|[0-9]| )*([a-zA-Z]|[0-9])+)*", lobbyName)) {
            CreateLobbyRequest msg = new CreateLobbyRequest(lobbyName, new UserDTO(loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getEMail()), lobbyPassword);
            eventBus.post(msg);
            LOG.info("CreateLobbyRequest wurde gesendet.");
        } else {
            new AlertBox(Alert.AlertType.WARNING, "Bitte geben Sie einen gültigen Lobby Namen ein!\n\nDieser darf aus Buchstaben, Zahlen und Leerzeichen bestehen, aber nicht mit einem Leerzeichen beginnen oder enden.", "Fehler");
        }
        lobbynameField.clear();
        passwordField.clear();
        lobbynameField.requestFocus();
    }

    /**
     *
     * Beim Drücken auf den Abbrechen Button schließt sich das Fenster.
     *
     * @param actionEvent Das ActionEvent
     * @author Paula
     * @since Sprint 7
     */
    @FXML
    public void onCancelButtonPressed(ActionEvent actionEvent) {
        eventBus.post(new CloseCreateLobbyEvent());
        passwordField.clear();
        lobbynameField.clear();
    }

    /**
     *
     * Benutzer wird geupdated.
     *
     * @param message Die UpdatedUserMessage
     * @author Paula
     * @since Sprint 7
     */
    @Subscribe
    public void updatedUser(UpdatedUserMessage message) {
        if(loggedInUser != null && loggedInUser.getUsername().equals(message.getOldUser().getUsername()))
            loggedInUser = message.getUser();
    }
}


