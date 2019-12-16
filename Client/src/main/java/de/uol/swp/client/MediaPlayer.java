package de.uol.swp.client;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;

public class MediaPlayer {

    private Logger LOG = LogManager.getLogger(getClass());

    private File media;
    private boolean started;
    private Clip clip;
    private Type type;

    public MediaPlayer(File media, Type type) {
        this.media = media;
        this.type = type;
        this.started = false;
    }

    public void play() {
        Platform.runLater(() -> {
            try {

                //Sound laden
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(media.getPath()));  //Sound als Stream oeffnen
                BufferedInputStream bufferedInputStream = new BufferedInputStream(audioInputStream);    //Stream Buffer erstellen
                AudioFormat af = audioInputStream.getFormat();  //AudioFormat laden
                int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());   //Sounddatenlaenge bestimmen
                byte[] audio = new byte[size];  //Variable zum speichern der Sounddatei erstellen
                DataLine.Info info = new DataLine.Info(Clip.class, af, size);   //Soundinformationen zwischenspeichern
                bufferedInputStream.read(audio, 0, size);   //Sounddatei gebuffert einlesen
                clip = (Clip) AudioSystem.getLine(info);    //Eingelesene Sounddatei als "Clip" speichern
                clip.open(af, audio, 0, size);  //Clip oeffnen mit gegebenen Informationen

                //Sounddatei abspielen
                started = true;
                if (type.equals(Type.Music)) clip.loop(-1);    //Wenn Hintergrundmusic -> Unendlich Loopen
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
                LOG.debug("Fehler beim abspielen von " + media.getPath());
            }
        });
    }

    public enum Type {
        Music,
        Sound
    }
}
