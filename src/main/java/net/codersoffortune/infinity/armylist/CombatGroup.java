package net.codersoffortune.infinity.armylist;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CombatGroup {
    private int group_number;
    private final List<CombatGroupMember> members = new ArrayList<>();

    public static String getTint(int group_number) {
        switch( group_number ) {
            case 1:
                return "\"r\": 0.854901969,\n    \"g\": 0.09803913,\n    \"b\": 0.0941175446";
            case 2:
                return "\"r\": 0.121568627,\n    \"g\": 0.52941176,\n    \"b\": 1.0";
        }
        throw new IllegalArgumentException("N4 only allows for 2 combat groups!");
    }

    public static CombatGroup fromCode(ByteBuffer data) {
        CombatGroup result = new CombatGroup();
        // first off, who are we?
        result.setGroup_number(Armylist.readVLI(data));
        // and how big?
        int group_size = Armylist.readVLI(data);
        for (int i = 0; i < group_size; i++) {
            result.addMember(CombatGroupMember.fromCode(data));
        }
        return result;
    }


    public void addMember(CombatGroupMember member) {
        this.members.add(member);
    }

    public int getGroup_number() {
        return group_number;
    }

    public void setGroup_number(int group_number) {
        this.group_number = group_number;
    }

    public int getGroup_size() {
        return members.size();
    }

    public List<CombatGroupMember> getMembers() {
        return members;
    }
}
