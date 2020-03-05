package de.uol.swp.client.sound;

/**
 * Das Media Player Interface.
 *
 * @author Keno O.
 * @since Sprint 4
 */
public interface MediaPlayer {

    /**
     * Spielt das aktuelle Lied ab.
     *
     * @author Keno O.
     * @since Sprint 4
     */
    void play();

    /**
     * Schaltet das aktuelle Lied stumm.
     *
     * @author Keno O.
     * @since Sprint 4
     */
    void mute();

    /**
     * Hebt die Stummschaltung des Liedes auf.
     *
     * @author Keno O.
     * @since Sprint 4
     */
    void unMute();

    /**
     * Prüft ob das aktuelle Lied abgespielt wird.
     *
     * @return boolean true oder false
     * @author Keno O.
     * @since Sprint 4
     */
    boolean isStarted();

    /**
     * Setzt die Lautstärke des Liedes.
     *
     * @param Volume Die Lautstärke
     * @author Keno O.
     * @since Sprint 4
     */
    void setVolume(double Volume);

    /**
     * Stoppt das aktuelle Lied.
     *
     * @author Keno O.
     * @since Sprint 4
     */
    void stop();

}
