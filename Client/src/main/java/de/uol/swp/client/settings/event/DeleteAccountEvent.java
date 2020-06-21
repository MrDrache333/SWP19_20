package de.uol.swp.client.settings.event;

import de.uol.swp.common.user.User;

public class DeleteAccountEvent {

    private final User user;

    /**
     * Konstruktor des DeleteAccountEvent
     *
     * @param user Der zu löschende Nutzer
     * @author Anna
     * @since Sprint 4
     */
    public DeleteAccountEvent(User user) {
        this.user = user;
    }

    /**
     * Gibt den User zurück
     *
     * @return user der User
     * @author Anna
     * @since Sprint 4
     */
    public User getUser() {
        return user;
    }
}
