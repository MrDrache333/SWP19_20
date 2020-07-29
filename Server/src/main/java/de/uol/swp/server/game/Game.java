package de.uol.swp.server.game;

import com.google.inject.Inject;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.server.chat.Chat;

import java.util.UUID;

/**
 * Objekt zum Verwalten eines Spiels
 */
public class Game {

    /**
     * Der Chat.
     */
    private final Chat chat;
    /**
     * Der Playground.
     */
    private final Playground playground;
    /**
     * Die GameID.
     */
    private final UUID gameID;

    /**
     * Erstellt ein neues Spiel.
     *
     * @param lobby Die Lobby, die das Spiel gestartet hat
     * @param chat  Der Chat
     * @author Keno O.
     * @since Sprint 5
     */
    @Inject
    Game(Lobby lobby, Chat chat, GameService gameService) {
        this.chat = chat;
        this.gameID = lobby.getLobbyID();
        playground = new Playground(lobby, gameService);
    }

    /**
     * Gibt den Playground zurück.
     *
     * @return playground der Playground
     * @author Ferit
     * @since Sprint 5
     */
    public Playground getPlayground() {
        return playground;
    }

    /**
     * Gibt die GameID zurück.
     *
     * @return Die GameID
     * @author Keno O.
     * @since Sprint 5
     */
    public UUID getGameID() {
        return gameID;
    }
}
