package net.codersoffortune.infinity.metadata.unit;

import java.util.List;

public class StatModifier {
    // {"type":"stat","stat":"cc","q":6}
    private String stat;
    private int q;
    private List<Integer> extra;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public List<Integer> getExtra() {
        return extra;
    }

    public void setExtra(List<Integer> extra) {
        this.extra = extra;
    }
}
