package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.unit.ProfileGroup;
import net.codersoffortune.infinity.metadata.unit.ProfileOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMapping {
    private List<Integer> inSectorals = new ArrayList<>();
    private int id;
    private Map<Integer,OptionMapping> options = new HashMap<>();

    public void addGroup(int sectoral, final ProfileGroup group) {
        inSectorals.add(sectoral);
        this.id = group.getId();
        for(ProfileOption po : group.getOptions()) {
            if( !options.containsKey(po.getId())) {
                options.put(po.getId(), new OptionMapping());
            }
            options.get(po.getId()).add(sectoral, po);
        }
    }
}
