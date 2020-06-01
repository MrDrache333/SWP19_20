package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;
import java.util.UUID;

public class AbstractLobbyMessage extends AbstractServerMessage {


    private static final long serialVersionUID = 5623324582947276712L;
    private UserDTO user;
    private UUID lobbyID;

    public AbstractLobbyMessage() {
    }

    /**
     * Initialisiert eine neue AbstractLobbyMessage
     *
     * @param lobbyID Die LobbyID
     * @param user    Der User
     * @author Marco, Anna, Keno S, Marvin
     * @since Start
     */
    public AbstractLobbyMessage(UUID lobbyID, UserDTO user) {
        this.lobbyID = lobbyID;
        this.user = user;
    }

    /**
     * Gibt den UserDTO der Lobby zurück
     *
     * @return
     * @author Marco
     * @since Start
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Gibt die ID der Lobby zurück
     *
     * @return ID der Lobby
     * @author Anna
     * @since Sprint 2
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Vergleicht zwei AbstractLobbyMessages
     *
     * @return boolean ob die beiden Messages gleich sind
     * @author Keno S, Marvin
     * @since Sprint 3
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyMessage that = (AbstractLobbyMessage) o;
        return Objects.equals(lobbyID, that.lobbyID) &&
                Objects.equals(user, that.user);
    }

    /**
     * Generiert HashCode aus LobbyID und User
     *
     * @return hash.Value
     * @author Keno S, Marvin
     * @since Sprint 4
     */
    @Override
    public int hashCode() {
        return Objects.hash(lobbyID, user);
    }
}
