package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.CompactedUnit;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.EquivalenceMapping;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Describes the set of models needs to represent an entire faction.
 */
public class Catalogue {

    private static final Logger logger = LogManager.getLogger(Catalogue.class);

    public static final String[] CSV_HEADERS = new String[]{"sectoral", "unit", "group", "profile", "option", "name", "weapons", "equip",
            "meshName1", "decals1", "meshes1",
            "meshName2", "decals2", "meshes2",
            "meshName3", "decals3", "meshes3",
            "meshName4", "decals4", "meshes4",
            "meshName5", "decals5", "meshes5",
    };

    private final Set<EquivalenceMapping> unitEquivalenceMappings = new HashSet<>();
    private List<PrintableUnit> unitList = new ArrayList<>();

    public Set<EquivalenceMapping> getMappings() {
        return unitEquivalenceMappings;
    }

    /**
     * Builds the list of the representative units.
     */
    private void makeList() {
        unitList = unitEquivalenceMappings.stream().map(EquivalenceMapping::getBaseUnit).collect(Collectors.toList());
        java.util.Collections.sort(unitList);
    }

    public void addUnits(final Map<Integer, SectoralList> sectorals, FACTION faction, boolean useMercs) throws InvalidObjectException {
        for (SECTORAL sectoral : faction.getSectorals()) {
            addUnits(sectorals.get(sectoral.getId()), sectoral, useMercs);
        }
    }

    public void addUnits(FACTION faction, boolean useMercs) throws IOException {
        addUnits(Database.getInstance().getSectorals(), faction, useMercs);
    }

    public void addUnits(final SectoralList list, SECTORAL sectoral_idx, boolean useMercs) throws InvalidObjectException {
        logger.debug("Adding Units for " + sectoral_idx.toString());
        for (Unit unit : list.getUnits()) {
            if (!useMercs && unit.isMerc()) continue;
            logger.trace("Parsing " + unit.toString());
            for (CompactedUnit cu : unit.getAllDistinctUnits()) {
                logger.trace("Adding " + cu.toString());
                PrintableUnit pu = cu.getPrintableUnit(sectoral_idx);
                boolean claimed = unitEquivalenceMappings.stream().anyMatch(x -> x.addUnitMaybe(pu));
                if (!claimed)
                    unitEquivalenceMappings.add(new EquivalenceMapping(pu));
            }
        }
        makeList();
    }


    /**
     * Generate a Json string of _all_ the units, in a faction bag format for TTS
     * @param faction to label the bag as for
     * @param ms set of models to use for the conversion
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(FACTION faction, final ModelSet ms, boolean doAddons) throws IOException {
        return asJson(faction, ms, false, doAddons);
    }

    /**
     * Generate a Json string of _all_ the units, in a faction bag format for TTS
     * @param faction to label the bag as for
     * @param ms set of models to use for the conversion
     * @param multiplesOnly whether to only output units with multiple models
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(FACTION faction, final ModelSet ms, boolean multiplesOnly, boolean doAddons) throws IOException {
        // Make sure the templates are loaded
        Database.getInstance();
        String template = faction.getTemplate();
        logger.info("Building JSON for " + faction.getName());
        return asJsonInner(template, ms, multiplesOnly, doAddons);
    }

    /**
     * Generate a Json string of _all_ the units, in a sectoral bag format for TTS
     * @param sectoral to label the bag as for
     * @param ms set of models to use for the conversion
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(SECTORAL sectoral, final ModelSet ms, boolean doAddons) throws IOException {
        return asJson(sectoral, ms, false, doAddons);
    }

    /**
     * Generate a Json string of _all_ the units, in a sectoral bag format for TTS
     * @param sectoral to label the bag as for
     * @param ms set of models to use for the conversion
     * @param multiplesOnly whether to only output units with multiple models
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(SECTORAL sectoral, final ModelSet ms, boolean multiplesOnly, boolean doAddons) throws IOException {
        // Make sure the templates are loaded
        Database.getInstance();
        String template = sectoral.getTemplate();
        logger.info("Building JSON for " + sectoral.getName());
        return asJsonInner(template, ms, multiplesOnly, doAddons);

    }


    private String asJsonInner(final String template, final ModelSet ms, boolean multiplesOnly, boolean doAddons) {
        List<PrintableUnit> allUnits = new ArrayList<>();

        for( EquivalenceMapping equivalenceMapping : unitEquivalenceMappings) {
            // Don't bother with unit groups without models
            if (!ms.hasOneOf(equivalenceMapping)) {
                logger.warn("Unable to find models for "+ equivalenceMapping.getBaseUnit());
                continue;
            }

            if( multiplesOnly && ms.getCountFor(equivalenceMapping)<2 ) continue;

            allUnits.add(equivalenceMapping.getBaseUnit());
            allUnits.addAll(equivalenceMapping.getEquivalentUnits());
        }

        // sort reversed due to ordering in the bag.
        allUnits.sort(Comparator.comparing(PrintableUnit::getName).reversed());
        List<String> units = allUnits.stream().map(u->u.asFactionJSON(ms, doAddons)).collect(Collectors.toList());
        String unit_list = String.join(",\n", units);
        return String.format(template, unit_list);
    }

    /**
     * Generates a CSV file containing _just_ the base units for assigning models to groups.
     *
     * @param filename to write the csv out to
     * @throws IOException on error.
     */
    public void toCSV(String filename, final ModelSet ms) throws IOException {
        FileWriter fh = new FileWriter(filename);
        try (CSVPrinter out = new CSVPrinter(fh, CSVFormat.EXCEL.withHeader(CSV_HEADERS))) {
            for (PrintableUnit u : unitList) u.printCSVRecord(out, ms);
        }
    }

    /**
     * Generates a CSV file containing _just_ the missing base units for assigning models to groups.
     *
     * @param filename to write the csv out to
     * @return true iff there are any missing decals
     * @throws IOException on error.
     */
    public boolean toModellessCSV(final String filename, final ModelSet ms) throws IOException {
        List<PrintableUnit> units = getModellessList(ms);
        if (units.isEmpty()) return false;

        FileWriter fh = new FileWriter(filename);
        try (CSVPrinter out = new CSVPrinter(fh, CSVFormat.EXCEL.withHeader(CSV_HEADERS))) {
            for (PrintableUnit u : units) u.printCSVRecord(out, ms);
        }
        return true;
    }

    public List<PrintableUnit> getUnitList() {
        return unitList;
    }


    /**
     * Ask which units lack models.
     *
     * @return a list of all PrintableUnits with no associated models.
     */
    public List<PrintableUnit> getModellessList(final ModelSet ms) {
        return unitList.stream().filter(u->!ms.hasUnit(u.getUnitID())).collect(Collectors.toList());
    }

    public List<PrintableUnit> getModellessList() {
        return getModellessList(Database.getModelSet());
    }
}
