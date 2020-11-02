package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.db.Database;

import java.sql.SQLException;

public class Decoder {
    /***
     * Simple app to convert an army list code to a text output.
     * Basically a test thing.
     */

    private static Database db;
    static String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    public static void main(String[] args) {
        try {
            Class.forName(driver);
            db = Database.getInstance();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Hello");
        System.out.println(java.lang.System.getProperty("java.class.path"));
    }
}
