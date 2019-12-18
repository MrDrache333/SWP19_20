package de.uol.swp.server.communication;

import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;

import java.util.Objects;
import java.util.UUID;

public class UUIDSession implements Session {

    private final String sessionId;
    private final User user;

    private UUIDSession(User user) {
        synchronized (UUIDSession.class) {
            this.sessionId = String.valueOf(UUID.randomUUID());
            this.user = user;
        }
    }

    public static Session create(User user) {
        return new UUIDSession(user);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUIDSession session = (UUIDSession) o;
        return Objects.equals(sessionId, session.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "SessionId: " + sessionId;
    }

}