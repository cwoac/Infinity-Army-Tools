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
    WhiteBanner(205, "White Banner", 2);

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
