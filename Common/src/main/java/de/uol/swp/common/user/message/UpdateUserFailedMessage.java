package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

public class UpdateUserFailedMessage extends AbstractServerMessage {

    private User user;
    private String message;

    public UpdateUserFailedMessage(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() { return user; }

    public String getMessage() { return message; }

}
