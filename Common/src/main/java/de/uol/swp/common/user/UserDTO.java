package de.uol.swp.common.user;

import java.util.Objects;

/**
 * The type User dto.
 *
 * @author Marco Grawunder
 */
public class UserDTO implements User {

    private final String username;
    private final String password;
    private final String eMail;

    /**
     * Initialisiert ein neues UserDTO.
     *
     * @param username Der Username
     * @param password Das Passwort
     * @param eMail    Die eMail
     */
    public UserDTO(String username, String password, String eMail) {
        assert Objects.nonNull(username);
        assert Objects.nonNull(password);
        this.username = username;
        this.password = password;
        this.eMail = eMail;
    }

    /**
     * Erzeugt ein neues UserDTO.
     *
     * @param user Der User
     * @return Das UserDTO
     */
    public static UserDTO create(User user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getEMail());
    }

    /**
     * Erzeugt eine neue UserDTO ohne Passwort
     *
     * @param user Der User
     * @return Das User DTO
     */
    public static UserDTO createWithoutPassword(User user) {
        return new UserDTO(user.getUsername(), "", user.getEMail());
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEMail() {
        return eMail;
    }

    @Override
    public User getWithoutPassword() {
        return new UserDTO(username, "", eMail);
    }

    @Override
    public int compareTo(User o) {
        return username.compareTo(o.getUsername());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserDTO)) {
            return false;
        }
        return Objects.equals(this.username, ((UserDTO) obj).username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
