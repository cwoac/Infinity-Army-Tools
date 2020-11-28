package net.codersoffortune.infinity.metadata;

import java.util.List;

public class Relation {
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
    private List<RelationUnit> units;
    private int min;
    private int max;
    private boolean group;

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public List<RelationUnit> getUnits() {
        return units;
    }

    public void setUnits(List<RelationUnit> units) {
        this.units = units;
    }
}
