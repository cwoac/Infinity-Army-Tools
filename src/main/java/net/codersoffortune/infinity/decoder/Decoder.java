package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.Faction;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.Metadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Decoder {
    /***
     * Simple app to convert an army list code to a text output.
     * Basically a test thing.
     */

    private static Database db;
    static String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    public static void main(String[] args) throws IOException {
        /*
        try {
            Class.forName(driver);
            db = Database.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        Metadata m = Metadata.loadMetadata();
        Map<Integer, FactionList> Factions = new HashMap<>();
        for(Faction f: m.getFactions()) {
            int id = f.getID();
            if (id == 901) continue; // NA2 doesn't have a vanilla option
            Factions.put(id, FactionList.loadFaction(String.valueOf(id)));
        }
        System.out.println("Hello");
    }
}
