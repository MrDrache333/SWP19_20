package de.uol.swp.common.game;

import de.uol.swp.common.lobby.Lobby;

import java.util.UUID;

/**
 * The interface Game management.
 */
public interface GameManagement {

    /**
     * Erstellt ein neues Spiel mit einer übergebenen Lobby.
     *
     * @param lobby Die zu nutzende Lobby
     */
    void createGame(Lobby lobby);

    /**
     * Löscht ein vorhandenes Spiel
     *
     * @param id Die Lobby- bzw. Spiel-ID
     */
    void deleteGame(UUID id);

    /**
     * Gibt ein SPiel anhand seinder ID zurück
     *
     * @param id Die Spiel-ID
     * @return Das Spiel
     */
    Game getGame(UUID id);

}
