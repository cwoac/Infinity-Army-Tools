package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.Unit;
import net.codersoffortune.infinity.tts.Model;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Top level class to hold all the details and models in a bunch of things.
 * Designed to be done at the faction level really.
 */
public class OldCatalogue {
    private Map<Integer, UnitMapping> units = new HashMap<>();

    /**
     * Iterate over a sectoral list loaded from ArmyBuilder
     *
     * @param sectoralList the sectoral to add
     */
    public void addUnits(int idx, final SectoralList sectoralList) {
        for (Unit unit : sectoralList.getUnits()) {
            if (!units.containsKey(unit.getID())) {
                units.put(unit.getID(), new UnitMapping());
            }
            units.get(unit.getID()).add(idx, unit);
        }
    }

    public void addTTSModel(TTSModel model, int unit_idx, int group_idx, int option_idx) {
        units.get(unit_idx).addTTSModel(model, group_idx, option_idx);
    }

    public void addTTSModel(TTSModel model, int unit_idx, int option_idx) {
        addTTSModel(model, unit_idx,1, option_idx);
    }

    public Optional<TTSModel> getTTSModel(int unit_idx, int group_idx, int option_idx) {
        return units.get(unit_idx).getTTSModel(group_idx, option_idx);
    }

    public Optional<TTSModel> getTTSModel(int unit_idx, int option_idx) {
        return getTTSModel(unit_idx, 1, option_idx);
    }

    public void addTTSModels(ModelSet ms) {
        for(Map.Entry<Integer, Model> entry : ms.getModels().entrySet()) {
            // TODO:: Handle special cases
            int profile_idx;
            // I'm still sorry about this code.
            for( Map.Entry<Integer, Map<Integer, Map<Integer, List<TTSModel>>>> groupEntry : entry.getValue().getItems().entrySet()) {
                for( Map.Entry<Integer, Map<Integer, List<TTSModel>>> profileEntry : groupEntry.getValue().entrySet())
                {
                    profile_idx = profileEntry.getKey();
                    if (profile_idx != 1 ) {
                        // TODO:: Handle these!
                        continue;
                    }
                    for( Map.Entry<Integer, List<TTSModel>> optionEntry : profileEntry.getValue().entrySet()) {
                        optionEntry.getValue().stream().forEach(x->addTTSModel(x, entry.getKey(), groupEntry.getKey(), optionEntry.getKey()));
                    }
                }
            }
        }
    }

    public void toCSV(String filename) throws IOException {
        FileWriter fh = new FileWriter(filename);
        String[] headers = {"unit,", "group", "profile", "option","decals","meshes"};
        try(CSVPrinter out = new CSVPrinter(fh, CSVFormat.EXCEL.withHeader(headers))) {
            for( Map.Entry<Integer, UnitMapping> unitEntry : units.entrySet() ) {
                for( Map.Entry<Integer, GroupMapping> groupEntry : unitEntry.getValue().getGroups().entrySet()) {
                    // TODO:: Profiles.
                    for( Map.Entry<Integer, OptionMapping> optionEntry : groupEntry.getValue().getOptions().entrySet()) {
                        TTSModel model = optionEntry.getValue().getModel();
                        out.printRecord(unitEntry.getKey(),
                                groupEntry.getKey(),
                                1,
                                optionEntry.getKey(),
                                model!=null ? model.getDecals() :" ",
                                model!=null ? model.getMeshes() :"");
                    }
                }
            }
        }
    }

}
