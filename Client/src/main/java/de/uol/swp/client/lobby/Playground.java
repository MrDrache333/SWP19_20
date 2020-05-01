package de.uol.swp.client.lobby;

import de.uol.swp.common.game.AbstractPlayground;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.user.User;

public class Playground extends AbstractPlayground {
    private Phase.Type phase = Phase.Type.ActionPhase;
    private User[] player;
    private User loggedInUser;
    private User playerOnTurn;
}
