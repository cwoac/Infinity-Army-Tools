package net.codersoffortune.infinity.metadata.unit;

import java.util.Objects;

public class ProfileInclude {
    // {"q":1,"group":2,"option":1}
    private int q;
    private int group;
    private int option;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileInclude that = (ProfileInclude) o;
        return q == that.q &&
                group == that.group &&
                option == that.option;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, group, option);
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }
}
