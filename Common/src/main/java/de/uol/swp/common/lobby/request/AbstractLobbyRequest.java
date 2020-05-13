package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;
import java.util.UUID;

public class AbstractLobbyRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -7532249321141216659L;
    private UUID lobbyID;
    private UserDTO user;

    public AbstractLobbyRequest() {
    }

    /**
     * Eine Anfrage wird erstellt die allgemein mit der Lobby zu tun hat.
     *
     * @param lobbyID LobbyID der Lobby auf die sich die Anfrage bezieht
     * @author Marco, Marvin
     * @since Start
     */
    public AbstractLobbyRequest(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    /**
     * Eine Anfrage wird erstellt die allgemein mit der Lobby zu tun hat.
     *
     * @param lobbyID LobbyID der Lobby auf die sich die Anfrage bezieht
     * @param user    User der die Anfrage verschickt
     * @author Marco, Marvin
     * @since Start
     */
    public AbstractLobbyRequest(UUID lobbyID, UserDTO user) {
        this.lobbyID = lobbyID;
        this.user = user;
    }

    /**
     * LobbyID wird zurückgegeben um die sich Anfrage handelt.
     *
     * @return Die LobbyID
     * @author Marco, Marvin
     * @since Start
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Benutzer wird zurückgegben der die Anfrage verschickt hat.
     *
     * @return User der die Anfrage verschickt hat
     * @author Marco
     * @since Start
     */
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Es wird verglichen ob zwei Objekte gleich sind.
     *
     * @param o Objekt mit dem verglichen werden soll
     * @return True wenn die IDs und Benutzer gleich sind
     * @author Marco, Marvin
     * @since Start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyRequest that = (AbstractLobbyRequest) o;
        return Objects.equals(lobbyID, that.lobbyID) &&
                Objects.equals(user, that.user);
    }

    /**
     * Es wird die LobbyID und der Benutzer gehasht
     *
     * @return hashCode aus Lobbyname und User
     * @author Marco, Marvin
     * @since Start
     */
    @Override
    public int hashCode() {
        return Objects.hash(lobbyID, user);
    }
}
