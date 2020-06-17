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
@SuppressWarnings("UnstableApiUsage")
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
     * @since Sprint 3
     */

    public void joinLobby(UUID lobbyID, UserDTO user, Boolean isBot) {
        LobbyJoinUserRequest request = new LobbyJoinUserRequest(lobbyID, user, isBot);
        bus.post(request);
        LOG.info("LobbyJoinUserRequest wurde gesendet.");
    }

    /**
     * Erstellt eine LobbyLeaveUserRequest und postet diese auf den EventBus.
     *
     * @param lobbyID die LobbyID zum Lobbynamen
     * @param user    der User der die Lobby verlassen will
     * @author Julia, Paula
     * @since Sprint 3
     */

    public void leaveLobby(UUID lobbyID, UserDTO user) {
        LobbyLeaveUserRequest request = new LobbyLeaveUserRequest(lobbyID, user);
        bus.post(request);
    }

    /**
     * Erstellt ein RetrieveAllOnlineLobbiesRequest und postet diese auf den Eventbus.
     *
     * @author Julia
     * @since Sprint 2
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
     * @since Sprint 3
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
     * @since Sprint 4
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
     * Erstellt einen SendChosenCardsRequest, um die ausgewählten Karten an den Server zu schicken.
     *
     * @param lobbyID     die ID der Lobby
     * @param chosenCards die gewählten Aktionskarten
     * @author Anna, Fenja
     * @since Sprint 7
     */
    public void sendChosenCards(UUID lobbyID, ArrayList<Short> chosenCards) {
        SendChosenCardsRequest requestMessage = new SendChosenCardsRequest(lobbyID, chosenCards);
        bus.post(requestMessage);
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