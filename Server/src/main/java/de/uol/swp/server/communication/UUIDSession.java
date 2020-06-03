package de.uol.swp.server.communication;

import de.uol.swp.common.user.Session;
import de.uol.swp.common.user.User;
import de.uol.swp.server.game.player.bot.BotPlayer;

import java.util.Objects;
import java.util.UUID;

public class UUIDSession implements Session {

    private final String sessionId;
    private User user;
    private BotPlayer bot;

    /**
     * Instanziiert neue Session mit zufälliger UUID
     *
     * @param user Benutzer der Session
     * @author Keno S., Ferit
     * @since Sprint 3
     */
    private UUIDSession(User user) {
        synchronized (UUIDSession.class) {
            this.sessionId = String.valueOf(UUID.randomUUID());
            this.user = user;
        }
    }

    private UUIDSession(BotPlayer botPlayer) {
        synchronized (UUIDSession.class) {
            this.sessionId = String.valueOf(UUID.randomUUID());
            this.bot = botPlayer;
        }
    }

    /**
     * Erstellt neue Session mit zufälliger UUID
     *
     * @param user Benutzer der Session
     * @return Neue UUIDSession
     * @author Ferit
     * @since Sprint 3
     */
    public static Session create(User user) {
        return new UUIDSession(user);
    }

    public static Session create(BotPlayer bot) {
        return new UUIDSession(bot);
    }

    /**
     * Getter für SessionID
     *
     * @return Die SessionID
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Getter für Benutzer
     *
     * @return Der Benutzer
     * @author Ferit
     * @since Sprint 3
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * Setter für Benutzer
     *
     * @param user Der neue Benutzer
     * @author Ferit
     * @since Sprint 3
     */
    @Override
    public void updateUser(User user) {
        this.user = user;
    }

    /**
     * Vergleicht Session mit einer anderen Session
     *
     * @param o Die andere Session
     * @return true wenn gleich, false falls nicht
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUIDSession session = (UUIDSession) o;
        return Objects.equals(sessionId, session.sessionId);
    }

    /**
     * Hasht die SessionID
     *
     * @return Hash als Integer
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    /**
     * Gibt SessionID als String wieder
     *
     * @return "SessiondId: [sessionID]"
     * @author Marco Grawunder
     * @since Start
     */
    @Override
    public String toString() {
        return "SessionId: " + sessionId;
    }

}