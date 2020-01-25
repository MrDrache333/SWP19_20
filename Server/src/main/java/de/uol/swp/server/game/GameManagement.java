package de.uol.swp.server.game;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.server.chat.Chat;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Das Gamemanagement verwaltet die laufenden Spiele
 *
 * @author kenoO
 * @since Sprint 4
 */
public class GameManagement {

    private static Map<UUID, Game> games = new TreeMap<>();
    private static LobbyManagement lobbyManagement = new LobbyManagement();
    private static ChatManagement chatManagement = new ChatManagement();

    /**
     * Erstellt ein neues Spiel, übergibt die zugehörige Lobby und den Chat und fügt dies dann der Map hinzu
     *
     * @param lobbyID Die Lobby-ID
     */
    public void createGame(UUID lobbyID) {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID.toString());
        Optional<Chat> chat = chatManagement.getChat(lobbyID.toString());
        if (lobby.isPresent() && chat.isPresent()) {
            Game game = new Game(lobbyID, lobby.get(), chat.get());
            games.put(lobbyID, game);
        }
    }

    /**
     * Löscht ein Spiel aus der Liste
     *
     * @param id Die ID
     */
    public void deleteGame(UUID id) {
        games.remove(id);
    }

    /**
     * Gibt ein Spiel anhand der Spiel-ID aus der Liste zurück
     *
     * @param id Die ID
     * @return Das Spiel
     */
    public Optional<Game> getGame(UUID id) {
        try {
            Game game = games.get(id);
            return Optional.of(game);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
