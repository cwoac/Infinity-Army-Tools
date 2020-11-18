package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.Model;
import net.codersoffortune.infinity.tts.ModelSet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URL;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TTS_Decoder {

    //private final static String test_list = "ZQpwYW5vY2VhbmlhASCBLAIBCQABAQEAAAEBAQAAAQEDAAABAQIAAAEBBQAAAQEIAAABAQkAAAEBBgAADAEBAAIEAITGAQUAAITAAQIAAA0BBQAADAEBAA==";

    private static Database db;
    private static final boolean useMercs = false;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        db = Database.getInstance();

        //ModelSet fullMS = buildMegaModelSet();
        //fullMS.writeFile("All.models");

//        ModelSet ms = new ModelSet("models.json");
//        //ms.readBinFile("All.models");
//        ms.writeFile("models2.json");
//        ModelSet ms2 = new ModelSet("models2.json");


        loadFromJSON(false);
        importFromCSV();


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

    private static ModelSet buildMegaModelSet() throws IOException {
        ModelSet result = new ModelSet();

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
            if( faction == FACTION.NA2 ) continue; // handled below
            System.out.printf("Reading %s%n", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals(), faction, false);
            EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
            ems.readJson(old_bags.get(faction.getId()));
            result.addModelSet(ems.expand());
        }
        for( SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            System.out.printf("Reading %s%n", sectoral.getName());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, false);
            EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
            ems.readJson(old_bags.get(sectoral.getId()));
            result.addModelSet(ems.expand());
        }

        return result;
    }

    private static void loadFromJSON(boolean check) throws IOException, ClassNotFoundException {
        Map<Integer, URL> old_bags = new HashMap<>();
        ModelSet ms = new ModelSet("models2.json");


        //for( FACTION faction : FACTION.values()) {
        for( FACTION faction : Arrays.asList(FACTION.Aleph)) {
            if( faction == FACTION.NA2 ) continue; // handled below
            System.out.printf("Reading %s%n", faction.getName());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals(), faction, useMercs);

            c.toCSV(String.format("%s.csv", faction.name()), ms);
            String factionJson = c.asJson(faction, ms);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", faction.getName())));
            writer.append(factionJson);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", faction.getName())));
            for (PrintableUnit m : c.getModellessList(ms))
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            if( check ) {
                ModelSet ms3 = new EquivalentModelSet(c.getMappings());
                ms3.readJson(factionJson);
            }
        }

        for( SECTORAL sectoral : FACTION.NA2.getSectorals()){
            System.out.printf("Reading %s%n", sectoral.getName());


            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, useMercs);

            c.toCSV(String.format("%s.csv", sectoral.name()), ms);
            String sectoralJSON = c.asJson(sectoral, ms);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", sectoral.getName())));
            writer.append(sectoralJSON);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", sectoral.getName())));
            for (PrintableUnit m : c.getModellessList(ms))
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            if( check ) {
                ModelSet ms3 = new EquivalentModelSet(c.getMappings());
                ms3.readJson(sectoralJSON);
            }
        }
    }

    private static void importFromCSV() throws IOException {
        //for( FACTION faction : FACTION.values()) {
        for( FACTION faction : Arrays.asList(FACTION.Aleph)) {

        if( faction == FACTION.NA2 ) continue;
            System.out.printf("Reading %s%n", faction.getName());
            String csvFile = String.format("%s2.csv", faction.name());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals(), faction, useMercs);
            EquivalentModelSet ms = new EquivalentModelSet(c.getMappings());
            // Yes this will fail first time. You need to make this file yourself!
            try {
                ms.readCSV(csvFile);
            } catch (IOException e) {
                // yes this is nasty. sorry.
                System.out.printf("%s not found. skipping%n", csvFile);
                continue;
            }
            String faction_json = c.asJson(faction, ms);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", faction.getName())));
            writer.append(faction_json);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", faction.getName())));
            for (PrintableUnit m : c.getModellessList(ms))
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new EquivalentModelSet(c.getMappings());
            ms3.readJson(faction_json);
        }

        for( SECTORAL sectoral : FACTION.NA2.getSectorals()) {
            System.out.printf("Reading %s%n", sectoral.getName());
            String csvFile = String.format("%s2.csv", sectoral.name());
            Catalogue c = new Catalogue();
            c.addUnits(db.getSectorals().get(sectoral.getId()), sectoral, useMercs);
            EquivalentModelSet ms = new EquivalentModelSet(c.getMappings());
            // Yes this will fail first time. You need to make this file yourself!
            try {
                ms.readCSV(csvFile);
            } catch (IOException e) {
                // yes this is nasty. sorry.
                System.out.printf("%s not found. skipping%n", csvFile);
                continue;
            }
            String faction_json = c.asJson(sectoral, ms);
            BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("%s.json", sectoral.getName())));
            writer.append(faction_json);
            writer.close();
            writer = new BufferedWriter(new FileWriter(String.format("%s missing.txt", sectoral.getName())));
            for (PrintableUnit m : c.getModellessList(ms))
                writer.append(String.format("Missing: (%s) %s %s\n", m.getUnitID(), m.getName(), m.getDistinguisher()));
            writer.close();
            // Test the outputted JSON is good
            ModelSet ms3 = new EquivalentModelSet(c.getMappings());
            ms3.readJson(faction_json);
        }
    }
}


