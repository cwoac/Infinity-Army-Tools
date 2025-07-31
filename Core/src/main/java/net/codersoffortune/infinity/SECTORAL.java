package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public enum SECTORAL {
    // Irritatingly, have to store parent as ID due to java class initialisation sequencing.
    PanO(101, "PanOceania", 1, null, null, null, null, true),
//    Shock(102, "Shock Army of Acontecimento", 1, null, null, null, null, false),
    MO(103, "Military Orders", 1, null, null, null, null, false),
//    NCA(104, "Neoterran Capitaline Army", 1, null, null, null, null, false),
//    Varuna(105, "Varuna Immediate Reaction Division", 1, null, null, null, null, false),
    Svaralheima(106, "Svalarheima's Winter Force", 1, null, null, null, null, false),
    Kestral(107, "Kestral Colonial Force", 1, null,null,null,null, false),

    CodeCapital(199, "Code: Capital", 1, null, null, null, null,false),
    YuJing(201, "Yu Jing", 2, null, null, null, null, true),
    IS(202, "Imperial Service", 2, null, null, null, null, false),
    IA(204, "Invincible Army", 2, null, null, null, null, false),
    WhiteBanner(205, "White Banner", 2, null, null, null, null, false),
    DaebakForce(299, "Daebak Force",2, null, null, null, null, false),
    Ariadna(301, "Ariadna", 3, null, null, null, null, true),
//    Caladonia(302, "Caledonian Highlander Army", 3, null, null, null, null, false),
//    Merovingienne(303, "Force de Reponse Rapide Merovingienne", 3, null, null, null, null, false),
    USAriadna(304, "USAriadna Ranger Force", 3, null, null, null, null, false),
    TAC(305, "Tartary Army Corps", 3, null, null, null, null, false),
    Kosmoflot(306, "Kosmoflot", 3, null, null, null, null, false),
    LEquipeArgent(399, "L'Équipe Argent", 3, null, null, null, null, false),
    Haqq(401, "Haqqislam", 4, null, null, null, null, true),
    Hassassins(402, "Hassassin Bahram", 4, null, null, null, null, false),
    Qapu(403, "Qapu Khalqi", 4, null, null, null, null, false),
    Ramah(404, "Ramah Taskforce", 4, null, null, null, null, false),
    MelekReactionGroup(499, "Melek Reaction Group",4, null, null, null, null, false),
    Nomads(501, "Nomads", 5, null, null, null, null, true),
    Corregidor(502, "Jurisdictional Command of Corregidor", 5, null, null, null, null, false),
    Bakunin(503, "Jurisdictional Command of Bakunin", 5, null, null, null, null, false),
    Tunguska(504, "Jurisdictional Command of Tunguska", 5, null, null, null, null, false),
    ViperaPersuitForce(599, "Vipera Persuit Force", 5, null, null, null, null, false),
    CA(601, "Combined Army", 6, null, null, null, null, true),
    MAF(602, "Morat Aggression Force", 6, null, null, null, null, false),
    Shasvastii(603, "Shasvastii Expeditionary Force", 6, null, null, null, null, false),
    Onyx(604, "Onyx Contact Force", 6, null, null, null, null, false),
    ExrahComissariat(699, "The Exrah Comissariat",6, null, null, null, null, false),
    Aleph(701, "Aleph", 7, null, null, null, null, true),
    SteelPhalanx(702, "Steel Phalanx", 7, null, null, null, null, false),
    OSS(703, "Operations Subsection of the SSS", 7, null, null, null, null, false),
    AnkProgram(799, "Ank Program", 7, null, null, null, null, false),
    Tohaa(801, "Tohaa", 8, null, null, null, null, true),
    DerasKaar(899, "Deras Kaar", 8, null, null, null, null, false),
    //NA2(901, "Non-Aligned Armies", 9)
    Druze(902, "Druze Bayram Security", 9, "b3b2b2", "\"r\": 0.701,\"g\": 0.698,\"b\": 0.698", "\"r\": 0.35,\"g\": 0.35,\"b\": 0.35", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966224/5FA81C66E611EAE59ABC6A05C190ED7DCDF7A631/", true),
//    JSA(903, "Japanese Secessionist Army", 9, "E52520", "\"r\": 0.898,\"g\": 0.145,\"b\": 0.125", "\"r\": 0.45,\"g\": 0.07,\"b\": 0.07", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966763/CF11A8BB790C44353C46ABC4625B6F2230BBABCE/", true),
    Ikari(904, "Ikari Company", 9, "FDC509", "\"r\": 0.992,\"g\": 0.772,\"b\": 0.035", "\"r\": 0.5,\"g\": 0.38,\"b\": 0.02", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966677/DF032843625CE57368370E7DE1C328F254197911/", true),
//    Starco(905, "Starco Free Company of the Star", 9, "f25255", "\"r\": 0.960,\"g\": 0.145,\"b\": 0.333", "\"r\": 0.48,\"g\": 0.08,\"b\": 0.16", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711967999/497FC76AF695AFA2234A7096A92D5B5E6938D6BF/", true),
//    SpiralCorps(906, "Spiral Corps", 9, "B9DB01", "\"r\": 0.725,\"g\": 0.858,\"b\": 0.003", "\"r\": 0.37,\"g\": 0.43,\"b\": 0.001", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711967793/76F49352D56B3120175544A3E2E9656E43F8B70E/", true),
//    FoCo(907, "Foreign Company", 9, "1FB09A", "\"r\": 0.121,\"g\": 0.690,\"b\": 0.603", "\"r\": 0.06,\"g\": 0.30,\"b\": 0.30", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966400/424CB4FEF8EB63B41BA3C9CC819C28293AEB06CF/", true),
    Dahshat(908, "Dahshat Company", 9, "D1D1B2", "\"r\": 0.819,\"g\": 0.819,\"b\": 0.698", "\"r\": 0.41,\"g\": 0.41,\"b\": 0.35", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966042/A616304EACFBBF0F873B68E42A47B5E13DB5351D/", true),
    WiCo(909, "White Company", 9, "537771", "\"r\": 0.325,\"g\": 0.466,\"b\": 0.443", "\"r\": 0.16,\"g\": 0.23,\"b\": 0.23", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711968385/40CDC721303D235A54E4DF01E6F157E6B67A9DE0/", true),
//    JSAContractedBackup(998, "Contracted Back-up", 9, "E52520", "\"r\": 0.898,\"g\": 0.145,\"b\": 0.125", "\"r\": 0.45,\"g\": 0.07,\"b\": 0.07", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966763/CF11A8BB790C44353C46ABC4625B6F2230BBABCE/", true),
    ContractedBackup(999, "Contracted Back-up", 9, "b3b2b2", "\"r\": 0.701,\"g\": 0.698,\"b\": 0.698", "\"r\": 0.35,\"g\": 0.35,\"b\": 0.35", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966224/5FA81C66E611EAE59ABC6A05C190ED7DCDF7A631/", true),
//    Hayabusa(1199, "Hayabusa", 9,"E52520", "\"r\": 0.898,\"g\": 0.145,\"b\": 0.125", "\"r\": 0.45,\"g\": 0.07,\"b\": 0.07", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966763/CF11A8BB790C44353C46ABC4625B6F2230BBABCE/", true),
    O12(1001, "O-12", 10, null, null, null, null, true),
    Starmarda(1002, "Starmada, Bureau Aegis Naval Police Department", 10, null, null, null, null, false),
    Torchlight(1003, "Torchlight Brigade", 10, null, null, null, null, false),
    TeamsGladius(1099, "Teams Gladius", 10, null, null, null, null, false),
    JSA(1101, "Japanese Secessionist Army", 11, null, null, null, null, true),
    Shinden(1102, "Shindenbutai", 11, null, null, null, null, false),
    Oban(1103, "Ōban", 11, null, null, null, null, false);
    //CODEONE(10000, "Code One", 100)
//    C1PanO(10101, "PanOceania (C1)", 10000, "00B0F2", "\"r\": 0.0, \"g\": 0.6901961, \"b\": 0.9490196", "\"r\": 0.0, \"g\": 0.35, \"b\": 0.47", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711967272/04967D96ACBEBB645E5FA5F365BB6D7AEBCC06CC/", true),
//    C1YuJing(10201, "Yu Jing (C1)", 10000, "FF9000", "\"r\": 1.0, \"g\": 0.5647059, \"b\": 0.0", "\"r\": 0.5, \"g\": 0.23, \"b\": 0.0", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711968553/F98F9BDC03079C010366C6239DF43D8CABA5BFCE/", true),
//    C1Ariadna(10301, "Ariadna (C1)", 10000, "007B25", "\"r\": 0.0, \"g\": 0.4823529, \"b\": 0.145097345", "\"r\": 0.0, \"g\": 0.24, \"b\": 0.07", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711965679/4D42A1D4EA0DF0FA8298EBBF3A81426B962D2175/", true),
//    C1Haqqislam(10401, "Haqqislam (C1)", 10000, "E6DA9B", "\"r\": 0.9019608, \"g\": 0.854901969, \"b\": 0.607843161", "\"r\": 0.45, \"g\": 0.42, \"b\": 0.3", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966501/CAF6361A0EB11C8630081AF3D5D25694E03B6E14/", true),
//    C1Nomads(10501, "Nomads (C1)", 10000, "ce181f", "\"r\": 0.807843149, \"g\": 0.09411695, \"b\": 0.1215679", "\"r\": 0.4, \"g\": 0.05, \"b\": 0.06", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711966928/141B310D2B3842D0CA9E79901B72B0EAABB0D5C3/", true),
//    C1CA(10601, "Combined Army (C1)", 10000, "813CAA", "\"r\": 0.5058824, \"g\": 0.235293388, \"b\": 0.6666667", "\"r\": 0.25, \"g\": 0.12, \"b\": 0.33", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711965838/68902014928ED2D5F9C276A1254260D395EE9020/", true),
//    C1Aleph(10701, "Aleph (C1)", 10000, "AFA7BC", "\"r\": 0.6862744, \"g\": 0.654902, \"b\": 0.737255", "\"r\": 0.35, \"g\": 0.33, \"b\": 0.36", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711965557/5E5F36F03124D394BEC60D13FB37FED7F9360E05/", true),
//    C1O12(11001, "O-12 (C1)", 10000, "726C96", "\"r\": 0.305881649, \"g\": 0.27843067, \"b\": 0.4666664", "\"r\": 0.15, \"g\": 0.14, \"b\": 0.23", "https://steamusercontent-a.akamaihd.net/ugc/2036224757711967022/D545AFAB6DEA17368E442BD2B7E256C4E11E4751/", true);
    private final int id;
    private final String name;
    private final int parent;
    private final String fontTint;
    private final String tint;
    private final String secondaryTint;
    private final String hex;

    private final boolean isTopLevel;

    SECTORAL(int id, String name, int parent, String fontTint, String tint, String secondaryTint, String hex, boolean isTopLevel) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.fontTint = fontTint;
        this.tint = tint;
        this.secondaryTint = secondaryTint;
        this.hex = hex;
        this.isTopLevel = isTopLevel;
    }

    public static SECTORAL getByID(final int id) {
        for (SECTORAL s : values()) {
            if (s.id == id) return s;
        }
        return null;
    }

    public static SECTORAL getByName(final String name) {
        for (SECTORAL s : values()) {
            if (s.name.equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
// TODO:: Add SectoralList as a member.

    public FACTION getParent() {
        return FACTION.getByID(parent);
    }

    public String getFontTint() {
        if (fontTint != null) return fontTint;
        return getParent().getFontTint();
    }

    public String getTint() {
        if (tint != null) return tint;
        return getParent().getTint();
    }

    public String getSecondaryTint() {
        if (secondaryTint != null) return secondaryTint;
        return getParent().getSecondaryTint();
    }

    public String getHex() {
        if (hex != null) return hex;
        return getParent().getTint();
    }

    public boolean getIsTopLevel() {
        return isTopLevel;
    }
    /**
     * Loads the faction bag template and prepopulates the faction specific parts
     *
     * @return A string containing the bag with a single %s remaining for the contents
     */
    public String getTemplate() {
        return String.format(Database.getFactionTemplate(),
                getFontTint(),
                name,
                getTint(),
                "%s", // yes, really.
                getHex()
        );
    }

    /**
     * Which game (N4/C1) is this sectoral associated with?
     * May in the future need to add this as a parameter, but for now we can just go on ID value
     * @return the Game this is for.
     */
    public GAME getGame() {
        if (this.id > 10000) return GAME.CODE_ONE;
        return GAME.N4;
    }

    /**
     * A list of all the sectorals that do not have a parent entity (so faction, NA2 and C1 sectorals)
     */
    public static final Collection<SECTORAL> topLevelSectorals =
            Arrays.stream(SECTORAL.values()).filter(SECTORAL::getIsTopLevel).collect(Collectors.toList());
}
