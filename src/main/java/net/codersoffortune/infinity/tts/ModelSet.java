package net.codersoffortune.infinity.tts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.metadata.unit.UnitID;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelSet {
    private final Map<UnitID, List<TTSModel>> models = new HashMap<>();

    public void readJson(final String input) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(input);
        JsonNode contents = jn.findPath("ContainedObjects");
        for (JsonNode child : contents) {
            String[] descLines = child.get("Description").asText().split("\n");
            String code = descLines[descLines.length - 1];
            UnitID unitID = UnitID.decode(code);
            String name = child.get("Nickname").asText();
            String decals = child.get("AttachedDecals").toString();
            String meshes = child.get("CustomMesh").toString();
            TTSModel model = new TTSModel(name, decals, meshes);
            addModel(unitID, model);
        }
    }

    /**
     * Loader to read the contents of an old (hand done) bag, where the descriptions are of the form option_array_index + unit number
     * @param input json file to process
     * @throws IOException on failure
     */
    public void readOldJson(final URL input, final FACTION faction, final FactionList factionList) throws IOException {
        ObjectMapper om = new ObjectMapper();
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
                System.out.println(String.format("Unable to find a model for %s", name));
                continue;
            }
            String option = matcher.group("opt");
            int optionIdx = Integer.parseInt(matcher.group("opt"), 16);
            String unit = matcher.group("unit");
            int unitIdx = Integer.parseInt(matcher.group("unit"), 16);
            if ((faction == FACTION.PanOceania && unitIdx == 22))  {
                // Not all models in the bag are currently in factions (due to sectorals awaiting updates)
                // 22 - Neoterra auxilia
                continue;
            }
            String decals = child.get("AttachedDecals").toString();
            String meshes = child.get("CustomMesh").toString();
            addModelOld(factionList, faction.getId(), unitIdx, optionIdx, new TTSModel(name,decals,meshes));
        }
    }

    public void addModel(UnitID unitID, TTSModel model) {
        // TODO:: Maybe dedupe?
        if (!models.containsKey(unitID))
            models.put(unitID, new ArrayList<>());
        models.get(unitID).add(model);
    }

    /**
     * Add a model to the set, loading from an old style unit ref (unit number, profile array index).
     *
     * @param faction_idx       which faction this model is based in (don't have the information to pick more specifically).
     * @param unit_idx          the index value of the unit
     * @param profile_array_idx the index into the array of units for which option to use (NOTE: This is _not_ the option_idx)
     * @param model the model to add
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
        int profile_group = 1;
        int profile = 1;
        int option = unit.getProfileGroups().get(0).getOptions().get(profile_array_idx).getId();

        addModel(new UnitID(faction_idx, unit_idx,1,1, option), model);
    }

    public Map<UnitID, List<TTSModel>> getModels() {
        return models;
    }

    public List<TTSModel> getModels(UnitID unitID) {
        return models.get(unitID);
    }
}
