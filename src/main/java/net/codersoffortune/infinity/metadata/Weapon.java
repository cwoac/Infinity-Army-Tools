package net.codersoffortune.infinity.metadata;

import java.util.List;
import java.util.Map;

public class Weapon {
    private int ID;
    private String type;
    private String name;
    private String mode;
    private String wiki;
    private int ammunition;
    private String burst;
    private String damage;
    private String saving;
    private List<String> properties;
    private Map<String, RangeBand> distance;

    public Weapon() {
    }

    public Weapon(int ID, String type, String name, String mode, String wiki, int ammunition, String burst, String damage, String saving, List<String> properties, Map<String, RangeBand> distance) {
        setID(ID);
        setType(type);
        setName(name);
        setMode(mode);
        setWiki(wiki);
        setAmmunition(ammunition);
        setBurst(burst);
        setDamage(damage);
        setSaving(saving);
        setProperties(properties);
        setDistance(distance);
    }

    public Map<String, RangeBand> getDistance() {
        return distance;
    }

    public void setDistance(Map<String, RangeBand> distance) {
        this.distance = distance;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public String getSaving() {
        return saving;
    }

    public void setSaving(String saving) {
        this.saving = saving;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getBurst() {
        return burst;
    }

    public void setBurst(String burst) {
        this.burst = burst;
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }
}
