package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.unit.ProfileGroup;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.TTSModel;

import java.util.*;

/**
 * Top level mapping item.
 */
public class UnitMapping {
    // Which sectorals
    private List<Integer> inSectorals = new ArrayList<>();
    private int id;
    private Map<Integer, GroupMapping> groups = new HashMap<>();

    public void add(int sectoral, Unit unit){
        inSectorals.add(sectoral);
        this.id = unit.getID();
        for(ProfileGroup pg : unit.getProfileGroups()) {
            if( !groups.containsKey(pg.getId()) ) {
                groups.put(pg.getId(), new GroupMapping());
            }
            groups.get(pg.getId()).addGroup(sectoral, pg);
        }
    }

    public void addTTSModel(TTSModel model, int option_idx) {
        addTTSModel(model, 1, option_idx);
    }
    public void addTTSModel(TTSModel model, int group_idx, int option_idx) {
        groups.get(group_idx).addTTSModel(model, option_idx);
    }

    public Optional<TTSModel> getTTSModel(int option_idx) {
        return getTTSModel(1, option_idx);
    }

    public Optional<TTSModel> getTTSModel(int group_idx, int option_idx) {
        return groups.get(group_idx).getTTSModel(option_idx);
    }

    public Map<Integer, GroupMapping> getGroups() {
        return groups;
    }
}