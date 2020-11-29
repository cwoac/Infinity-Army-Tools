package net.codersoffortune.infinity.db;

import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.metadata.Faction;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.ModelSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Database {
    private static final Logger logger = LogManager.getLogger();

    private final static String METADATA_URL = "https://api.corvusbelli.com/army/infinity/en/metadata";
    private final static String FACTION_URL_FORMAT = "https://api.corvusbelli.com/army/units/en/%d";
    /*
       Templates loaded from the resource.
       Slightly hacky in that I am making them static inside a singleton, however this means they are
       safe to access without having to call getInstance() and have to handle an IO exception.
       Obviously, if you call the getters before getInstance has ever been called then you will get blanks.
    */
    private static String unitTemplate = "";
    private static List<String> silhouetteTemplates = new ArrayList<>();
    private static Map<Integer, String> addonTemplates = new HashMap<>();
    private static String bagTemplate = "";
    private static String factionTemplate = "";
    private static String transmutedUnitTemplate = "";
    private static ModelSet modelSet = null;
    private static volatile Database dbSingleton;
    Metadata metadata;
    Map<Integer, SectoralList> sectorals;

    public static Map<Integer, Integer> addonSizes = Map.of(
            0, 25,
            1, 25,
            2, 25,
            3, 40,
            4, 55,
            5, 40,
            6, 40,
            7, 55,
            8, 70
    );

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

        addonTemplates = Map.of(
                25, getResourceFileAsString("templates/25mm_addon.json"),
                40, getResourceFileAsString("templates/40mm_addon.json"),
                55, getResourceFileAsString("templates/55mm_addon.json"),
                70, getResourceFileAsString("templates/70mm_addon.json")
        );

        unitTemplate = getResourceFileAsString("Templates/model_template");
        transmutedUnitTemplate = getResourceFileAsString("Templates/transmuted_model_template");
        factionTemplate = getResourceFileAsString("templates/faction_template");
        bagTemplate = getResourceFileAsString("templates/bag_template");
        modelSet = new ModelSet("resources/model catalogue.json");

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

    public static String getAddonTemplate( int silhouetteSize ) {
        return addonTemplates.get(addonSizes.get(silhouetteSize));
    }

    public static String getFactionTemplate() {
        return factionTemplate;
    }

    public static ModelSet getModelSet() { return modelSet; }

    private static String getResourceFileAsString(String fileName) throws IOException {
        return Files.readString(new File("resources/" + fileName).toPath(), StandardCharsets.UTF_8);
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

    public static Database updateAll() throws IOException {
        // reload the database
        synchronized (Database.class) {
            if (dbSingleton != null) {
                dbSingleton = null;
            }
            logger.info("Updating metadata");
            BufferedInputStream in = new BufferedInputStream(new URL(METADATA_URL).openStream());

            Files.copy(in, Paths.get("resources/metadata.json"), StandardCopyOption.REPLACE_EXISTING);
            for (SECTORAL s : SECTORAL.values()) {
                logger.info("Updating {}", s.getName());
                in = new BufferedInputStream(new URL(String.format(FACTION_URL_FORMAT, s.getId())).openStream());
                Files.copy(in, Paths.get(String.format("resources/%d.json", s.getId())), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        return getInstance();
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

    public Optional<Unit> getUnitName(int unitId, SECTORAL sectoral) {
        SectoralList f = sectorals.get(sectoral.getId());
        if (f == null) {
            return Optional.empty();
        }

        return f.getUnit(unitId);
    }

    public String getVersion() {
        return sectorals.get(101).getVersion();
    }

    /**
     * Write the faction jsons to the indicated directory
     *
     * @param outputDir to write to
     * @throws IOException on error
     */
    public void writeJson(File outputDir, boolean doAddons) throws IOException {
        outputDir.mkdir();
        for (FACTION faction : FACTION.values()) {
            if (faction == FACTION.NA2) continue; // They have per sectoral boxes
            logger.info("Reading {}", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            logger.info("Writing JSON");
            String factionJson = c.asJson(faction, modelSet, doAddons);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outputDir.getPath(), faction.getName())));
            writer.append(factionJson);
            writer.close();
        }

        logger.info("Parsing NA2 Sectorals");
        for (SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            System.out.printf("Reading %s%n", sectoral.getName());

            Catalogue c = new Catalogue();
            c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);

            String sectoralJSON = c.asJson(sectoral, modelSet, doAddons);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outputDir.getPath(), sectoral.getName())));
            writer.append(sectoralJSON);
            writer.close();
        }
    }

    /**
     * write the details of any missing decals
     *
     * @param outDir where to put the details
     * @return true iff there are any missing decals
     * @throws IOException on failure
     */
    public boolean writeMissing(File outDir) throws IOException {
        outDir.mkdir();
        boolean anyMissing = false;

        for (FACTION faction : FACTION.values()) {
            if (faction == FACTION.NA2) continue; // They have per sectoral boxes
            logger.info("Reading {}", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            anyMissing |= c.toModellessCSV(String.format("output/%s missing.csv", faction.name()), modelSet);
        }

        logger.info("Parsing NA2 Sectorals");
        for (SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            System.out.printf("Reading %s%n", sectoral.getName());

            Catalogue c = new Catalogue();
            c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);
            anyMissing |= c.toModellessCSV(String.format("output/%s missing.csv", sectoral.name()), modelSet);
        }
        return anyMissing;
    }

    /**
     * write the details of any duplicate decals
     *
     * @param outDir where to put the details
     * @throws IOException on failure
     */
    public void writeDuplicates(File outDir) throws IOException {
        outDir.mkdir();
        for (FACTION faction : FACTION.values()) {
            if (faction == FACTION.NA2) continue; // They have per sectoral boxes
            logger.info("Reading {}", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            logger.info("Writing JSON");
            String factionJson = c.asJson(faction, modelSet, true);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outDir.getPath(), faction.getName())));
            writer.append(factionJson);
            writer.close();
        }

        logger.info("Parsing NA2 Sectorals");
        for (SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            logger.info("Reading {}", sectoral.getName());

            Catalogue c = new Catalogue();
            c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);
            logger.info("Writing JSON");
            String sectoralJSON = c.asJson(sectoral, modelSet, true);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outDir.getPath(), sectoral.getName())));
            writer.append(sectoralJSON);
            writer.close();
        }
    }
}
