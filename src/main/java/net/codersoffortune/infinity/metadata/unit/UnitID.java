package net.codersoffortune.infinity.metadata.unit;

import java.util.Objects;

/**
 * POJO to explicitly identify a unit option
 */
public class UnitID {
    private final int sectoral_idx;
    private final int unit_idx;
    private final int group_idx;
    private final int profile_idx;
    private final int option_idx;

    public UnitID(int sectoral_idx, int unit_idx, int group_idx, int profile_idx, int option_idx) {
        this.sectoral_idx = sectoral_idx;
        this.unit_idx = unit_idx;
        this.group_idx = group_idx;
        this.profile_idx = profile_idx;
        this.option_idx = option_idx;
    }

    @Override
    public String toString() {
        return "UnitID{" +
                "s:" + sectoral_idx +
                ";u:" + unit_idx +
                ";g:" + group_idx +
                ";p:" + profile_idx +
                ";o:" + option_idx +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitID unitID = (UnitID) o;
        return sectoral_idx == unitID.sectoral_idx &&
                unit_idx == unitID.unit_idx &&
                group_idx == unitID.group_idx &&
                profile_idx == unitID.profile_idx &&
                option_idx == unitID.option_idx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectoral_idx, unit_idx, group_idx, profile_idx, option_idx);
    }

    public int getSectoral_idx() {
        return sectoral_idx;
    }

    public int getUnit_idx() {
        return unit_idx;
    }

    public int getGroup_idx() {
        return group_idx;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public int getOption_idx() {
        return option_idx;
    }
}
