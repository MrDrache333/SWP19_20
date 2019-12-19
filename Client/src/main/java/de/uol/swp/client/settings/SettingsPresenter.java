package de.uol.swp.client.settings;

import de.uol.swp.common.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Settings presenter.
 */
public class SettingsPresenter {

    /**
     * The constant fxml.
     */
    public static final String fxml = "/fxml/SettingsView.fxml";
    private static final Logger LOG = LogManager.getLogger(SettingsPresenter.class);


    public SettingsPresenter(){
    }


    @FXML
    public void onSaveButtonPressed(ActionEvent event){
    //TODO implement
    }

    public void onDeleteAccountButtonPressed(ActionEvent actionEvent) {
        //TODO implement
    }

    public void onCancelButtonPressed(ActionEvent actionEvent) {
        //TODO implement
    }
}
