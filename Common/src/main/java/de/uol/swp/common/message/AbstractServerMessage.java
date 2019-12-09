package de.uol.swp.common.message;

import de.uol.swp.common.user.Session;

import java.util.ArrayList;
import java.util.List;

public class AbstractServerMessage extends AbstractMessage implements ServerMessage {

    private List<Session> receiver = new ArrayList<>();

    public List<Session> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<Session> receiver) {
        this.receiver = receiver;
    }
}
