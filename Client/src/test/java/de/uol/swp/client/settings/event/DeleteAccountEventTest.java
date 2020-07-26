package de.uol.swp.client.settings.event;

import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testet die Klasse DeleteAccountEvent
 *
 * @author Rike
 * @since Sprint10
 */
public class DeleteAccountEventTest {

    private static final User defaultUser = new UserDTO("Herbert", "1234", "herbert@muster.de");

    @Test
    public void createDeleteAccountEvent() {
        DeleteAccountEvent event = new DeleteAccountEvent(defaultUser);
        assertEquals(defaultUser, event.getUser());
    }
}
