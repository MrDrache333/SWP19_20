package de.uol.swp.client.settings.event;

import de.uol.swp.common.user.User;

public class DeleteAccountEvent {

    private User user;

    public DeleteAccountEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
