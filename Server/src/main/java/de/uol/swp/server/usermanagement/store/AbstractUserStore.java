package de.uol.swp.server.usermanagement.store;


import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Abstrakter Benutzerspeicher
 *
 * @author Marco
 * @since Start
 */
@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractUserStore implements UserStore {

    /**
     * @param toHash Der String, der gehasht werden möchte
     * @return Hash Der gehashte String
     * @author Marco
     * @since Start
     */
    protected String hash(String toHash) {
        return Hashing.sha256()
                .hashString(toHash, StandardCharsets.UTF_8)
                .toString();
    }

}
