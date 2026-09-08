package net.codersoffortune.infinity.metadata.unit;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SpecItem {
    // attrs is a mix of ProfileItem (weapon/skill/equip) and StatModifier (stat) entries,
    // disambiguated at parse time by SpecAttrDeserializer.
    @JsonDeserialize(contentUsing = SpecAttrDeserializer.class)
    private List<Object> attrs;

    public List<Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<Object> attrs) {
        this.attrs = attrs;
    }
}
