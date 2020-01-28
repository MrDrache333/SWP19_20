package de.uol.swp.client;

import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Java Implementation
 *
 * @author Keno O.
 * @since Sprint3
 */
public class Notifyer {

    /**
     * Überprüft ob Benachrichtigungen unterstützt werden.
     *
     * @return ob die Notification unterstützt wird.
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
     * Schickt eine Benachrichtigung.
     *
     * @param messageType der MessageType
     * @param title       der Titel
     * @param message     die Nachricht
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
     * Der enum MessageType.
     */
    public enum MessageType {
        /**
         * Eine Fehler-Message
         */
        ERROR,
        /**
         * Eine Warn-Message
         */
        WARNING,
        /**
         * Eine Informations-Message
         */
        INFO,
        /**
         * eine einfache Message
         */
        NONE

    }
}

/**
 * Der Typ Unable to notify exception.
 */
class UnableToNotifyException extends RuntimeException {

    /**
     * Instanziiert eine neue Unable to notify exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public UnableToNotifyException(String message, Throwable cause) {
        super(message, cause);
    }

}
