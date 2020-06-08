package de.uol.swp.common.user;

import java.io.Serializable;

/**
 * Das Interface für einen User.
 */
public interface User extends Serializable, Comparable<User> {
    /**
     * Gibt den Benutzernamen zurück.
     *
     * @return Der Benutzername
     */
    String getUsername();

    /**
     * Gibt das Passwort des Benutzers zurück.
     *
     * @return Das Passwort
     */
    String getPassword();

    /**
     * Gibt die E-Mailadresse des Benutzers zurück.
     *
     * @return Die E-Mailadresse
     */
    String getEMail();

    /**
     * Gibt den Benutzer ohne sein Passwort zurück
     *
     * @return Der Benutzer ohne sein Passwort
     */
    User getWithoutPassword();

    boolean getIsBot();
}
