package net.codersoffortune.infinity.metadata;

import java.util.List;

public class FactionFilters {
    private List<FilterItem> peripheral;
    private List<FilterAttr> attrs;
    private List<FilterItem> category;
    private List<FilterItem> ammunition;
    private List<FilterItem> chars;
    private List<FilterItem> type;
    private List<FilterItem> equip;
    private List<FilterItem> skills;
    private List<FilterItem> weapons;
    private List<FilterItem> extras;
    private List<FilterItem> points;
    private List<SWCFilterItem> swc;

    public List<FilterItem> getExtras() {
        return extras;
    }

    public void setExtras(List<FilterItem> extras) {
        this.extras = extras;
    }

    public List<FilterItem> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<FilterItem> weapons) {
        this.weapons = weapons;
    }

    public List<FilterItem> getSkills() {
        return skills;
    }

    public void setSkills(List<FilterItem> skills) {
        this.skills = skills;
    }

    public List<FilterItem> getEquip() {
        return equip;
    }

    public void setEquip(List<FilterItem> equip) {
        this.equip = equip;
    }

    public List<FilterItem> getChars() {
        return chars;
    }

    public void setChars(List<FilterItem> chars) {
        this.chars = chars;
    }

    public List<FilterItem> getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(List<FilterItem> ammunition) {
        this.ammunition = ammunition;
    }

    public List<FilterItem> getCategory() {
        return category;
    }

    public void setCategory(List<FilterItem> category) {
        this.category = category;
    }

    public List<FilterAttr> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<FilterAttr> attrs) {
        this.attrs = attrs;
    }

    public List<FilterItem> getPeripheral() {
        return peripheral;
    }

    public void setPeripheral(List<FilterItem> peripheral) {
        this.peripheral = peripheral;
    }

    public List<FilterItem> getType() {
        return type;
    }

    public void setType(List<FilterItem> type) {
        this.type = type;
    }

    public List<FilterItem> getPoints() {
        return points;
    }

    public void setPoints(List<FilterItem> points) {
        this.points = points;
    }

    public List<SWCFilterItem> getSwc() {
        return swc;
    }

    public void setSwc(List<SWCFilterItem> swc) {
        this.swc = swc;
    }

    public List<FilterItem> getByType(final FilterType filterType) {
        switch(filterType){
            case peripheral: return peripheral;
            case category: return category;
            case ammunition: return ammunition;
            case chars: return chars;
            case type: return type;
            case equip: return equip;
            case skills: return skills;
            case weapons: return weapons;
            case extras: return extras;
        }
        throw new IllegalStateException("Shouldn't get here");
    }
}
