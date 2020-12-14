package net.codersoffortune.infinity.tts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the details of a TTS model
 */
public class Model {
    private int faction;
    private int unit;
    // Yes, this is monstrousity, the map is group->profile->option->[model]
    private Map<Integer, Map<Integer, Map<Integer, List<TTSModel>>>> items = new HashMap<>();

    public List<TTSModel> getModel(int group, int profile, int option) {
        return items.get(group).get(profile).get(option);
    }

    public Model() {
    }

    public Model(int faction, int unit) {
        this.faction = faction;
        this.unit = unit;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public Map<Integer, Map<Integer, Map<Integer, List<TTSModel>>>> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Map<Integer, Map<Integer, List<TTSModel>>>> items) {
        this.items = items;
    }

    public void addMesh(int profile_group_idx, int profile_idx, int option_idx, String modelName, String decals, String meshes) {
        if (!items.containsKey(profile_group_idx))
            items.put(profile_group_idx, new HashMap<>());
        Map<Integer, Map<Integer, List<TTSModel>>> group_map = items.get(profile_group_idx);
        if (!group_map.containsKey(profile_idx))
            group_map.put(profile_idx, new HashMap<>());
        Map<Integer, List<TTSModel>> profile_map = group_map.get(profile_idx);
        if (!profile_map.containsKey(option_idx))
            profile_map.put(option_idx, new ArrayList<>());
        profile_map.get(option_idx).add(new DecalBlockModel(modelName, decals, meshes));
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }
}
