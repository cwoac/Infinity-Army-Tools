package net.codersoffortune.infinity.metadata;

import net.codersoffortune.infinity.metadata.unit.Unit;

/**
 * Holds the details of a TTS model
 */
public class Model {
    // TODO:: make these proper links
    private int faction_idx;
    private int unit_idx;
    private int profile_group_idx;
    private int profile_idx;
    private int option_idx;
    private String decals;
    private String meshes;

    public Model() {
    }

    /**
     * Constructor for a Model object.
     *
     * @param factionList       The faction to look up values against.
     * @param unit_idx          the index value of the unit
     * @param profile_array_idx the index into the array of units for which option to use (NOTE: This is _not_ the option_idx)
     * @param decals            Json representing the decals for this model
     * @param meshes            Json representing the meshes for this model
     */
    public Model(final FactionList factionList, int unit_idx, int profile_array_idx, String decals, String meshes) {
        Unit unit = factionList.getUnit(unit_idx).orElseThrow(IllegalArgumentException::new);
        setUnit_idx(unit_idx);
        setDecals(decals);
        setMeshes(meshes);
        // TODO:: Handle the cases where it is not. E.g. proxies
        setProfile_group_idx(1);
        // TODO:: Handle the cases where it is not. E.g. pilots
        // TODO:: Handle the Su Jian
        setProfile_idx(1);
        setOption_idx(unit.getProfileGroups().get(0).getOptions().get(profile_array_idx).getId());
    }

    public int getFaction_idx() {
        return faction_idx;
    }

    public void setFaction_idx(int faction_idx) {
        this.faction_idx = faction_idx;
    }

    public int getUnit_idx() {
        return unit_idx;
    }

    public void setUnit_idx(int unit_idx) {
        this.unit_idx = unit_idx;
    }

    public int getProfile_group_idx() {
        return profile_group_idx;
    }

    public void setProfile_group_idx(int profile_group_idx) {
        this.profile_group_idx = profile_group_idx;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public void setProfile_idx(int profile_idx) {
        this.profile_idx = profile_idx;
    }

    public int getOption_idx() {
        return option_idx;
    }

    public void setOption_idx(int option_idx) {
        this.option_idx = option_idx;
    }

    public String getDecals() {
        return decals;
    }

    public void setDecals(String decals) {
        this.decals = decals;
    }

    public String getMeshes() {
        return meshes;
    }

    public void setMeshes(String meshes) {
        this.meshes = meshes;
    }
}
