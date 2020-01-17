package de.uol.swp.server.usermanagement;

import com.google.common.eventbus.EventBus;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.request.RegisterUserRequest;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    static final User userToRegister = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    static final User userWithSameName = new UserDTO("Marco", "Marco2", "Marco2@Grawunder.com");

    final EventBus bus = new EventBus();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final UserService userService = new UserService(bus, userManagement);

    /**
     *Test, ob ein neuer Nutzer registriert werden kann.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void registerUserTest() {
        final RegisterUserRequest request = new RegisterUserRequest(userToRegister);

        bus.post(request);

        final User loggedInUser = userManagement.login(userToRegister.getUsername(), userToRegister.getPassword());

        assertNotNull(loggedInUser);
        assertEquals(loggedInUser, userToRegister);
    }

    /**
     *Test, dass der alte Nutzer nicht überschrieben wird, falls versucht wird einen neuen Nutzer mit dem gleichem Namen zu registrieren.
     *
     * @author Marco
     * @since Start
     */
    @Test
    void registerSecondUserWithSameName() {
        final RegisterUserRequest request = new RegisterUserRequest(userToRegister);
        final RegisterUserRequest request2 = new RegisterUserRequest(userWithSameName);

        bus.post(request);
        bus.post(request2);

        final User loggedInUser = userManagement.login(userToRegister.getUsername(), userToRegister.getPassword());

        assertNotNull(loggedInUser);
        assertEquals(loggedInUser, userToRegister);

        assertNotEquals(loggedInUser.getEMail(), userWithSameName.getEMail());

    }

}