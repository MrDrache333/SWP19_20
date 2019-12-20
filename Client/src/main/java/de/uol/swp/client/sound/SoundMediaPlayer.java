package de.uol.swp.client.sound;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;

/**
 * The type Media player.
 */
public class SoundMediaPlayer implements MediaPlayer {

    private Logger LOG = LogManager.getLogger(getClass());

    private Sound sound;
    private boolean started;
    private Clip clip;
    private Type type;

    /**
     * Instantiates a new Media player.
     *
     * @param sound the sound
     * @param type  the type
     */
    public SoundMediaPlayer(Sound sound, Type type) {
        this.sound = sound;
        this.type = type;
        this.started = false;
    }

    /**
     * Plays the current Track.
     */
    @Override
    public void play() {
        Platform.runLater(() -> {
            try {

                //Sound laden
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(getClass().getResource(sound.getPath()).toExternalForm().replace("file:", "")));  //Sound als Stream oeffnen
                BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);    //Stream Buffer erstellen
                AudioFormat af = audioInputStream.getFormat();  //AudioFormat laden
                int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());   //Sounddatenlaenge bestimmen
                byte[] audio = new byte[size];  //Variable zum speichern der Sounddatei erstellen
                DataLine.Info info = new DataLine.Info(Clip.class, af, size);   //Soundinformationen zwischenspeichern
                bufferedInputStream.read(audio, 0, size);   //Sounddatei gebuffert einlesen
                clip = (Clip) AudioSystem.getLine(info);    //Eingelesene Sounddatei als "Clip" speichern
                clip.open(af, audio, 0, size);  //Clip oeffnen mit gegebenen Informationen
                setVolume(type.equals(Type.Music) ? 0.1 : 0.5);
                clip.addLineListener(event -> {
                    if (event.getType().equals(LineEvent.Type.STOP)) {
                        started = false;
                    } else if (event.getType().equals(LineEvent.Type.START)) {
                        started = true;
                    }
                });

                //Sounddatei abspielen
                if (type.equals(Type.Music)) clip.loop(-1);    //Wenn Hintergrundmusic -> Unendlich Loopen
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
                LOG.debug("Fehler beim abspielen von " + sound.getPath());
            }
        });
    }

    /**
     * Mute the current playing Track.
     */
    @Override
    public void mute() {
        setMuted(true);
    }

    /**
     * @param muted The State, wether the Track should be muted or not
     */
    private void setMuted(boolean muted) {
        BooleanControl muteControl = (BooleanControl) clip
                .getControl(BooleanControl.Type.MUTE);
        muteControl.setValue(muted);
    }

    /**
     * Un mute the current playing Track.
     */
    @Override
    public void unMute() {
        setMuted(true);
    }

    /**
     * Set the Volume of the current Track (Must be between 0.0 and 1.0).
     *
     * @param Volume the final volume
     */
    @Override
    public void setVolume(double Volume) {
        if (Volume < 0 || Volume > 1) return;
        double dB = (Math.log(Volume) / Math.log(10.0) * 20.0);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) dB);
    }

    /**
     * Gibt .
     *
     * @return Value of started.
     */
    @Override
    public boolean isStarted() {
        return started;
    }

    /**
     * Stops the current Track.
     */
    @Override
    public void stop() {
        clip.stop();
        started = false;
    }

    /**
     * The enum Type.
     */
    public enum Type {
        /**
         * Music type.
         */
        Music,
        /**
         * Sound type.
         */
        Sound
    }

    /**
     * The enum Sound.
     */
    public enum Sound {

        /**
         * Intro sound.
         */
        Intro("/music/intro.wav"),
        /**
         * Button hover sound.
         */
        Button_Hover("/sounds/button_mouseover.wav"),
        /**
         * Button pressed sound.
         */
        Button_Pressed("/sounds/button_pressed.wav"),
        /**
         * Window opened sound.
         */
        Window_Opened("/sounds/window_opened.wav"),
        /**
         * Message send sound.
         */
        Message_Send("/sounds/message_send.wav"),
        /**
         * Message receive sound.
         */
        Message_Receive("/sounds/message_receive.wav");

        private String path;


        Sound(String path) {
            this.path = path;
        }

        /**
         * Get path string.
         *
         * @return the Path
         */
        public String getPath() {
            return this.path;
        }
    }
}
