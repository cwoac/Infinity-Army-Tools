package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.Metadata;
import net.codersoffortune.infinity.metadata.SectoralList;

import java.io.IOException;

public class Decoder {
    /***
     * Simple app to convert an army list code to a text output.
     * Basically a test thing.
     */
    /*
    private static final String[] testcodes = {"gfUGbm9tYWRzASCBLAEBAQCEQwGPnwA=",
            "gMkHeXUtamluZwEggSwBAQMAgqgBiSMAAICbAYIqAACAhQECAA==",
            "gr8Kb3BlcmF0aW9ucwEggSwBAQwAgkgBBgAAglUBAQAAglUCAQAAglUFAgAAhK0BAgAAhGkBBwAAgLcBAQAAgLcBAQAAgLcBBQAAgLcBBAAAhhUBAQAAegEEAA==",
            "g40Nd2hpdGUtY29tcGFueQEggSwCAQUAhbUBBAAAgVIBAwAAgIUBAQAAgKEBAQAAg7YBAgACAQAkAQIA",
            "g+oIc3Rhcm1hZGEKUGFpbiBUcmFpboEsAQEKAIXtAQIAAILyAQEAAIWwAQIAAIX+AQEAAIXnAQUAAIYAAQMAAIW5AQEAAIWuAQIAAIW4AQEAAIX6AQQA",
            "g+oIc3Rhcm1hZGEDVEFHgSwCAQkAhe0BAgAAhacBBQAAhacBAQAAhacBBAAAhecBAgAAhfkBAwAAhbIBAgAAhbcBAQAAgMABAQACBgCFvQECAACFrgECAACFtQEEAACFtQEEAACGAAEGAACFuwEBAA==",
            "gMwPaW52aW5jaWJsZS1hcm15ClN0ZWVsIHJhaW6BLAIBCQCE3AEFAACAogECAACE3gEFAACF2QEBAAB8AQYAAHwBCQAAfAEBAACE4AEDAACE4AECAAIEAITgAQQAAICMAQEAAICOAQEAAICOAQEA",
            "gMwPaW52aW5jaWJsZS1hcm15D0NvcmUgYW5kIGhhcnJpc4EsAgEJAICWAQsAAITvAQIAAITeAQMAAITeAQUAAHwBCQAAfAEBAAB8AQEAAHwBAQAAgJ8BAQACBACE6wEDAACE3AEEAACAogECAACAjgEBAA=="

    };*/
    private static final String[] testcodes = {
            "gr8Kb3BlcmF0aW9ucwEggSwBAQEAgkkBAgA=",
            "g+oIc3Rhcm1hZGEDVEFHgSwCAQkAhe0BAgAAhacBBQAAhacBAQAAhacBBAAAhecBAgAAhfkBAwAAhbIBAgAAhbcBAQAAgMABAQACBgCFvQECAACFrgECAACFtQEEAACFtQEEAACGAAEGAACFuwEBAA==",
            "gMwPaW52aW5jaWJsZS1hcm15D0NvcmUgYW5kIGhhcnJpc4EsAgEJAICWAQsAAITvAQIAAITeAQMAAITeAQUAAHwBCQAAfAEBAAB8AQEAAHwBAQAAgJ8BAQACBACE6wEDAACE3AEEAACAogECAACAjgEBAA==",
            //"gfUGbm9tYWRzASCBLAEBAQCGDwABAA==",
            //"gfUGbm9tYWRzASCBLAEBAQCGDwACAA==",
            "g40Nd2hpdGUtY29tcGFueQEggSwBAQEAqDwBAQA=", // anaconda
            "g40Nd2hpdGUtY29tcGFueQEggSwBAQEAqIwAAwA=", //scarfaceb
            "gr8Kb3BlcmF0aW9ucwEggSwBAQIAglUBAQAAglUCAQA=",
            "g40Nd2hpdGUtY29tcGFueQEggSwBAQEAJAECAA=="
    };

    public static void main(String[] args) throws IOException {
        //preload the db
        Database db = Database.getInstance();



        for (String code : testcodes) {
            Armylist list = Armylist.fromArmyCode(code);
            Metadata m = db.getMetadata(list.getGame());
            SectoralList fl = db.getSectorals().get(list.getSectoral().getId());
            MappedFactionFilters filters = new MappedFactionFilters(fl.getFilters());
            //TODO:: Load the right modelset
            String moo = list.asJson(filters, null, false);
            list.pretty_print();
        }
    }
}
