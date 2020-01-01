package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class OpenSettingsRequest extends AbstractRequestMessage {

    private User user;

    public OpenSettingsRequest(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}
