package net.codersoffortune.infinity.db;

import net.codersoffortune.infinity.metadata.Faction;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Database {
    /*
       Templates loaded from the resource.
       Slightly hacky in that I am making them static inside a singleton, however this means they are
       safe to access without having to call getInstance() and have to handle an IO exception.
       Obviously, if you call the getters before getInstance has ever been called then you will get blanks.
    */
    private static String unitTemplate = "";
    private static List<String> silhouetteTemplates = new ArrayList<>();
    private static String bagTemplate = "";
    private static String factionTemplate = "";

    private Database() throws IOException {
        // block reflection
        if (dbSingleton != null) {
            throw new RuntimeException("Don't try and create Database directly");
        }

        metadata = Metadata.loadMetadata();
        factions = new HashMap<>();
        for (Faction f : metadata.getFactions()) {
            int id = f.getID();
            if (id == 901) continue; // NA2 doesn't have a vanilla option
            factions.put(id, SectoralList.loadFaction(String.valueOf(id)));
        }

        silhouetteTemplates = Arrays.asList(
                getResourceFileAsString("templates/S1.json"),
                getResourceFileAsString("templates/S2.json"),
                getResourceFileAsString("templates/S3.json"),
                getResourceFileAsString("templates/S4.json"),
                getResourceFileAsString("templates/S5.json"),
                getResourceFileAsString("templates/S6.json"),
                getResourceFileAsString("templates/S7.json")
        );
        unitTemplate = getResourceFileAsString("Templates/model_template");
        factionTemplate = getResourceFileAsString("templates/faction_template");
        bagTemplate = getResourceFileAsString("templates/bag_template");
    }

    public static String getBagTemplate() {
        return bagTemplate;
    }

    public static String getUnitTemplate() {
        return unitTemplate;
    }

    public static List<String> getSilhouetteTemplates() {
        return silhouetteTemplates;
    }

    public static String getFactionTemplate() {
        return factionTemplate;
    }

    // in memory structures
    Metadata metadata;
    Map<Integer, SectoralList> factions;

    private static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static Database getInstance() throws IOException {
        if (dbSingleton == null) {
            // thread safe
            synchronized (Database.class) {
                if (dbSingleton == null) {
                    dbSingleton = new Database();
                }
            }
        }
        return dbSingleton;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Map<Integer, SectoralList> getFactions() {
        return factions;
    }

    private static volatile Database dbSingleton;

    public Optional<Unit> getUnitName(int unit_id, int faction_id) {
        SectoralList f = factions.get(faction_id);
        if (f == null) {
            // TODO:: Throw exception?
            return Optional.empty();
        }

        return f.getUnit(unit_id);
    }

}
