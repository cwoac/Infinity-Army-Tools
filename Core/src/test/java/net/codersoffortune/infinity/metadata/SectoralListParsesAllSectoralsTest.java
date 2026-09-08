package net.codersoffortune.infinity.metadata;

import net.codersoffortune.infinity.SECTORAL;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Regression test: every sectoral's JSON must still parse via SectoralList.load().
 * Guards against Corvus Belli API schema changes (e.g. field renames/additions)
 * silently breaking Jackson deserialization for any one sectoral.
 */
class SectoralListParsesAllSectoralsTest {

    @ParameterizedTest(name = "{0}")
    @EnumSource(SECTORAL.class)
    void loadsWithoutThrowing(SECTORAL sectoral) {
        assertDoesNotThrow(() -> SectoralList.load(String.valueOf(sectoral.getId())),
                () -> "Failed to parse sectoral " + sectoral.name() + " (" + sectoral.getId() + ")");
    }
}
