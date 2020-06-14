package de.uol.swp.common.lobby;

import de.uol.swp.common.SerializationTestHelper;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Testklasse der LobbyMessage
 *
 * @author Keno S.
 * @since Sprint 4
 */
public class LobbyMessageSerializableTest {

    private static final UserDTO defaultUser = new UserDTO("marco", "marco", "marco@grawunder.de");
    private final UUID testUUID = UUID.randomUUID();
    private final LobbyDTO defaultLobby = new LobbyDTO("test", defaultUser, testUUID, "test");

    /**
     * Testet die Message
     *
     * @author Keno S.
     * @since Sprint 4
     */
    @Test
    void testLobbyMessagesSerializable() {
        SerializationTestHelper.checkSerializableAndDeserializable(new CreateLobbyRequest("test", defaultUser, ""),
                CreateLobbyRequest.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new LobbyJoinUserRequest(testUUID, defaultUser, false),
                LobbyJoinUserRequest.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new LobbyLeaveUserRequest(testUUID, defaultUser),
                LobbyLeaveUserRequest.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new UserJoinedLobbyMessage(testUUID, defaultUser, defaultUser, defaultLobby),
                UserJoinedLobbyMessage.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new UserLeftLobbyMessage(testUUID, defaultUser, defaultUser, defaultLobby),
                UserLeftLobbyMessage.class);
    }


}