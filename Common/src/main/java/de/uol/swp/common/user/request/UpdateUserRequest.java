package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.Objects;

/**
 * Request zum Updaten eines neuen Users
 *
 * @author Marco
 * @since Start
 */
public class UpdateUserRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 1834977681414180873L;
    final private User toUpdate;
    private final User oldUser;
    private final String currentPassword;

    /**
     * Benutzeranfrage aktualisieren
     *
     * @param user            der User
     * @param oldUser         der alter User
     * @param currentPassword Jetzige Passwort
     */
    public UpdateUserRequest(User user, User oldUser, String currentPassword) {
        this.toUpdate = user;
        this.oldUser = oldUser;
        this.currentPassword = currentPassword;
    }

    /**
     * Gibt den User
     *
     * @return gibt den aktuellen User
     * @author Marco
     * @since Start
     */
    public User getUser() {
        return toUpdate;
    }

    /**
     * Gibt den Alten User an
     *
     * @return Alter User
     * @author Marco
     * @since Start
     */
    public User getOldUser() {
        return oldUser;
    }

    /**
     * Gibt das aktuelle Passwort
     *
     * @return Jetzige Passwort
     * @author Marco
     * @since Start
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Überprüft ob das übergebene Objekt mit diesem (this) übereinstimmt
     *
     * @param o
     * @return true wenn die Objekte gleich sind, sonst false
     * @author Keno S.
     * @since Sprint 3
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(toUpdate, that.toUpdate);
    }

    /**
     * Generiert einen Hash Code aus dem zu upgedateten User
     *
     * @return der HashCode
     * @author Keno S.
     * @since Sprint 3
     */
    @Override
    public int hashCode() {
        return Objects.hash(toUpdate);
    }
}
