package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

public class UpdatedUserMessage extends AbstractServerMessage {

    private User user;
    private User oldUser;

    public UpdatedUserMessage(User user, User oldUser) {
        this.user = user;
        this.oldUser = oldUser;
    }

    public User getUser() { return user; }

    public User getOldUser() { return oldUser; }
}
