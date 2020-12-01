package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * As much as I tried to avoid having a hardcoded faction list, it is unfortunately needed for some of the neater things
 * unless I really work against myself.
 */
public enum FACTION {
    PanOceania(1, "PanOceania", Arrays.asList(SECTORAL.PanO, SECTORAL.MO, SECTORAL.Varuna, SECTORAL.Svaralheima),"00B0F2", "\"r\": 0.0, \"g\": 0.6901961, \"b\": 0.9490196", "http://cloud-3.steamusercontent.com/ugc/1685996137035355524/04967D96ACBEBB645E5FA5F365BB6D7AEBCC06CC/"),
    YuJing(2, "Yu Jing", Arrays.asList(SECTORAL.YuJing, SECTORAL.IS, SECTORAL.IA, SECTORAL.WhiteBanner), "FF9000", "\"r\": 1.0, \"g\": 0.5647059, \"b\": 0.0", "http://cloud-3.steamusercontent.com/ugc/1685996137035358127/F98F9BDC03079C010366C6239DF43D8CABA5BFCE/"),
    Ariadna(3, "Ariadna", Arrays.asList(SECTORAL.Ariadna, SECTORAL.USAriadna, SECTORAL.TAC, SECTORAL.Kosmoflot), "007B25", "\"r\": 0.0, \"g\": 0.4823529, \"b\": 0.145097345", "http://cloud-3.steamusercontent.com/ugc/1685996137035360244/4D42A1D4EA0DF0FA8298EBBF3A81426B962D2175/"),
    Haqqislam(4,"Haqqislam", Arrays.asList(SECTORAL.Haqq, SECTORAL.Hassassins, SECTORAL.Ramah),"E6DA9B", "\"r\": 0.9019608, \"g\": 0.854901969, \"b\": 0.607843161", "http://cloud-3.steamusercontent.com/ugc/1685996137035361819/CAF6361A0EB11C8630081AF3D5D25694E03B6E14/"),
    Nomads(5,"Nomads", Arrays.asList(SECTORAL.Nomads, SECTORAL.Corregidor, SECTORAL.Bakunin, SECTORAL.Tunguska), "ce181f", "\"r\": 0.807843149, \"g\": 0.09411695, \"b\": 0.1215679", "http://cloud-3.steamusercontent.com/ugc/1685996137035363314/141B310D2B3842D0CA9E79901B72B0EAABB0D5C3/"),
    CombinedArmy(6,"Combined Army",Arrays.asList(SECTORAL.CA, SECTORAL.MAF, SECTORAL.Onyx, SECTORAL.Shasvastii), "813CAA", "\"r\": 0.5058824, \"g\": 0.235293388, \"b\": 0.6666667", "http://cloud-3.steamusercontent.com/ugc/1685996137035365049/68902014928ED2D5F9C276A1254260D395EE9020/"),
    Aleph(7, "Aleph",Arrays.asList(SECTORAL.Aleph, SECTORAL.OSS),"AFA7BC", "\"r\": 0.6862744, \"g\": 0.654902, \"b\": 0.737255","http://cloud-3.steamusercontent.com/ugc/1685996137035317962/5E5F36F03124D394BEC60D13FB37FED7F9360E05/"),
    Tohaa(8, "Tohaa",Arrays.asList(SECTORAL.Tohaa), "B9DB01","\"r\": 0.725,\"g\": 0.858,\"b\": 0.003" ,"http://cloud-3.steamusercontent.com/ugc/1685996137035368420/FC12BD8A21823CB5F973B0F148A5915719A006D4/"),
    NA2(9, "Non-Aligned Armies",Arrays.asList(SECTORAL.Druze, SECTORAL.JSA, SECTORAL.Ikari, SECTORAL.Starco, SECTORAL.SpiralCorps, SECTORAL.FoCo, SECTORAL.Dahshat, SECTORAL.WiCo),"808080", "\"r\": 0.5, \"g\": 0.5, \"b\": 0.5", ""),
    O12(10, "O-12",Arrays.asList(SECTORAL.O12, SECTORAL.Starmarda), "726C96", "\"r\": 0.305881649, \"g\": 0.27843067, \"b\": 0.4666664", "http://cloud-3.steamusercontent.com/ugc/1685996137035366882/D545AFAB6DEA17368E442BD2B7E256C4E11E4751/");

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

    public List<SECTORAL> getSectorals() {
        return sectorals;
    }

    public String getFontTint() {
        return fontTint;
    }

    public String getTint() {
        return tint;
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
