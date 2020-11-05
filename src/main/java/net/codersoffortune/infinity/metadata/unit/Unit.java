package net.codersoffortune.infinity.metadata.unit;

import java.util.List;
import java.util.Map;

public class Unit {
    //"id":161,"idArmy":38,"canonical":201,"isc":"Yáozăo","iscAbbr":null,"notes":null,"name":"YÁOZĂO",
    // "options":[],
    //            "slug":"yaozao",
    //            "filters":{"categories":[8],"skills":[28,84,162,243],"equip":[],"chars":[2,5,21,27],"types":[5],"weapons":[71],"ammunition":[37]}}
    private int ID;
    private int idArmy;
    private int canonical;
    private String isc;
    private String iscAbbr;
    private String notes;
    private String name;
    private List<ProfileGroup> ProfileGroups;
    private List<ProfileOption> options;
    private String slug;
    private Map<String, List<Integer>> filters;

    public CompactedUnit getUnit(final int group, final int option) throws IllegalArgumentException {
        ProfileGroup pg = ProfileGroups.stream().filter(x -> x.getId() == group).findFirst().orElseThrow(IllegalArgumentException::new);
        ProfileOption po = pg.getOptions().stream().filter(x -> x.getId() == option).findFirst().orElseThrow(IllegalArgumentException::new);

        CompactedUnit result = new CompactedUnit();
        result.setName(po.getName());
        result.addEquipment(po.getEquip());
        result.addSkills(po.getSkills());
        result.addWeapons(po.getWeapons());

        return result;
    }

    public Map<String, List<Integer>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, List<Integer>> filters) {
        this.filters = filters;
    }

    public List<ProfileOption> getOptions() {
        return options;
    }

    public void setOptions(List<ProfileOption> options) {
        this.options = options;
    }

    public List<ProfileGroup> getProfileGroups() {
        return ProfileGroups;
    }

    public void setProfileGroups(List<ProfileGroup> profileGroups) {
        ProfileGroups = profileGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIscAbbr() {
        return iscAbbr;
    }

    public void setIscAbbr(String iscAbbr) {
        this.iscAbbr = iscAbbr;
    }

    public String getIsc() {
        return isc;
    }

    public void setIsc(String isc) {
        this.isc = isc;
    }

    public int getCanonical() {
        return canonical;
    }

    public void setCanonical(int canonical) {
        this.canonical = canonical;
    }

    public int getIdArmy() {
        return idArmy;
    }

    public void setIdArmy(int idArmy) {
        this.idArmy = idArmy;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
