package de.uol.swp.common.message;

import de.uol.swp.common.user.Session;

import java.io.Serializable;
import java.util.Optional;

/**
 * Basisschnittstelle aller Nachrichten
 *
 * @author Marco Grawunder
 * @since Start
 */

public interface Message extends Serializable {

    /**
     * Ruft den aktuellen Nachrichtenkontext ab
     *
     * @return
     * @author Marco
     * @since Start
     */
    Optional<MessageContext> getMessageContext();

    /**
     * Ermöglicht das Festlegen eines MessageContext, z.b. für Netzwerkzwecke
     *
     * @param messageContext der Massage Kontext
     * @author Marco
     * @since Start
     */
    void setMessageContext(MessageContext messageContext);

    /**
     * Aktuelle Sitzung abrufen
     *
     * @return
     * @author Marco
     * @since Start
     */
    Optional<Session> getSession();

    /**
     * Legt die aktuelle Sitzung fest
     *
     * @param session die Sitzung
     * @author Marco
     * @since Start
     */
    void setSession(Session session);

    /**
     * Ermöglicht das Erstellen einer neuen Nachricht basierend auf der angegebenen Nachricht (Kopie).
     *
     * @param otherMessage andere Nachricht
     * @author Marco
     * @since Start
     */
    void initWithMessage(Message otherMessage);
}
