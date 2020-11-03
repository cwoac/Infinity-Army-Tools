package net.codersoffortune.infinity.metadata;

import java.util.List;

public class ProfileItem {
    // Can be a skill, weapon or piece of equipment
    //{"extra":[6],"id":28,"order":3}
    //{"q":1,"id":120,"order":1}
    private int q; // TODO:: WHAT DOES THIS MEAN?
    private List<Integer> extra;
    private int id;
    private int order;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getExtra() {
        return extra;
    }

    public void setExtra(List<Integer> extra) {
        this.extra = extra;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }
}
