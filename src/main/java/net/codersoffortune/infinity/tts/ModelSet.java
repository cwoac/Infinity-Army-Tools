package net.codersoffortune.infinity.tts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelSet {
    private int faction;
    @JsonIgnore
    private FactionList factionList;
    private Map<Integer, Model> models = new HashMap<>();

    public ModelSet() {
    }

    public ModelSet(final FactionList factionList, final int faction) {
        this.factionList = factionList;
        this.faction = faction;

    }

    public FactionList getFactionList() {
        return factionList;
    }

    public void setFactionList(FactionList factionList) {
        this.factionList = factionList;
    }

    /**
     * Add a model to the set
     *
     * @param unit_idx          the index value of the unit
     * @param profile_array_idx the index into the array of units for which option to use (NOTE: This is _not_ the option_idx)
     * @param decals            Json representing the decals for this model
     * @param meshes            Json representing the meshes for this model
     */
    public void addModel(int unit_idx, int profile_array_idx, String decals, String meshes) {
        Optional<Unit> maybeUnit = factionList.getUnit(unit_idx);
        if (!maybeUnit.isPresent()) {
            System.out.println(unit_idx);
        }
        Unit unit = factionList.getUnit(unit_idx).orElseThrow(IllegalArgumentException::new);
        // TODO:: Handle the cases where it is not. E.g. proxies
        int profile_group = 1;
        // TODO:: Handle the cases where it is not. E.g. pilots
        // TODO:: Handle the Su Jian
        int profile = 1;
        int option = 1;
        try {
            option = unit.getProfileGroups().get(0).getOptions().get(profile_array_idx).getId();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (!models.containsKey(unit_idx)) {
            models.put(unit_idx, new Model(faction, unit_idx));
        }
        models.get(unit_idx).addMesh(profile_group, profile, option, decals, meshes);
    }

    // TODO:: randomly select when multiple options are availiable?
    public TTSModel getModel(int unit_idx, int group_idx, int profile_idx, int option_idx) {
        try {
            return models.get(unit_idx).getModel(group_idx, profile_idx, option_idx).get(0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public Map<Integer, Model> getModels() {
        return models;
    }

    public void setModels(Map<Integer, Model> models) {
        this.models = models;
    }
}
