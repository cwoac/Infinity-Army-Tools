package net.codersoffortune.infinity.tts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.metadata.unit.UnitID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
