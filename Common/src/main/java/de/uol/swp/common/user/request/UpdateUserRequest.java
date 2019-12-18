package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;
import java.util.Objects;

public class UpdateUserRequest extends AbstractRequestMessage {

    final private User toUpdate;

    public UpdateUserRequest(User user) {
        this.toUpdate = user;
    }

    public User getUser() {
        return toUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(toUpdate, that.toUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toUpdate);
    }
}
