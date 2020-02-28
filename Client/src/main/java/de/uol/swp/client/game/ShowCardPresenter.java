package de.uol.swp.client.game;

import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.SceneManager;
import de.uol.swp.client.game.event.CloseShowCardEvent;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.settings.event.CloseSettingsEvent;
import de.uol.swp.client.settings.event.DeleteAccountEvent;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.UserService;
import de.uol.swp.common.user.message.UpdatedUserMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

/**
 * Der ShowCard-Presenter
 * @author Rike
 * @since Sprint5
 */

public class ShowCardPresenter extends AbstractPresenter {

    /**
     * Die FXML Konstente
     */

    public static final String fxml = "fxml/ShowCardView.fxml";
    public static final String css = "css/SettingsPresenter.css";
    private static final Logger LOG = LogManager.getLogger(ShowCardPresenter.class);

    private User loggedInUser;
    private String cardID;
    private LobbyService lobbyService;
    private UserService userService;
    private EventBus eventBus;

    @FXML
    private Button buy;
    @FXML
    private Button back;

    //TODO: Nach Anpassung Abfragen (Zeile 196 - 215) entfernen -> Zeile 67-69
    private boolean istDran = true;
    private boolean inKaufphase = true;
    private boolean genuegendGeld = true;

    public ShowCardPresenter (User loggedInUser, String cardID, LobbyService lobbyService, UserService userService, EventBus eventBus){
        this.loggedInUser = loggedInUser;
        this.cardID = cardID;
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.eventBus = eventBus;
    }

    @FXML
    public void onBuyCardButtonPressed(ActionEvent event){
        if (istDran && inKaufphase && genuegendGeld){
            //TODO: Karte muss übergeben werden; Bewegung ausführen: AnimationManagement.buyCard(cardImage);
            System.out.println(cardID + " wurde gekauft");
            eventBus.post(new CloseShowCardEvent());
        }
        else if (!istDran){
            Platform.runLater(()->{
                SceneManager.showAlert(Alert.AlertType.WARNING, "Du bist nicht dran!", "Fehler");
            });
        }
        else if (istDran && !inKaufphase){
            //TODO: Durch Abfrage ob Spieler in Kaufphase ersetzen
            Platform.runLater(()->{
                SceneManager.showAlert(Alert.AlertType.WARNING, "Du bist nicht in der Kaufphase!", "Fehler");
            });
        }
        else if (istDran && inKaufphase && !genuegendGeld){
            //TODO: Durch Abfrage ob Spieler genügend Geld auf der Hand hat ersetzen
            Platform.runLater(()->{
                SceneManager.showAlert(Alert.AlertType.WARNING, "Du hast nicht genügend Geld!", "Fehler");
            });
        }
    }

    @FXML
    public void onBackButtonPressed(ActionEvent event){
        eventBus.post(new CloseShowCardEvent());
    }

}
