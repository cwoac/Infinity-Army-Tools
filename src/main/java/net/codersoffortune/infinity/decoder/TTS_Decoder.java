package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.ModelSet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TTS_Decoder {

    //private final static String test_list = "ZQpwYW5vY2VhbmlhASCBLAIBCQABAQEAAAEBAQAAAQEDAAABAQIAAAEBBQAAAQEIAAABAQkAAAEBBgAADAEBAAIEAITGAQUAAITAAQIAAA0BBQAADAEBAA==";

    private static Database db;
    private static final boolean useMercs = false;

    public static void main(String[] args) throws IOException {
        db = Database.getInstance();

        importFromCSV();

      //  loadFromJSON();


//

        System.out.println("All Done\n");
//        FactionList factionList = new FactionList(FACTION.NA2, db.getSectorals());
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

//        for( SECTORAL sectoral : FACTION.NA2.getSectorals()) {
//            Catalogue c = new Catalogue();
//            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, false);
//            String csvFile = String.format("%s test2.csv", sectoral.getName());
//            c.addTTSModelsFromCSV(csvFile);
//            String sectoralJSON = c.asJson(sectoral);
//            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", sectoral.getName())));
//            writer.append(sectoralJSON);
//            writer.close();
//        }

//        System.out.println("All dumps created");



        System.out.println("moo");
    }

    private static void loadFromJSON() throws IOException {
        Map<Integer, URL> old_bags = new HashMap<>();
        // first find all the old bags
        for( FACTION faction : FACTION.values()) {
            if( faction == FACTION.NA2 ) continue; // handled below
            URL old_bag = TTS_Decoder.class.getResource(String.format("/wip/%s.json", faction.getName()));
            old_bags.put(faction.getId(), old_bag);
        }

        for( SECTORAL sectoral : FACTION.NA2.getSectorals() ) {
            URL old_bag = TTS_Decoder.class.getResource(String.format("/wip/%s.json", sectoral.getName()));
            old_bags.put(sectoral.getId(), old_bag);
        }
        System.out.println("Bags opened\n");

        for( FACTION faction : FACTION.values()) {
        //for( FACTION faction : Arrays.asList(FACTION.Aleph)) {
            if( faction == FACTION.NA2 ) continue; // handled below
            System.out.printf("Reading %s%n", faction.getName());
            ModelSet ms = new ModelSet();
            ms.readJson(old_bags.get(faction.getId()));


            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals(), faction, useMercs);
            c.addTTSModels(ms);
            c.toCSV(String.format("%s.csv", faction.name()));
            String factionJson = c.asJson(faction);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", faction.getName())));
            writer.append(factionJson);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", faction.getName())));
            for (PrintableUnit m : c.getModellessList())
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new ModelSet();
            ms3.readJson(factionJson);
        }

        for( SECTORAL sectoral : FACTION.NA2.getSectorals()){
            System.out.printf("Reading %s%n", sectoral.getName());
            ModelSet ms = new ModelSet();
            ms.readJson(old_bags.get(sectoral.getId()));

            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, useMercs);
            c.addTTSModels(ms);
            c.toCSV(String.format("%s.csv", sectoral.name()));
            String sectoralJSON = c.asJson(sectoral);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", sectoral.getName())));
            writer.append(sectoralJSON);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", sectoral.getName())));
            for (PrintableUnit m : c.getModellessList())
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new ModelSet();
            ms3.readJson(sectoralJSON);
        }
    }

    private static void importFromCSV() throws IOException {
        for( FACTION faction : FACTION.values()) {
            if( faction == FACTION.NA2 ) continue;
            System.out.printf("Reading %s%n", faction.getName());
            String csvFile = String.format("%s2.csv", faction.name());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals(), faction, useMercs);
            // Yes this will fail first time. You need to make this file yourself!
            try {
                c.addTTSModelsFromCSV(csvFile);
            } catch (IOException e) {
                // yes this is nasty. sorry.
                System.out.printf("%s not found. skipping%n", csvFile);
                continue;
            }
            String faction_json = c.asJson(faction);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", faction.getName())));
            writer.append(faction_json);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", faction.getName())));
            for (PrintableUnit m : c.getModellessList())
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new ModelSet();
            ms3.readJson(faction_json);
        }

        for( SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            System.out.printf("Reading %s%n", sectoral.getName());
            String csvFile = String.format("%s2.csv", sectoral.name());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, useMercs);
            // Yes this will fail first time. You need to make this file yourself!
            try {
                c.addTTSModelsFromCSV(csvFile);
            } catch (IOException e) {
                // yes this is nasty. sorry.
                System.out.printf("%s not found. skipping%n", csvFile);
                continue;
            }
            String faction_json = c.asJson(sectoral);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", sectoral.getName())));
            writer.append(faction_json);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", sectoral.getName())));
            for (PrintableUnit m : c.getModellessList())
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new ModelSet();
            ms3.readJson(faction_json);
        }
    }
}


