package de.uol.swp.common.game.request;

import de.uol.swp.common.lobby.request.AbstractLobbyRequest;
import de.uol.swp.common.user.UserDTO;

import java.util.UUID;

/**
 * Erstellt einen GameGiveUpRequest, welche eine Anfrage vom Client an den Server schickt, dass ein bestimmter Spieler in dem spezifischen Game aufgeben möchte.
 *
 * @author Haschem, Ferit
 * @since Sprint 6
 */
public class GameGiveUpRequest extends AbstractLobbyRequest {

    private static final long serialVersionUID = 7986167787009372236L;
    private final UserDTO givingUpUser;
    private final UUID LobbyID;
    private final Boolean givingUp;

    /**
     * Der Konstruktor des GameGiveUpRequest
     *
     * @param givingUpUser       der User, welcher aufgibt
     * @param theSpecificLobbyID die Lobby-ID
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public GameGiveUpRequest(UserDTO givingUpUser, UUID theSpecificLobbyID) {
        this.givingUpUser = givingUpUser;
        this.LobbyID = theSpecificLobbyID;
        this.givingUp = true;
    }

    /**
     * Gigbt den User zurück, welcher aufgeben möchte
     *
     * @return givingUpUSer der User, welcher aufgeben möchte
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public UserDTO getGivingUpUser() {
        return givingUpUser;
    }

    /**
     * Gibt die Lobby-ID zurück
     *
     * @return theSpecificLobbyID die Lobby-ID
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public UUID getLobbyID() {
        return LobbyID;
    }

    /**
     * Gibt zurück, ob der User aufgeben möchte
     *
     * @return wantsToGiveUP ob der User aufgeben möchte (Boolean)
     * @author Haschem, Ferit
     * @since Sprint 6
     */
    public Boolean getGivingUp() {
        return givingUp;
    }
}
