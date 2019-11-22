package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

import java.util.UUID;

public class UpdateAllOnlineLobbiesRequest extends AbstractRequestMessage {

    private String name;
    private boolean value;

    public UpdateAllOnlineLobbiesRequest(String name, boolean value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean getValue() { return value; }
}