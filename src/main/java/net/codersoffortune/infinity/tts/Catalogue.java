package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.CompactedUnit;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.metadata.unit.UnitID;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
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

    private static final Logger logger = LogManager.getLogger();

    public static final String[] CSV_HEADERS = new String[]{"sectoral", "unit", "group", "profile", "option", "name", "weapons", "equip",
            "meshName1", "decals1", "meshes1",
            "meshName2", "decals2", "meshes2",
            "meshName3", "decals3", "meshes3",
            "meshName4", "decals4", "meshes4",
            "meshName5", "decals5", "meshes5",
    };

//
//    public Map<String, Collection<String>> getEquivalences() {
//        Map<String, Collection<String>> results = new HashMap<>();
//        unitEquivalenceMappings.stream().filter(m -> !m.getEquivalents().isEmpty())
//                .forEach(u -> results.put(u.getRepresentative().toString(),
//                        u.getEquivalents().stream().map(PrintableUnit::toString).collect(Collectors.toList())));
//        return results;
//    }

    private final Set<EquivalenceMapping> unitEquivalenceMappings = new HashSet<>();
    private List<PrintableUnit> unitList = new ArrayList<>();

    public Set<EquivalenceMapping> getMappings() {
        return unitEquivalenceMappings;
    }

    /**
     * Builds the list of the representative units.
     */
    private void makeList() {
        unitList = unitEquivalenceMappings.stream().map(EquivalenceMapping::getRepresentative).collect(Collectors.toList());
        java.util.Collections.sort(unitList);
    }

    public void addUnits(final Map<Integer, SectoralList> sectorals, FACTION faction, boolean useMercs) throws InvalidObjectException {
        for (SECTORAL sectoral : faction.getSectorals()) {
            addUnits(sectorals.get(sectoral.getId()), sectoral, useMercs);
        }
    }

    public void addUnits(final SectoralList list, SECTORAL sectoral_idx, boolean useMercs) throws InvalidObjectException {
        MappedFactionFilters filters = list.getMappedFilters();
        logger.debug("Adding Units for " + sectoral_idx.toString());
        for (Unit unit : list.getUnits()) {
            if (!useMercs && unit.isMerc()) continue;
            logger.trace("Parsing " + unit.toString());
            for (CompactedUnit cu : unit.getAllDistinctUnits()) {
                logger.trace("Adding " + cu.toString());
                PrintableUnit pu =cu.getPrintableUnit(filters, sectoral_idx);
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
    public String asJson(FACTION faction, final ModelSet ms) throws IOException {
        return asJson(faction, ms, false);
    }

    /**
     * Generate a Json string of _all_ the units, in a faction bag format for TTS
     * @param faction to label the bag as for
     * @param ms set of models to use for the conversion
     * @param multiplesOnly whether to only output units with multiple models
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(FACTION faction, final ModelSet ms, boolean multiplesOnly) throws IOException {
        // Make sure the templates are loaded
        Database.getInstance();
        String template = faction.getTemplate();
        logger.info("Building JSON for " + faction.getName());
        return asJsonInner(template, ms, multiplesOnly);
    }

    /**
     * Generate a Json string of _all_ the units, in a sectoral bag format for TTS
     * @param sectoral to label the bag as for
     * @param ms set of models to use for the conversion
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(SECTORAL sectoral, final ModelSet ms) throws IOException {
        return asJson(sectoral, ms, false);
    }

    /**
     * Generate a Json string of _all_ the units, in a sectoral bag format for TTS
     * @param sectoral to label the bag as for
     * @param ms set of models to use for the conversion
     * @param multiplesOnly whether to only output units with multiple models
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(SECTORAL sectoral, final ModelSet ms, boolean multiplesOnly) throws IOException {
        // Make sure the templates are loaded
        Database.getInstance();
        String template = sectoral.getTemplate();
        logger.info("Building JSON for " + sectoral.getName());
        return asJsonInner(template, ms, multiplesOnly);

    }


    private String asJsonInner(final String template, final ModelSet ms, boolean multiplesOnly) {
        List<PrintableUnit> allUnits = new ArrayList<>();

        for( EquivalenceMapping equivalenceMapping : unitEquivalenceMappings) {
            // Don't bother with unit groups without models
            if (!ms.hasOneOf(equivalenceMapping)) {
                logger.warn("Unable to find models for "+equivalenceMapping.baseUnit.toString());
                continue;
            }

            if( multiplesOnly && ms.getCountFor(equivalenceMapping)<2 ) continue;

            allUnits.add(equivalenceMapping.baseUnit);
            allUnits.addAll(equivalenceMapping.equivalentUnits);
        }

        // sort reversed due to ordering in the bag.
        allUnits.sort(Comparator.comparing(PrintableUnit::getName).reversed());
        List<String> units = allUnits.stream().map(u->u.asFactionJSON(ms)).collect(Collectors.toList());
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
     * @throws IOException on error.
     */
    public void toModellessCSV(final String filename, final ModelSet ms) throws IOException {
        FileWriter fh = new FileWriter(filename);
        try (CSVPrinter out = new CSVPrinter(fh, CSVFormat.EXCEL.withHeader(CSV_HEADERS))) {
            for (PrintableUnit u : getModellessList(ms)) u.printCSVRecord(out, ms);
        }
    }

    /**
     * Represents a set of visibly equivalent units
     */
    static class EquivalenceMapping {
        private final Set<PrintableUnit> equivalentUnits = new HashSet<>();
        private final Set<PrintableUnit> allUnits = new HashSet<>();
        private final PrintableUnit baseUnit;

        public EquivalenceMapping(PrintableUnit base) {
            baseUnit = base;
            allUnits.add(base);
        }

        /**
         * Check to see if the passed unit is visibily equivalent to the ones in this mapping. If so, claim it.
         *
         * @param unit to consider
         * @return true iff the unit is claimed.
         */
        public boolean addUnitMaybe(PrintableUnit unit) {
            if (!baseUnit.isEquivalent(unit)) {
                return false;
            }
            if (baseUnit.functionallyEquals(unit)) {
                // Don't bother adding it if it is the base object
                return true;
            }
            equivalentUnits.add(unit);
            allUnits.add(unit);
            return true;
        }

        public boolean contains(UnitID unitID) {
            return allUnits.stream().anyMatch(u->u.getUnitID().equals(unitID));
        }


        /**
         * Get a representative printable unit for this mapping.
         *
         * @return A printable unit from this mapping
         */
        public PrintableUnit getRepresentative() {
            return baseUnit;
        }

        public Collection<PrintableUnit> getAllUnits() {
            return allUnits;
        }

        public Collection<PrintableUnit> getEquivalents() {
            return equivalentUnits;
        }

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
}
