package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

public class UpdateAllOnlineLobbiesRequest extends AbstractRequestMessage {

    private String name;
    private boolean joinLobby;

    public UpdateAllOnlineLobbiesRequest(String name, boolean joinLobby) {
        this.name = name;
        this.joinLobby = joinLobby;
    }

    public String getName() {
        return name;
    }

    public boolean getJoinLobby() { return joinLobby; }
}