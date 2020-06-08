package de.uol.swp.common.user;

/**
 * Interface der Session. Wird vom Server in UUIDSession implementiert.
 */
public interface Session {

    String getSessionId();

    User getUser();

    void updateUser(User user);
}
