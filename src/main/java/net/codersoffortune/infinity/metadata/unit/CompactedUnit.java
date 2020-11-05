package net.codersoffortune.infinity.metadata.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * A compacted unit is the final profile got from taking a specific profile group / option combination on a unit.
 */
public class CompactedUnit {
    private List<ProfileItem> weapons = new ArrayList<>();
    private List<ProfileItem> skills = new ArrayList<>();
    private List<ProfileItem> equipment = new ArrayList<>();
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProfileItem> getEquipment() {
        return equipment;
    }

    public void addEquipment(List<ProfileItem> equipment) {
        this.equipment.addAll(equipment);
    }

    public List<ProfileItem> getSkills() {
        return skills;
    }

    public void addSkills(List<ProfileItem> skills) {
        this.skills.addAll(skills);
    }

    public List<ProfileItem> getWeapons() {
        return weapons;
    }

    public void addWeapons(List<ProfileItem> weapons) {
        this.weapons.addAll(weapons);
    }
}
