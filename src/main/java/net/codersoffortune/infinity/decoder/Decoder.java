package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.Metadata;

import java.io.IOException;

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
        FactionList f = FactionList.loadFaction("909");
        System.out.println("Hello");
    }
}
