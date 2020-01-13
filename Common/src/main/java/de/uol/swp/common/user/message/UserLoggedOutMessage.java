package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.Objects;

/**
 * Die Klasse der User logged out message.
 *
 * @author Marco Grawunder
 * @since Sprint 0
 */
public class UserLoggedOutMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -2071886836547126480L;
    private String username;

    /**
     * Konstruktor der User logged out message.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     */
    public UserLoggedOutMessage() {
    }

    /**
     * Die Klasse der User logged out messages.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @param username Der Username
     */
    public UserLoggedOutMessage(String username) {
        this.username = username;
    }

    /**
     * Gibt den Username zurück.
     *
     * @author Marco Grawunder
     * @since Sprint 0
     * @return username Der Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Vergleicht das aufrufende Objekt mit dem Übergebendem. Liefert bei
     * Gleichheit "true" zurück. Sofern das Objekt null ist oder nicht der Klasse entspricht,
     * wird "false" zurückgegeben. Andersweitig wird das Ergebnis eines weiteren Objektvergleiches der
     * Nutzernamen zurückgegeben.
     *
     * @author Keno S.
     * @since Sprint 4
     * @param o Ein Objekt
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoggedOutMessage that = (UserLoggedOutMessage) o;
        return Objects.equals(username, that.username);
    }

    /**
     * Generiert einen Hashwert des Usernames und gibt diesen zurück.
     *
     * @author Keno S.
     * @since Sprint 4
     * @return hash Ein Hashwert des Usernames
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
