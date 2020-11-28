package net.codersoffortune.infinity.db;

import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.metadata.Faction;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    private static String transmutedUnitTemplate = "";
    private static List<String> impersonationTemplates = new ArrayList<>();

    private Database() throws IOException {
        // block reflection
        if (dbSingleton != null) {
            throw new RuntimeException("Don't try and create Database directly");
        }

        metadata = Metadata.loadMetadata();
        sectorals = new HashMap<>();
        for (Faction f : metadata.getFactions()) {
            int id = f.getID();
            if (id == 901) continue; // NA2 doesn't have a vanilla option
            sectorals.put(id, SectoralList.load(String.valueOf(id)));
        }

        /* A little note on S0. Technically, this is SX, which is the 3mm high version of the silhouette.
           However, the only units which have SX on their profile are the seed embryos, which are all Sx2,
           so S0 is S2 scaled to 3mm high.
         */
        silhouetteTemplates = Arrays.asList(
                getResourceFileAsString("templates/S0.json"),
                getResourceFileAsString("templates/S1.json"),
                getResourceFileAsString("templates/S2.json"),
                getResourceFileAsString("templates/S3.json"),
                getResourceFileAsString("templates/S4.json"),
                getResourceFileAsString("templates/S5.json"),
                getResourceFileAsString("templates/S6.json"),
                getResourceFileAsString("templates/S7.json"),
                getResourceFileAsString("templates/S8.json")
        );
        unitTemplate = getResourceFileAsString("Templates/model_template");
        transmutedUnitTemplate = getResourceFileAsString("Templates/transmuted_model_template");
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

    Metadata metadata;
    Map<Integer, SectoralList> sectorals;

    private static String getResourceFileAsString(String fileName) throws IOException {
        return Files.readString(new File("resources/"+fileName).toPath(), StandardCharsets.UTF_8);
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

    public static String getTransmutedUnitTemplate() {
        return transmutedUnitTemplate;
    }

    public static List<String> getImpersonationTemplates() {
        return impersonationTemplates;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Map<Integer, SectoralList> getSectorals() {
        return sectorals;
    }

    private static volatile Database dbSingleton;

    public Optional<Unit> getUnitName(int unitId, SECTORAL sectoral) {
        SectoralList f = sectorals.get(sectoral.getId());
        if (f == null) {
            return Optional.empty();
        }

        return f.getUnit(unitId);
    }

}
