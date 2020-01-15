package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.Objects;

/**
 * Eine Nachricht, die einen neuen eingelogten Nutzer angibt.
 *
 * @author Marco Grawunder
 */
public class UserLoggedInMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -2071886836547126480L;
    private String username;

    public UserLoggedInMessage() {
    }

    /**
     * Der Konstruktor bekommt einen Usernamen, den er als Attribut speichert.
     *
     * @param username Der aktuelle Username
     * @author Marco Grawunder
     * @since Sprint2
     */

    public UserLoggedInMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Vergleicht diese Klasse mit einem anderen Objekt
     *
     * @param o Das zu vergleichende Objekt.
     * @author Marco Grawunder
     * @since Sprint3
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoggedInMessage that = (UserLoggedInMessage) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
