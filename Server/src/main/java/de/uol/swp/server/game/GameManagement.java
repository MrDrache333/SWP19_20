package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.common.game.exception.GameManagementException;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.server.chat.Chat;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger LOG = LogManager.getLogger(GameManagement.class);
    private static Map<UUID, Game> games = new TreeMap<>();
    private final LobbyManagement lobbyManagement;
    private final ChatManagement chatManagement;

    /**
     * Erstellt ein neues gameManagement
     *
     * @param chatManagement  Das Chat Management
     * @param lobbyManagement Das Lobby Management
     */
    @Inject
    public GameManagement(ChatManagement chatManagement, LobbyManagement lobbyManagement) {
        this.lobbyManagement = lobbyManagement;
        this.chatManagement = chatManagement;

    }

    /**
     * Erstellt ein neues Spiel, übergibt die zugehörige Lobby und den Chat und fügt dies dann der Map hinzu
     *
     * @param lobbyID Die Lobby-ID
     * @author KenoO
     * @since Sprint 5
     */
    void createGame(UUID lobbyID) {
        if (games.containsKey(lobbyID))
            throw new GameManagementException("Game with ID " + lobbyID.toString() + " allready exists!");
        Optional<String> lobbyName = lobbyManagement.getName(lobbyID);
        if (lobbyName.isPresent()) {
            Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
            chatManagement.createChat(lobbyID.toString());
            Optional<Chat> chat = chatManagement.getChat(lobbyID.toString());


            if (lobby.isPresent()) {
                LOG.debug("Lobby is Present!");
                if (chat.isPresent()) {
                    LOG.debug("Chat is Present!");
                    Game game = new Game(lobby.get(), chat.get());
                    games.put(lobbyID, game);
                }
            } else
                throw new GameManagementException("Chat or Lobby not found!");

        } else
            throw new GameManagementException("Lobby-ID not found!");
    }

    /**
     * Löscht ein Spiel aus der Liste
     *
     * @param id Die ID
     * @author KenoO
     * @since Sprint 5
     */
    public void deleteGame(UUID id) {
        games.remove(id);
    }

    /**
     * Gibt ein Spiel anhand der Spiel-ID aus der Liste zurück
     *
     * @param id Die ID
     * @return Das Spiel
     * @author KenoO
     * @since Sprint 5
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
