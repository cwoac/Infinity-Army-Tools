package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.Util;
import net.codersoffortune.infinity.collection.VisibleItem;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;

import java.io.InvalidObjectException;
import java.util.ArrayList;
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

    private final Unit unit;
    private final int unit_idx;
    private final int group_idx;
    private final int option_idx;
    private final int profile_idx;
    private final Profile profile;
    private final ProfileGroup group;
    private final ProfileOption option;
    private final String name;

    private final boolean hasPrivateInformation;
    private final boolean dismounted;

    public CompactedUnit(Unit unit, ProfileGroup group, Profile profile, ProfileOption option) {
        this(unit, group, profile, option, false);
    }

    public CompactedUnit(Unit unit, ProfileGroup group, Profile profile, ProfileOption option, boolean isDismounted) {
        this.unit = unit;
        this.unit_idx = unit.getID();
        this.group = group;
        this.profile = profile;
        this.option = option;
        this.dismounted = isDismounted;
        profile_idx = profile.getId();
        group_idx = group.getId();
        option_idx = option.getId();
        name = option.getName();

        // Seed embryos don't have the capabilities of their option. Delightfully.
        if (!Util.hasSeedState(unit_idx) || profile_idx > 1) {
            weapons.addAll(option.getWeapons().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
            skills.addAll(option.getSkills().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
            equipment.addAll(option.getEquip().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
            peripheral.addAll(option.getPeripheral().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        }
        weapons.addAll(profile.getWeapons().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        skills.addAll(profile.getSkills().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        calcPublicSkills();
        hasPrivateInformation = skills.stream().anyMatch(x -> Util.hiddenSkills.contains(x.getId()));
        equipment.addAll(profile.getEquip().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        peripheral.addAll(profile.getPeripheral().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));

        weapons.sort(Comparator.comparing(ProfileItem::getOrder));
        equipment.sort(Comparator.comparing(ProfileItem::getOrder));
        skills.sort(Comparator.comparing(ProfileItem::getOrder));
        // Not sure if anyone _has_ more than one peripheral, but just in case.
        peripheral.sort(Comparator.comparing(ProfileItem::getOrder));

    }

    @Override
    public String toString() {
        return "CompactedUnit{" +
                "u:" + unit_idx +
                ", g:" + group_idx +
                ", o:" + option_idx +
                ", p:" + profile_idx +
                '}';
    }

    // chars to ignore : 2,5
    static boolean skipCharacteristic(int c) {
        // 2 - no cube, 5 = not impetuous
        return !(c == 2 || c == 5);
    }

    public boolean isDismounted() { return this.dismounted; }

    /**
     * Checks whether to suppress a hidden information skill
     * Also strip fireteam info. TODO:: Workout if this is really what we want to do.
     *
     * @param c skill number to check
     * @return true iff this skill is good to print
     */
    static boolean skipSkills(int c) {
        return !Util.privateSkills.contains(c);
    }

    public List<ProfileItem> getWeapons() {
        return weapons;
    }

    public List<ProfileItem> getSkills() {
        return skills;
    }

    public boolean publicallyEqual(CompactedUnit other) {
        return other.getPublicSkills().equals(getPublicSkills()) &&
                other.getWeapons().equals(weapons) &&
                other.getEquipment().equals(equipment) &&
                other.getPublicChars().equals(getPublicChars());
    }

    public boolean hasNoPrivateInformation() {
        return !hasPrivateInformation;
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


    public Collection<ProfileItem> getPublicSkills() {
        return publicSkills;
    }

    public List<Integer> getPublicChars() {
        return profile.getChars().stream().filter(CompactedUnit::skipCharacteristic).collect(Collectors.toList());
    }

    private void calcPublicSkills() {
        getSkills().stream().filter(x -> skipSkills(x.getId())).forEach(publicSkills::add);
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

    public Unit getUnit() {
        return unit;
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

    private boolean isInterestingWeapon(final ProfileItem weapon) {
        return Util.interestingWeapons.contains(weapon.getId()) || (weapon.getExtra() != null &&
                weapon.getExtra().stream().anyMatch(Util.interestingWeaponMods::contains));

    }

    public List<ProfileItem> getInterestingWeapons(final MappedFactionFilters filters) {
        List<ProfileItem> result = new ArrayList<>();
        if (weapons.isEmpty()) return result;

        ProfileItem firstWeapon;

        // First get the lowest indexed BS weapon
        Optional<ProfileItem> maybeBSWeapon = weapons.stream()
                .filter(w -> filters.getItem(FilterType.weapons, w.getId()).getType().equalsIgnoreCase("BS"))
                .min(Comparator.comparing(ProfileItem::getOrder));

        if (maybeBSWeapon.isPresent()) {
            firstWeapon = maybeBSWeapon.get();

            if (!isInterestingWeapon(firstWeapon)) {
                // Make sure we don't double add
                result.add(firstWeapon);
            }
        }
        // Now add anything on the interesting weapons list
        weapons.stream()
                .filter(this::isInterestingWeapon)
                .sorted(Comparator.comparing(ProfileItem::getOrder))
                .forEach(result::add);

        return result;
    }

    public Collection<VisibleItem> getVisibleItems() {
        Collection<VisibleItem> result = new ArrayList<>();
        result.addAll(equipment.stream()
                .filter(it -> Util.modelledEquipment.contains(it.getId()))
                .map(it -> new VisibleItem(it, FilterType.equip))
                .collect(Collectors.toList()));
        result.addAll(skills.stream()
                .filter(it -> Util.physicalModelSkills.contains(it.getId()))
                .map(it -> new VisibleItem(it, FilterType.skills))
                .collect(Collectors.toList()));
        result.addAll(weapons.stream()
                .filter(it -> !Util.invisibleWeapons.contains(it.getId()))
                .map(it -> new VisibleItem(it, FilterType.weapons))
                .collect(Collectors.toList()));
        return result;
    }

    public PrintableUnit getPrintableUnit(SECTORAL sectoral) throws InvalidObjectException {
        return new PrintableUnit(this, sectoral);
    }
}
