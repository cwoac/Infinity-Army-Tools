package net.codersoffortune.infinity.metadata.unit;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Handle the special case of a unit with the selected group being 0. This represents a paired group of valued units
     * that you have to buy as pair.
     *
     * @param option the option of the setup to take (this applies to the first unit in all the cases I've seen so far
     * @return Collection of the units included in this choice
     * @throws IllegalArgumentException if an invalid option is specified
     */
    private Collection<CompactedUnit> get0GroupUnits(final int option) throws IllegalArgumentException {
        Collection<CompactedUnit> result = new ArrayList<>();
        ProfileOption po = getOptions().stream().filter(x -> x.getId() == option).findFirst().orElseThrow(IllegalArgumentException::new);

        for (ProfileInclude pi : po.getIncludes()) {
            Collection<CompactedUnit> includedUnit = getUnits(pi.getGroup(), pi.getOption());
            for (int i = 0; i < pi.getQ(); i++)
                result.addAll(includedUnit);
        }

        return result;
    }

    public Collection<CompactedUnit> getUnits(final int group, final int option) throws IllegalArgumentException {
        if (group == 0) {
            return get0GroupUnits(option);
        }
        Collection<CompactedUnit> result = new ArrayList<>();

        ProfileGroup pg = ProfileGroups.stream().filter(x -> x.getId() == group).findFirst().orElseThrow(IllegalArgumentException::new);
        // Note: technically, the unit also has a List<Options>, however this is (currently!) used only for jazz+billie and scarface+cordelia,
        // and maps directly to the options of profile1, and serves to provide the total cost of taking both of the unit.
        ProfileOption po = pg.getOptions().stream().filter(x -> x.getId() == option).findFirst().orElseThrow(IllegalArgumentException::new);


        CompactedUnit unit = new CompactedUnit();
        unit.setName(po.getName());
        unit.addEquipment(po.getEquip());
        unit.addSkills(po.getSkills());
        unit.addWeapons(po.getWeapons());
        result.add(unit);
        // now check for included elements.
        for (ProfileInclude pi : po.getIncludes()) {
            Collection<CompactedUnit> includedUnit = getUnits(pi.getGroup(), pi.getOption());
            for (int i = 0; i < pi.getQ(); i++)
                result.addAll(includedUnit);
        }
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
