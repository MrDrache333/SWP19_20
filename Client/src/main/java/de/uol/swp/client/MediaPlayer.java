package de.uol.swp.client;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;

/**
 * The type Media player.
 */
public class MediaPlayer {

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
    public MediaPlayer(Sound sound, Type type) {
        this.sound = sound;
        this.type = type;
        this.started = false;
    }

    /**
     * Play.
     */
    public void play() {
        Platform.runLater(() -> {
            try {

                //Sound laden
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(sound.getPath()));  //Sound als Stream oeffnen
                BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);    //Stream Buffer erstellen
                AudioFormat af = audioInputStream.getFormat();  //AudioFormat laden
                int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());   //Sounddatenlaenge bestimmen
                byte[] audio = new byte[size];  //Variable zum speichern der Sounddatei erstellen
                DataLine.Info info = new DataLine.Info(Clip.class, af, size);   //Soundinformationen zwischenspeichern
                bufferedInputStream.read(audio, 0, size);   //Sounddatei gebuffert einlesen
                clip = (Clip) AudioSystem.getLine(info);    //Eingelesene Sounddatei als "Clip" speichern
                clip.open(af, audio, 0, size);  //Clip oeffnen mit gegebenen Informationen
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
     * Gibt .
     *
     * @return Value of started.
     */
    public boolean isStarted() {
        return started;
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
        Window_Opened("/sounds/window_opened.wav");

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
