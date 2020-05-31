package de.uol.swp.common.lobby;

import de.uol.swp.common.user.User;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Das Interface Lobby.
 *
 * @author Marco Grawunder
 * @since Sprint 0
 */
public interface Lobby {

    /**
     * Gibt den Namen zurück.
     *
     * @return Den Namen
     */
    String getName();

    /**
     * Aktualisiert den Besitzer einer Lobby.
     *
     * @param user Der Nutzer
     */
    void updateOwner(User user);

    /**
     * Gibt den aktuellen Lobby Owner zurück.
     *
     * @return Der Lobby Owner
     */
    User getOwner();

    /**
     * Fügt einen Nutzer zu der Lobby hinzu.
     *
     * @param user Der Nutzer
     */
    void joinUser(User user);

    boolean onlyBotsLeft(UUID lobbyID);

    /**
     * Entfernt einen Nutzer aus der Lobby.
     *
     * @param user Der Nutzer
     */
    void leaveUser(User user);

    /**
     * Gibt alle Nutzer zurück.
     *
     * @return Die Nutzer
     */
    Set<User> getUsers();

    /**
     * Gibt die Lobby ID zurück.
     *
     * @return Die Lobby ID
     */
    UUID getLobbyID();

    /**
     * Setzt die Lobby ID für die Lobby.
     *
     * @param lobbyID Die Lobby ID
     */
    void setLobbyID(UUID lobbyID);

    /**
     * Gibt die Anzahl der Spieler zurück.
     *
     * @return Die Spieler
     */
    int getPlayers();

    /**
     * Setzt den Bereit Status eines Nutzers.
     *
     * @param user   Der Nutzer
     * @param status Der Status
     */
    void setReadyStatus(User user, boolean status);

    /**
     * Gibt den Bereit Status zurück.
     *
     * @param user Der Nutzer
     * @return Der Bereit Status
     */
    boolean getReadyStatus(User user);

    /**
     * Gibt zurück, wie groß die Lobby maximal sein kann.
     *
     * @author Timo, Rike
     * @since Sprint 3
     */
    Integer getMaxPlayer();

    /**
     * Setzt die maximale Lobbygröße.
     *
     * @param maxPlayer Die neue maximale Lobbygröße
     * @author Timo, Rike
     * @since Sprint 3
     */

    void setMaxPlayer(Integer maxPlayer);

    /**
     * Gibt jeden Bereit Status zurück.
     *
     * @return Jeden Bereit Status
     */
    TreeMap<String, Boolean> getEveryReadyStatus();


    /**
     * @return gibt das Passwort einer Lobby zurück
     * @author Rike Hochheiden
     */
    String getLobbyPassword();

    /**
     * Gibt an ob in der Lobby gerade ein Spiel läuft
     *
     * @return true wenn ein Spiel aktiv ist, sonst false
     */
    boolean getInGame();

    /**
     * Setzt ob in der Lobby gerade ein Spiel läuft
     *
     * @param inGame true wenn ein Spiel aktiv ist, sonst false
     */
    void setInGame(boolean inGame);

    ArrayList<Short> getChosenCards();

    /**
     * Setzt die ausgewählten Karten in der Lobby
     *
     * @param chosenCards
     */
    void setChosenCards(ArrayList<Short> chosenCards);
}
