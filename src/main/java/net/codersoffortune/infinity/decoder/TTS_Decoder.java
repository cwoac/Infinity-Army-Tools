package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTS_Decoder {

    private final static String test_list = "ZQpwYW5vY2VhbmlhASCBLAIBCQABAQEAAAEBAQAAAQEDAAABAQIAAAEBBQAAAQEIAAABAQkAAAEBBgAADAEBAAIEAITGAQUAAITAAQIAAA0BBQAADAEBAA==";

    public static void main(String[] args) throws IOException {
        Database db = Database.getInstance();
        ObjectMapper om = new ObjectMapper();
        FACTION faction = FACTION.YuJing;
        URL old_bag = TTS_Decoder.class.getResource(String.format("/sources/%s N4", faction.getName()));
        JsonNode jn = om.readTree(old_bag);
        // Now get to the contents of the bag
        JsonNode contents = jn.findPath("ContainedObjects");
        Pattern idPattern = Pattern.compile("(?<opt>[\\dA-Fa-f])(?<unit>[\\dA-Fa-f]{5})");
        // TODO:: Move this generation into the inner loading code

        FactionList factionList = new FactionList(faction, db.getSectorals());

        ModelSet ms = new ModelSet();
        for (JsonNode child : contents) {
            String[] descLines = child.get("Description").asText().split("\n");
            String name = child.get("Nickname").asText();
            String code = descLines[descLines.length - 1];//.split("\\]?\\[-\\]\\[?")[1];
            Matcher matcher = idPattern.matcher(code);
            if (!matcher.find()) {
                // TODO:: Proper logging. Possibly validate that this is a proxy model?
                System.out.println(String.format("Unable to find a model for %s", name));
                continue;
            }
            String option = matcher.group("opt");
            int optionIdx = Integer.parseInt(matcher.group("opt"), 16);
            String unit = matcher.group("unit");
            int unitIdx = Integer.parseInt(matcher.group("unit"), 16);
            if ((faction == FACTION.PanOceania && unitIdx == 22))  {
                // Not all models in the bag are currently in factions (due to sectorals awaiting updates)
                // 22 - Neoterra auxilia
                continue;
            }
            String decals = child.get("AttachedDecals").toString();
            String meshes = child.get("CustomMesh").toString();
            ms.addModelOld(factionList, faction.getId(), unitIdx, optionIdx, new TTSModel(name,decals,meshes));
        }
        String output = om.writeValueAsString(ms);
        ModelSet ms2 = om.readValue(output, ModelSet.class);
        String output2 = om.writeValueAsString(ms2);
        assert (output.equals(output2));
        //Armylist testList = Armylist.fromArmyCode(test_list);
        //MappedFactionFilters mff = dFactionFilters(db.getFactions().get(101).getFilters());
        //String bag = testList.asJson(mff, ms2);
        Catalogue c = new Catalogue();
        boolean useMercs = false;

        c.addUnits(db.getSectorals(), faction, useMercs);
        c.addTTSModels(ms);
        c.toCSV(String.format("%s test.csv", faction.name()));

        Map<String, Collection<String>> eq = c.getEquivalences();
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
        ModelSet ms3 = new ModelSet();
        ms3.readJson(faction_json);

        System.out.println("moo");
    }

}
