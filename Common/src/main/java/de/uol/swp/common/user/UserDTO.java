package de.uol.swp.common.user;

import java.util.Objects;

/**
 * The type User dto.
 *
 * @author Marco Grawunder
 */
public class UserDTO implements User {

    private static final long serialVersionUID = 4146689909145644727L;
    private final String username;
    private final String password;
    private final String eMail;
    private boolean isBot;

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

    public UserDTO(String username, String password, String eMail, boolean isBot) {
        assert Objects.nonNull(username);
        assert Objects.nonNull(password);
        this.username = username;
        this.password = password;
        this.eMail = eMail;
        this.isBot = isBot;
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

    /**
     * Der Name des Benutzers wird zurückgegeben.
     *
     * @return Name des Useres
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Das Passwort des Benutzers wird zurückgeben
     *
     * @retrun das Passwort des Benutzers
     * @author Marco
     * @since Start
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Die Email des Benutzers wird zurückgeben
     *
     * @retrun Die Email des Benutzers
     * @author Marco
     * @since Start
     */
    @Override
    public String getEMail() {
        return eMail;
    }

    /**
     * Der Benutzers wird zurückgeben ohne Passwort
     *
     * @retrun Der Benutzer ohne Passwort
     * @author Marco
     * @since Start
     */
    @Override
    public User getWithoutPassword() {
        return new UserDTO(username, "", eMail);
    }

    /**
     * Es wird ermittelt um wie viel Chars der Username länger ist als der vom Parameter
     *
     * @param user Der User dessen Username verglichen werden soll
     * @return Länge der Differenz
     * @author Marco, Darian
     * @since Start
     */
    @Override
    public int compareTo(User user) {
        return username.compareTo(user.getUsername());
    }

    /**
     * Es wird überprüft ob die zwei Objekte gleich sind
     *
     * @param o Das Objekt mit der verglichen werden soll
     * @return True wenn die Objekte gleich sind
     * @author Marco
     * @since Start
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserDTO)) {
            return false;
        }
        return Objects.equals(this.username, ((UserDTO) obj).username);
    }

    /**
     * Es wird der Username gehasht
     *
     * @return Der hashCode aus Username
     * @author Marco
     * @since Start
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }


    public boolean getIsBot() {
        return this.isBot;
    }
}
