package net.codersoffortune.infinity.metadata.unit;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * POJO to explicitly identify a unit option
 */
public class UnitID {
    private final int sectoral_idx;
    private final int unit_idx;
    private final int group_idx;
    private final int profile_idx;
    private final int option_idx;

    public UnitID(int sectoral_idx, int unit_idx, int group_idx, int profile_idx, int option_idx) {
        this.sectoral_idx = sectoral_idx;
        this.unit_idx = unit_idx;
        this.group_idx = group_idx;
        this.profile_idx = profile_idx;
        this.option_idx = option_idx;
    }

    @Override
    public String toString() {
        return "UnitID{" +
                "s:" + sectoral_idx +
                ";u:" + unit_idx +
                ";g:" + group_idx +
                ";p:" + profile_idx +
                ";o:" + option_idx +
                '}';
    }

    /**
     * Convert a toString representation back into the object.
     * @param input from toString
     */
    public UnitID(String input) {
        Pattern unitPattern = Pattern.compile("s:(?<s>\\d*);u:(?<u>\\d*);g:(?<g>\\d*);p:(?<p>\\d*);o:(?<o>\\d*)}");
        Matcher matcher = unitPattern.matcher(input);
        // TODO:: Proper error checking
        matcher.find();
        sectoral_idx = Integer.parseInt(matcher.group("s"));
        unit_idx = Integer.parseInt(matcher.group("u"));
        group_idx = Integer.parseInt(matcher.group("g"));
        profile_idx = Integer.parseInt(matcher.group("p"));
        option_idx = Integer.parseInt(matcher.group("o"));
    }

    private final static Pattern pattern = Pattern.compile("\\[(?<s>[0-9A-Fa-f]{6})]\\[-]\\[(?<u>[0-9A-Fa-f]{6})]\\[-]\\[(?<g>[0-9A-Fa-f]{6})]\\[-]\\[(?<p>[0-9A-Fa-f]{6})]\\[-]\\[(?<o>[0-9A-Fa-f]{6})]\\[-]");

    public static UnitID decode(final String input) {
        Matcher m = pattern.matcher(input);
        // TODO:: validate
        if (!m.find()) {
            System.out.println("moo");
        }
        return new UnitID(Integer.valueOf(m.group("s"), 16),
                Integer.valueOf(m.group("u"), 16),
                Integer.valueOf(m.group("g"), 16),
                Integer.valueOf(m.group("p"), 16),
                Integer.valueOf(m.group("o"), 16)
        );
    }

    /**
     * Convert to a string suitible for 'hiding' at the bottom of a description block
     *
     * @return encoded version of this UnitID
     */
    public String encode() {
        return String.format("[%06X][-][%06X][-][%06X][-][%06X][-][%06X][-]",
                sectoral_idx,
                unit_idx,
                group_idx,
                profile_idx,
                option_idx
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitID unitID = (UnitID) o;
        return sectoral_idx == unitID.sectoral_idx &&
                unit_idx == unitID.unit_idx &&
                group_idx == unitID.group_idx &&
                profile_idx == unitID.profile_idx &&
                option_idx == unitID.option_idx;
    }

    /**
     * Test whether two UnitIDs are equal ignoring sectoral.
     * @param o the other UnitID to test against
     * @return true iff they match (ignoring sectoral).
     */
    public boolean almost_equals(UnitID o) {
        return unit_idx == o.unit_idx &&
                group_idx == o.group_idx &&
                profile_idx == o.profile_idx &&
                option_idx == o.option_idx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectoral_idx, unit_idx, group_idx, profile_idx, option_idx);
    }

    public int getSectoral_idx() {
        return sectoral_idx;
    }

    public int getUnit_idx() {
        return unit_idx;
    }

    public int getGroup_idx() {
        return group_idx;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public int getOption_idx() {
        return option_idx;
    }
}
