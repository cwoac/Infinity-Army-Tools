package net.codersoffortune.infinity.armylist;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CombatGroup {
    private int group_number;
    private List<CombatGroupMember> members = new ArrayList<>();

    public static CombatGroup fromCode(ByteBuffer data) {
        CombatGroup result = new CombatGroup();
        // first off, who are we?
        result.setGroup_number(data.get() & 0xff);
        // and how big?
        int group_size = data.get() & 0xff;

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
