package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.unit.ProfileOption;

import java.util.ArrayList;
import java.util.List;

public class OptionMapping {
    List<Integer> inSectorals = new ArrayList<>();
    private ProfileOption option = null;
    private int id;

    public void add(int sectoral, ProfileOption profileOption) {
        this.inSectorals.add(sectoral);
        this.id = profileOption.getId();
        if (option != null ) {
            // Make sure they match
            assert(option.equals(profileOption));
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

}
