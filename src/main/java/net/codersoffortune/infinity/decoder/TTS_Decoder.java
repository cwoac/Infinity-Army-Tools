package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.ModelSet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class TTS_Decoder {

    private final static String test_list = "ZQpwYW5vY2VhbmlhASCBLAIBCQABAQEAAAEBAQAAAQEDAAABAQIAAAEBBQAAAQEIAAABAQkAAAEBBgAADAEBAAIEAITGAQUAAITAAQIAAA0BBQAADAEBAA==";

    public static void main(String[] args) throws IOException {
        Database db = Database.getInstance();
        ObjectMapper om = new ObjectMapper();

        boolean useMercs = false;

        for( FACTION faction : FACTION.values()) {
            URL old_bag = TTS_Decoder.class.getResource(String.format("/sources/%s N4", faction.getName()));
            ModelSet ms = new ModelSet();
            FactionList factionList = new FactionList(faction, db.getSectorals());
            ms.readOldJson(old_bag, faction, factionList);

            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals(), faction, useMercs);
            c.addTTSModels(ms);
            c.toCSV(String.format("%s test.csv", faction.name()));
        }

        System.out.println("All dumps created");

        for( FACTION faction : FACTION.values()) {
            Catalogue c2 = new Catalogue();
            c2.addUnits(db.getSectorals(), faction, useMercs);
            // Yes this will fail first time. You need to make this file yourself!
            c2.fromCSV(String.format("%s test2.csv", faction));
            String faction_json = c2.asJson(faction);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", faction.getName())));
            writer.append(faction_json);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", faction.getName())));
            for (PrintableUnit m : c2.getModellessList())
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new ModelSet();
            ms3.readJson(faction_json);
        }

        System.out.println("moo");
    }

}
