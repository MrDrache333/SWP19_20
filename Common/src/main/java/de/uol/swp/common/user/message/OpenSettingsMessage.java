package de.uol.swp.common.user.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.User;

public class OpenSettingsMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 2091724274376103037L;
    private User user;
    private boolean inLobby;

    public OpenSettingsMessage(User user, boolean inLobby) {
        this.user = user;
        this.inLobby = inLobby;
    }

    public User getUser() {
        return user;
    }

    public boolean isInLobby() {
        return inLobby;
    }
}