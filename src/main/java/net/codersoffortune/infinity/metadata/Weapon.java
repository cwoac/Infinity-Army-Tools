package net.codersoffortune.infinity.metadata;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.util.List;
import java.util.Map;

//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Weapon {
    private int id;
    private String type;
    private String name;
    private String mode;
    private String wiki;
    @JsonIdentityReference(alwaysAsId = true)
    private Ammunition ammunition;
    private String burst;
    private String damage;
    private String saving;
    private List<String> properties;
    private Map<String, RangeBand> distance;

    public Weapon() {
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

    public Ammunition getAmmunition() {
        return ammunition;
    }

    //  @JsonProperty("ammunition")
    public void setAmmunition(Ammunition ammunition) {
        // this.ammunition = new Ammunition();
        //this.ammunition.setId(ID);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
