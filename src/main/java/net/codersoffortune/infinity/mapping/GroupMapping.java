package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.unit.ProfileGroup;
import net.codersoffortune.infinity.metadata.unit.ProfileOption;
import net.codersoffortune.infinity.tts.TTSModel;

import java.util.*;

public class GroupMapping {
    private List<Integer> inSectorals = new ArrayList<>();
    private int id;
    private Map<Integer,OptionMapping> options = new HashMap<>();

    public void addGroup(int sectoral, final ProfileGroup group) {
        inSectorals.add(sectoral);
        this.id = group.getId();
        for(ProfileOption po : group.getOptions()) {
            int hc = po.getId();
            if( !options.containsKey(hc)) {
                options.put(hc, new OptionMapping());
            }
            options.get(hc).add(sectoral, po);
        }
    }

    public void addTTSModel(TTSModel model, int option_idx) {
        // TODO:: Consider how we are handling the same model but different image for different sectorials.
        options.values().stream()
                .filter(x->x.getId()==option_idx)
                .forEach(x->x.addModel(model));
    }

    public Optional<TTSModel> getTTSModel(int option_idx) {
        return options.values().stream()
                .filter(x->x.getId()==option_idx)
                .map(OptionMapping::getModel)
                .findAny();
    }

    public Map<Integer, OptionMapping> getOptions() {
        return options;
    }
}
