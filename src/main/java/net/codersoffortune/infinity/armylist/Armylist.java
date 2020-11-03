package net.codersoffortune.infinity.armylist;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Armylist {
    private int faction;
    private String faction_name;
    private String army_name;
    private int max_points;
    private List<CombatGroup> combatGroups = new ArrayList<>();

    static int readVLI(ByteBuffer data) {
        data.mark();
        int result = data.get();
        if (result < 0) {
            data.reset();
            result = data.getShort() & 0xffff & ~(1 << 15L);
        }
        return result;
    }


    public static Armylist fromArmyCode(final String armyCode) {
        byte[] data = Base64.getDecoder().decode(armyCode);
        ByteBuffer dataBuffer = ByteBuffer.wrap(data);
        Armylist result = new Armylist();

        // who are we playing as?
        result.setFaction(readVLI(dataBuffer));

        // Not sure why they embed the faction name as well, but *shrug*
        int faction_length = dataBuffer.get() & 0xffffff;
        byte[] faction_name_data = new byte[faction_length];
        dataBuffer.get(faction_name_data, 0, faction_length);
        result.setFaction_name(new String(faction_name_data, StandardCharsets.UTF_8));

        // Get the army name, if set. Fun fact, the default army name is ' '.
        int army_name_length = dataBuffer.get() & 0xffffff;
        if (army_name_length > 0) {
            byte[] army_name_data = new byte[army_name_length];
            dataBuffer.get(army_name_data, 0, army_name_length);
            result.setArmy_name(new String(army_name_data, StandardCharsets.UTF_8));
        }

        // The next nibble is always 8 afaict. I don't know what it maps to. TODO:: figure out what the 8 is.
        result.setMax_points(dataBuffer.getShort() & 0xfff);

        // how many combat groups?
        int combat_group_count = dataBuffer.get() & 0xff;
        for (int i = 0; i < combat_group_count; i++) {
            result.addCombatGroup(CombatGroup.fromCode(dataBuffer));
        }
        return result;
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

    public int getFaction() {
        return faction;
    }

    public void setFaction(int faction) {
        this.faction = faction;
    }

    public String getFaction_name() {
        return faction_name;
    }

    public void setFaction_name(String faction_name) {
        this.faction_name = faction_name;
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
}
