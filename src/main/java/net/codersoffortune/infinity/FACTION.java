package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

import java.util.Arrays;
import java.util.List;


/**
 * As much as I tried to avoid having a hardcoded faction list, it is unfortunately needed for some of the neater things
 * unless I really work against myself.
 */
public enum FACTION {
    PanOceania(1, "PanOceania", Arrays.asList(101,103,105,106),"00B0F2", "\"r\": 0.0, \"g\": 0.6901961, \"b\": 0.9490196", "http://cloud-3.steamusercontent.com/ugc/1685996137035355524/04967D96ACBEBB645E5FA5F365BB6D7AEBCC06CC/"),
    YuJing(2, "Yu Jing", Arrays.asList(201, 202, 204, 205), "FF9000", "\"r\": 1.0, \"g\": 0.5647059, \"b\": 0.0", "http://cloud-3.steamusercontent.com/ugc/1685996137035358127/F98F9BDC03079C010366C6239DF43D8CABA5BFCE/");

    // TODO:: Finish adding the factions

    private final int id;
    private final String name;
    private final List<Integer> sectorals;
    private final String fontTint;
    private final String tint;
    private final String hex;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSectorals() {
        return sectorals;
    }

    public String getFontTint() {
        return fontTint;
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

    FACTION(int id, String name, List<Integer> sectorals, String fontTint, String tint, String hex) {
        this.id = id;
        this.name = name;
        this.sectorals = sectorals;
        this.fontTint = fontTint;
        this.tint=tint;
        this.hex = hex;
    }
}
