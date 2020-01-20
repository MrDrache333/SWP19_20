package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.User;

import java.util.Objects;

public class UpdateUserRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = 1834977681414180873L;
    final private User toUpdate;
    private User oldUser;
    private String currentPassword;

    public UpdateUserRequest(User user, User oldUser, String currentPassword) {
        this.toUpdate = user;
        this.oldUser = oldUser;
        this.currentPassword = currentPassword;
    }

    public User getUser() {
        return toUpdate;
    }

    public User getOldUser() {
        return oldUser;
    }

    public String getCurrentPassword() {
        return currentPassword;
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
