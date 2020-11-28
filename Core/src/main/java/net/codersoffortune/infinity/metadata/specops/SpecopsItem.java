package net.codersoffortune.infinity.metadata.specops;

import java.util.List;

public class SpecopsItem {
    private int exp;
    private int id;
    private List<SpecopsNestedItem> skills;
    private List<SpecopsNestedItem> weapons;
    private List<SpecopsNestedItem> equip;
    private List<Integer> extras;

    public List<SpecopsNestedItem> getSkills() {
        return skills;
    }

    public void setSkills(List<SpecopsNestedItem> skills) {
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public List<SpecopsNestedItem> getEquip() {
        return equip;
    }

    public void setEquip(List<SpecopsNestedItem> equip) {
        this.equip = equip;
    }

    public List<SpecopsNestedItem> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<SpecopsNestedItem> weapons) {
        this.weapons = weapons;
    }

    public List<Integer> getExtras() {
        return extras;
    }

    public void setExtras(List<Integer> extras) {
        this.extras = extras;
    }
}
