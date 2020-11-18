package net.codersoffortune.infinity;

import java.util.Arrays;
import java.util.List;

/**
 * Private class for all things hardcoded, utility and otherwise "don't quite have a home".
 *
 * The name is an in-joke.
 */
public class Util {

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
