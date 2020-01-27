package de.uol.swp.server.game;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.server.chat.Chat;

import java.util.UUID;

/**
 * Objekt zum Verwalten eines Spiels
 */
public class Game {

    /**
     * The Chat.
     */
    private Chat chat;
    /**
     * The Playground.
     */
    private Playground playground;
    /**
     * The Game id.
     */
    private UUID gameID;

    /**
     * Erstellt ein neues Spiel
     *
     * @param lobby Die Lobby, die das Spiel gestartet hat
     * @param chat  the chat
     * @author KenoO
     * @since Sprint 5
     */
    Game(Lobby lobby, Chat chat) {
        this.chat = chat;
        this.gameID = lobby.getLobbyID();
        playground = new Playground(lobby);
    }

    /**
     * Gets game id.
     *
     * @return the game id
     * @author KenoO
     * @since Sprint 5
     */
    public UUID getGameID() {
        return gameID;
    }
}
