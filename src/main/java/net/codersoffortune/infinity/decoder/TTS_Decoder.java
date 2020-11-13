package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.ModelSet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class TTS_Decoder {

    private final static String test_list = "ZQpwYW5vY2VhbmlhASCBLAIBCQABAQEAAAEBAQAAAQEDAAABAQIAAAEBBQAAAQEIAAABAQkAAAEBBgAADAEBAAIEAITGAQUAAITAAQIAAA0BBQAADAEBAA==";

    public static void main(String[] args) throws IOException {
        Database db = Database.getInstance();
        ObjectMapper om = new ObjectMapper();

        boolean useMercs = false;

//        for( FACTION faction : FACTION.values()) {
//            System.out.println(String.format("Reading %s", faction.getName()));
//            URL old_bag = TTS_Decoder.class.getResource(String.format("/sources/%s N4", faction.getName()));
//            ModelSet ms = new ModelSet();
//            FactionList factionList = new FactionList(faction, db.getSectorals());
//            ms.readOldJson(old_bag, faction, factionList);
//
//            Catalogue c = new Catalogue();
//            c.addUnits(db.getSectorals(), faction, faction==FACTION.NA2?true:useMercs);
//            c.addTTSModels(ms);
//            c.toCSV(String.format("%s test.csv", faction.name()));
//        }
        FactionList factionList = new FactionList(FACTION.NA2, db.getSectorals());
//        for( SECTORAL sectoral : FACTION.NA2.getSectorals()) {
//            String naFile = String.format("/sources/%s N4", sectoral.getName());
//            System.out.println("Loading "+ naFile);
//            URL naURL = TTS_Decoder.class.getResource(naFile);
//            if( naURL == null ) {
//                continue;
//            }
//            ModelSet ms = new ModelSet();
//            ms.readOldJson(naURL, FACTION.NA2, factionList);
//            Catalogue c = new Catalogue();
//            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, false);
//            c.addTTSModels(ms);
//            c.toCSV(String.format("%s test.csv", sectoral.getName()));
//        }

        for( SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, false);
            String csvFile = String.format("%s test2.csv", sectoral.getName());
            c.addTTSModelsFromCSV(csvFile);
            String sectoralJSON = c.asJson(sectoral);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", sectoral.getName())));
            writer.append(sectoralJSON);
            writer.close();
        }

        System.out.println("All dumps created");

//        for( FACTION faction : FACTION.values()) {
//            if( faction == FACTION.NA2 ) continue;
//            System.out.println(String.format("Reading %s", faction.getName()));
//            Catalogue c2 = new Catalogue();
//            c2.addUnits(db.getSectorals(), faction,  faction==FACTION.NA2?true:useMercs);
//            // Yes this will fail first time. You need to make this file yourself!
//            c2.addTTSModelsFromCSV(String.format("%s test2.csv", faction));
//            String faction_json = c2.asJson(faction);
//            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", faction.getName())));
//            writer.append(faction_json);
//            writer.close();
//            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", faction.getName())));
//            for (PrintableUnit m : c2.getModellessList())
//                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
//            writer.close();
//            // Test the outputted JSON is good
//            ModelSet ms3 = new ModelSet();
//            ms3.readJson(faction_json);
//        }

        System.out.println("moo");
    }

}
