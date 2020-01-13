package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.Objects;

public class RegisterUserRequest extends AbstractRequestMessage {

    final private User toCreate;

    /**
     * Instanziiert einen neuen RegisterUserRequest
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
     * @return
     */
    @Override
    public boolean authorizationNeeded() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterUserRequest that = (RegisterUserRequest) o;
        return Objects.equals(toCreate, that.toCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toCreate);
    }
}
