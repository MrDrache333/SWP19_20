package de.uol.swp.server.game;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.server.chat.Chat;

import java.util.UUID;

public class Game {

    private Lobby lobby;
    private Chat chat;
    private UUID gameID;

    public Game(UUID lobbyID, Lobby lobby, Chat chat) {
        this.chat = chat;
        this.lobby = lobby;
        this.gameID = lobbyID;
    }
}
