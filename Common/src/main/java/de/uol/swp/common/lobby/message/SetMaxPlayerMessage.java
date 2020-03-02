package de.uol.swp.common.lobby.message;

import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.user.User;

import java.util.UUID;

/**
 * SetMaxPlayer Message als Rückgabemessage inkl. Getter & Setter
 *
 * @author Timo, Rike
 * @since Sprint 3
 */
public class SetMaxPlayerMessage extends AbstractLobbyMessage {

    private static final long serialVersionUID = 8520048304588405366L;
    private Integer maxPlayer;
    private UUID lobbyID;
    private boolean setMaxPlayerSet;
    private User owner;
    private LobbyDTO lobby;

    /**
     * Erstellt eine neue Set max player message.
     */
    public SetMaxPlayerMessage() {
        //Konstruktor für Serialisierung
    }

    /**
     * Erstellt eine neue Set max player message.
     *
     * @param maxPlayer       Anzahl der maximalen Spieler in dieser Lobby
     * @param lobbyID         Die Lobby-ID
     * @param setMaxPlayerSet Ob die maximalen Spieler erfolgreich gesetzt wurden
     * @param owner           Der Besitzer der Lobby
     * @param lobby           Die Lobby, deren max. Spielerzahl gesetzt wurde
     */
    public SetMaxPlayerMessage(Integer maxPlayer, UUID lobbyID, boolean setMaxPlayerSet, User owner, LobbyDTO lobby) {
        this.maxPlayer = maxPlayer;
        this.lobbyID = lobbyID;
        this.setMaxPlayerSet = setMaxPlayerSet;
        this.owner = owner;
        this.lobby = lobby;
    }

    /**
     * Gibt die Anzahl der maximal in der Lobby erlaubten User zurück
     *
     * @return Anzahl der maximal erlaubten Spieler der Lobby
     */
    public Integer getMaxPlayer() {
        return maxPlayer;
    }

    /**
     * Gibt zurück, ob die maximale Anzahl an Benutzern inerhalb der Lobby übernommen wurde
     *
     * @return ob die maximale Anzahl an Benutzern inerhalb der Lobby übernommen wurde
     */
    public boolean isSetMaxPlayerSet() {
        return setMaxPlayerSet;
    }

    /**
     * Gibt den aktuellen Besitzer der Lobby zurück
     *
     * @return Der aktuelle Besitzer der Lobby
     */
    public User getOwner() {
        return owner;
    }

    @Override
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Legt die Lobby-ID fest
     *
     * @param lobbyID Die Lobby-ID
     */
    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    /**
     * Gibt die Lobby zurück
     *
     * @return Die Lobby
     */
    public LobbyDTO getLobby() {
        return lobby;
    }


}

