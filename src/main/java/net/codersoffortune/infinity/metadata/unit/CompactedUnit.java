package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FilterItem;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.Metadata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A compacted unit is the final profile got from taking a specific profile group / option combination on a unit.
 */
public class CompactedUnit {
    private final List<ProfileItem> weapons = new ArrayList<>();
    private final List<ProfileItem> skills = new ArrayList<>();
    private final List<ProfileItem> equipment = new ArrayList<>();
    private Profile profile;
    private String name;

    // chars to ignore : 2,5
    static boolean skipCharacteristic(final int c) {
        // 2 - no cube, 5 = not impetuous
        return !(c == 2 || c == 5);
    }

    /**
     * Checks whether to suppress a hidden information skill
     *
     * @param c skill number to check
     * @return true iff this skill is good to print
     */
    static boolean skipSkills(final int c) {
        // 119 - lt
        // 69 strategos l1
        // 70 strategos l2
        // 26 chain of command
        return !(c == 119 || c == 70 || c == 69 || c == 26);
    }

    public String getTTSSilhouette() throws IOException {
        return Armylist.getResourceFileAsString(String.format("templates/S%d.json", getProfile().getS()));
        //return String.format("S%d",getProfile().getS());
    }

    public String getTTSNickName(final MappedFactionFilters filters) {
        //TODO:: implement a filter to select the 'interesting' options

        return String.format("[FFFFFF]%s[-] %s",
                getName(),
                filters.getItem(FilterType.weapons, getWeapons().get(0).getId()).getName()
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
        List<String> weapons = getWeapons().stream().map(x -> x.toString(filters, FilterType.weapons)).collect(Collectors.toList());
        weapons.addAll(getProfile().getWeapons().stream().map(x -> x.toString(filters, FilterType.weapons)).collect(Collectors.toList()));
        result.append(String.format("%s[-]\\n", String.join(" ● ", weapons)));
        result.append("[ddffdd][sub]---------Equipment--------\\n[/sub]\\n");
        List<String> equipment = getEquipment().stream().map(x -> x.toString(filters, FilterType.equip)).collect(Collectors.toList());
        equipment.addAll(getProfile().getEquip().stream().map(x -> x.toString(filters, FilterType.equip)).collect(Collectors.toList()));
        result.append(String.format("%s[-]\\n", String.join(" ● ", equipment)));
        result.append("[ddddff][sub]----------Skills ---------\\n[/sub]\\n");
        List<String> skills = getSkills().stream().filter(x -> skipSkills(x.getId())).map(x -> x.toString(filters, FilterType.skills)).collect(Collectors.toList());
        skills.addAll(getProfile().getSkills().stream().filter(x -> skipSkills(x.getId())).map(x -> x.toString(filters, FilterType.skills)).collect(Collectors.toList()));
        result.append(String.format("%s[-]\\n[000013][-]", String.join(" ● ", skills)));
        return result.toString();
    }


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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
