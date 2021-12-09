package net.codersoffortune.infinity.armylist;

import net.codersoffortune.infinity.GAME;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.Equipment;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.Skill;
import net.codersoffortune.infinity.metadata.Weapon;
import net.codersoffortune.infinity.metadata.unit.CompactedUnit;
import net.codersoffortune.infinity.metadata.unit.ProfileItem;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Armylist {
    private GAME game;
    private String army_code;
    private SECTORAL sectoral;
    private String sectoralName;
    private String army_name;
    private int max_points;
    private final List<CombatGroup> combatGroups = new ArrayList<>();

    static int readVLI(ByteBuffer data) {
        data.mark();
        int result = data.get();
        if (result < 0) {
            data.reset();
            result = data.getShort() & 0xffff & ~(1 << 15L);
        }
        return result;
    }



    public static Armylist fromArmyCode(final String armyCode) throws IllegalArgumentException {
        byte[] data;

        // So sometimes CB urlencodes the army code and sometimes it doesn't. Yay
        try {
            data = Base64.getDecoder().decode(armyCode);
        } catch ( IllegalArgumentException exception) {
            try {
                String decoded = URLDecoder.decode(armyCode, StandardCharsets.UTF_8);
                data = Base64.getDecoder().decode(decoded);
            } catch (Throwable t) {
                throw new IllegalArgumentException("Failed to decode army code");
            }
        }

        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        Armylist result = new Armylist();
        result.setArmy_code(armyCode);
        // who are we playing as?
        result.setSectoral(readVLI(dataBuffer));
        result.setGame(result.getSectoral().getGame());
        // Not sure why they embed the faction name as well, but *shrug*
        int faction_length = readVLI(dataBuffer);
        byte[] faction_name_data = new byte[faction_length];
        dataBuffer.get(faction_name_data, 0, faction_length);

        result.setSectoralName(new String(faction_name_data, StandardCharsets.UTF_8));

        // Get the army name, if set. Fun fact, the default army name is ' '.
        int army_name_length = dataBuffer.get() & 0xffffff;
        if (army_name_length > 0) {
            byte[] army_name_data = new byte[army_name_length];
            dataBuffer.get(army_name_data, 0, army_name_length);
            result.setArmy_name(new String(army_name_data, StandardCharsets.UTF_8));
        }

        result.setMax_points(readVLI(dataBuffer));

        // how many combat groups?
        int combat_group_count = readVLI(dataBuffer);
        for (int i = 0; i < combat_group_count; i++) {
            result.addCombatGroup(CombatGroup.fromCode(dataBuffer, result.getSectoral()));
        }
        return result;
    }

    public void pretty_print() throws IOException {
        Database db = Database.getInstance();
        Metadata metadata = db.getMetadata(game);
        List<Weapon> weapons = metadata.getWeapons();
        List<Skill> skills = metadata.getSkills();
        List<Equipment> equipment = metadata.getEquips();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("CODE: %s\n", army_code));
        sb.append(String.format("%s: %S\n", sectoralName, army_name));
        for (CombatGroup cg : combatGroups) {
            sb.append(String.format("Combat Group %d: %d members\n", cg.getGroup_number(), cg.getGroup_size()));
            for (CombatGroupMember cgm : cg.getMembers()) {
                Optional<Unit> maybeUnit = db.getUnitName(cgm.getId(), getSectoral());
                if (maybeUnit.isEmpty()) continue;
                Unit unit = maybeUnit.get();
                Collection<CompactedUnit> cus = unit.getUnits(cgm.getGroup(), cgm.getOption());
                // TODO:: neaten this. Indents? Streams?
                for (CompactedUnit cu : cus) {
                    sb.append(String.format("%s", cu.getName()));
                    // skills
                    List<String> unit_skills = new ArrayList<>();
                    for (ProfileItem skill : cu.getSkills()) {
                        int s_id = skill.getId();
                        Optional<Skill> s = skills.stream().filter(x -> x.getID() == s_id).findFirst();
                        s.ifPresent(value -> unit_skills.add(value.getName()));
                    }
                    if (!unit_skills.isEmpty()) {
                        sb.append(String.format(" (%s)", String.join(", ", unit_skills)));
                    }
                    // weapons+equipment
                    List<String> unit_things = new ArrayList<>();
                    for (ProfileItem weapon : cu.getWeapons()) {
                        int w_id = weapon.getId();
                        Optional<Weapon> w = weapons.stream().filter(x -> x.getId() == w_id).findFirst();
                        w.ifPresent(value -> unit_things.add(value.getName()));
                    }
                    for (ProfileItem equip : cu.getEquipment()) {
                        int w_id = equip.getId();
                        Optional<Equipment> w = equipment.stream().filter(x -> x.getID() == w_id).findFirst();
                        w.ifPresent(value -> unit_things.add(value.getName()));

                    }
                    if (!unit_things.isEmpty()) {
                        sb.append(String.format(" [%s]\n", String.join(", ", unit_things)));
                    }
                }
            }
        }
        System.out.println(sb);
    }

    /**
     * Create a json representation suitible for use in TTS
     *
     * @return a String!
     */
    public String asJson(final EquivalentModelSet equivalentModelSet, boolean doAddons) throws IOException {
        Database db = Database.getInstance();

        List<String> units = new ArrayList<>();
        for (CombatGroup cg : getCombatGroups()) {
            for (CombatGroupMember cgm : cg.getMembers()) {
                Optional<Unit> maybeUnit = db.getUnitName(cgm.getId(), getSectoral());
                if (maybeUnit.isEmpty()) continue;
                Unit unit = maybeUnit.get();
                units.addAll(unit.getUnitsForTTS(cg, cgm.getGroup(), cgm.getOption(), equivalentModelSet, sectoral, doAddons));
            }
        }

        // now put them in the bag
        String description = String.format("%s - %s", getSectoralName(), getArmy_name());

        String unit_list = String.join(",\n", units);
        return String.format(Database.getBagTemplate(), description, unit_list);
    }

    public void addCombatGroup(final CombatGroup combatGroup) {
        this.combatGroups.add(combatGroup);
    }

    public List<CombatGroup> getCombatGroups() {
        return this.combatGroups;
    }

    public int getCombatGroupCount() {
        return this.combatGroups.size();
    }

    public SECTORAL getSectoral() {
        return sectoral;
    }


    public void setSectoral(int sectoral) throws IllegalArgumentException {
        this.sectoral = SECTORAL.getByID(sectoral);
        if (this.sectoral == null) {
            throw new IllegalArgumentException(String.format("Sectoral %d not found", sectoral));
        }
    }

    public String getSectoralName() {
        return sectoralName;
    }

    public void setSectoralName(String sectoralName) {
        this.sectoralName = sectoralName;
    }

    public String getArmy_name() {
        return army_name;
    }

    public void setArmy_name(String army_name) {
        this.army_name = army_name;
    }

    public int getMax_points() {
        return max_points;
    }

    public void setMax_points(int max_points) {
        this.max_points = max_points;
    }

    public String getArmy_code() {
        return army_code;
    }

    public void setArmy_code(String army_code) {
        this.army_code = army_code;
    }

    public GAME getGame() {
        return game;
    }

    public void setGame(GAME game) {
        this.game = game;
    }
}
