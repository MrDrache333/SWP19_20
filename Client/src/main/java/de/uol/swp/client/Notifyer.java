package de.uol.swp.client;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Java Implementation
 *
 * @author Keno Oelrichs Garcia
 */
public class Notifyer {

    /**
     * Is supported boolean.
     *
     * @return if the Nofification is supported
     */
    public boolean isSupported() {
        try {
            Class.forName("java.awt.SystemTray");
            return SystemTray.isSupported();
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            return false;
        }
    }

    /**
     * Notify.
     *
     * @param messageType the message type
     * @param title       the title
     * @param message     the message
     */
    public void notify(MessageType messageType, String title, String message) {
        if (isSupported())
            try {
                final SystemTray systemTray = SystemTray.getSystemTray();
                String imageName = "/images/info.png";
                Image image = ImageIO.read(getClass().getResource(imageName));
                final TrayIcon trayIcon = new TrayIcon(image, title);
                systemTray.add(trayIcon);
                trayIcon.displayMessage(title, message, TrayIcon.MessageType.valueOf(messageType.name()));
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    systemTray.remove(trayIcon);
                }).start();
            } catch (Exception e) {
                throw new UnableToNotifyException("Unable to notify with java", e);
            }
    }

    /**
     * The enum Message type.
     */
    public enum MessageType {
        /**
         * An error message
         */
        ERROR,
        /**
         * A warning message
         */
        WARNING,
        /**
         * An information message
         */
        INFO,
        /**
         * Simple message
         */
        NONE

    }
}

/**
 * The type Unable to notify exception.
 */
class UnableToNotifyException extends RuntimeException {

    /**
     * Instantiates a new Unable to notify exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public UnableToNotifyException(String message, Throwable cause) {
        super(message, cause);
    }

}
