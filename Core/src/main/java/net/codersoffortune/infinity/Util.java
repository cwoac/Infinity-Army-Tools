package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.unit.CompactedUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.Map.entry;

/**
 * Private class for all things hardcoded, utility and otherwise "don't quite have a home".
 *
 * The name is an in-joke.
 */
public class Util {
    private static final Logger logger = LogManager.getLogger(Util.class);


    // Visible properties

    /* Most primary weapons are visible, so a list of the ones which tend not to be
        61, 199 AP mines
        78 AP+DA CC
        72 AP+Shock CC
        6 AP CC
        5 CC
        147 Chest Mines
        20 CrazyKoala
        174 CyberMines
        17 D-Charges
        11 DA CC
        96 Dropbears
        7 EM CC
        47 EM Grenades
        197 EM Mines
        25 EMitter
        46 eclipse grenades
        8 exp cc
        72 flash pulse
        44 grenades
        150 madtraps
        176 mine dispenser
        9 mono CC
        62 mono mines
        65 nanopulser
        71 para cc
        203-205 pheroware
        154 pitcher
        73 sepsitor
        114 sepsitor plus
        10 shock cc
        196 shock mines
        45 smoke grenades
        153 t2 cc
        13 viral cc
        63 viral mines
        149 vorpal cc
        182 wild parrot
        175 zapper
     */
    public static final List<Integer> invisibleWeapons = Arrays.asList(61, 199, 78, 72, 6, 5, 147, 20, 174, 17, 11, 96, 7, 47, 197, 25, 46, 8, 72, 44, 150, 176, 9, 62, 65, 71, 203, 204, 205, 154, 73, 114, 10, 196, 45, 153, 13, 63, 149, 182, 175);

    /* Visible equipment on the other hand, is much rarer.
           100 hacking device
           101 hd+
           106 medkit
           107 motorcycle
           145 KHD
           182 Evo HD
           205 AI motorcycle
           247 gizmokit
         */
    public static final List<Integer> visibleEquipment = Arrays.asList(100, 101, 106, 107, 145, 182, 205, 247);

    /* So, to avoid duplicating skill based equipment, here is a seperate list.
        TODO:: can this replace visibleEquipment because of skills?
     */
    public static final List<Integer> modelledEquipment = Arrays.asList(107,205);

    /* Very few skills are 'visible'. The main use is to enforce different models when weapons + kit would
     * suggest they are the same (i.e. hacking related stuff mostly).
     *    255 - remdriver
     */
    public static final List<Integer> visibleSkills = Collections.singletonList(255);

    /** Subtly different (I think) from visibleSkills are physicalModelSkills. These are used by CB in descriptors
     * of the model, and either tend to be reflected in incidental details (bags) or poses (e.g. the hacker hands)
     *
     * 49 engineer
     * 53 doctor
     * 59 forward observer
     * 64 paramedic
     * 1000 hacker
     */
    public static final List<Integer> physicalModelSkills = Arrays.asList(49, 53,59, 64, 1000);

    // Quite a few weapons look the same.
    public static final Set<Set<Integer>> visiblyEquivalentWeapons = Set.of(
            // Sniper Rifles
            Set.of(12,72,98,99,116,117,129,144,156),
            // Shotguns
            Set.of(17,18,65,66,78,79,139,140,160,161),
            // Marksman rifles
            Set.of(9,71,81,91,92,125,142),
            // combi
            Set.of(19,28,70,151),
            // rifles
            Set.of(11,22,121,143,155)
    );

    // Names
    public static final List<Integer> nameEdgeCases = Arrays.asList(
            20, // Joan
            42, // Joan
            613, // achilles
            749, // achilles
            53, // Sheskiin
            1503, // Sheskiin
            143, // TZE
            165 // TZE
    );

    // 119 - lt
    // 69 strategos l1
    // 70 strategos l2
    // 26 chain of command
    // 207 counter intelligence
    // 120 FT:Core
    // 121 FT:Haris
    // 125 FT:Ent
    // 181 FT:Duo
    public final static List<Integer> privateSkills =
            Arrays.asList(119,69,70,26,120,121,125,181, 207);

    public final static List<Integer> hiddenSkills =
            Arrays.asList(119,69,70,26, 207);

    public final static List<Integer> bikes =
            Arrays.asList(107, 205);

    /**
     * Figure out the correct name to put along the top of the model.
     * This is _almost_ always the unit name.
     * When it isn't, it is _usually_ the profile name.
     * Except when it is both. Or if it depends on the profile ID
     * Basically this is a hot mess of edge cases
     * @param cu the compacted unit to name
     * @param profileName the name of the current profile
     * @param optionName the name of the current option
     * @return The correct name for the model
     */
    public static String getName(final CompactedUnit cu, String profileName, final String optionName) {
        // First the so awkward I have to basically hard code the value ones.

        // seed soldiers
        if (cu.getUnit_idx() == 512 || cu.getUnit_idx() == 513) {
            return cu.getProfile_idx()==1 ? profileName : cu.getName();
        }

        // Su-Jian
        if( cu.getUnit_idx() == 155 ) return String.format("%s (%s)", cu.getName(), profileName);

        // 112 motorized
        if( cu.getUnit_idx() == 1284 && cu.getProfile_idx() > 1) {
            profileName = "DISMOUNTED";
        }

        // edge cases that explicitly require just the profile name
        if( nameEdgeCases.contains(cu.getUnit_idx()) ) return profileName;

        String result = optionName;
        if ((cu.getProfile_idx() % Util.sapperProfileOffset) > 1) {
            result = result + " (" + profileName + ")";
        }

        // sappers
        if (cu.isFoxhole()) {
            return String.format("%s (foxhole)", result);
        }

        return result;
    }

    /**
     * An interesting weapon is one that, if present, should be in the users title line.
     */
    public static final List<Integer> interestingWeapons = Arrays.asList(
        1, // AK Kanone
        12, // BSG
        15, // HRMC
        28, // LSG
        44, // HSG
        50, // Adhesive launcher
        70, // Assault Pistol
        14, // Blitzen
        18, // chain rifle
        80, // chain colt
        81, // VulkSG
        201, // EM grenade launcher
        82, // Flammenspeer
        194, // gren launcher
        57, // heavy flamer
        56, // light flamer
        58, // missile launcher
        65, // nanopulser
        68, // panzerf
        77, // SMG
        195, // smoke GL
        154, // Pitcher
        176, // mine dispenser
        179, // viral bow
        193, // bow
        197, // EM mines
        88, // LRL
        96, // Drop Bears
        20, // koalas
        23, // E/Marat
        196, // shock mines
        174, // cyber mines
        175 // zapper
    );

    /**
     * A weapon can also be interesting if it has an interesting mod
     */
    public static final List<Integer> interestingWeaponMods = Arrays.asList(
      4, // +1D
      271, // +1BS (unsure if any weapons have this though)
      30, //shock
      8, // +1B
      39 // viral
    );

    /**
     * Some equipment, even if it is on CB's list, just isn't that interesting.
     *
     */
    public static final List<Integer> boringEquipment = Arrays.asList(
            104, // holoprojector
            24, // holomask
            238, // deactivator
            169 //tinbot
    );

    public static final List<Integer> interestingSkills = Arrays.asList(
      49, // engineer
      53, // doctor
      59, // forward observer
      61, // TR
      189, // Specialist
      64, // paramedic
      67, // sixth sense
      1000 // hacker
    );

    public static final Map<Integer, String> SkillAbbreviations = Map.of(
            19, "MA1",
            20, "MA2",
            21, "MA3",
            22, "MA4",
            23, "MA5",
            59, "FO",
            61, "TR",
            189, "Specialist",
            204, "SpecOps"

    );

    public static final Map<Integer, String> EquipAbbreviations = Map.of(
            100, "HD",
            101, "HD+",
            145, "KHD",
            182, "Evo HD",
            114, "MSV1",
            115, "MSV2",
            116, "MSV3"
    );

    //TODO:: Load this from a file?
    //	Map<String, String> map = Map.ofEntries(
    //	  new AbstractMap.SimpleEntry<String, String>("name", "John"),

    public static final Map<Integer, String> WeaponAbbreviations = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(1, "AK-Kanone"),
            new AbstractMap.SimpleEntry<>(2, "HMG"),
            new AbstractMap.SimpleEntry<>(3, "AP HMG"),
            new AbstractMap.SimpleEntry<>(4, "Multi HMG"),
            new AbstractMap.SimpleEntry<>(26, "BSG"),
            new AbstractMap.SimpleEntry<>(28, "LSG"),
            new AbstractMap.SimpleEntry<>(29, "HSG"),
            new AbstractMap.SimpleEntry<>(31, "Rfl"),
            new AbstractMap.SimpleEntry<>(32, "AP Rfl"),
            new AbstractMap.SimpleEntry<>(33, "Combi"),
            new AbstractMap.SimpleEntry<>(34, "Sniper"),
            new AbstractMap.SimpleEntry<>(36, "Multi Sniper"),
            new AbstractMap.SimpleEntry<>(38, "T2 Sniper"),
            new AbstractMap.SimpleEntry<>(39, "Viral Sniper"),
            new AbstractMap.SimpleEntry<>(40, "Plasma Rfl"),
            new AbstractMap.SimpleEntry<>(41, "Multi Rfl"),
            new AbstractMap.SimpleEntry<>(44, "Grn"),
            new AbstractMap.SimpleEntry<>(45, "Smoke Grn"),
            new AbstractMap.SimpleEntry<>(46, "Eclipse Grn"),
            new AbstractMap.SimpleEntry<>(47, "E/M Grn"),
            new AbstractMap.SimpleEntry<>(50, "ADHL"),
            new AbstractMap.SimpleEntry<>(56, "LFT"),
            new AbstractMap.SimpleEntry<>(57, "HFT"),
            new AbstractMap.SimpleEntry<>(58, "ML"),
            new AbstractMap.SimpleEntry<>(62, "Mono Mines"),
            new AbstractMap.SimpleEntry<>(65, "Nano"),
            new AbstractMap.SimpleEntry<>(68, "Pnzrf"),
            new AbstractMap.SimpleEntry<>(69, "Pst"),
            new AbstractMap.SimpleEntry<>(70, "Assault Pst"),
            new AbstractMap.SimpleEntry<>(77, "SMG"),
            new AbstractMap.SimpleEntry<>(81, "VSG"),
            new AbstractMap.SimpleEntry<>(84, "MMR"),
            new AbstractMap.SimpleEntry<>(88, "LRL"),
            new AbstractMap.SimpleEntry<>(89, "HRL"),
            new AbstractMap.SimpleEntry<>(92, "Stun Pst"),
            new AbstractMap.SimpleEntry<>(98, "Breaker Pst"),
            new AbstractMap.SimpleEntry<>(100, "AP Hvy Pst"),
            new AbstractMap.SimpleEntry<>(103, "Breaker Combi"),
            new AbstractMap.SimpleEntry<>(110, "K1 SR"),
            new AbstractMap.SimpleEntry<>(155, "Shock MMR"),
            new AbstractMap.SimpleEntry<>(156, "Viral Pst"),
            new AbstractMap.SimpleEntry<>(169, "Multi MMR"),
            new AbstractMap.SimpleEntry<>(179, "Viral Bow"),
            new AbstractMap.SimpleEntry<>(181, "K1 MMR"),
            new AbstractMap.SimpleEntry<>(189, "Multi Pst"),
            new AbstractMap.SimpleEntry<>(193, "Bow"),
            new AbstractMap.SimpleEntry<>(194, "GL"),
            new AbstractMap.SimpleEntry<>(195, "Smoke GL"),
            new AbstractMap.SimpleEntry<>(201, "E/M GL")
    );

    public static final Map<FilterType, Map<Integer, String>> abbreviations = Map.of(
            FilterType.equip, EquipAbbreviations,
            FilterType.weapons, WeaponAbbreviations,
            FilterType.skills, SkillAbbreviations
    );

    /**
     * Return whether this unit is one which will have a seed state
     * @param unit_idx the unit to check
     * @return true iff it is a seed embryo unit
     */
    public static boolean hasSeedState(int unit_idx) {
        return unit_idx==512 || unit_idx == 513;
    }

//    public static final List<Integer> boringSkills = Arrays.asList(
//            161, // forward deployment
//            242 // triangulated fire
//            131, // discover
//            138, // bioimmune
//            19,20,21,22,23, // MAL1-5
//            24, // beserk
//            25, // booty
//            28, // mimetism
//            156, // marksman
//            29, //camo
//            33, // parachute
//            162, // immune
//            35, //c jump
//            164, // stealth
//            39, // nbw
//            40, //dodge
//            47, // infiltrate
//            55, // metach
//            56, // minelayer
//            58, // terrain
//            60,
//
//
//
//    );

    public static final int sapperProfileOffset = 10;


    // Distances
    private static boolean inInches = true;
    private static final DecimalFormat distanceFormatter = new DecimalFormat("#.#");

    /**
     * Convert a value from CM to 'CB Inches' - so, 5 per 2.
     * @param cm distance
     * @return the distance in inches.
     */
    public static float toInches(final float cm) {
        return Math.round(cm / 2.5f);
    }

    public static String formatDistance(float in) {
        return formatDistance(in, false);
    }

    public static String formatDistance(float in, boolean addUnit) {
        float distance;
        String unit;
        if (inInches) {
            distance = toInches(in);
            unit = "\\\"";
        } else {
            distance = in;
            unit = "cm";
        }
        if( addUnit)
            return String.format("%s%s", distanceFormatter.format(distance), unit);
        return distanceFormatter.format(distance);
    }

    /**
     * Switch the units used for distances globally.
     * @param inInches true if you want to use inches.
     */
    public static void setInInches(boolean inInches) {
        Util.inInches = inInches;
    }





    private Util() { throw new UnsupportedOperationException("Do not instantiate me."); }
}
