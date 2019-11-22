package de.uol.swp.common.lobby.request;

import de.uol.swp.common.message.AbstractRequestMessage;

import java.util.UUID;

public class UpdateAllOnlineLobbiesRequest extends AbstractRequestMessage {

    //TODO name nach Möglichkeit durch UUID der Lobby ersetzen
    // (muss beim Beitreten oder Verlassen einer Lobby mitgesendet werden),
    // da Name nicht eindeutig

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