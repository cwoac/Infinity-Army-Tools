package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * As much as I tried to avoid having a hardcoded faction list, it is unfortunately needed for some of the neater things
 * unless I really work against myself.
 */
public enum FACTION {
    PanOceania(1, "PanOceania", Arrays.asList(SECTORAL.PanO, SECTORAL.MO, SECTORAL.Varuna, SECTORAL.Svaralheima),"00B0F2", "\"r\": 0.0, \"g\": 0.6901961, \"b\": 0.9490196", "http://cloud-3.steamusercontent.com/ugc/1685996137035355524/04967D96ACBEBB645E5FA5F365BB6D7AEBCC06CC/"),
    YuJing(2, "Yu Jing", Arrays.asList(SECTORAL.YuJing, SECTORAL.IS, SECTORAL.IA, SECTORAL.WhiteBanner), "FF9000", "\"r\": 1.0, \"g\": 0.5647059, \"b\": 0.0", "http://cloud-3.steamusercontent.com/ugc/1685996137035358127/F98F9BDC03079C010366C6239DF43D8CABA5BFCE/"),
    Ariadna(3, "Ariadna", Arrays.asList(SECTORAL.Ariadna, SECTORAL.USAriadna, SECTORAL.TAC, SECTORAL.Kosmoflot), "007B25", "\"r\": 0.0, \"g\": 0.4823529, \"b\": 0.145097345", "http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/"),
    Haqqislam(4,"Haqqislam", Arrays.asList(SECTORAL.Haqq, SECTORAL.Hassassins, SECTORAL.Ramah),"E6DA9B", "\"r\": 0.9019608, \"g\": 0.854901969, \"b\": 0.607843161", "http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/"),
    Nomads(5,"Nomads", Arrays.asList(SECTORAL.Nomads, SECTORAL.Corregidor, SECTORAL.Bakunin, SECTORAL.Tunguska), "ce181f", "\"r\": 0.807843149, \"g\": 0.09411695, \"b\": 0.1215679", "http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/"),
    CombinedArmy(6,"Combined Army",Arrays.asList(SECTORAL.CA, SECTORAL.MAF, SECTORAL.Onyx, SECTORAL.Shasvastii), "813CAA", "\"r\": 0.5058824, \"g\": 0.235293388, \"b\": 0.6666667", "http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/"),
    Aleph(7, "Aleph",Arrays.asList(SECTORAL.Aleph, SECTORAL.OSS),"AFA7BC", "\"r\": 0.6862744, \"g\": 0.654902, \"b\": 0.737255","http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/"),
    // TODO:: Handle NA2 colours properly.
    NA2(9, "Non-Aligned Armies",Arrays.asList(SECTORAL.Druze, SECTORAL.JSA, SECTORAL.Ikari, SECTORAL.Starco, SECTORAL.SpiralCorps, SECTORAL.FoCo, SECTORAL.Dahshat, SECTORAL.WiCo),"f25255", "\"r\": 0.32700938, \"g\": 0.46554023, \"b\": 0.4444081", "http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/"),
    O12(10, "O-12",Arrays.asList(SECTORAL.O12, SECTORAL.Starmarda), "726C96", "\"r\": 0.305881649, \"g\": 0.27843067, \"b\": 0.4666664", "http://cloud-3.steamusercontent.com/ugc/929303676573612409/FF751A206976EBD2B75A29196FDCBB590ECB0A43/");


    // TODO:: Finish adding the factions

    private final int id;
    private final String name;
    private final List<SECTORAL> sectorals;
    private final String fontTint;
    private final String tint;
    private final String hex;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSectoralIdxs() {
        return sectorals.stream().map(SECTORAL::getId).collect(Collectors.toList());
    }

    public List<SECTORAL> getSectorals() {
        return sectorals;
    }

    public String getTint() {
        return tint;
    }

    public static FACTION getFactionForSectoral(int sectoral){
        for(FACTION faction : FACTION.values()) {
            if (faction.sectorals.contains(sectoral))
                return faction;
        }
        return null;
    }

    public static FACTION getByID(final int id) {
        for( FACTION f: values()) {
            if( f.id == id) return f;
        }
        return null;
    }

    /**
     * Loads the faction bag template and prepopulates the faction specific parts
     *
     * @return A string containing the bag with a single %s remaining for the contents
     */
    public String getTemplate() {
        return String.format(Database.getFactionTemplate(),
                fontTint,
                name,
                tint,
                "%s", // yes, really.
                hex
        );
    }

    FACTION(int id, String name, List<SECTORAL> sectorals, String fontTint, String tint, String hex) {
        this.id = id;
        this.name = name;
        this.sectorals = sectorals;
        this.fontTint = fontTint;
        this.tint=tint;
        this.hex = hex;
    }
}
