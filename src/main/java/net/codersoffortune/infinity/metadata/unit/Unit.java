package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private List<ProfileGroup> profileGroups;
    private List<ProfileOption> options;
    private String slug;
    private Map<String, List<Integer>> filters;

    @Override
    public String toString() {
        return String.format("Unit{%d: %s}",ID,
                (iscAbbr==null)?isc:iscAbbr);
    }

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


    /**
     * Irritatingly merc status is simply denoted by having a unit ID over 10k.
     * @return whether this unit is a merc.
     */
    public boolean isMerc() {
        return this.ID > 10000;
    }

    /**
     * Get the units as a collection of json strings suitible for importing into TTS
     *
     * @param group    profile group selected (usually 1)
     * @param option   profile choice selected
     * @param filters  Needed for mapping types, orders as they are stored in the faction data not meta data?
     * @param modelSet the set of availiable models.
     * @return collection of strings, one per model.
     * @throws IllegalArgumentException on error.
     */
    public Collection<String> getUnitsForTTS(final int combat_group,
                                             final int group,
                                             final int option,
                                             final MappedFactionFilters filters,
                                             final ModelSet modelSet,
                                             final SECTORAL sectoral) throws IllegalArgumentException, InvalidObjectException {
        //TODO:: Handle pilots
        Collection<CompactedUnit> compactedUnits = getUnits(group, option);
        Collection<PrintableUnit> printableUnits = new ArrayList<>();
        for( CompactedUnit cu: compactedUnits){
            printableUnits.add(cu.getPrintableUnit(filters, sectoral));
        }
        return printableUnits.stream()
                .map(pu -> pu.asArmyJSON(combat_group, modelSet))
                .collect(Collectors.toList());
    }

    public Collection<CompactedUnit> getAllDistinctUnits() {
        Collection<CompactedUnit> source = getAllUnits();
        Collection<CompactedUnit> result = new ArrayList<>();
        source.forEach(s->{if(result.stream().allMatch(r->!r.publicallyEqual(s)))result.add(s);});
        return result;
    }

    /**
     * Returns whether this profile group is a transmuter or not.
     * @param group to check
     * @return true iff the first profile of this group has transmutation
     */
    private boolean hasTransmutation(final ProfileGroup group) {
        // TODO:: Is this the only value for transmutation?
        return group.getProfiles().get(0).getSkills().stream()
                .anyMatch(s->s.getId()==246);
    }

    public Collection<CompactedUnit> getAllUnits() {
        Collection<CompactedUnit> result = new ArrayList<>();
        for( ProfileGroup group: profileGroups) {
            if( group.getProfiles().size() > 1 && hasTransmutation(group) ) {
                // TODO::zondnauts
                result.addAll(group.getOptions().stream()
                        .map(o -> new TransmutedCompactedUnit(ID, group, group.getProfiles(), o))
                        .collect(Collectors.toList()));
            } else {
                group.getProfiles().forEach(
                        profile -> result.addAll(group.getOptions().stream()
                                .map(o -> new CompactedUnit(ID, group, profile, o))
                                .collect(Collectors.toList()))
                );
            }
        }
        return result;
    }

    public Collection<CompactedUnit> getUnits(final int group, final int option) throws IllegalArgumentException {
        if (group == 0) {
            return get0GroupUnits(option);
        }
        Collection<CompactedUnit> result = new ArrayList<>();

        ProfileGroup pg = profileGroups.stream().filter(x -> x.getId() == group).findFirst().orElseThrow(IllegalArgumentException::new);
        // Note: technically, the unit also has a List<Options>, however this is (currently!) used only for jazz+billie and scarface+cordelia,
        // and maps directly to the options of profile1, and serves to provide the total cost of taking both of the unit.
        ProfileOption po = pg.getOptions().stream().filter(x -> x.getId() == option).findFirst().orElseThrow(IllegalArgumentException::new);

        List<Profile> profiles = pg.getProfiles();
        if (profiles.isEmpty()) {
            throw new IllegalArgumentException("Asked for Profile group with no profile");
        }
        if (profiles.size() > 1) {
            System.out.println("moo");
        }
        CompactedUnit unit = new CompactedUnit(getID(), pg, profiles.get(0), po);

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
        return profileGroups;
    }

    public void setProfileGroups(List<ProfileGroup> profileGroups) {
        this.profileGroups = profileGroups;
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
