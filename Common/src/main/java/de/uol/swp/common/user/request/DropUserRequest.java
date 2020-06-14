package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

public class DropUserRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 5260937585947688764L;
    private final User user;

    /**
     * Instanziiert die Request
     *
     * @param user Der zu löschende User
     * @author Anna
     * @since Sprint 4
     */

    public DropUserRequest(User user) {
        this.user = user;
    }

    /**
     * Getter für User
     *
     * @return Der zu löschende User
     * @author Anna
     * @since Sprint 4
     */
    public User getUser() {
        return user;
    }
}
