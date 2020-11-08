package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.unit.ProfileGroup;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // TODO:: Handle group 0 cases.
    }
}