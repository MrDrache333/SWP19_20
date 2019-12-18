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
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean authorizationNeeded() {
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
