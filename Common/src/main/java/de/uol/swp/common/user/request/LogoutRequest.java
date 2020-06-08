package de.uol.swp.common.user.request;

import de.uol.swp.common.message.AbstractRequestMessage;

/**
 * Eine Request, welcher vom Client zum Server geschickt wird, um auszuloggen.
 * Um "hardLogout" erweitert, dieser wird nicht so behandelt als wäre er abwendbar. Verwendet bei schließen des Fensters.
 *
 * @author Marco Grawunder, Marvin
 */

public class LogoutRequest extends AbstractRequestMessage {

    private static final long serialVersionUID = -5912075449879112061L;

    private boolean hardLogout = false;

    public LogoutRequest() {
        super();
    }

    public LogoutRequest(boolean hardLogout) {
        super();
        this.hardLogout = hardLogout;
    }

    public boolean isHardLogout() {
        return hardLogout;
    }
}
