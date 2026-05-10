package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.EquivalenceMapping;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.DecalBlockModel;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitDebugTest {

    // === EDIT THESE TO DEBUG A SPECIFIC UNIT ===
    private static final int SECTORAL_ID = 601;
    private static final int UNIT_ID = 1885;//680;//
    // ===========================================

    private static final String FAKE_IMAGE_URL = "https://steamusercontent-a.akamaihd.net/ugc/1018702952398939866/D864D2D93070B9D71D078DABB3E58AC86AA4C2D7/";
    private static final String FAKE_DECALS = "[]";

    @Test
    void debugUnit() throws Exception {
        SECTORAL sectoral = Arrays.stream(SECTORAL.values())
                .filter(s -> s.getId() == SECTORAL_ID)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown sectoral ID: " + SECTORAL_ID));

        Database.getInstance();
        ModelSet realMs = Database.getModelSet();

        SectoralList fullList = SectoralList.load(String.valueOf(SECTORAL_ID));
        Unit targetUnit = fullList.getUnit(UNIT_ID)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unit ID " + UNIT_ID + " not found in sectoral " + SECTORAL_ID));

        System.out.println("=== Unit Debug ===");
        System.out.println("Sectoral: " + sectoral.getName() + " (" + SECTORAL_ID + ")");
        System.out.println("Unit: " + targetUnit);
        System.out.println();

        SectoralList singleUnitList = new SectoralList();
        singleUnitList.setUnits(Collections.singletonList(targetUnit));

        Catalogue catalogue = new Catalogue();
        catalogue.addUnits(singleUnitList, sectoral, false);

        // Build a debug model set: real models where they exist, fake placeholder otherwise
        EquivalentModelSet debugMs = new EquivalentModelSet(catalogue.getMappings());
        //debugMs.addModelSet(realMs);

        System.out.println("=== PrintableUnits Generated ===");
        Set<EquivalenceMapping> mappingSet = catalogue.getMappings();
        for (EquivalenceMapping mapping : mappingSet) {
            Set<PrintableUnit> unitSet = mapping.getAllUnits();
            for (PrintableUnit pu : unitSet) {
                if (pu.getUnitID().getProfile_idx() <= 1) {
                    debugMs.addModel(pu.getUnitID(),
                            new DecalBlockModel("debug_placeholder", FAKE_DECALS, FAKE_IMAGE_URL));
                }
            }
        }

        System.out.println("=== Faction JSON Output ===");
        String factionJson = catalogue.asJson(sectoral, debugMs, true);
        System.out.println(factionJson);

        assertTrue(factionJson != null && !factionJson.isEmpty(), "Faction JSON should not be empty");
    }
}
