package net.codersoffortune.infinity.metadata;

import java.util.Arrays;
import java.util.List;

/**
 * Due to inconsistencies in the CB Api, different (transmuted) models need to have their name generated differently.
 * This is sufficiently complicated that I've pulled this mess out into its own class.
 */
public class TransmutedNameHelper {

    private static final List<Integer> profile1UsesUnit = Arrays.asList(
            300,   // anaconda
            383,   // iguana
            647,   // sakiel
            648,   // gao-rael
            649,   // gao-tarsos
            650,   // ectros
            657,   // neema saatar
            660,   // nikoul
            661,   // kotail
            680,   // armand
            746,   // kosuil
            790,   // sukeul
            801,   // taqeul
            1127,  // ratnik
            1305,  // kiel-saan
            1306,  // draal
            1309,  // kiiutan
            1552,  // rasail
            1553,  // kerail
            1554,  // jaan staar
            1559,  // gorgos
            1584,  // chernobog
            1667,  // reinf: sakiel
            1668,  // reinf: rasail
            1669,  // reinf: ectros
            1671,  // reinf: kosuil
            1672,  // reinf: draal
            1677,  // reinf: neema
            1690,  // reinf: anaconda
            1720,  // maximus
            1721,  // reinf: maximus
            1848,  // switchers gruppa
            1852,  // zeybek
            1904,  // tarkshya
            10300, // reinf: anaconda (alt)
            10680  // reinf: armand (alt)
    );

    private static final List<Integer> profile1UsesProfile = Arrays.asList(
            1885,  // sartroid ranters
            1886  // sartroid puzzlers
    );

    private static final List<Integer> usesProfile = Arrays.asList(
            300,   // anaconda operator
            383,   // iguana operator
            1690,  // reinf: anaconda operator
            1720,  // operator maximus
            1721,  // reinf: operator maximus
            1852,  // zeybek operator
            1885,  // sartroid ranters
            1886,  // sartroid puzzlers
            1904,  // tarkshya operator
            10300  // reinf: anaconda operator (alt)
    );

    public static String getName( String unitName,
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
