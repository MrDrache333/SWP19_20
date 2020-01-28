package de.uol.swp.common.lobby.message;

import de.uol.swp.common.message.AbstractServerMessage;

import java.util.UUID;

public class StartGameMessage extends AbstractServerMessage {

    private static final long serialVersionUID = 8407406031710024918L;
    private String lobbyName;
    private UUID lobbyID;

    /**
     * Die Spielstartnachricht bekommt den Lobbynamen und die LobbyID übergeben
     *
     * @param lobbyName, lobbyID
     * @author Darian
     * @since Sprint3
     */
    public StartGameMessage(String lobbyName, UUID lobbyID) {
        this.lobbyName = lobbyName;
        this.lobbyID = lobbyID;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }
}
