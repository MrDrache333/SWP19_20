package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

public class UserDroppedMessage extends AbstractServerMessage {

    private User user;

    public UserDroppedMessage(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
