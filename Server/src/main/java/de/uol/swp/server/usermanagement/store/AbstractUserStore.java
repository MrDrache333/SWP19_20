package de.uol.swp.server.usermanagement.store;


import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Abstrakter Benutzerspeicher
 *
 * @author Marco
 * @since Start
 */
public abstract class AbstractUserStore implements UserStore {

    /**
     *
     * @param toHash
     * @return Hash
     * @author Marco
     * @since Start
     */
    protected String hash(String toHash) {
        return Hashing.sha256()
                .hashString(toHash, StandardCharsets.UTF_8)
                .toString();
    }

}
