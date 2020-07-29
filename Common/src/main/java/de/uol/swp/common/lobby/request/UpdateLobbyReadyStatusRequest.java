package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Anfrage, um den Bereitstatus eines Benutzers in einer Lobby zu ändern.
 *
 * @author KenoO, Marvin
 * @since Sprint 2
 */
public class UpdateLobbyReadyStatusRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = -2904997600691722894L;
    private final boolean Ready;

    /**
     * Erstellt einen neuen Update lobby ready status request.
     *
     * @param lobbyID Die Lobby-ID
     * @param user    Der zu updatende Benutzer
     * @param ready   Der neue Bereit-Status
     */
    public UpdateLobbyReadyStatusRequest(UUID lobbyID, UserDTO user, boolean ready) {
        super(lobbyID, user);
        this.Ready = ready;
    }

    /**
     * Gibt den Bereitstatus des Benutzers zurück
     *
     * @return Ist der Benutzer bereit
     */
    public boolean isReady() {
        return Ready;
    }
}
