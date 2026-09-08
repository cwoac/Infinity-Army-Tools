package net.codersoffortune.infinity.metadata.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The Corvus Belli API sends a "spectables" block on some units (e.g. spec-ops operatives).
 * Its attrs[] arrays are a tagged union of ProfileItem (weapon/skill/equip) and StatModifier (stat)
 * shapes, resolved by SpecAttrDeserializer. This uses the same plain ObjectMapper configuration as
 * SectoralList.load() (no extra module registration), since @JsonDeserialize(contentUsing=...) on
 * SpecItem.attrs is sufficient on its own.
 */
class SpectablesTest {

    private static final String JSON = "{" +
            "\"specball\": { \"items\": [ { \"attrs\": [ " +
            "{\"type\":\"weapon\",\"id\":14}, {\"type\":\"stat\",\"stat\":\"cc\",\"q\":6} ] } ] }," +
            "\"table\": { \"items\": [ { \"attrs\": [ {\"type\":\"skill\",\"id\":28,\"extra\":[6]} ] } ] }" +
            "}";

    @Test
    void deserializesTaggedAttrsToConcreteTypes() throws Exception {
        ObjectMapper om = new ObjectMapper();
        Spectables spectables = om.readValue(JSON, Spectables.class);

        assertNotNull(spectables.getSpecball());
        List<Object> specballAttrs = spectables.getSpecball().getItems().get(0).getAttrs();
        assertEquals(2, specballAttrs.size());

        Object weaponAttr = specballAttrs.get(0);
        assertInstanceOf(ProfileItem.class, weaponAttr);
        assertEquals(14, ((ProfileItem) weaponAttr).getId());

        Object statAttr = specballAttrs.get(1);
        assertInstanceOf(StatModifier.class, statAttr);
        assertEquals("cc", ((StatModifier) statAttr).getStat());
        assertEquals(6, ((StatModifier) statAttr).getQ());

        assertNotNull(spectables.getTable());
        List<Object> tableAttrs = spectables.getTable().getItems().get(0).getAttrs();
        Object skillAttr = tableAttrs.get(0);
        assertInstanceOf(ProfileItem.class, skillAttr);
        assertEquals(28, ((ProfileItem) skillAttr).getId());
        assertEquals(List.of(6), ((ProfileItem) skillAttr).getExtra());
    }
}
