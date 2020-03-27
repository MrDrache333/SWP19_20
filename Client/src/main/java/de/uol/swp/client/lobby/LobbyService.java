package de.uol.swp.client.lobby;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.LobbyUser;
import de.uol.swp.common.lobby.request.*;
import de.uol.swp.common.message.RequestMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Der lobbyService
 *
 * @author Marco
 * @since Start
 */
public class LobbyService {

    private static final Logger LOG = LogManager.getLogger(LobbyService.class);
    private final EventBus bus;

    /**
     * Instanziiert einen neuen LobbyService.
     *
     * @param bus der Bus
     * @author Marco
     * @since Start
     */

    @Inject
    public LobbyService(EventBus bus) {
        this.bus = bus;
    }

    /**
     * Erstellt ein LobbyJoinUserRequest und postet diese auf den Eventbus.
     *
     * @param lobbyID die LobbyID der Lobby der beigetreten werden soll
     * @param user    der User der der Lobby beitreten will
     * @author Julia, Paula, Marvin
     * @since Sprint3
     */

    public void joinLobby(UUID lobbyID, UserDTO user) {
        LobbyJoinUserRequest request = new LobbyJoinUserRequest(lobbyID, user);
        bus.post(request);
    }

    /**
     * Erstellt eine LobbyLeaveUserRequest und postet diese auf den EventBus.
     *
     * @param lobbyID die LobbyID zum Lobbynamen
     * @param user    der User der die Lobby verlassen will
     * @author Julia, Paula
     * @since Sprint3
     */

    public void leaveLobby(UUID lobbyID, UserDTO user) {
        LobbyLeaveUserRequest request = new LobbyLeaveUserRequest(lobbyID, user);
        bus.post(request);
    }

    /**
     * Erstellt eine LeaveAllLobbiesOnLogoutRequest und postet diese auf den EventBus.
     *
     * @param user der User der alle Lobbys verlassen will
     * @author Julia, Paula
     * @since Sprint3
     */

    public void leaveAllLobbiesOnLogout(UserDTO user) {
        LeaveAllLobbiesOnLogoutRequest request = new LeaveAllLobbiesOnLogoutRequest(user);
        bus.post(request);
    }

    /**
     * Erstellt ein RetrieveAllOnlineLobbiesRequest und postet diese auf den Eventbus.
     *
     * @author Julia
     * @since Sprint2
     */

    public List<Lobby> retrieveAllLobbies() {
        RetrieveAllOnlineLobbiesRequest request = new RetrieveAllOnlineLobbiesRequest();
        bus.post(request);
        return null;
    }

    /**
     * Alternative Requesterstellung über lobbyID statt Name.
     *
     * @param lobbyID LobbyID über die die Request gestellt wird
     * @author Marvin
     * @since Sprint3
     */

    public ArrayList<LobbyUser> retrieveAllUsersInLobby(UUID lobbyID) {
        RequestMessage request = new RetrieveAllOnlineUsersInLobbyRequest(lobbyID);
        bus.post(request);
        return null;
    }


    public void setLobbyUserStatus(UUID lobbyID, UserDTO user, boolean status) {
        RequestMessage request = new UpdateLobbyReadyStatusRequest(lobbyID, user, status);
        bus.post(request);
    }

    /**
     * Anfrage wird erstellt und abgeschickt um Spieler zu kicken.
     *
     * @param lobbyID    Die LobbyID
     * @param gameOwner  Der Besitzer
     * @param userToKick Der zu entfernende Benutzer
     * @author Darian, Marvin
     * @since sprint4
     */
    public void kickUser(UUID lobbyID, UserDTO gameOwner, UserDTO userToKick) {
        RequestMessage request = new KickUserRequest(lobbyID, gameOwner, userToKick);
        bus.post(request);
    }

    /**
     * Erstellt einen SetMaxPlayerRequest.
     *
     * @param maxPlayer    die maximale Spielerzahl die gesetzt werden soll
     * @param lobbyID      die LobbyID der Lobby
     * @param loggedInUser der eingeloggte User
     * @author Timo, Rike, Marvin
     * @since Sprint 3
     */
    public void setMaxPlayer(UUID lobbyID, User loggedInUser, Integer maxPlayer) {
        SetMaxPlayerRequest cmd = new SetMaxPlayerRequest(lobbyID, (UserDTO) loggedInUser, maxPlayer);
        bus.post(cmd);
    }

    /**
     * Erstellt eine neue Stage und öffnet darin die Anleitung als WebView
     *
     * @author Timo
     * @since Sprint 5
     */
    public void startWebView() {
        Stage primaryStage = new Stage();
        File file = new File("Client/src/main/resources/html/anleitung/index.html");

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(file.toURI().toString());

        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 500, 800);

        primaryStage.setTitle("Dominion - Spieleanleitung");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}