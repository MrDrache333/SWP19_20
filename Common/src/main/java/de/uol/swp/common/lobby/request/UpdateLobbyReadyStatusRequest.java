package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * Anfrage, um den Bereitstatus eines Benutzers in einer Lobby zu ändern.
 * @author KenoO
 * @since Sprint2
 */
public class UpdateLobbyReadyStatusRequest extends AbstractLobbyRequest {

    private boolean Ready;

    /**
     * Erstellt einen neuen Update lobby ready status request.
     *
     * @param lobby Die Lobby-ID
     * @param user  Der zu updatende Benutzer
     * @param ready Der neue Bereit-Status
     */
    public UpdateLobbyReadyStatusRequest(String lobby, UserDTO user, boolean ready) {
        super(lobby, user);
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
