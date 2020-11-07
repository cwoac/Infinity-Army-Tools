package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProfileItem {
    // Can be a skill, weapon or piece of equipment
    //{"extra":[6],"id":28,"order":3}
    //{"q":1,"id":120,"order":1}
    private int q; // quantity
    private List<Integer> extra;
    private int id;
    private int order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileItem that = (ProfileItem) o;
        return q == that.q &&
                id == that.id &&
                order == that.order &&
                Objects.equals(extra, that.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, extra, id, order);
    }

    /**
     * Build a nicely formatted version, including the extras
     * e.g. Heavy Machine Gun (+1 Burst)
     *
     * @param filters the filters to look up the text from
     * @param type    the type of item this represents (yes, I know it's odd we don't store it in the object)
     * @return A formatted string.
     */
    public String toString(final MappedFactionFilters filters, final FilterType type) {
        StringBuilder result = new StringBuilder();
        result.append(filters.getItem(type, getId()).getName());
        if (getExtra() != null && !getExtra().isEmpty()) {
            List<String> extras = extra.stream().map(x -> filters.getItem(FilterType.extras, x).getName())
                    .collect(Collectors.toList());
            result.append(String.format("(%s)", String.join(", ", extras)));
        }
        return result.toString();
    }

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
