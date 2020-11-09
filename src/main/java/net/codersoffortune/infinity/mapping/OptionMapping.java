package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.unit.ProfileOption;
import net.codersoffortune.infinity.tts.TTSModel;

import java.util.ArrayList;
import java.util.List;

public class OptionMapping {
    List<Integer> inSectorals = new ArrayList<>();
    private ProfileOption option = null;
    private List<TTSModel> models = new ArrayList<>();
    private int id;

    public void add(int sectoral, ProfileOption profileOption) {
        this.inSectorals.add(sectoral);
        this.id = profileOption.getId();
        if (option != null ) {
            // Make sure they match
            if(! option.equals(profileOption) ) {
                System.out.println("Opps");
            }
        } else {
            option = profileOption;
        }
    }

    public List<Integer> getInSectorals() {
        return inSectorals;
    }

    public void setInSectorals(List<Integer> inSectorals) {
        this.inSectorals = inSectorals;
    }

    public ProfileOption getOption() {
        return option;
    }

    public void setOption(ProfileOption option) {
        this.option = option;
    }

    public TTSModel getModel() {
        // TODO:: What to do about multiple models, or faction specific models?
        return models.size()>0 ? models.get(0) : null;
    }

    public void addModel(TTSModel model) {
        this.models.add(model);
    }

    public int getId() {
        return id;
    }
}
