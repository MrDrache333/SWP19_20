package de.uol.swp.client.settings;

import com.google.common.eventbus.EventBus;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.settings.event.CloseDeleteAccountEvent;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Presenter für das Account löschen Fenster
 *
 * @author Anna
 * @since Sprint 4
 */
@SuppressWarnings("UnstableApiUsage, unused")
public class DeleteAccountPresenter {

    /**
     * Der Pfad zur FXML dieses Presenters.
     */
    public static final String fxml = "/fxml/DeleteAccountView.fxml";
    /**
     * Der Pfad zum Stylesheet.
     */
    public static final String css = "css/DeleteAccountPresenter.css";
    private static final Logger LOG = LogManager.getLogger(DeleteAccountPresenter.class);

    private final User loggedInUser;
    private final LobbyService lobbyService;
    private final UserService userService;
    private final EventBus eventBus;

    /**
     * Instanziert ein neuen DeleteAccountPresenter.
     *
     * @param loggedInUser Der aktuelle Benutzer
     * @param lobbyService Der zu verwendene Lobby-Service
     * @param userService  Der zu verwendene User-Service
     * @param eventBus     Der zu verwendene Event-Bus
     * @author Anna
     * @since Sprint 4
     */
    public DeleteAccountPresenter(User loggedInUser, LobbyService lobbyService, UserService userService, EventBus eventBus) {
        this.loggedInUser = loggedInUser;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.eventBus = eventBus;
    }

    /**
     * Wenn der Ja-Button innerhalb des Fenster gedrückt wurde.
     *
     * @param actionEvent Das ActionEvent, was diese Methode aufgerufen hat
     * @author Anna
     * @since Sprint 4
     */
    @FXML
    public void onYesButtonPressed(ActionEvent actionEvent) {
        userService.hardLogout(loggedInUser);
        LOG.debug("Der Benutzer " + loggedInUser.getUsername() + " wurde ausgeloggt!");
        userService.dropUser(loggedInUser);
        LOG.debug("Der Benutzer " + loggedInUser.getUsername() + " hat seinen Account gelöscht!");
    }

    /**
     * Wenn der Nein-Button innerhalb des Fenster gedrückt wurde.
     *
     * @param actionEvent Das ActionEvent, was diese Methode aufgerufen hat
     * @author Anna
     * @since Sprint 4
     */
    @FXML
    public void onNoButtonPressed(ActionEvent actionEvent) {
        eventBus.post(new CloseDeleteAccountEvent());
    }
}
