package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.Util;
import net.codersoffortune.infinity.armylist.CombatGroup;
import net.codersoffortune.infinity.collection.VisibleItem;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InvalidObjectException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class Unit {
    private static final Logger logger = LogManager.getLogger(Unit.class);

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
        return String.format("Unit{%d: %s}", ID,
                (iscAbbr == null) ? isc : iscAbbr);
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
     *
     * @return whether this unit is a merc.
     */
    public boolean isMerc() {
        return this.ID > 10000;
    }

    /**
     * Get the units as a collection of json strings suitible for importing into TTS
     *
     * @param group              profile group selected (usually 1)
     * @param option             profile choice selected
     * @param equivalentModelSet the set of availiable models.
     * @return collection of strings, one per model.
     * @throws IllegalArgumentException on error.
     */
    public Collection<String> getUnitsForTTS(final CombatGroup combatGroup,
                                             final int group,
                                             final int option,
                                             final EquivalentModelSet equivalentModelSet,
                                             final SECTORAL sectoral,
                                             final boolean doAddons) throws IllegalArgumentException, InvalidObjectException {
        //TODO:: Handle pilots
        Collection<CompactedUnit> compactedUnits = getPublicUnits(group, option);
        Collection<PrintableUnit> printableUnits = new ArrayList<>();
        for (CompactedUnit cu : compactedUnits) {
            printableUnits.add(cu.getPrintableUnit(sectoral));
        }
        return printableUnits.stream()
                .peek(pu -> logger.trace("Converting {}", pu.getName()))
                .map(pu -> pu.asArmyJSON(combatGroup, equivalentModelSet, doAddons))
                .collect(Collectors.toList());
    }

    public Collection<CompactedUnit> getAllDistinctUnits() {
        Collection<CompactedUnit> source = getAllUnits();
        Collection<CompactedUnit> result = new ArrayList<>();
        source.forEach(s -> {
            if (result.stream().noneMatch(r -> r.publicallyEqual(s))) result.add(s);
        });
        return result;
    }

    /**
     * Returns whether this profile group is a transmuter or not.
     *
     * @param group to check
     * @return true iff the first profile of this group has transmutation
     */
    private boolean hasTransmutation(final ProfileGroup group) {
        // 246 > Transmutation
        // 205 > AI Motorcycle
        // 66 > seed embryo
        Profile profile = group.getProfiles().get(0);
        return profile.getSkills().stream()
                .anyMatch(s -> (s.getId() == 246 || s.getId() == 66)) ||
                profile.getEquip().stream()
                        .anyMatch(s -> s.getId() == 205);
    }

    private boolean isSeedSoldier(final ProfileGroup group) {
        Profile profile = group.getProfiles().get(0);
        // 512 == seed soldier
        // 513 == cadmus
        return getID() == 512 || getID() == 513;
        // 66 -> seed embryo
    }

    /**
     * Check if an option has sapper. Have to check both the group and the option (curse you moblots!)
     *
     * @param group  the group to check
     * @param option the option to check
     * @return true iff we have the sapper skill
     */
    private boolean isSapper(final ProfileGroup group, final ProfileOption option) {
        return group.getProfiles().stream().anyMatch(it -> it.getSkills().stream().anyMatch(s -> s.getId() == 89))
                || option.getSkills().stream()
                .anyMatch(s -> s.getId() == 89);
    }

    /**
     * Inner helper function to generate the correct CompactedUnit for a given group/option pairing.
     *
     * @param pg the group to generate for
     * @param po the option to generate for
     * @return a CompactedUnit representing that group/option of this unit.
     */
    private List<CompactedUnit> getCompactedUnits(final ProfileGroup pg, final ProfileOption po) {
        final List<Profile> ps = pg.getProfiles();
        final List<CompactedUnit> result = new ArrayList<>();

        // Sappers get double profiles. Yay!
        if (isSapper(pg, po)) {
            result.add(new TransmutedCompactedUnit(this, pg, getSapperProfileList(pg), po));
        } else {
            // Note that having transmutation makes no sense _unless_ there are multiple profiles to transmute to.
            // But some units have multiple profiles without transmute (e.g. bikes and riders).
            if (ps.size() > 1 && hasTransmutation(pg) && !Util.hasSeedState(this.ID)) {
                result.add(new TransmutedCompactedUnit(this, pg, pg.getProfiles(), po));
            } else {
                // Just a boring unit(s). Or a seed soldier.

                // If it is a biker then the first profile is the mounted version.
                boolean dismountedBiker = false;
                for( Profile profile: ps) {
                    result.add(new CompactedUnit(this, pg, profile, po, dismountedBiker));
                    if (profile.getEquip().stream().anyMatch(pi->Util.bikes.contains(pi.getId()))) {
                        dismountedBiker = true;
                    }
                }
            }
        }

        // now check for included elements.
        // There is currently a bug in that the Szalamandra Squad shows as having an include when it doesn't.
        if (ID == 420) return result;
        for (ProfileInclude pi : po.getIncludes()) {
            Collection<CompactedUnit> includedUnits = getUnits(pi.getGroup(), pi.getOption());
            // profile can have the same include multiple times.
            for (int i = 0; i < pi.getQ(); i++) {
                result.addAll(includedUnits);
            }
        }

        return result;
    }

    public Collection<CompactedUnit> getAllUnits() {
        Collection<CompactedUnit> result = new ArrayList<>();
        for (ProfileGroup group : profileGroups) {
            for (ProfileOption option : group.getOptions()) {
                result.addAll(getCompactedUnits(group, option));
            }
        }
        return result;
    }

    /**
     * Get a list of the weapons available to this unit which may be represented on a model.
     *
     * @return a list of items that are plausibly visible on a model
     */
    public Collection<VisibleItem> getVisibleWeapons() {
        Collection<VisibleItem> result = new ArrayList<>();
        // We could iterate over all options / profiles, but the filters seem to have everything
        result.addAll(filters.get("weapons").stream().filter(it -> !Util.invisibleWeapons.contains(it))
                .map(it -> new VisibleItem(it, FilterType.weapons))
                .collect(Collectors.toList()));

        return result;
    }

    public Collection<VisibleItem> getVisibleSkills() {
        Collection<VisibleItem> result = new ArrayList<>();
        // We could iterate over all options / profiles, but the filters seem to have everything
        result.addAll(filters.get("skills").stream().filter(Util.physicalModelSkills::contains)
                .map(it -> new VisibleItem(it, FilterType.skills))
                .collect(Collectors.toList()));

        return result;
    }

    public Collection<VisibleItem> getPhysicalEquipment() {
        Collection<VisibleItem> result = new ArrayList<>();
        // We could iterate over all options / profiles, but the filters seem to have everything
        result.addAll(filters.get("equipment").stream().filter(Util.modelledEquipment::contains)
                .map(it -> new VisibleItem(it, FilterType.equip))
                .collect(Collectors.toList()));

        return result;
    }

    public Collection<CompactedUnit> getPublicUnits(final int group, final int option) throws IllegalArgumentException {
        Collection<CompactedUnit> result = new ArrayList<>();
        Collection<CompactedUnit> unsortedUnits = getUnits(group, option);
        for (CompactedUnit compactedUnit : unsortedUnits) {
            if (compactedUnit.hasNoPrivateInformation()) {
                // Easy!
                result.add(compactedUnit);
                continue;
            }
            // Ah. Need to find the public version of this.
            ProfileGroup pg = profileGroups.stream().filter(x -> x.getId() == group).findFirst().orElseThrow(IllegalArgumentException::new);
            List<Profile> profiles = pg.getProfiles();
            for (ProfileOption po : pg.getOptions()) {
                if (po.hasPrivateInformation()) continue;
                CompactedUnit candidate = new CompactedUnit(this, pg, profiles.get(0), po);
                if (candidate.publicallyEqual(compactedUnit)) {
                    result.add(candidate);
                    break;
                }
            }
            if (result.isEmpty()) {
                // everything either had private information or was not equal to the presented unit.
                // so fall back on the unit itself.
                result.add(compactedUnit);
            }
        }

        return result;
    }

    public Collection<CompactedUnit> getUnits(final int group, final int option) throws IllegalArgumentException {
        if (group == 0) {
            return get0GroupUnits(option);
        }

        ProfileGroup pg;
        try {
            pg = profileGroups.stream().filter(x -> x.getId() == group).findFirst().orElseThrow(IllegalArgumentException::new);
        } catch (IllegalArgumentException e) {
            throw(e);
        }
        // Note: technically, the unit also has a List<Options>, however this is (currently!) used only for jazz+billie and scarface+cordelia,
        // and maps directly to the options of profile1, and serves to provide the total cost of taking both of the unit.
        ProfileOption po = pg.getOptions().stream().filter(x -> x.getId() == option).findFirst().orElseThrow(IllegalArgumentException::new);

        return getCompactedUnits(pg, po);
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

    /**
     * In order to track sapper decals, we need to generate custom profiles for the foxhole state.
     *
     * @param pg group to generate the new profiles from
     * @return a list of profiles, both normal and sapper versions
     */
    private static List<Profile> getSapperProfileList(final ProfileGroup pg) {
        // first grab all the normal profiles
        List<Profile> profileList = pg.getProfiles().stream()
                .filter(it -> it.getId() < Util.sapperProfileOffset)
                .collect(Collectors.toList());
        // now clone them, adding 10 to their ID and setting silhouette to 3
        // But don't clone sapper profiles!
        profileList.addAll(pg.getProfiles().stream()
                .filter(it -> it.getId() < Util.sapperProfileOffset)
                .map(Profile::sapperCopy)
                .collect(Collectors.toList()));
        return profileList;
    }
}
