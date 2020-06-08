package de.uol.swp.client.sound;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;

/**
 * Die Klasse SoundMediaPlayer.
 *
 * @author Keno Oelrichs Garcia
 * @since Sprint 3
 */
public class SoundMediaPlayer implements MediaPlayer {

    private static boolean soundEnabled = true;
    private static final ArrayList<SoundMediaPlayer> playingSounds = new ArrayList<>();
    private final Logger LOG = LogManager.getLogger(getClass());
    private final Sound sound;
    private boolean started;
    private Clip clip;
    private final Type type;

    /**
     * Initialisiert einen neuen SoundMediaPlayer.
     *
     * @param sound Die Sound Art, die festgelegt werden soll.
     * @param type  Der Typ (entweder Sound oder Music).
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    public SoundMediaPlayer(Sound sound, Type type) {
        this.sound = sound;
        this.type = type;
        this.started = false;
        playingSounds.add(this);
    }

    /**
     * Is sound enabled boolean.
     *
     * @return the boolean
     */
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    /**
     * Stoppt alle Sounds, oder startet die Musik
     *
     * @param play Ob gestoppt oder gespielrt werden soll.
     */
    public static void setSound(boolean play) {
        soundEnabled = play;
        if (!play) {
            playingSounds.forEach(SoundMediaPlayer::stop);
            ArrayList<SoundMediaPlayer> temp = new ArrayList<>(playingSounds);
            temp.stream().filter(e -> e.type.equals(Type.Sound)).forEach(playingSounds::remove);
        } else
            playingSounds.stream().filter(e -> e.type.equals(Type.Music)).forEach(SoundMediaPlayer::play);
    }

    /**
     * Spielt den aktuellen Track ab.
     *
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Override
    public void play() {
        if (soundEnabled)
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
                            if (!type.equals(Type.Music)) playingSounds.remove(this);
                        } else if (event.getType().equals(LineEvent.Type.START)) {
                            started = true;
                        }
                    });
                    // Sounddatei abspielen
                    if (type.equals(Type.Music)) clip.loop(-1);    //Wenn Hintergrundmusic -> Unendlich Loopen
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.debug("Fehler beim Abspielen von " + sound.getPath());
                }
            });
    }

    /**
     * Ruft eine Funktion auf, um den Ton für den Player auszuschalten.
     *
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Override
    public void mute() {
        setMuted(true);
    }

    /**
     * Stellt den Ton für den Nutzer an oder aus.
     *
     * @param muted Der Status, ob der Track stumm sein soll oder nicht.
     */
    private void setMuted(boolean muted) {
        BooleanControl muteControl = (BooleanControl) clip
                .getControl(BooleanControl.Type.MUTE);
        muteControl.setValue(muted);
    }

    /**
     * Stellt den Ton für den Nutzer ein.
     *
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Override
    public void unMute() {
        setMuted(false);
    }

    /**
     * Setzt die Lautstärke des Aktuellen Players (Wert muss zwischen 0.0 und 1.0 liegen).
     *
     * @param Volume Die finale Lautstärke.
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Override
    public void setVolume(double Volume) {
        if (Volume < 0 || Volume > 1) return;
        double dB = (Math.log(Volume) / Math.log(10.0) * 20.0);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue((float) dB);
    }

    /**
     * Gibt den Wert zurück, ob der Player läuft oder nicht.
     *
     * @return Den Wert von started.
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Override
    public boolean isStarted() {
        return started;
    }

    /**
     * Stoppt den aktuellen Player
     *
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
     */
    @Override
    public void stop() {
        if (clip != null) clip.stop();
        started = false;
    }

    /**
     * Die Enumeration Type.
     *
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
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
     * Die Enumeration Sound.
     *
     * @author Keno Oelrichs Garcia
     * @since Sprint 3
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

        private final String path;

        /**
         * Setzt den Pfad-String.
         *
         * @param path Der Pfad
         * @author Keno Oelrichs Garcia
         * @since Sprint 3
         */
        Sound(String path) {
            this.path = path;
        }

        /**
         * Gibt den Pfad-String zurück.
         *
         * @return the Path
         * @author Keno Oelrichs Garcia
         * @since Sprint 3
         */
        public String getPath() {
            return this.path;
        }

    }
}
