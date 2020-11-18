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
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Describes the set of models needs to represent an entire faction.
 */
public class Catalogue {

    public static final String[] CSV_HEADERS = new String[]{"sectoral", "unit", "group", "profile", "option", "name", "weapons", "equip",
            "meshName1", "decals1", "meshes1",
            "meshName2", "decals2", "meshes2",
            "meshName3", "decals3", "meshes3",
            "meshName4", "decals4", "meshes4",
            "meshName5", "decals5", "meshes5",
    };


    public Map<String, Collection<String>> getEquivalences() {
        Map<String, Collection<String>> results = new HashMap<>();
        unitMappings.stream().filter(m -> !m.getEquivalents().isEmpty())
                .forEach(u -> results.put(u.getRepresentative().toString(),
                        u.getEquivalents().stream().map(PrintableUnit::toString).collect(Collectors.toList())));
        return results;
    }

    private final Set<Mapping> unitMappings = new HashSet<>();
    private List<PrintableUnit> unitList = new ArrayList<>();
    private Set<UnitID> validIDs = new HashSet<>();

    /**
     * Helper function to hide some of the nastiness of reading in a CSV file.
     *
     * @param row the row to read
     * @return a list of 0..5 TTSModels read from that row.
     */
    private static List<TTSModel> readTTSRow(CSVRecord row) {
        List<TTSModel> result = new ArrayList<>();
        String name = row.get("meshName1");
        // TODO:: There has to be a nicer way to do this, surely.
        if (name.isEmpty()) return result;
        String decals = row.get("decals1");
        String meshes = row.get("meshes1");
        result.add(new TTSModel(name, meshes, decals));
        name = row.get("meshName2");
        if (name.isEmpty()) return result;
        decals = row.get("decals2");
        meshes = row.get("meshes2");
        result.add(new TTSModel(name, meshes, decals));
        name = row.get("meshName3");
        if (name.isEmpty()) return result;
        decals = row.get("decals3");
        meshes = row.get("meshes3");
        result.add(new TTSModel(name, meshes, decals));
        name = row.get("meshName4");
        if (name.isEmpty()) return result;
        decals = row.get("decals4");
        meshes = row.get("meshes4");
        result.add(new TTSModel(name, meshes, decals));
        name = row.get("meshName5");
        if (name.isEmpty()) return result;
        decals = row.get("decals5");
        meshes = row.get("meshes5");
        result.add(new TTSModel(name, meshes, decals));
        return result;
    }

    /**
     * Builds the list of the representative units.
     */
    private void makeList() {
        unitList = unitMappings.stream().map(Mapping::getRepresentative).collect(Collectors.toList());
        java.util.Collections.sort(unitList);
    }

    public void addUnits(final Map<Integer, SectoralList> sectorals, FACTION faction, boolean useMercs) throws InvalidObjectException {
        for (SECTORAL sectoral : faction.getSectorals()) {
            addUnits(sectorals.get(sectoral.getId()), sectoral, useMercs);
        }
    }

    public void addUnits(final SectoralList list, SECTORAL sectoral_idx, boolean useMercs) throws InvalidObjectException {
        MappedFactionFilters filters = list.getMappedFilters();
        for (Unit unit : list.getUnits()) {
            if (!useMercs && unit.isMerc()) continue;

            for (CompactedUnit cu : unit.getAllDistinctUnits()) {

                PrintableUnit pu =cu.getPrintableUnit(filters, sectoral_idx);
                boolean claimed = unitMappings.stream().anyMatch(x -> x.addUnitMaybe(pu));
                if (!claimed)
                    unitMappings.add(new Mapping(pu));
                validIDs.add(pu.getUnitID());
            }
        }
        makeList();
    }


    /**
     * Add the TTS models defined to the units in the catalogue
     * @param modelSet to parse.
     */
    public void addTTSModels(final ModelSet modelSet) {
        for (Map.Entry<UnitID, Set<TTSModel>> modelEntry : modelSet.getModels().entrySet()) {
            // Only add models for units we actually care about
            if( !validIDs.contains(modelEntry.getKey())) continue;
            for( Mapping unitMapping : this.unitMappings ) {
                if(unitMapping.contains(modelEntry.getKey())) {
                    unitMapping.baseUnit.addTTSModels(modelEntry.getValue());
                    unitMapping.equivalentUnits.forEach(m->m.addTTSModels(modelEntry.getValue()));
                }
            }
        }
    }

    private Map<UnitID, List<PrintableUnit>> buildReverseMap() throws InvalidObjectException {
        Map<UnitID, List<PrintableUnit>> result = new HashMap<>();
        for (Mapping mapping : unitMappings) {
            UnitID unitID = mapping.baseUnit.getUnitID();
            if (result.containsKey(unitID)) {
                throw new InvalidObjectException("Duplicate UnitID in CSV:" + unitID);
            }
            List<PrintableUnit> pus = new ArrayList<>();
            pus.add(mapping.baseUnit);
            pus.addAll(mapping.getEquivalents());
            result.put(unitID, pus);
        }
        return result;
    }

    /**
     * Parse a CSV file (in the same format as from toCSV), loading in the models.
     * TODO:: Should this actually be in ModelSet?
     * @param filename in CSV format to read in
     * @throws IOException on error
     */
    public void addTTSModelsFromCSV(String filename) throws IOException {
        // First build a mapping so we know where to stash the models.
        Map<UnitID, List<PrintableUnit>> reverseMap = buildReverseMap();

        FileReader fh = new FileReader(filename);
        Iterable<CSVRecord> rows = CSVFormat.EXCEL
                .withHeader(CSV_HEADERS)
                .withFirstRecordAsHeader().parse(fh);
        for (CSVRecord row : rows) {
            // What models does it have?
            List<TTSModel> new_models = readTTSRow(row);
            if (new_models.isEmpty()) continue;
            // Now figure out where to put it.
            UnitID target = new UnitID(Integer.parseInt(row.get("sectoral")),
                    Integer.parseInt(row.get("unit")),
                    Integer.parseInt(row.get("group")),
                    Integer.parseInt(row.get("profile")),
                    Integer.parseInt(row.get("option")));
            // Only add models for units we actually care about
            if( !validIDs.contains(target)) {
                System.out.println(String.format("Undesired unit %s in csv file", target));
                continue;
            }


            if (reverseMap.containsKey(target)) {
                reverseMap.get(target).forEach(
                        pu -> pu.addTTSModels(new_models)
                );
            } else {
                throw new InvalidObjectException(String.format("Unable to find target %s in catalogue", target));
            }
        }
    }

    /**
     * Generate a Json string of _all_ the units, in a faction bag format for TTS
     * @param faction to label the bag as for
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(FACTION faction) throws IOException {
        //TODO:: Check we have all the models / log missing.

        // Make sure the templates are loaded
        Database.getInstance();
        String template = faction.getTemplate();
        return asJsonInner(template);
    }

    /**
     * Generate a Json string of _all_ the units, in a sectoral bag format for TTS
     * @param sectoral to label the bag as for
     * @return json representation of the catalogue
     * @throws IOException on failure
     */
    public String asJson(SECTORAL sectoral) throws IOException {
        // Make sure the templates are loaded
        Database.getInstance();
        String template = sectoral.getTemplate();
        return asJsonInner(template);

    }


    private String asJsonInner(final String template) {
        List<PrintableUnit> allUnits = new ArrayList<>();

        for( Mapping mapping : unitMappings ) {
            // Don't bother with unit groups without models
            if( mapping.baseUnit.getModels().isEmpty()) continue;
            allUnits.add(mapping.baseUnit);
            allUnits.addAll(mapping.equivalentUnits);
        }

        // sort reversed due to ordering in the bag.
        allUnits.sort(Comparator.comparing(PrintableUnit::getName).reversed());
        List<String> units = allUnits.stream().map(PrintableUnit::asFactionJSON).collect(Collectors.toList());
        String unit_list = String.join(",\n", units);
        return String.format(template, unit_list);
    }

    /**
     * Generates a CSV file containing _just_ the base units for assigning models to groups.
     *
     * @param filename to write the csv out to
     * @throws IOException on error.
     */
    public void toCSV(String filename) throws IOException {
        FileWriter fh = new FileWriter(filename);
        try (CSVPrinter out = new CSVPrinter(fh, CSVFormat.EXCEL.withHeader(CSV_HEADERS))) {
            for (PrintableUnit u : unitList) u.printCSVRecord(out);
        }
    }

    /**
     * Represents a set of visibly equivalent units
     */
    private static class Mapping {
        private final Set<PrintableUnit> equivalentUnits = new HashSet<>();
        private final PrintableUnit baseUnit;

        public Collection<PrintableUnit> getEquivalentUnits() {
            return equivalentUnits;
        }


        public Mapping(PrintableUnit base) {
            baseUnit = base;
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
            if (baseUnit.equals(unit)) {
                // Don't bother adding it if it is the base object
                return true;
            }
            equivalentUnits.add(unit);
            return true;
        }

        public boolean contains(UnitID unitID) {
            if (baseUnit.getUnitID().almost_equals(unitID)) return true;
            return this.equivalentUnits.stream().anyMatch(x -> x.getUnitID().almost_equals(unitID));
        }


        /**
         * Get a representative printable unit for this mapping.
         *
         * @return A printable unit from this mapping
         */
        public PrintableUnit getRepresentative() {
            return baseUnit;
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
    public List<PrintableUnit> getModellessList() {
        return unitList.stream().filter(u -> u.getModels().isEmpty()).collect(Collectors.toList());
    }
}
