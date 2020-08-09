package de.uol.swp.server.usermanagement.store;

import com.google.common.base.Strings;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.usermanagement.UserUpdateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Dieser UserStore benutzt eine Datenbank zum Speichern von Usern.
 * Wichtig: Dieser UserStore gibt niemals das Passwort eines Users zurück!
 *
 * @author Keno S
 * @since Sprint 9
 */
public class DatabaseBasedUserStore extends AbstractUserStore implements UserStore {

    private static final Logger LOG = LogManager.getLogger(DatabaseBasedUserStore.class);

    //private final Map<String, User> users = new HashMap<>();
    // TODO: Spalte username in der DB ggf. anpassen?

    private static final String SQL_SELECT_ALL_USER = "SELECT username, password, email FROM user";
    private static final String SQL_SELECT_USER = "SELECT username, password, email FROM user WHERE username = ?";
    private static final String SQL_SELECT_USER_PWD = "SELECT username, password, email FROM user WHERE username = ? AND password = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM user WHERE username = ?";
    private static final String SQL_UPDATE_USER = "UPDATE user SET username = ?, password = ?, email = ? WHERE username = ? AND password = PASSWORD(?)";
    private static final String SQL_INSERT_USER = "INSERT INTO user (username, password, email) VALUES (?, ?, ?);";

    /**
     * Diese Hilfsmethode stellt eine Verbindung mit einer Datenbank her und gibt diese zurück.
     *
     * @return Die neue Datenbankverbindung
     * @author Keno S.
     * @since Sprint 9
     */
    private Connection establishConnection() {

        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db.sqlite");

            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user (" +
                    "username VARCHAR(50) NOT NULL PRIMARY KEY," +
                    "email VARCHAR(100) NOT NULL," +
                    "password VARCHAR(100) NOT NULL" +
                    ")";
            statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {
            LOG.debug(e.getClass().getName() + ": " + e.getMessage());
        }
        LOG.debug("Opened database successfully");
        return conn;
    }

    /**
     * Stellt eine User Abfrage an die Datenbank mit dem Namen und Passwort der Anfrage.
     * Wenn ein User gefunden wird, wird dieser ohne Passwort zurückgegeben.
     * Sonst wird ein Optional.empty() zurückgegeben.
     *
     * @author Keno S.
     * @param username Der Username
     * @param password Das Passwort des Users
     * @return Den (nicht) gefundenen User der Datenbank
     * @since Sprint 9
     */
    @Override
    public Optional<User> findUser(String username, String password) {

        User user = null;

        try {

            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER_PWD);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new UserDTO(rs.getString("username"), "", rs.getString("email"));
            }

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }

        /*User usr = users.get(username);
        if (usr != null && usr.getPassword().equals(password)) {
            return Optional.of(usr);
        }
        return Optional.empty();*/
    }

    /**
     * Stellt eine User Abfrage an die Datenbank mit dem Namen der Anfrage.
     * Wenn ein User gefunden wird, wird dieser ohne Passwort zurückgegeben.
     * Sonst wird ein Optional.empty() zurückgegeben.
     *
     * @author Keno S.
     * @param username Der Username
     * @return Den (nicht) gefundenen User der Datenbank
     * @since Sprint 9
     */
    @Override
    public Optional<User> findUser(String username) {

        User user = null;

        try {
            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER);

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new UserDTO(rs.getString("username"), "", rs.getString("email"));
            }

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        if (user != null) {
            return Optional.of(user);
        } else {
            return Optional.empty();
        }

        /*
        User usr = users.get(username);
        if (usr != null) {
            return Optional.of(usr.getWithoutPassword());
        }
        return Optional.empty();
        */
    }

    /**
     * Mit den gegebenen Strings wird eine Insert Abfrage an die Datenbank gestellt, um den User hinzuzufügen.
     *
     * @author Keno S.
     * @param username Der gewählte Username
     * @param password Das gewählte Passwort
     * @param eMail    Die angegebene E-Mail-Adresse
     * @return Den (nicht) hinzugefügten Nutzer
     * @since Sprint )
     */
    @Override
    public User createUser(String username, String password, String eMail) {

        if (Strings.isNullOrEmpty(username))
            throw new IllegalArgumentException("Username darf nicht leer sein!");
        else if (Strings.isNullOrEmpty(password))
            throw new IllegalArgumentException("Passwort darf nicht leer sein!");
        else if (Strings.isNullOrEmpty(eMail))
            throw new IllegalArgumentException("E-Mail darf nicht leer sein!");

        User user = null;

        try {
            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_USER);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, eMail);

            stmt.executeUpdate();

            stmt = conn.prepareStatement(SQL_SELECT_USER_PWD);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                user = new UserDTO(rs.getString("username"), "", rs.getString("email"));

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        return user;

        /*
        User usr = new UserDTO(username, password, eMail);
        users.put(username, usr);
        return usr;
        */
    }

    /**
     * Aktualisiert in der Datenbank einen bereits vorhandenen User, indem zuerst eine normale Update Anfrage an die Datenbank gestellt wird.
     * Wenn dieser keinen Eintrag findet, wird nichts aktualisiert. Wenn einer gefunden wurde, dann wird dieser aktualiert.
     * Anschließend wird eine Abfrage mit den aktualisierten Daten gestellt um zu gucken, ob die Update Anfrage erfolgreich war.
     * Wenn der User Null ist, wirft die Methode einen Fehler.
     *
     * @author Keno S.
     * @param username        Der neue Username
     * @param password        Das neue Passwort des Users
     * @param eMail           Die neue E-Mail-Adresse des Users
     * @param oldUser         Der alte User
     * @param currentPassword Das momentane Passwort des Users
     * @return Den aktualisierten User
     * @since Sprint 9
     */
    @Override
    public User updateUser(String username, String password, String eMail, User oldUser, String currentPassword) {

        User user = null;

        try {
            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_USER);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, eMail);
            stmt.setString(4, oldUser.getUsername());
            stmt.setString(5, currentPassword);

            stmt.executeUpdate();

            stmt = conn.prepareStatement(SQL_SELECT_USER_PWD);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                user = new UserDTO(rs.getString("username"), "", rs.getString("email"));

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }
        if (user == null)
            throw new UserUpdateException("Das eingegebene Passwort ist nicht korrekt.\n\n Gib dein aktuelles Passwort ein.");
        else
            return user;

        /*
        users.remove(oldUsername);
        User usr = new UserDTO(username, password, eMail);
        users.put(username, usr);
        return usr;
        */
    }

    /**
     * Entfernt einen User aus der Datenbank, wenn dieser vorhanden ist.
     *
     * @author Keno S.
     * @param username Der zu löschende User
     * @since Sprint 9
     */
    @Override
    public void removeUser(String username) {

        try {
            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_USER);

            stmt.setString(1, username);

            stmt.executeUpdate();

            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        //users.remove(username);
    }

    /**
     * Diese Funktion gibt alle User aus der Datenbank in einer ArrayList zurück.
     *
     * @author Keno S.
     * @return Alle Usereinträge der Datenbank
     * @since Sprint 9
     */
    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try {
            Connection conn = establishConnection();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL_USER);

            while (rs.next()) {
                allUsers.add(new UserDTO(rs.getString("username"), "", rs.getString("email")));
            }

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        return allUsers;

        /*
        users.values().forEach(u -> retUsers.add(u.getWithoutPassword()));
        return retUsers;
        */
    }
}
