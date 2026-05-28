package net.codersoffortune.infinity.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Due to inconsistencies in the CB Api, different (transmuted) models need to have their name generated differently.
 * This is sufficiently complicated that I've pulled this mess out into its own class.
 */
public class TransmutedNameHelper {

    // Units with symbiont armour: primary profile name = unit name; secondary = armour form
    private static final List<Integer> symbiotArmourUnits = Arrays.asList(
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
            1848,  // switchers gruppa
            10680  // reinf: armand
    );

    // TAG + operator units: primary profile = TAG (unit name), secondary = operator
    private static final List<Integer> tagOperatorUnits = Arrays.asList(
            300,   // anaconda
            383,   // iguana
            1690,  // reinf: anaconda
            1720,  // maximus
            1721,  // reinf: maximus
            1852,  // zeybek
            1904,  // tarkshya
            10300  // reinf: anaconda (alt)
    );

    // Units where every profile has its own self-describing name
    private static final List<Integer> selfDescribingProfileUnits = Arrays.asList(
            1885,  // sartroids: ranters
            1886   // sartroids: puzzlers
    );

    private static final List<Integer> profile1UsesUnit = concat(symbiotArmourUnits, tagOperatorUnits);
    private static final List<Integer> profile1UsesProfile = selfDescribingProfileUnits;
    private static final List<Integer> usesProfile = concat(tagOperatorUnits, selfDescribingProfileUnits);

    @SafeVarargs
    private static <T> List<T> concat(List<T>... lists) {
        List<T> result = new ArrayList<>();
        for (List<T> l : lists) result.addAll(l);
        return Collections.unmodifiableList(result);
    }

    public static String getName(String unitName,
                                  String profileName,
                                  int unit_idx,
                                  boolean primary) {
        if (primary) {
            if (profile1UsesUnit.contains(unit_idx)) return unitName;
            if (profile1UsesProfile.contains(unit_idx)) return profileName;
        } else {
            if (usesProfile.contains(unit_idx)) return profileName;
        }
        return String.format("%s - %s", unitName, profileName);
    }

    private TransmutedNameHelper() { throw new UnsupportedOperationException("Do not instantiate me."); }
}
