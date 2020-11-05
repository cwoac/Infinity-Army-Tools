package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.db.Database;

import java.io.IOException;
import java.sql.SQLException;

public class Decoder {
    /***
     * Simple app to convert an army list code to a text output.
     * Basically a test thing.
     */
    private static final String[] testcodes = {"gfUGbm9tYWRzASCBLAEBAQCEQwGPnwA=",
            "gMkHeXUtamluZwEggSwBAQMAgqgBiSMAAICbAYIqAACAhQECAA==",
            "gr8Kb3BlcmF0aW9ucwEggSwBAQwAgkgBBgAAglUBAQAAglUCAQAAglUFAgAAhK0BAgAAhGkBBwAAgLcBAQAAgLcBAQAAgLcBBQAAgLcBBAAAhhUBAQAAegEEAA==",
            "g40Nd2hpdGUtY29tcGFueQEggSwCAQUAhbUBBAAAgVIBAwAAgIUBAQAAgKEBAQAAg7YBAgACAQAkAQIA",
            "g+oIc3Rhcm1hZGEKUGFpbiBUcmFpboEsAQEKAIXtAQIAAILyAQEAAIWwAQIAAIX+AQEAAIXnAQUAAIYAAQMAAIW5AQEAAIWuAQIAAIW4AQEAAIX6AQQA",
            "g+oIc3Rhcm1hZGEDVEFHgSwCAQkAhe0BAgAAhacBBQAAhacBAQAAhacBBAAAhecBAgAAhfkBAwAAhbIBAgAAhbcBAQAAgMABAQACBgCFvQECAACFrgECAACFtQEEAACFtQEEAACGAAEGAACFuwEBAA==",
            "gMwPaW52aW5jaWJsZS1hcm15ClN0ZWVsIHJhaW6BLAIBCQCE3AEFAACAogECAACE3gEFAACF2QEBAAB8AQYAAHwBCQAAfAEBAACE4AEDAACE4AECAAIEAITgAQQAAICMAQEAAICOAQEAAICOAQEA",
            "gMwPaW52aW5jaWJsZS1hcm15D0NvcmUgYW5kIGhhcnJpc4EsAgEJAICWAQsAAITvAQIAAITeAQMAAITeAQUAAHwBCQAAfAEBAAB8AQEAAHwBAQAAgJ8BAQACBACE6wEDAACE3AEEAACAogECAACAjgEBAA=="

    };

    public static void main(String[] args) throws IOException, SQLException {

        try {
            // preload the db.
            Database.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (String code : testcodes) {
            Armylist list = Armylist.fromArmyCode(code);
            list.pretty_print();
        }
    }
}
