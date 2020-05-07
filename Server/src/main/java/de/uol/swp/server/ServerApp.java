package de.uol.swp.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.chat.ChatService;
import de.uol.swp.server.communication.Server;
import de.uol.swp.server.di.ServerModule;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.game.GameService;
import de.uol.swp.server.lobby.LobbyService;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Klasse ServerApp
 * Startet bei Aufruf den Server (Ablaufklasse)
 *
 * @author Marco Grawunder
 * @since Sprint 0
 */
class ServerApp {

    private static final Logger LOG = LogManager.getLogger(ServerApp.class);

    /**
     * Startet den Server in einem festgelegten Portbereich.
     * Bei Fehlern wird eine Exception geworfen.
     *
     * @param args
     * @throws Exception Die Exception
     * @author Marco Grawunder
     * @since Sprint 0
     */
    public static void main(String[] args) throws Exception {
        int port = -1;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                // Kein Inhalt, es sollen die Standardwerte genutzt werden
            }
        }
        if (port < 0) {
            port = 8889;
        }
        LOG.info("Starting Server on port " + port);

        // Komponenten erstellen
        Injector injector = Guice.createInjector(new ServerModule());
        createServices(injector);
        injector.getInstance(Server.class).start(port);
    }

    /**
     * Erstellt einen neuen Service inkl. eines Usermanagements und eines Chatmanagements.
     *
     * @param injector Der Injector
     * @throws Exception Die Exception
     * @author Marco Grawunder, Keno O
     * @since Sprint 0
     */
    private static void createServices(Injector injector) {
        UserManagement userManagement = injector.getInstance(UserManagement.class);
        ChatManagement chatManagement = injector.getInstance(ChatManagement.class);
        GameManagement gameManagement = injector.getInstance(GameManagement.class);

        // TODO: Nach der Registrierung entfernen (Von Marco --> Nur übersetzt)
        for (int i = 0; i < 100; i++) {
            userManagement.createUser(new UserDTO("test" + i, "test" + i, "test" + i + "@test.de"));
        }

        // Erstelle den globalen Chat
        chatManagement.createChat("global");

        // Bemerkung: Da diese Dienste von keiner anderen Klasse referenziert werden,
        // müssen wir hier Instanzen erzeugen (und Abhängigkeiten injizieren).
        injector.getInstance(UserService.class);
        injector.getInstance(ChatService.class);
        injector.getInstance(GameManagement.class);
        injector.getInstance(AuthenticationService.class);
        injector.getInstance(LobbyService.class);
        injector.getInstance(GameService.class);
    }

}
