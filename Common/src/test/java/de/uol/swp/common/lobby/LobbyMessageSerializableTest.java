package de.uol.swp.common.lobby;

import de.uol.swp.common.SerializationTestHelper;
import de.uol.swp.common.lobby.message.UserJoinedLobbyMessage;
import de.uol.swp.common.lobby.message.UserLeftLobbyMessage;
import de.uol.swp.common.lobby.request.CreateLobbyRequest;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class LobbyMessageSerializableTest {

    private static final UserDTO defaultUser = new UserDTO("marco", "marco", "marco@grawunder.de");
    private final UUID testUUID = UUID.randomUUID();

    @Test
    void testLobbyMessagesSerializable() {
        SerializationTestHelper.checkSerializableAndDeserializable(new CreateLobbyRequest("test", defaultUser),
                CreateLobbyRequest.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new LobbyJoinUserRequest("test", defaultUser, testUUID),
                LobbyJoinUserRequest.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new LobbyLeaveUserRequest("test", defaultUser, testUUID),
                LobbyLeaveUserRequest.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new UserJoinedLobbyMessage("test", defaultUser, testUUID, defaultUser),
                UserJoinedLobbyMessage.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new UserLeftLobbyMessage("test", defaultUser, testUUID),
                UserLeftLobbyMessage.class);
    }


}