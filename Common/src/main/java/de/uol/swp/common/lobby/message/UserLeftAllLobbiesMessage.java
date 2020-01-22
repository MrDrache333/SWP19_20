package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.List;

public class UserLeftAllLobbiesMessage extends AbstractLobbyMessage {

    private UserDTO user;
    private List<LobbyDTO> lobbies;

    /**
     * Instanziiert eine UserLeftAllLobbiesMessage
     *
     * @param user    Der User, der alle Lobbies verlassen hat
     * @param lobbies Liste aller Lobbies
     * @author Julia
     * @since Sprint4
     */
    public UserLeftAllLobbiesMessage(UserDTO user, List<LobbyDTO> lobbies) {
        this.user = user;
        this.lobbies = lobbies;
    }

    /**
     * Gibt den User zurück
     *
     * @return Der User
     * @author Julia
     * @since Sprint4
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Gibt die Liste aller Lobbies zurück
     *
     * @return Liste aller Lobbies
     * @author Julia
     * @since Sprint4
     */
    public List<LobbyDTO> getLobbies() {
        return lobbies;
    }
}
