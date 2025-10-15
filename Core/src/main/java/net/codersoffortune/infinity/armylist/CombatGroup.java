package net.codersoffortune.infinity.armylist;

import net.codersoffortune.infinity.SECTORAL;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CombatGroup {
    private int group_number;
    private final List<CombatGroupMember> members = new ArrayList<>();
    private SECTORAL sectoral;

    public String getTint() {
        switch( group_number ) {
            case 1:
                return sectoral.getTint();
            case 2:
                return sectoral.getSecondaryTint();
        }
        throw new IllegalArgumentException("N5 only allows for 2 combat groups!");
    }

    public static CombatGroup fromCode(ByteBuffer data, SECTORAL sectoral) {
        CombatGroup result = new CombatGroup();
        // first off, who are we?
        result.setGroup_number(Armylist.readVLI(data));
        // and how big?
        int group_size = Armylist.readVLI(data);
        for (int i = 0; i < group_size; i++) {
            result.addMember(CombatGroupMember.fromCode(data));
        }
        result.setSectoral(sectoral);
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

    public SECTORAL getSectoral() {
        return sectoral;
    }

    public void setSectoral(SECTORAL sectoral) {
        this.sectoral = sectoral;
    }
}
