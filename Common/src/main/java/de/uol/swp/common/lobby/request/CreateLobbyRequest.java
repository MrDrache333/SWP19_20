package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

/**
 * Request um eine neue Lobby zu erstellen. Wird vom Client zum Server geschickt.
 *
 * @author Paula, Haschem, Ferit, Rike
 * @version 0.1
 * @since Sprint2
 */
public class CreateLobbyRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = -1053476876954594887L;
    String lobbyPassword;

    /**
     * Erstellt einen Request.
     */
    public CreateLobbyRequest() {
    }

    /**
     * Erstellt einen Request.
     *
     * @param lobbyName Der gewünschte Lobbyname
     * @param owner     Der User, welcher die Lobby erstellen will.
     */
    public CreateLobbyRequest(String lobbyName, UserDTO owner) {
        super(lobbyName, owner);
        this.lobbyPassword = "";
    }

    /**
     * Erstellt einen Request mit Passwort.
     *
     * @param lobbyName     Der gewünschte Lobbyname
     * @param lobbyPassword Das Passwort.
     * @param owner         Der User, welcher die Lobby erstellen will.
     */
    public CreateLobbyRequest(String lobbyName, String lobbyPassword, UserDTO owner) {
        super(lobbyName, owner);
        this.lobbyPassword = lobbyPassword;
    }

    /**
     * Getter für den User, welcher die Lobby erstellt.
     *
     * @return den Besitzer der Lobby.
     */
    public UserDTO getOwner() {
        return getUser();
    }

    /**
     * Setzt den Besitzer der Lobby.
     *
     * @param owner der Ersteller der Lobby.
     */
    public void setOwner(UserDTO owner) {
        setUser(owner);
    }

    /**
     * Getter für das Lobbypasswort.
     *
     * @return das Lobby-Passwort.
     */
    public String getLobbyPassword() {
        return lobbyPassword;
    }
}
