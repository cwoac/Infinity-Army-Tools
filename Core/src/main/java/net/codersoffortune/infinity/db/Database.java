package net.codersoffortune.infinity.db;

import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.GAME;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.collection.ModelCollection;
import net.codersoffortune.infinity.metadata.Faction;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Database {
    public final static String MODEL_CATALOGUE_FILE = "resources/model catalogue.json";
    private static final Logger logger = LogManager.getLogger(Database.class);

    private final static String FACTION_URL_FORMAT = "https://api.corvusbelli.com/army/units/en/%d";
    /*
       Templates loaded from the resource.
       Slightly hacky in that I am making them static inside a singleton, however this means they are
       safe to access without having to call getInstance() and have to handle an IO exception.
       Obviously, if you call the getters before getInstance has ever been called then you will get blanks.
    */
    private static String unitTemplate = "";
    private static Map<Integer, String> addonTemplates = new HashMap<>();
    private static String bagTemplate = "";
    private static String factionTemplate = "";
    private static String meshTemplate = "";
    private static String transmutedUnitTemplate = "";
    private static String seedTemplate = "";
    private static String decalTemplate = "";
    private static ModelSet modelSet = null;
    private static volatile Database dbSingleton;
    private static Map<GAME, Metadata> metadataMap;
    private static ModelCollection physicalModels;

    Map<Integer, SectoralList> sectorals;

    public static final Map<Integer, Integer> addonSizes = Map.of(
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

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        metadataMap = Metadata.loadMetadata();
        sectorals = new HashMap<>();
        for( GAME game : GAME.values()) {
            for (Faction f : metadataMap.get(game).getFactions()) {
                int id = f.getID();
                if (id == 901) continue; // NA2 doesn't have a vanilla option
                SectoralList sl = SectoralList.load(String.valueOf(id));
                sectorals.put(id, sl);
                sl.getMappedFilters();
            }
        }

        addonTemplates = Map.of(
                25, getResourceFileAsString("templates/25mm_addon.json"),
                40, getResourceFileAsString("templates/40mm_addon.json"),
                55, getResourceFileAsString("templates/55mm_addon.json"),
                70, getResourceFileAsString("templates/70mm_addon.json")
        );

        meshTemplate = getResourceFileAsString("Templates/mesh_template.json");
        unitTemplate = getResourceFileAsString("Templates/unit_template.json");
        seedTemplate = getResourceFileAsString("Templates/seed_embryo_template.json");
        transmutedUnitTemplate = getResourceFileAsString("Templates/transmuted_model_template");
        factionTemplate = getResourceFileAsString("templates/faction_template");
        bagTemplate = getResourceFileAsString("templates/bag_template");
        loadModelSet();
        decalTemplate = getResourceFileAsString("templates/decal_template.json");

        physicalModels = ModelCollection.Companion.load(getResourceFileAsString("physical_models.json"));
    }

    public void loadModelSet() throws IOException {
        modelSet = new ModelSet(MODEL_CATALOGUE_FILE);
    }

    public static String getBagTemplate() {
        return bagTemplate;
    }

    public static String getMeshTemplate() { return meshTemplate; }

    public static String getUnitTemplate() { return unitTemplate; }

    public static String getSeedTemplate() { return seedTemplate; }

    public static String getAddonTemplate( int silhouetteSize ) {
        return addonTemplates.get(addonSizes.get(silhouetteSize));
    }

    public static String getFactionTemplate() {
        return factionTemplate;
    }

    public static ModelSet getModelSet() { return modelSet; }

    public static ModelCollection getPhysicalModels() { return physicalModels; }

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

    private static BufferedInputStream getStreamForURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("Origin", "https://infinitytheuniverse.com");
        urlConnection.setRequestProperty("Referer", "https://infinitytheuniverse.com/");
        return new BufferedInputStream(urlConnection.getInputStream());
    }

    public static Database updateAll() throws IOException {


        // reload the database
        synchronized (Database.class) {
            if (dbSingleton != null) {
                dbSingleton = null;
            }
            for( GAME game: GAME.values()) {
                logger.info("Updating metadata for {}", game.getName());

                BufferedInputStream in = getStreamForURL(game.getMetadataURL());
                Files.copy(in, Paths.get(game.getMetadataFile()), StandardCopyOption.REPLACE_EXISTING);

            }
            for (SECTORAL s : SECTORAL.values()) {
                logger.info("Updating {}", s.getName());
                BufferedInputStream in = getStreamForURL(String.format(FACTION_URL_FORMAT, s.getId()));
                Files.copy(in, Paths.get(String.format("resources/%d.json", s.getId())), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        return getInstance();
    }

    public static String getDecalTemplate() {
        return decalTemplate;
    }

    public Metadata getMetadata(GAME game) {
        return metadataMap.get(game);
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
        for (FACTION faction : FACTION.armyFactions) {
            logger.info("Reading {}", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
            ems.addModelSet(modelSet);
            logger.info("Writing JSON for {}", faction.getName());
            String factionJson = c.asJson(faction, ems, doAddons);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outputDir.getPath(), faction.getName()), StandardCharsets.UTF_8));
            writer.append(factionJson);
            writer.close();
        }

        logger.info("Parsing container sectorals");
        for(FACTION faction : FACTION.values()) {
            if( !faction.isContainerOnly()) continue;
            logger.info("Parsing sectoral {}", faction.getName());
            for (SECTORAL sectoral : faction.getSectorals()) {
                System.out.printf("Reading %s%n", sectoral.getName());

                Catalogue c = new Catalogue();
                c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);
                EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
                ems.addModelSet(modelSet);

                String sectoralJSON = c.asJson(sectoral, ems, doAddons);
                logger.info("Writing sectoral json for {}", sectoral.getName());
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outputDir.getPath(), sectoral.getName()), StandardCharsets.UTF_8));
                writer.append(sectoralJSON);
                writer.close();
            }
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

        for (FACTION faction : FACTION.armyFactions) {
            logger.info("Reading {}", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            anyMissing |= c.toModellessCSV(String.format("%s/%s missing.csv", outDir.getPath(), faction.name()), modelSet);
        }

        logger.info("Parsing container Sectorals");
        for( FACTION faction : FACTION.armyFactions) {
            logger.info("Handling {}", faction.getName());
            for (SECTORAL sectoral : faction.getSectorals()) {
                System.out.printf("Reading %s%n", sectoral.getName());

                Catalogue c = new Catalogue();
                c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);
                anyMissing |= c.toModellessCSV(String.format("%s/%s missing.csv", outDir.getPath(), sectoral.name()), modelSet);
            }
        }


        return anyMissing;
    }

    private void readMissingFaction(File inFile, FACTION faction) throws IOException {
        logger.info("About to parse {} as {}", inFile.getName(), faction.getName());
        Catalogue c = new Catalogue();
        c.addUnits(sectorals, faction, false);
        EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
        ems.readCSV(inFile);
        modelSet.addModelSet(ems.expand());
    }

    private void readMissingSectoral(File inFile, SECTORAL sectoral) throws IOException {
        logger.info("About to parse {} as {}", inFile.getName(), sectoral.getName());
        Catalogue c = new Catalogue();
        c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);
        EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
        ems.readCSV(inFile);
        modelSet.addModelSet(ems.expand());
    }

    /**
     * Read in a bunch of CSV files, hopefully containing details of missing models
     * @param inDir to parse
     * @throws IOException on failure
     */
    public void readMissing(File inDir) throws IOException {
       for( File file : inDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"))) {
           // TODO:: Should probably validate this
           final String base_name = file.getName().replaceFirst(" missing.csv","");
           // Is it a Faction bag?
           Optional<FACTION> factionMaybe = Arrays.stream(FACTION.values())
                   .filter(x->x.getName().equalsIgnoreCase(base_name))
                   .findFirst();
           if( factionMaybe.isPresent() ) {
               readMissingFaction(file, factionMaybe.get());
               continue;
           }
           // OK, maybe it is a (NA2) sectoral?
           Optional<SECTORAL> sectoralMaybe = Arrays.stream(SECTORAL.values())
                   .filter(x->x.getName().equalsIgnoreCase(base_name))
                   .findFirst();
           if( sectoralMaybe.isPresent() ) {
               readMissingSectoral(file, sectoralMaybe.get());
               continue;
           }
           logger.warn("Unable to identify what {} is.", base_name);
       }
       modelSet.writeFile("modelset_updated.json");
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
            if (faction.isContainerOnly()) continue; // They have per sectoral boxes
            logger.info("Reading {}", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            logger.info("Writing duplicate JSON for {}", faction.getName());
            String factionJson = c.asJson(faction, modelSet, true);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outDir.getPath(), faction.getName()), StandardCharsets.UTF_8));
            writer.append(factionJson);
            writer.close();
        }

        logger.info("Parsing container sectorals");
        for( FACTION faction : FACTION.values()) {
            if(!faction.isContainerOnly()) continue;
            logger.info("Parsing {}", faction.getName());
            for (SECTORAL sectoral : faction.getSectorals()) {
                logger.info("Reading {}", sectoral.getName());

                Catalogue c = new Catalogue();
                c.addUnits(sectorals.get(sectoral.getId()), sectoral, false);
                logger.info("Writing duplicate JSON for {}", sectoral.getName());
                String sectoralJSON = c.asJson(sectoral, modelSet, true);
                BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s/%s.json", outDir.getPath(), sectoral.getName()), StandardCharsets.UTF_8));
                writer.append(sectoralJSON);
                writer.close();
            }
        }
    }
}
