package de.uol.swp.server.usermanagement.store;

import com.google.common.base.Strings;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is a user store that is based on a database.
 *
 * @author Keno S
 * <p>
 * Important: This store will never return the password of a user
 */

public class DatabaseBasedUserStore extends AbstractUserStore implements UserStore {

    private static final Logger LOG = LogManager.getLogger(DatabaseBasedUserStore.class);

    //private final Map<String, User> users = new HashMap<>();
    // TODO: Spalte username in der DB ggf. anpassen?

    private static final String SQL_SELECT_ALL_USER = "SELECT username, password, email FROM user";
    private static final String SQL_SELECT_USER = "SELECT username, password, email FROM user " + "WHERE username = ?";
    private static final String SQL_SELECT_USER_PWD = "SELECT username, password, email FROM user " + "WHERE username = ? AND password = PASSWORD(?)";
    private static final String SQL_DELETE_USER = "DELETE FROM user " + "WHERE username = ?";
    private static final String SQL_UPDATE_USER = "UPDATE user " + "SET username = ?, password = PASSWORD(?), email = ? WHERE username = ?";
    private static final String SQL_INSERT_USER = "INSERT INTO user (username, password, email) " + "VALUES (?, PASSWORD(?), ?);";

    private Connection establishConnection() {

        Connection conn = null;

        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();

            dataSource.setUser("SWP_DB_Admin");
            dataSource.setPassword("Salatsoße11223440");
            dataSource.setUrl("jdbc:mariadb://kog-nas.synology.me:3307/SWP");

            conn = dataSource.getConnection();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }
        return conn;
    }

    @Override
    public Optional<User> findUser(String username, String password) {

        User usr = null;

        try {

            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER_PWD);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usr = new UserDTO(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        if (usr != null) {
            return Optional.of(usr);
        } else {
            return Optional.empty();
        }

        /*User usr = users.get(username);
        if (usr != null && usr.getPassword().equals(password)) {
            return Optional.of(usr);
        }
        return Optional.empty();*/
    }

    @Override
    public Optional<User> findUser(String username) {

        User usr = null;

        try {
            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER);

            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usr = new UserDTO(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        if (usr != null) {
            return Optional.of(usr);
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

    @Override
    public User createUser(String username, String password, String eMail) {

        if (Strings.isNullOrEmpty(username)) {
            throw new IllegalArgumentException("Username must not be null");
        }

        User usr = null;

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
                usr = new UserDTO(rs.getString("username"), rs.getString("password"), rs.getString("email"));

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        return usr;

        /*
        User usr = new UserDTO(username, password, eMail);
        users.put(username, usr);
        return usr;
        */
    }

    @Override
    public User updateUser(String username, String password, String eMail, User oldUser, String currentPassword) {
        return null;
    }

    @Override
    public User updateUser(String username, String password, String eMail, String oldUsername) {

        User usr = null;

        try {
            Connection conn = establishConnection();

            PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_USER);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, eMail);
            stmt.setString(4, oldUsername);

            stmt.executeUpdate();

            stmt = conn.prepareStatement(SQL_SELECT_USER_PWD);

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                usr = new UserDTO(rs.getString("username"), rs.getString("password"), rs.getString("email"));

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        return usr;

        /*
        users.remove(oldUsername);
        User usr = new UserDTO(username, password, eMail);
        users.put(username, usr);
        return usr;
        */
    }

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

    @Override
    public List<User> getAllUsers() {
        List<User> retUsers = new ArrayList<>();

        try {
            Connection conn = establishConnection();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL_USER);

            while (rs.next()) {
                retUsers.add(new UserDTO(rs.getString("username"), rs.getString("password"), rs.getString("email")));
            }

            rs.close();
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            LOG.debug(e.getMessage());
        }

        return retUsers;

        /*
        users.values().forEach(u -> retUsers.add(u.getWithoutPassword()));
        return retUsers;
        */
    }
}
