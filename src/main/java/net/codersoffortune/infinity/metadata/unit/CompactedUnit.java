package net.codersoffortune.infinity.metadata.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A compacted unit is the final profile got from taking a specific profile group / option combination on a unit.
 */
public class CompactedUnit {
    private final List<ProfileItem> weapons = new ArrayList<>();
    private final List<ProfileItem> skills = new ArrayList<>();
    private final List<ProfileItem> equipment = new ArrayList<>();
    private final List<ProfileItem> peripheral = new ArrayList<>();
    private final Set<ProfileItem> publicSkills = new HashSet<>();



    private final int unit_idx;
    private final int group_idx;
    private final int option_idx;
    private final int profile_idx;
    private final Profile profile;
    private final ProfileGroup group;
    private final ProfileOption option;
    public List<ProfileItem> getWeapons() {
        return weapons;
    }

    public List<ProfileItem> getSkills() {
        return skills;
    }


    private final String name;



    public CompactedUnit(int unit_idx, ProfileGroup group, Profile profile, ProfileOption option) {
        this.unit_idx = unit_idx;
        this.group = group;
        this.profile = profile;
        this.option = option;
        profile_idx = profile.getId();
        group_idx = group.getId();
        option_idx = option.getId();
        name = option.getName();
        weapons.addAll(option.getWeapons().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        skills.addAll(option.getSkills().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        equipment.addAll(option.getEquip().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        peripheral.addAll(option.getPeripheral().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        weapons.addAll(profile.getWeapons().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        skills.addAll(profile.getSkills().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        calcPublicSkills();
        equipment.addAll(profile.getEquip().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        peripheral.addAll(profile.getPeripheral().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));

    }

    /**
     * Returns whether two CompactedUnits are the same based on public information
     *
     * Note we _don't_ compare peripherals as they are a separate item in the game.
     * @param other CompactedUnit to test against
     * @return true if their public details are the same
     */
    public boolean publicallyEqual( CompactedUnit other ) {
        return other.getPublicSkills().equals(getPublicSkills()) &&
                other.getWeapons().equals(weapons) &&
                other.getEquipment().equals(equipment) &&
                other.getPublicChars().equals(getPublicChars());
    }

    public int getGroup_idx() {
        return group_idx;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public int getOption_idx() {
        return option_idx;
    }

    // chars to ignore : 2,5
    static boolean skipCharacteristic(final int c) {
        // 2 - no cube, 5 = not impetuous
        return !(c == 2 || c == 5);
    }

    // 119 - lt
    // 69 strategos l1
    // 70 strategos l2
    // 26 chain of command
    // 120 FT:Core
    // 121 FT:Haris
    // 181 FT:Duo
    private final static List<Integer> privateSkills =
            Arrays.asList(119,69,70,26,120,121,181);
    /**
     * Checks whether to suppress a hidden information skill
     * Also strip fireteam info. TODO:: Workout if this is really what we want to do.
     * @param c skill number to check
     * @return true iff this skill is good to print
     */
    static boolean skipSkills(final int c) {
        return !privateSkills.contains(c);
    }

    private void calcPublicSkills() {
        skills.stream().filter(x -> skipSkills(x.getId())).forEach(publicSkills::add);
    }

    public Collection<ProfileItem> getPublicSkills() {
        return publicSkills;
    }

    public List<Integer> getPublicChars() {
        return profile.getChars().stream().filter(CompactedUnit::skipCharacteristic).collect(Collectors.toList());
    }


    public String getName() {
        return name;
    }


    public List<ProfileItem> getEquipment() {
        return equipment;
    }

    public Profile getProfile() {
        return profile;
    }


    public int getUnit_idx() {
        return unit_idx;
    }

    public List<ProfileItem> getPeripheral() {
        return peripheral;
    }

    public ProfileGroup getGroup() {
        return group;
    }

    public ProfileOption getOption() {
        return option;
    }

    /**
     * Calculate if there are any weapons added to this profile compared to the base (option10)
     * @return OptionalOf the profileItem with the lowest index (so most important)
     */
    public Optional<ProfileItem> getInterestingWeaponMaybe() {
        if (option_idx == 1) return Optional.empty();

        Set<ProfileItem> theirWeapons = new HashSet<>(group.getOptions().get(1) .getWeapons());
        Set<ProfileItem> ourWeapons = new HashSet<>(weapons);
        ourWeapons.removeAll(theirWeapons);
        return ourWeapons.stream().sorted(Comparator.comparing(ProfileItem::getOrder)).findFirst();
    }

}
