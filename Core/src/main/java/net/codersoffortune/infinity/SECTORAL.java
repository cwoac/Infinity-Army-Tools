package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

public enum SECTORAL {
    // Irritatingly, have to store parent as ID due to java class initialisation sequencing.
    PanO(101, "PanOceania", 1, null, null, null, null),
    Shock(102, "Shock Army of Acontecimento", 1, null, null, null, null),
    MO(103, "Military Orders", 1, null, null, null, null),
    NCA(104, "Neoterran Capitaline Army", 1, null, null, null, null),
    Varuna(105, "Varuna Immediate Reaction Division", 1, null, null, null, null),
    Svaralheima(106, "Svalarheima's Winter Force", 1, null, null, null, null),
    YuJing(201, "Yu Jing", 2, null, null, null, null),
    IS(202, "Imperial Service", 2, null, null, null, null),
    IA(204, "Invincible Army", 2, null, null, null, null),
    WhiteBanner(205, "White Banner", 2, null, null, null, null),
    Ariadna(301, "Ariadna", 3, null, null, null, null),
    Caladonia(302, "Caledonian Highlander Army", 3, null, null, null, null),
    Merovingienne(303, "Force de Reponse Rapide Merovingienne", 3, null, null, null, null),
    USAriadna(304, "USAriadna Ranger Force", 3, null, null, null, null),
    TAC(305, "Tartary Army Corps", 3, null, null, null, null),
    Kosmoflot(306, "Kosmoflot", 3, null, null, null, null),
    Haqq(401, "Haqqislam", 4, null, null, null, null),
    Hassassins(402, "Hassassin Bahram", 4, null, null, null, null),
    Qapu(403, "Qapu Khalqi", 4, null, null, null, null),
    Ramah(404, "Ramah Taskforce", 4, null, null, null, null),
    Nomads(501, "Nomads", 5, null, null, null, null),
    Corregidor(502, "Jurisdictional Command of Corregidor", 5, null, null, null, null),
    Bakunin(503, "Jurisdictional Command of Bakunin", 5, null, null, null, null),
    Tunguska(504, "Jurisdictional Command of Tunguska", 5, null, null, null, null),
    CA(601, "Combined Army", 6, null, null, null, null),
    MAF(602, "Morat Aggression Force", 6, null, null, null, null),
    Shasvastii(603, "Shasvastii Expeditionary Force", 6, null, null, null, null),
    Onyx(604, "Onyx Contact Force", 6, null, null, null, null),
    Aleph(701, "Aleph", 7, null, null, null, null),
    SteelPhalanx(702, "Steel Phalanx", 7, null, null, null, null),
    OSS(703, "Operations Subsection of the SSS", 7, null, null, null, null),
    Tohaa(801, "Tohaa", 8, null, null, null, null),
    //NA2(901, "Non-Aligned Armies", 9)
    Druze(902, "Druze Bayram Security", 9, "b3b2b2", "\"r\": 0.701,\"g\": 0.698,\"b\": 0.698", "\"r\": 0.35,\"g\": 0.35,\"b\": 0.35", "http://cloud-3.steamusercontent.com/ugc/1685996137035396568/5FA81C66E611EAE59ABC6A05C190ED7DCDF7A631/"),
    JSA(903, "Japanese Secessionist Army", 9, "E52520", "\"r\": 0.898,\"g\": 0.145,\"b\": 0.125", "\"r\": 0.45,\"g\": 0.07,\"b\": 0.07", "http://cloud-3.steamusercontent.com/ugc/1685996137035402246/CF11A8BB790C44353C46ABC4625B6F2230BBABCE/"),
    Ikari(904, "Ikari Company", 9, "FDC509", "\"r\": 0.992,\"g\": 0.772,\"b\": 0.035", "\"r\": 0.5,\"g\": 0.38,\"b\": 0.02", "http://cloud-3.steamusercontent.com/ugc/1685996137035400697/DF032843625CE57368370E7DE1C328F254197911/"),
    Starco(905, "Starco Free Company of the Star", 9, "f25255", "\"r\": 0.960,\"g\": 0.145,\"b\": 0.333", "\"r\": 0.48,\"g\": 0.08,\"b\": 0.16", "http://cloud-3.steamusercontent.com/ugc/1685996137035403622/497FC76AF695AFA2234A7096A92D5B5E6938D6BF/"),
    SpiralCorps(906, "Spiral Corps", 9, "B9DB01", "\"r\": 0.725,\"g\": 0.858,\"b\": 0.003", "\"r\": 0.37,\"g\": 0.43,\"b\": 0.001", "http://cloud-3.steamusercontent.com/ugc/1685996137035406102/76F49352D56B3120175544A3E2E9656E43F8B70E/"),
    FoCo(907, "Foreign Company", 9, "1FB09A", "\"r\": 0.121,\"g\": 0.690,\"b\": 0.603", "\"r\": 0.06,\"g\": 0.30,\"b\": 0.30", "http://cloud-3.steamusercontent.com/ugc/1685996137035398673/424CB4FEF8EB63B41BA3C9CC819C28293AEB06CF/"),
    Dahshat(908, "Dahshat Company", 9, "D1D1B2", "\"r\": 0.819,\"g\": 0.819,\"b\": 0.698", "\"r\": 0.41,\"g\": 0.41,\"b\": 0.35", "http://cloud-3.steamusercontent.com/ugc/1685996137035395355/A616304EACFBBF0F873B68E42A47B5E13DB5351D/"),
    WiCo(909, "White Company", 9, "537771", "\"r\": 0.325,\"g\": 0.466,\"b\": 0.443", "\"r\": 0.16,\"g\": 0.23,\"b\": 0.23", "http://cloud-3.steamusercontent.com/ugc/1685996137035407416/40CDC721303D235A54E4DF01E6F157E6B67A9DE0/"),
    O12(1001, "O-12", 10, null, null, null, null),
    Starmarda(1002, "Starmada, Bureau Aegis Naval Police Department", 10, null, null, null, null),
    //CODEONE(10000, "Code One", 100)
    C1PanO(10101, "PanOceania (C1)", 10000, "00B0F2", "\"r\": 0.0, \"g\": 0.6901961, \"b\": 0.9490196", "\"r\": 0.0, \"g\": 0.35, \"b\": 0.47", "http://cloud-3.steamusercontent.com/ugc/1685996137035355524/04967D96ACBEBB645E5FA5F365BB6D7AEBCC06CC/"),
    C1YuJing(10201, "Yu Jing (C1)", 10000, "FF9000", "\"r\": 1.0, \"g\": 0.5647059, \"b\": 0.0", "\"r\": 0.5, \"g\": 0.23, \"b\": 0.0", "http://cloud-3.steamusercontent.com/ugc/1685996137035358127/F98F9BDC03079C010366C6239DF43D8CABA5BFCE/"),
    C1Ariadna(10301, "Ariadna", 10000, "007B25", "\"r\": 0.0, \"g\": 0.4823529, \"b\": 0.145097345", "\"r\": 0.0, \"g\": 0.24, \"b\": 0.07", "http://cloud-3.steamusercontent.com/ugc/1685996137035360244/4D42A1D4EA0DF0FA8298EBBF3A81426B962D2175/"),
    C1Nomads(10501, "Nomads", 10000, "ce181f", "\"r\": 0.807843149, \"g\": 0.09411695, \"b\": 0.1215679", "\"r\": 0.4, \"g\": 0.05, \"b\": 0.06", "http://cloud-3.steamusercontent.com/ugc/1685996137035363314/141B310D2B3842D0CA9E79901B72B0EAABB0D5C3/"),
    C1CA(10601, "Combined Army (C1)", 10000, "813CAA", "\"r\": 0.5058824, \"g\": 0.235293388, \"b\": 0.6666667", "\"r\": 0.25, \"g\": 0.12, \"b\": 0.33", "http://cloud-3.steamusercontent.com/ugc/1685996137035365049/68902014928ED2D5F9C276A1254260D395EE9020/"),
    C1O12(11001, "O-12 (C1)", 10000, "726C96", "\"r\": 0.305881649, \"g\": 0.27843067, \"b\": 0.4666664", "\"r\": 0.15, \"g\": 0.14, \"b\": 0.23", "http://cloud-3.steamusercontent.com/ugc/1685996137035366882/D545AFAB6DEA17368E442BD2B7E256C4E11E4751/");
    private final int id;
    private final String name;
    private final int parent;
    private final String fontTint;
    private final String tint;
    private final String secondaryTint;
    private final String hex;

    SECTORAL(int id, String name, int parent, String fontTint, String tint, String secondaryTint, String hex) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.fontTint = fontTint;
        this.tint = tint;
        this.secondaryTint = secondaryTint;
        this.hex = hex;
    }

    public static SECTORAL getByID(final int id) {
        for (SECTORAL s : values()) {
            if (s.id == id) return s;
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
}
