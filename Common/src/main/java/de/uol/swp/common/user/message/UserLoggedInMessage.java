package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.Objects;

/**
 * A message to indicate a new logged in user
 *
 * @author Marco Grawunder
 */
public class UserLoggedInMessage extends AbstractServerMessage {

    private static final long serialVersionUID = -2071886836547126480L;
    private String username;

    public UserLoggedInMessage() {
    }

    public UserLoggedInMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

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
