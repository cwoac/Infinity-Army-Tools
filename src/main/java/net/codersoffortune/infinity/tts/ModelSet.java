package net.codersoffortune.infinity.tts;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.ProfileOption;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.metadata.unit.UnitID;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.codersoffortune.infinity.tts.Catalogue.CSV_HEADERS;

public class ModelSet {
    final Map<UnitID, Set<TTSModel>> models = new HashMap<>();

    transient Set<Catalogue.EquivalenceMapping> equivalenceMappings;

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

    public Set<Catalogue.EquivalenceMapping> getEquivalenceMappings() {
        return equivalenceMappings;
    }

    public void setEquivalenceMappings(Set<Catalogue.EquivalenceMapping> equivalenceMappings) {
        this.equivalenceMappings = equivalenceMappings;
    }

    public void readJson(final String input) throws IOException {
        JsonNode jn = new ObjectMapper().readTree(input);
        readJsonInner(jn, true);
    }

    public void readJson(final URL input) throws IOException {
        JsonNode jn = new ObjectMapper().readTree(input);
        readJsonInner(jn, true);
    }

    private void readJsonInner(final JsonNode jn, boolean loadAll) {
        JsonNode contents = jn.findPath("ContainedObjects");
        for (JsonNode child : contents) {
            String[] descLines = child.get("Description").asText().split("\n");
            String code = descLines[descLines.length - 1];
            UnitID unitID = UnitID.decode(code);
            // If we are only searching for missing models, don't grab one we already have.
            if (!loadAll && models.containsKey(unitID)) continue;
            String name = child.get("Nickname").asText();
            String decals = child.get("AttachedDecals").toString();
            String meshes = child.get("CustomMesh").toString();
            TTSModel model = new TTSModel(name, decals, meshes);
            addModel(unitID, model);
        }
    }

    private void addModel(UnitID unitID, TTSModel model) {
        if (!models.containsKey(unitID))
            models.put(unitID, new HashSet<>());
        models.get(unitID).add(model);
    }

    public void readCSV(String filename) throws IOException {
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
            if (!models.containsKey(target)) {
                models.put(target, new HashSet<>(new_models));
            } else {
                models.get(target).addAll(new_models);
            }
        }
    }

    /**
     * Loader to read the contents of an old (hand done) bag, where the descriptions are of the form option_array_index + unit number
     *
     * @param input json file to process
     * @throws IOException on failure
     */
    public void readOldJson(final URL input, final FACTION faction, final FactionList factionList) throws IOException {
        ObjectMapper om = new ObjectMapper();
        if (input == null) {
            return;
        }
        JsonNode jn = om.readTree(input);
        JsonNode contents = jn.findPath("ContainedObjects");
        Pattern idPattern = Pattern.compile("(?<opt>[\\dA-Fa-f])(?<unit>[\\dA-Fa-f]{5})");
        for (JsonNode child : contents) {
            String[] descLines = child.get("Description").asText().split("\n");
            String name = child.get("Nickname").asText();
            String code = descLines[descLines.length - 1];//.split("\\]?\\[-\\]\\[?")[1];
            Matcher matcher = idPattern.matcher(code);
            if (!matcher.find()) {
                // TODO:: Proper logging. Possibly validate that this is a proxy model?
                System.out.printf("Unable to find a model for %s%n", name);
                continue;
            }
            String option = matcher.group("opt");
            int optionIdx = Integer.parseInt(matcher.group("opt"), 16);
            String unit = matcher.group("unit");
            int unitIdx = Integer.parseInt(matcher.group("unit"), 16);
            // Not all models in the bag are currently in factions (due to sectorals awaiting updates mostly).
            switch (faction) {
                case PanOceania:
                    // 22 - Neoterra auxilia
                    if (unitIdx == 22) continue;
                    break;
                case CombinedArmy:
                    if (unitIdx == 1506) continue;
                    break;
                case Aleph:
                    // 603 Ekdromos
                    // 604 Alke
                    // 606 Diomedes
                    // 607 Machoan
                    final List<Integer> greeks = Arrays.asList(603, 604, 606, 607);
                    if (greeks.contains(unitIdx)) continue;
                default:
                    break;
            }
            if ((faction == FACTION.PanOceania && unitIdx == 22)) {

                continue;
            }
            String decals = "";
            if (child.has("AttachedDecals")) {
                // Models which are just a token (e.g. seed embryos) may not have extra decals.
                decals = child.get("AttachedDecals").toString();
            }

            String meshes = child.get("CustomMesh").toString();
            addModelOld(factionList, faction.getId(), unitIdx, optionIdx, new TTSModel(name, decals, meshes));
        }
    }


    /**
     * Add a model to the set, loading from an old style unit ref (unit number, profile array index).
     *
     * @param faction_idx       which faction this model is based in (don't have the information to pick more specifically).
     * @param unit_idx          the index value of the unit
     * @param profile_array_idx the index into the array of units for which option to use (NOTE: This is _not_ the option_idx)
     * @param model             the model to add
     */
    public void addModelOld(final FactionList factionList, int faction_idx, int unit_idx, int profile_array_idx, TTSModel model) {
        // First convert the profile index back into a profile ID
        Optional<Unit> maybeUnit = factionList.getUnit(unit_idx);
        if (!maybeUnit.isPresent()) {
            // Can't look up something which doesn't exist.
            System.out.println(unit_idx);
        }

        Unit unit = factionList.getUnit(unit_idx).orElseThrow(IllegalArgumentException::new);

        // Don't have any information to assume it is anything but a profile for a normal unit
        List<ProfileOption> options = unit.getProfileGroups().get(0).getOptions();
        int option = 0;
        if (options.size() <= profile_array_idx) {
            System.out.printf("Can't find profile %d for %s, assuming 0%n", profile_array_idx, unit.getName());
            // Option is missing? Ah well, guess 0
        } else {
            option = options.get(profile_array_idx).getId();
        }

        addModel(new UnitID(faction_idx, unit_idx, 1, 1, option), model);
    }

    public Map<UnitID, Set<TTSModel>> getModels() {
        return models;
    }

    public Map<UnitID, Set<TTSModel>> getModelsForUnitID(int id) {
        Map<UnitID, Set<TTSModel>> result = new HashMap<>();
        for (Map.Entry<UnitID, Set<TTSModel>> entry : models.entrySet()) {
            if (entry.getKey().getUnit_idx() == id) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    public Set<TTSModel> getModels(final UnitID unitID) {
        // Do we have this model?
        if (hasUnit(unitID)) {
            return models.get(unitID);
        }
        // OK, do we know some equivalents for this model?
        Optional<Catalogue.EquivalenceMapping> maybeMapping = equivalenceMappings.stream().filter(m -> m.contains(unitID)).findFirst();
        if (!maybeMapping.isPresent()) {
            return new HashSet<>();
        }
        Catalogue.EquivalenceMapping mapping = maybeMapping.get();

        // OK, what about the equivalents?
        Optional<UnitID> equivalent = mapping.getAllUnits().stream()
                .map(PrintableUnit::getUnitID)
                .filter(this::hasUnit)
                .findAny();
        if (!equivalent.isPresent()) {
            return new HashSet<>();
        }

        return models.get(equivalent.get());
    }

    public boolean hasUnit(final UnitID unitID) {
        return models.containsKey(unitID);
    }

    /**
     * Check if we have models for at least one of an equivalence set
     *
     * @param mapping of UnitIDs which are equivalent
     * @return true iff at least one of unitIDs is in models.
     */
    public boolean hasOneOf(final Catalogue.EquivalenceMapping mapping) {
        return mapping.getAllUnits().stream().anyMatch(u -> models.containsKey(u.getUnitID()));
    }


}
