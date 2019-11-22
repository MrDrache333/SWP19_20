package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

import java.util.UUID;

public class UpdateAllOnlineLobbiesRequest extends AbstractRequestMessage {
    private String name;

    public UpdateAllOnlineLobbiesRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}