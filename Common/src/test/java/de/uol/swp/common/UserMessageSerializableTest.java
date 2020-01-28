package de.uol.swp.common;

import de.uol.swp.common.user.message.UserLoggedInMessage;
import de.uol.swp.common.user.message.UserLoggedOutMessage;
import de.uol.swp.common.user.message.UsersListMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class UserMessageSerializableTest {

    private static final int SIZE = 10;
    private static List<String> users = new ArrayList<>();

    /**
     *Zehn Nutzer werden der Liste hinzugefügt.
     *
     * @author Keno S.
     * @since Sprint3
     */
    static {
        for (int i = 0; i < SIZE; i++) {
            users.add("User" + i);
        }
    }

    /**
     * Test, ob die UserMessages Serializable sind.
     *
     * @author Keno S.
     * @since Sprint3
     */
    @Test
    void testUserMessagesSerializable() {
        SerializationTestHelper.checkSerializableAndDeserializable(new UserLoggedInMessage("test"),
                UserLoggedInMessage.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new UserLoggedOutMessage("test"),
                UserLoggedOutMessage.class);
        SerializationTestHelper.checkSerializableAndDeserializable(new UsersListMessage(users),
                UsersListMessage.class);
    }
}