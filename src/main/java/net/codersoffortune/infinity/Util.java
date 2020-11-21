package net.codersoffortune.infinity;

import java.util.Arrays;
import java.util.List;

/**
 * Private class for all things hardcoded, utility and otherwise "don't quite have a home".
 *
 * The name is an in-joke.
 */
public class Util {


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

    /* Very few skills are 'visible'. The main use is to enforce different models when weapons + kit would
     * suggest they are the same (i.e. hacking related stuff mostly).
     *    255 - remdriver
     */
    public static final List<Integer> visibleSkills = Arrays.asList(255);

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
    // 120 FT:Core
    // 121 FT:Haris
    // 125 FT: ent
    // 181 FT:Duo
    public final static List<Integer> privateSkills =
            Arrays.asList(119,69,70,26,120,121,125,181);

    /**
     * Figure out the correct name to put along the top of the model.
     * This is _almost_ always the unit name.
     * When it isn't, it is _usually_ the profile name.
     * Except when it is both.
     * @param unitId ID of the unit
     * @param profileName the name of the current profile
     * @param unitName the name of the current unit
     * @return The correct name for the model
     */
    public static String getName(int unitId, final String profileName, final String unitName) {
        if( nameEdgeCases.contains(unitId) ) return profileName;
        if( unitId == 155 ) return String.format("%s (%s)", unitName, profileName); // Su Jian
        return unitName;
    }

    /**
     * An interesting weapon is one that, if present, should be in the users title line.
     */
    public static final List<Integer> interestingWeapons = Arrays.asList(
        1, // AK Kanone
        50, // Adhesive launcher
        70, // Assault Pistol
        14, // Blitzen
        18, // chain rifle
        80, // chain colt
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
            238 // deactivator
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


    // Distances
    private static boolean inInches = true;

    /**
     * Convert a value from CM to 'CB Inches' - so, 5 per 2.
     * @param cm distance
     * @return the distance in inches.
     */
    public static int toInches(final int cm) {
        return cm / 5 * 2;
    }


    public static int formatDistance(final int in) {
        if (inInches) return toInches(in);
        return in;
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
