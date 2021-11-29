package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * As much as I tried to avoid having a hardcoded faction list, it is unfortunately needed for some of the neater things
 * unless I really work against myself.
 */
public enum FACTION {
    PanOceania(1, "PanOceania", false,SECTORAL.PanO, Arrays.asList(SECTORAL.PanO, SECTORAL.MO, SECTORAL.Varuna, SECTORAL.Svaralheima, SECTORAL.Shock, SECTORAL.NCA), "00B0F2", "\"r\": 0.0, \"g\": 0.6901961, \"b\": 0.9490196", "\"r\": 0.0, \"g\": 0.35, \"b\": 0.47", "http://cloud-3.steamusercontent.com/ugc/1685996137035355524/04967D96ACBEBB645E5FA5F365BB6D7AEBCC06CC/"),
    YuJing(2, "Yu Jing", false, SECTORAL.YuJing, Arrays.asList(SECTORAL.YuJing, SECTORAL.IS, SECTORAL.IA, SECTORAL.WhiteBanner), "FF9000", "\"r\": 1.0, \"g\": 0.5647059, \"b\": 0.0", "\"r\": 0.5, \"g\": 0.23, \"b\": 0.0", "http://cloud-3.steamusercontent.com/ugc/1685996137035358127/F98F9BDC03079C010366C6239DF43D8CABA5BFCE/"),
    Ariadna(3, "Ariadna", false, SECTORAL.Ariadna, Arrays.asList(SECTORAL.Ariadna, SECTORAL.Caladonia, SECTORAL.USAriadna, SECTORAL.TAC, SECTORAL.Kosmoflot, SECTORAL.Merovingienne), "007B25", "\"r\": 0.0, \"g\": 0.4823529, \"b\": 0.145097345", "\"r\": 0.0, \"g\": 0.24, \"b\": 0.07", "http://cloud-3.steamusercontent.com/ugc/1685996137035360244/4D42A1D4EA0DF0FA8298EBBF3A81426B962D2175/"),
    Haqqislam(4, "Haqqislam", false, SECTORAL.Haqq, Arrays.asList(SECTORAL.Haqq, SECTORAL.Hassassins, SECTORAL.Qapu,SECTORAL.Ramah), "E6DA9B", "\"r\": 0.9019608, \"g\": 0.854901969, \"b\": 0.607843161", "\"r\": 0.45, \"g\": 0.42, \"b\": 0.3", "http://cloud-3.steamusercontent.com/ugc/1685996137035361819/CAF6361A0EB11C8630081AF3D5D25694E03B6E14/"),
    Nomads(5, "Nomads", false, SECTORAL.Nomads, Arrays.asList(SECTORAL.Nomads, SECTORAL.Corregidor, SECTORAL.Bakunin, SECTORAL.Tunguska), "ce181f", "\"r\": 0.807843149, \"g\": 0.09411695, \"b\": 0.1215679", "\"r\": 0.4, \"g\": 0.05, \"b\": 0.06", "http://cloud-3.steamusercontent.com/ugc/1685996137035363314/141B310D2B3842D0CA9E79901B72B0EAABB0D5C3/"),
    CombinedArmy(6, "Combined Army", false, SECTORAL.CA, Arrays.asList(SECTORAL.CA, SECTORAL.MAF, SECTORAL.Onyx, SECTORAL.Shasvastii), "813CAA", "\"r\": 0.5058824, \"g\": 0.235293388, \"b\": 0.6666667", "\"r\": 0.25, \"g\": 0.12, \"b\": 0.33", "http://cloud-3.steamusercontent.com/ugc/1685996137035365049/68902014928ED2D5F9C276A1254260D395EE9020/"),
    Aleph(7, "Aleph", false, SECTORAL.Aleph, Arrays.asList(SECTORAL.Aleph, SECTORAL.SteelPhalanx,SECTORAL.OSS), "AFA7BC", "\"r\": 0.6862744, \"g\": 0.654902, \"b\": 0.737255", "\"r\": 0.35, \"g\": 0.33, \"b\": 0.36", "http://cloud-3.steamusercontent.com/ugc/1685996137035317962/5E5F36F03124D394BEC60D13FB37FED7F9360E05/"),
    Tohaa(8, "Tohaa", false, SECTORAL.Tohaa, Arrays.asList(SECTORAL.Tohaa), "B9DB01", "\"r\": 0.725,\"g\": 0.858,\"b\": 0.003", "\"r\": 0.36,\"g\": 0.43,\"b\": 0.0", "http://cloud-3.steamusercontent.com/ugc/1685996137035368420/FC12BD8A21823CB5F973B0F148A5915719A006D4/"),
    NA2(9, "Non-Aligned Armies", true, null, Arrays.asList(SECTORAL.Druze, SECTORAL.JSA, SECTORAL.Ikari, SECTORAL.Starco, SECTORAL.SpiralCorps, SECTORAL.FoCo, SECTORAL.Dahshat, SECTORAL.WiCo), "808080", "\"r\": 0.5, \"g\": 0.5, \"b\": 0.5", "\"r\": 0.25, \"g\": 0.25, \"b\": 0.25", ""),
    O12(10, "O-12", false, SECTORAL.O12, Arrays.asList(SECTORAL.O12, SECTORAL.Starmarda), "726C96", "\"r\": 0.305881649, \"g\": 0.27843067, \"b\": 0.4666664", "\"r\": 0.15, \"g\": 0.14, \"b\": 0.23", "http://cloud-3.steamusercontent.com/ugc/1685996137035366882/D545AFAB6DEA17368E442BD2B7E256C4E11E4751/"),
    CodeOne(10000, "Code One", true, null, Arrays.asList(SECTORAL.C1PanO, SECTORAL.C1YuJing, SECTORAL.C1CA, SECTORAL.C1O12, SECTORAL.C1Ariadna, SECTORAL.C1Nomads), "", "", "", "");

    private final int id;
    private final String name;
    private final boolean containerOnly;
    private final List<SECTORAL> sectorals;
    private final SECTORAL armySectoral;
    private final String fontTint;
    private final String tint;
    private final String secondaryTint;
    private final String hex;

    FACTION(int id, String name, boolean containerOnly, SECTORAL armySectoral, List<SECTORAL> sectorals, String fontTint, String tint, String secondaryTint, String hex) {
        this.id = id;
        this.name = name;
        this.containerOnly = containerOnly;
        this.armySectoral = armySectoral;
        this.sectorals = sectorals;
        this.fontTint = fontTint;
        this.tint = tint;
        this.secondaryTint = secondaryTint;
        this.hex = hex;
    }

    public static FACTION getByID(final int id) {
        for (FACTION f : values()) {
            if (f.id == id) return f;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SECTORAL getArmySectoral() { return armySectoral; }

    public List<SECTORAL> getSectorals() {
        return sectorals;
    }

    public String getFontTint() {
        return fontTint;
    }

    public String getTint() {
        return tint;
    }

    public String getSecondaryTint() {
        return secondaryTint;
    }

    /**
     * Some factions are only containers for other sectorals and not playable sectorals in their own right.
     * @return true iff this faction is a container faction (NA2, C1)
     */
    public boolean isContainerOnly() {
        return containerOnly;
    }

    /**
     * Handy list version of all the non-container factions.
     * Useful for the GUI, mostly.
     */
    public static Collection<FACTION> armyFactions =
            Arrays.stream(FACTION.values()).filter(x->!x.containerOnly).collect(Collectors.toList());

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
}
