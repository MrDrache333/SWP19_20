package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.Objects;

/**
 * Request zum Registrieren eines neues Users
 *
 * @author Marco
 * @since Start
 */
public class RegisterUserRequest extends AbstractRequestMessage {

    final private User toCreate;

    /**
     * Instanziiert ein RegisterUserRequest
     *
     * @param user der User, der registriert werden soll
     */
    public RegisterUserRequest(User user) {
        this.toCreate = user;
    }

    /**
     * Gibt den User zurück
     *
     * @return der User
     */
    public User getUser() {
        return toCreate;
    }

    /**
     * User muss nicht autorisiert sein, um diesen Request nutzen zu können
     *
     * @return false
     */
    @Override
    public boolean authorizationNeeded() {
        return false;
    }

    /**
     * Überprüft ob das übergebene Objekt mit diesem (this) übereinstimmt
     *
     * @param o das Objekt
     * @return true wenn die Objekte gleich sind, sonst false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterUserRequest that = (RegisterUserRequest) o;
        return Objects.equals(toCreate, that.toCreate);
    }

    /**
     * Generiert einen Hash Code aus dem zu registrierenden User
     *
     * @return der HashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(toCreate);
    }
}
