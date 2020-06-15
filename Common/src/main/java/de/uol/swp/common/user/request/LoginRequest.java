package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;

import java.util.Objects;

/**
 * A request send from client to server, trying to log in with
 * username and password
 *
 * @author Marco Grawunder
 */
public class LoginRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 7793454958390539421L;
    private final String username;
    private final String password;

    /**
     * Eine Anfrage wird erstellt um den User einzuloggen.
     *
     * @param username Der eingegebene Username
     * @param password Das eingegebene Passwort
     * @author Marco
     * @since Start
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public boolean authorizationNeeded() {
        return false;
    }

    /**
     * Der eingebene Benutzername wird zurückgegeben
     *
     * @return Der eingebene Benutzername
     * @author Marco
     * @since Start
     */
    public String getUsername() {
        return username;
    }

    /**
     * Das eingegebene Passwort wird zurückgegeben
     *
     * @return Das eingegebene Passwort
     * @author Marco
     * @since Start
     */
    public String getPassword() {
        return password;
    }

    /**
     * Es wird überprüft ob die zwei Objekte gleich sind
     *
     * @param o Das Objekt mit der verglichen werden soll
     * @return True wenn die Objekte gleich sind
     * @author Marco
     * @since Start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    /**
     * Es wird das Passwort und der Benutzer gehasht
     *
     * @return Der hashCode aus Username und dem Passwort
     * @author Marco
     * @since Start
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
