package net.codersoffortune.infinity.metadata.unit;

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
    private List<ProfileItem> weapons = new ArrayList<>();
    private List<ProfileItem> skills = new ArrayList<>();
    private List<ProfileItem> equipment = new ArrayList<>();
    private Profile profile;
    private String name;

    // chars to ignore : 2,5
    static boolean skipCharacteristic(final int c) {
        // 2 - no cube, 5 = not impetuous
        return !(c == 2 || c == 5);
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
        List<String> chars = getProfile().getChars().stream().filter(CompactedUnit::skipCharacteristic).map(x -> filters.getItem(FilterType.chars, x)).map(FilterItem::getName).collect(Collectors.toList());
        result.append(String.format("[b]%s[/b] ● [AAFFAA]%s\n", type, String.join(" ● ", chars)));

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
