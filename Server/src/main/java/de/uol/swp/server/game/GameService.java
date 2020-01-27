package de.uol.swp.server.game;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import de.uol.swp.server.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Der GameService. Verarbeitet alle Anfragen, die über den Bus gesendet werden.
 */
public class GameService extends AbstractService {

    private static final Logger LOG = LogManager.getLogger(GameService.class);

    private final GameManagement gameManagement;

    /**
     * Erstellt einen neuen GameService
     *
     * @param eventBus       Der zu nutzende EventBus
     * @param gameManagement Das GameManagement
     * @author KenoO
     * @since Sprint 5
     */
    @Inject
    public GameService(EventBus eventBus, GameManagement gameManagement) {
        super(eventBus);
        this.gameManagement = gameManagement;
    }

    //--------------------------------------
    // EVENTBUS
    //--------------------------------------
}
