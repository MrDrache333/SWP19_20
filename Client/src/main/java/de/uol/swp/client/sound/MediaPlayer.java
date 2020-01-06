package de.uol.swp.client.sound;

/**
 * The interface Player.
 */
public interface MediaPlayer {

    /**
     * Play the current Track.
     */
    void play();

    /**
     * Mute the current Track.
     */
    void mute();

    /**
     * Un mute the current Track.
     */
    void unMute();

    /**
     * Checks whether the current Track is playing.
     *
     * @return the boolean
     */
    boolean isStarted();

    /**
     * Sets the volume of the current Track.
     *
     * @param Volume the volume
     */
    void setVolume(double Volume);

    /**
     * Stops the current Track.
     */
    void stop();

}
