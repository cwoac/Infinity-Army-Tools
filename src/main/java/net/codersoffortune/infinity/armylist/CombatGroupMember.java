package net.codersoffortune.infinity.armylist;

import java.nio.ByteBuffer;

public class CombatGroupMember {
    private int id;
    private int profile;
    private int option;

    public static CombatGroupMember fromCode(ByteBuffer data) {
        CombatGroupMember result = new CombatGroupMember();
        data.get(); // always starts with 00
        result.setId(Armylist.readVLI(data));
        result.setProfile(Armylist.readVLI(data));
        result.setOption(Armylist.readVLI(data));
        data.get(); // always ends with 00
        return result;
    }

    @Override
    public String toString() {
        return "CombatGroupMember{" +
                "id=" + id +
                ", profile=" + profile +
                ", option=" + option +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}
