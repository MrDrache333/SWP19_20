package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;
import de.uol.swp.common.user.UserDTO;

import java.util.Objects;

public class AbstractLobbyRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -7532249321141216659L;
    private String lobbyName;
    private UserDTO user;

    public AbstractLobbyRequest() {
    }

    /**
     * Eine Anfrage wird erstellt die allgemein mit der Lobby zu tun hat.
     *
     * @param lobbyName Lobbyname um die sich die Anfrage handelt
     * @param user      User der die Anfrage verschickt
     * @author Marco
     * @since Start
     */
    public AbstractLobbyRequest(String lobbyName, UserDTO user) {
        this.lobbyName = lobbyName;
        this.user = user;
    }

    /**
     * Lobbyname wird zurückgegeben um die sich Anfrage handelt.
     *
     * @return Der Lobbyname
     * @author Marco
     * @since Start
     */
    public String getLobbyName() {
        return lobbyName;
    }

    public void setUser(UserDTO user) {
        this.user = user;
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

    /**
     * Es wird verglichen ob zwei Objekte gleich sind.
     *
     * @param o Objekt mit dem verglichen werden soll
     * @return True wenn die Benutzer gleich sind
     * @author Marco
     * @since Start
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractLobbyRequest that = (AbstractLobbyRequest) o;
        return Objects.equals(lobbyName, that.lobbyName) &&
                Objects.equals(user, that.user);
    }

    /**
     * Es wird der Lobbyname und der Benutzer gehasht
     *
     * @return hashCode aus Lobbyname und User
     * @author Marco
     * @since Start
     */
    @Override
    public int hashCode() {
        return Objects.hash(lobbyName, user);
    }
}
