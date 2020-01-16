package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;
import java.util.UUID;

public class AbstractLobbyMessage extends AbstractServerMessage {


    private UserDTO user;
    private UUID lobbyID;
    private String lobbyName;

    public AbstractLobbyMessage() {
    }

    /**
     * Initialisiert eine neue AbstractLobbyMessage
     *
     * @param lobbyName Der Lobbyname
     * @param user Der User
     * @param lobbyID Die LobbyID
     * @author Marco, Anna, Keno S
     * @since Start
     *
     */
    public AbstractLobbyMessage(String lobbyName, UserDTO user, UUID lobbyID) {
        this.lobbyName = lobbyName;
        this.user = user;
        this.lobbyID = lobbyID;
    }
    /**
     * Gibt den Namen der Lobby zurück
     *
     * @return Name der Lobby
     * @author Marco, Darian
     * @since Start
     */
    public String getLobbyName() {
        return lobbyName;
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
     * @since Sprint2
     */
    public UUID getLobbyID() {
        return lobbyID;
    }
    /**
     * ???
     *
     * @return
     * @author Keno S
     * @since Sprint3
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyMessage that = (AbstractLobbyMessage) o;
        return Objects.equals(lobbyName, that.lobbyName) &&
                Objects.equals(user, that.user);
    }
    /**
     * Generiert HashCode aus Lobbynamen und User
     *
     * @return hash.Value
     * @author Keno S
     * @since Sprint4
     */
    @Override
    public int hashCode() {
        return Objects.hash(lobbyName, user);
    }
}
