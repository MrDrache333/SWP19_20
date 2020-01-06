package de.uol.swp.common.user;

public interface Session {
    String getSessionId();

    User getUser();

    void updateUser(User user);
}
