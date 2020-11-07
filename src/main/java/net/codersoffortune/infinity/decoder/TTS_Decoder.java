package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FactionList;
import net.codersoffortune.infinity.metadata.Model;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TTS_Decoder {


    public static void main(String[] args) throws IOException, SQLException {
        Database db = Database.getInstance();
        ObjectMapper om = new ObjectMapper();
        URL x = TTS_Decoder.class.getResource("/sources/Fusiliers");
        JsonNode jn = om.readTree(x);
        // Now get to the contents of the bag
        JsonNode contents = jn.findPath("ContainedObjects");
        Pattern idPattern = Pattern.compile("(?<opt>[\\dA-Fa-f])(?<unit>[\\dA-Fa-f]{5})");
        List<Model> models = new ArrayList<>();
        FactionList panO = db.getFactions().get(101);
        for (JsonNode child : contents) {
            String[] descLines = child.get("Description").asText().split("\n");
            String name = child.get("Nickname").asText();
            String code = descLines[descLines.length - 1];//.split("\\]?\\[-\\]\\[?")[1];
            Matcher matcher = idPattern.matcher(code);
            if (!matcher.find()) {
                throw new InvalidObjectException(String.format("Unable to find unit ID in %s", code));
            }
            String option = matcher.group("opt");
            int optionIdx = Integer.parseInt(matcher.group("opt"), 16);
            String unit = matcher.group("unit");
            int unitIdx = Integer.parseInt(matcher.group("unit"), 16);
            String decals = child.get("AttachedDecals").toString();
            String meshes = child.get("CustomMesh").toString();

            models.add(new Model(panO, unitIdx, optionIdx, decals, meshes));
            System.out.println(String.format("[%d,%d] %s - %s", unitIdx, optionIdx, name, code));
        }
        String output = om.writeValueAsString(models);
        System.out.println(jn.toString());
    }

}
