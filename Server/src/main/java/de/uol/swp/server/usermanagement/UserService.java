package de.uol.swp.server.usermanagement;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import de.uol.swp.common.message.ResponseMessage;
import de.uol.swp.common.user.exception.RegistrationExceptionMessage;
import de.uol.swp.common.user.request.RegisterUserRequest;
import de.uol.swp.common.user.response.RegistrationSuccessfulResponse;
import de.uol.swp.server.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Mapping vom event bus calls to user management calls
 *
 * @author Marco Grawunder
 * @since Basissystem
 */
@SuppressWarnings("UnstableApiUsage, unused")
public class UserService extends AbstractService {

    private static final Logger LOG = LogManager.getLogger(UserService.class);

    private final UserManagement userManagement;

    /**
     * Erstellt einen neuen User service.
     *
     * @param eventBus       Der verwendete EventBus
     * @param userManagement Das verwendete UserManagement
     */
    @Inject
    public UserService(EventBus eventBus, UserManagement userManagement) {
        super(eventBus);
        this.userManagement = userManagement;
    }

    /**
     * Wenn auf dem Bus eine Anfrage zum erstellen eines Benutzers gesendet wurde
     *
     * @param req Der RegisterUser Request
     */
    @Subscribe
    private void onRegisterUserRequest(RegisterUserRequest req) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Neue Registrierungsanfrage mit folgendem User erhalten: " + req.getUser());
        }
        ResponseMessage returnMessage;
        try {
            //Versuchen den übergebenen neuen Benutzer zu registieren
            userManagement.createUser(req.getUser());
            returnMessage = new RegistrationSuccessfulResponse();
        } catch (Exception e) {
            LOG.error(e);
            //Bei Fehlern die Fehlermeldung an den Sender zurück senden
            returnMessage = new RegistrationExceptionMessage("Anlegen des folgenden User schlug fehl: " + req.getUser() + " " + e.getMessage());
        }

        //Wenn ein Kontext in dem Request übergeben wurde, dann übernehme diesen
        if (req.getMessageContext().isPresent()) {
            returnMessage.setMessageContext(req.getMessageContext().get());
        }
        post(returnMessage);
    }
}
