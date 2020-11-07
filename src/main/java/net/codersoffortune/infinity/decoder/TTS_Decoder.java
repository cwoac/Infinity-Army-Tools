package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.ModelSet;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTS_Decoder {

    private final static String test_list = "ZQpwYW5vY2VhbmlhASCBLAEBCQABAQEAAAEBAgAAAQEDAAABAQQAAAEBBQAAAQEGAAABAQgAAAEBCQAAAQEBAA==";

    public static void main(String[] args) throws IOException, SQLException {
        Database db = Database.getInstance();
        ObjectMapper om = new ObjectMapper();
        URL x = TTS_Decoder.class.getResource("/sources/Panoceania N4");
        JsonNode jn = om.readTree(x);
        // Now get to the contents of the bag
        JsonNode contents = jn.findPath("ContainedObjects");
        Pattern idPattern = Pattern.compile("(?<opt>[\\dA-Fa-f])(?<unit>[\\dA-Fa-f]{5})");
        //List<Model> models = new ArrayList<>();
        // TODO:: Move this generation into the inner loading code
        FactionList panO = new FactionList();
        panO.addSectorial(101, db.getFactions().get(101));
        panO.addSectorial(103, db.getFactions().get(103));
        panO.addSectorial(105, db.getFactions().get(105));
        panO.addSectorial(106, db.getFactions().get(106));
        ModelSet ms = new ModelSet(panO, 101);
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
            if (unitIdx == 22) {
                // Neoterra related, so not in currently.
                continue;
            }
            String decals = child.get("AttachedDecals").toString();
            String meshes = child.get("CustomMesh").toString();
            ms.addModel(unitIdx, optionIdx, decals, meshes);
            System.out.println(String.format("[%d,%d] %s - %s", unitIdx, optionIdx, name, code));
        }
        String output = om.writeValueAsString(ms);
        ModelSet ms2 = om.readValue(output, ModelSet.class);
        String output2 = om.writeValueAsString(ms2);
        assert (output.equals(output2));
        Armylist testList = Armylist.fromArmyCode(test_list);
        MappedFactionFilters mff = new MappedFactionFilters(db.getFactions().get(101).getFilters());
        String bag = testList.asJson(mff, ms2);
        System.out.println(jn.toString());

    }

}
