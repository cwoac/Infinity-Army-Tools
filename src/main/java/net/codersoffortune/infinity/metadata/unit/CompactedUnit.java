package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FilterItem;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.Metadata;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A compacted unit is the final profile got from taking a specific profile group / option combination on a unit.
 */
public class CompactedUnit {
    private final List<ProfileItem> weapons = new ArrayList<>();
    private final List<ProfileItem> skills = new ArrayList<>();
    private final List<ProfileItem> equipment = new ArrayList<>();
    private final List<ProfileItem> peripheral = new ArrayList<>();

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
        equipment.addAll(profile.getEquip().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));
        peripheral.addAll(profile.getPeripheral().stream().filter(ProfileItem::isNotNull).collect(Collectors.toList()));

    }

    public int getGroup_idx() {
        return group_idx;
    }

    public int getProfile_idx() {
        return profile.getId();
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

    public List<ProfileItem> getPublicSkills() {
        return skills.stream().filter(x -> skipSkills(x.getId())).collect(Collectors.toList());
    }

    public List<Integer> getPublicChars() {
        return profile.getChars().stream().filter(CompactedUnit::skipCharacteristic).collect(Collectors.toList());
    }

    public String getTTSSilhouette() throws IOException {
        // Important numbers:
        // skill 29 - Camouflage
        // skill 28 - Mimitism
        // extra 6,7 -3, -6

        Optional<ProfileItem> maybeMimetism = skills.stream().filter(x -> x.getId() == 28).findFirst();
        Optional<ProfileItem> maybeCamo = skills.stream().filter(x -> x.getId() == 29).findFirst();
        int silhouette = getProfile().getS();
        String template = Armylist.getResourceFileAsString(String.format("templates/S%d.json", silhouette));
        String diffuse = "http://cloud-3.steamusercontent.com/ugc/859478426278214079/BFA0CAEAE34C30E5A87F6FB2595C59417DCFFE27/";

        String stype;

        if (maybeCamo.isPresent()) {
            if (maybeMimetism.isPresent()) {
                int mod = 0;
                List<Integer> extras = maybeMimetism.get().getExtra();
                if (!extras.isEmpty()) {
                    switch (extras.get(0)) {
                        case 6:
                            mod = -3;
                            break;
                        case 7:
                            mod = -6;
                            break;
                        default:
                            throw new InvalidObjectException(String.format("Unknown mimitism type %s found", extras.get(0)));
                    }
                }
                stype = String.format("Camouflage (%d) S%d", mod, silhouette);
            } else {
                // no mimitism
                stype = String.format("Camouflage S%d", silhouette);
            }
            // edge case of single use camo
            if (!maybeCamo.get().getExtra().isEmpty() && maybeCamo.get().getExtra().get(0) == 43) {

                return String.format("%s,\n%s",
                        String.format(template, 2, stype, diffuse),
                        String.format(template, 3, String.format("Silhouette %d", silhouette), ""));
            }
        } else {
            diffuse = "";
            stype = String.format("Silhouette %d", silhouette);
        }

        return String.format(template, 2, stype, diffuse);
    }



    public String getTTSNickName(final MappedFactionFilters filters) {
        //TODO:: implement a filter to select the 'interesting' options
        String interesting = "";
        List<ProfileItem> publicSKills = getPublicSkills();
        if (publicSKills.isEmpty()) {
            interesting = filters.getItem(FilterType.weapons, weapons.get(0).getId()).getName();
        } else {
            interesting = filters.getItem(FilterType.skills, publicSKills.get(0).getId()).getName();
        }
        return String.format("[FFFFFF]%s[-] %s",
                getName(),
                interesting
        );
    }

    /**
     * Build the description for use in TTS
     *
     * @return a string version of the description
     */
    public String getTTSDescription(final MappedFactionFilters filters) throws IOException, SQLException {
        // Example
        //[b]REM[/b] ● [AAFFAA]Regular[-] ● Hackable \n
        //[sub]---------Attributes-------\n
        //[/sub]\n
        //[b]MOV[/b]: 6-6\n
        //[b]CC[/b]: 13\n
        //[b]BS[/b]: 8\n
        //[b]PH[/b]: 11\n
        //[b]WIP[/b]: 13\n
        //[b]ARM[/b]: 0\n
        //[b]BTS[/b]: 3\n
        //[B]STR[/B]: 1\n
        //[b]S[/b]: 3\n
        //[ffdddd][sub]----------Weapons---------\n
        //[/sub]\n
        //Flash Pulse ● PARA CC Weapon (-3) [-]\n
        //[ddffdd][sub]---------Equipment--------\n
        //[/sub]\n
        //Repeater [-]\n
        //[ddddff][sub]----------Skills----------\n
        //[/sub]\n
        //Remote Presence ● Courage ● Mimetism (-3) ● Immunity (Shock) [-]\n
        //[000013][-]"

        StringBuilder result = new StringBuilder();
        Database db = Database.getInstance();
        Metadata m = db.getMetadata();
        //[b]REM[/b] ● [AAFFAA]Regular[-] ● Hackable \n
        String type = filters.getItem(FilterType.type, getProfile().getType()).getName();
        List<String> chars = getProfile().getChars().stream().filter(CompactedUnit::skipCharacteristic)
                .map(x -> filters.getItem(FilterType.chars, x)).map(FilterItem::getName).collect(Collectors.toList());
        result.append(String.format("[b]%s[/b] ● [AAFFAA]%s[-]\\n", type, String.join(" ● ", chars)));
        result.append("[sub]---------Attributes-------\\n[/sub]\\n");
        // TODO:: Allow selecting inches.
        result.append(String.format("[b]MOV[/b]: %s\\n",
                getProfile().getMove().stream().map(String::valueOf).collect(Collectors.joining("-"))));
        result.append(String.format("[b]CC[/b]: %d\\n", getProfile().getCc()));
        result.append(String.format("[b]BS[/b]: %d\\n", getProfile().getBs()));
        result.append(String.format("[b]PH[/b]: %d\\n", getProfile().getPh()));
        result.append(String.format("[b]WIP[/b]: %d\\n", getProfile().getWip()));
        result.append(String.format("[b]ARM[/b]: %d\\n", getProfile().getArm()));
        result.append(String.format("[b]BTS[/b]: %d\\n", getProfile().getBts()));
        if (getProfile().isStr()) {
            result.append(String.format("[B]STR[/B]: %d\\n", getProfile().getW()));
        } else {
            result.append(String.format("[B]W[/B]: %d\\n", getProfile().getW()));
        }
        result.append(String.format("[B]S[/B]: %d\\n", getProfile().getS()));
        // kit
        result.append("[ffdddd][sub]----------Weapons---------\\n[/sub]\\n");
        List<String> weapon_names = weapons.stream().map(x -> x.toString(filters, FilterType.weapons)).collect(Collectors.toList());
        result.append(String.format("%s[-]\\n", String.join(" ● ", weapon_names)));
        result.append("[ddffdd][sub]---------Equipment--------\\n[/sub]\\n");
        List<String> equipment_names = equipment.stream().map(x -> x.toString(filters, FilterType.equip)).collect(Collectors.toList());
        result.append(String.format("%s[-]\\n", String.join(" ● ", equipment_names)));
        result.append("[ddddff][sub]----------Skills ---------\\n[/sub]\\n");
        List<String> skill_names = getPublicSkills().stream().map(x -> x.toString(filters, FilterType.skills)).collect(Collectors.toList());

        //TODO:: implement the reference code.
        result.append(String.format("%s[-]\\n[000013][-]", String.join(" ● ", skill_names)));
        return result.toString();
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
}
