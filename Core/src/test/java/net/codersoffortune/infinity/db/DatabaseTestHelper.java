package net.codersoffortune.infinity.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Installs cut-down templates into Database static fields for tests.
 * Same package as Database to access package-private fields.
 */
public class DatabaseTestHelper {

    public static void installTemplates(
            String unitTemplate,
            Map<Integer, String> addonTemplates,
            String bagTemplate,
            String factionTemplate,
            String meshTemplate,
            String transmutedUnitTemplate,
            String seedTemplate,
            String decalTemplate
    ) {
        Database.unitTemplate = unitTemplate;
        Database.addonTemplates = addonTemplates;
        Database.bagTemplate = bagTemplate;
        Database.factionTemplate = factionTemplate;
        Database.meshTemplate = meshTemplate;
        Database.transmutedUnitTemplate = transmutedUnitTemplate;
        Database.seedTemplate = seedTemplate;
        Database.decalTemplate = decalTemplate;
    }

    public static void reset() {
        Database.unitTemplate = "";
        Database.addonTemplates = new HashMap<>();
        Database.bagTemplate = "";
        Database.factionTemplate = "";
        Database.meshTemplate = "";
        Database.transmutedUnitTemplate = "";
        Database.seedTemplate = "";
        Database.decalTemplate = "";
    }
}
