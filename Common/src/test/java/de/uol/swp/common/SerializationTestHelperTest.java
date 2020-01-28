package de.uol.swp.common;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse für den SerializationTestHelper
 *
 * @author Marco
 * @since Start
 */
class SerializationTestHelperTest {

    /**
     * Test, ob ein nicht serialisierbares Objekt als solches erkannt und dementsprechend eine RuntimeException geworfen wird
     *
     * @author Marco
     * @since Start
     */
    @Test
    void checkNonSerializable() {
        assertThrows(RuntimeException.class, () ->
                SerializationTestHelper.checkSerializableAndDeserializable(new NotSerializable(), NotSerializable.class));
    }

    /**
     * Test, ob ein serialisierbares Objekt korrekt verarbeitet wird
     *
     * @author Marco
     * @since Start
     */
    @Test
    void checkSerializable() {
        assertTrue(SerializationTestHelper.checkSerializableAndDeserializable("Hallo", String.class));
    }

    /**
     * Private Klasse, die einen neuen Thread für den checkNonSerializable()-Test erstellt
     *
     * @author Marco
     * @since Start
     */
    private static class NotSerializable implements Serializable {
        private Thread thread = new Thread();
    }
}