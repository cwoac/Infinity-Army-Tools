package net.codersoffortune.infinity.metadata;

import java.util.List;

public class RelationUnit {
    // "units": [
    //                {
    //                    "unit": 613
    //                },
    //                {
    //                    "unit": 749
    //                }
    //            ],
    //            "min": 1,
    //            "max": 1,
    //            "group": false
    private int unit;
    private int profile;
    private List<RelationUnit> depends;
    private int min;
    private int minDependant;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public List<RelationUnit> getDepends() {
        return depends;
    }

    public void setDepends(List<RelationUnit> depends) {
        this.depends = depends;
    }

    public int getMinDependant() {
        return minDependant;
    }

    public void setMinDependant(int minDependant) {
        this.minDependant = minDependant;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
