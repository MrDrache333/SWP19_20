package de.uol.swp.common.lobby.request;

import de.uol.swp.common.user.UserDTO;

public class UpdateLobbiesRequest extends AbstractLobbyRequest {

    private UserDTO oldUser;
    private UserDTO updatedUser;

    public UpdateLobbiesRequest(UserDTO updatedUser, UserDTO oldUser) {
        this.updatedUser = updatedUser;
        this.oldUser = oldUser;
    }

    public UserDTO getUpdatedUser() { return updatedUser; }

    public UserDTO getOldUser() { return oldUser; }
}
