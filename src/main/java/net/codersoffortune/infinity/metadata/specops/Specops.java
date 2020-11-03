package net.codersoffortune.infinity.metadata.specops;

import net.codersoffortune.infinity.metadata.Unit;

import java.util.List;

public class Specops {
    private List<SpecopsItem> equip;
    private List<SpecopsItem> skills;
    private List<SpecopsItem> weapons;
    private List<Unit> units;

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public List<SpecopsItem> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<SpecopsItem> weapons) {
        this.weapons = weapons;
    }

    public List<SpecopsItem> getSkills() {
        return skills;
    }

    public void setSkills(List<SpecopsItem> skills) {
        this.skills = skills;
    }

    public List<SpecopsItem> getEquip() {
        return equip;
    }

    public void setEquip(List<SpecopsItem> equip) {
        this.equip = equip;
    }
}
