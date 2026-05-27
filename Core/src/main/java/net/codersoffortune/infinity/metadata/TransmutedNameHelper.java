package net.codersoffortune.infinity.metadata;

import java.util.Arrays;
import java.util.List;

/**
 * Due to inconsistencies in the CB Api, different (transmuted) models need to have their name generated differently.
 * This is sufficiently complicated that I've pulled this mess out into its own class.
 */
public class TransmutedNameHelper {

    private static final List<Integer> profile1UsesUnit = Arrays.asList(
            680, // armand
            300  // anaconda
    );

    private static final List<Integer> profile1UsesProfile = Arrays.asList(
            1885,
            1886
    );

    private static final List<Integer> usesProfile = Arrays.asList(
            1885,
            1886
    );

    public String getName( String unitName,
                           String profileName,
                           int unit_idx,
                           boolean primary) {
        if (primary) {
            // first profile is often different.
            if (profile1UsesUnit.contains(unit_idx)) return unitName;
            if (profile1UsesProfile.contains(unit_idx)) return profileName;

        } else {
            if (usesProfile.contains(unit_idx)) return profileName;
        }
        return String.format("%s - %s", unitName, profileName);
    }



    private TransmutedNameHelper() {throw new UnsupportedOperationException("Do not instantiate me.");}
}
