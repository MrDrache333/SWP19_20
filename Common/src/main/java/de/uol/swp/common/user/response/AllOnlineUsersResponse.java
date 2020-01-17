package de.uol.swp.common.user.response;

import de.uol.swp.common.message.AbstractResponseMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Klasse, die alle Nutzer enthält die online sind
 * @author Marco
 * @since Start
 */
public class AllOnlineUsersResponse extends AbstractResponseMessage {

    final private ArrayList<UserDTO> users = new ArrayList<>();

    public AllOnlineUsersResponse() {
        // needed for serialization
    }

    /**
     *Instanziieren der AllOnlineUsersResponse.
     *
     * @param users die Nutzer
     */
    public AllOnlineUsersResponse(Collection<User> users) {
        for (User user : users) {
            this.users.add(UserDTO.createWithoutPassword(user));
        }
    }

    /**
     * Eine Liste von Nutzern wird zurückgegeben.
     *
     * @return Liste von Nutzern
     */
    public List<UserDTO> getUsers() {
        return users;
    }

}
