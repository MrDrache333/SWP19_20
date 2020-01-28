package de.uol.swp.common.message;

abstract public class AbstractRequestMessage extends AbstractMessage implements RequestMessage {

    private static final long serialVersionUID = 5366167153636367089L;

    @Override
    public boolean authorizationNeeded() {
        return true;
    }
}
