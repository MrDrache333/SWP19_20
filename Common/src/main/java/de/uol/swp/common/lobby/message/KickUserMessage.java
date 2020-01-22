package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

public class KickUserMessage extends AbstractLobbyMessage {

    private UserDTO user;
    private LobbyDTO lobby;

    /**
     * Instanziiert eine KickUserMessage
     *
     * @param userToKick Der User, der aus der Lobby entfernt wurde
     * @param lobby      Die Lobby, aus der der User entfernt wurde
     * @author Darian, Julia
     * @since Sprint4
     */
    public KickUserMessage(UserDTO userToKick, LobbyDTO lobby) {
        this.lobby = lobby;
        this.user = userToKick;
    }

    /**
     * Gibt die Lobby zurück, aus der der User entfernt wurde
     *
     * @return Die Lobby
     * @author Julia
     * @since Sprint4
     */
    public LobbyDTO getLobby() {
        return lobby;
    }

    /**
     * Gibt den User zurück, der aus der Loby gekickt wurde
     *
     * @return Der User
     * @author Darian
     * @since Sprint4
     */
    public UserDTO getUser() {
        return user;
    }
}
