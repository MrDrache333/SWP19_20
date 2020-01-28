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
     * @author Keno O.
     * @since Sprint 4
     * @return boolean true oder false
     *
     */
    boolean isStarted();

    /**
     * Setzt die Lautstärke des Liedes.
     *
     * @author Keno O.
     * @since Sprint 4
     * @param Volume Die Lautstärke
     *
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
