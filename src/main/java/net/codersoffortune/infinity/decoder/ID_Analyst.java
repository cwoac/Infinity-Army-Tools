package net.codersoffortune.infinity.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.unit.UnitID;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ID_Analyst {



    public static void main(String[] args) throws IOException {
        Database db = Database.getInstance();
        ObjectMapper om = new ObjectMapper();
        ModelSet ms = new ModelSet();

        for(FACTION faction: FACTION.values()) {
            if (faction == FACTION.NA2) continue;
            String bagName = String.format("/wip/%s.json", faction.getName());
            System.out.println(String.format("Loading %s\n", bagName));
            URL bag = TTS_Decoder.class.getResource(bagName);
            ms.readJson(bag);
        }

        Map<UnitID, Set<TTSModel>> hmmm = new HashMap<>();
        for( Map.Entry<UnitID, Set<TTSModel>> entry : ms.getModels().entrySet() ) {
            if( entry.getKey().getUnit_idx() == 36) hmmm.put(entry.getKey(), entry.getValue());
        }

        Catalogue c  = new Catalogue();
        c.addUnits(db.getSectorals(), FACTION.NA2, false);
        c.toCSV("NA2.csv", ms);
        System.out.println("moo");
    }
}
