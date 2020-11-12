package net.codersoffortune.infinity;

public enum SECTORAL {
    // Irritatingly, have to store parent as ID due to java class initialisation sequencing.
    PanO(101, "PanOceania", 1),
    MO(103, "Military Orders", 1),
    Varuna(105, "Varuna Immediate Reaction Division", 1),
    Svaralheima(106, "Svalarheima's Winter Force", 1),
    YuJing(201, "Yu Jing", 2),
    IS(202, "Imperial Service", 2),
    IA(204, "Invincible Army", 2),
    WhiteBanner(205, "White Banner", 2),
    Ariadna(301, "Ariadna", 3),
    USAriadna(304, "USAriadna Ranger Force", 3),
    TAC(305, "Tartary Army Corps", 3),
    Kosmoflot(306, "Kosmoflot", 3),
    Haqq(401, "Haqqislam", 4),
    Hassassins(402, "Hassassin Bahram", 4),
    Ramah(404, "Ramah Taskforce", 4),
    Nomads(501, "Nomads", 5),
    Corregidor(502, "Jurisdictional Command of Corregidor", 5),
    Bakunin(503, "Jurisdictional Command of Bakunin", 5),
    Tunguska(504, "Jurisdictional Command of Tunguska", 5),
    CA(601, "Combined Army", 6),
    MAF(602, "Morat Aggression Force", 6),
    Shasvastii(603, "Shasvastii Expeditionary Force", 6),
    Onyx(604, "Onyx Contact Force", 6),
    Aleph(701, "Aleph", 7),
    OSS(703, "Operations Subsection of the SSS", 7),
    //NA2(901, "Non-Aligned Armies", 9)
    Druze(902, "Druze Bayram Security", 9),
    JSA(903, "Japanese Secessionist Army", 9),
    Ikari(904, "Ikari Company", 9),
    Starco(905, "Starco. Free Company of the Star", 9),
    SpiralCorps(906, "Spiral Corps", 9),
    FoCo(907, "Foreign Company", 9),
    Dahshat(908, "Dahshat Company", 9),
    WiCo(909, "White Company", 9),
    O12(1001, "O-12", 10),
    Starmarda(1002, "Starmada, Bureau Aegis Naval Police Department", 10);

    private final int id;
    private final String name;
    private final int parent;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FACTION getParent() {
        return FACTION.getByID(parent);
    }

    public static SECTORAL getByID(final int id) {
        for( SECTORAL s: values()) {
            if( s.id == id) return s;
        }
        return null;
    }
// TODO:: Add SectoralList as a member.



    SECTORAL(int id, String name, int parent) {
        this.id = id;
        this.name =name;
        this.parent = parent;
    }

}
