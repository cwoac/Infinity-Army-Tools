package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.mapping.GroupMapping;
import net.codersoffortune.infinity.mapping.OptionMapping;
import net.codersoffortune.infinity.mapping.UnitMapping;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.CompactedUnit;
import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.Unit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holder of the bag (as it were) for an entire faction.
 */
public class Catalogue {
    private final Set<PrintableUnit> units = new HashSet<>();
    private List<PrintableUnit> unitList = new ArrayList<>();


    private void makeList() {
        unitList = new ArrayList<>(units);
        java.util.Collections.sort(unitList);
    }

    public void addUnits(final SectoralList list) throws InvalidObjectException {
        MappedFactionFilters filters = list.getMappedFilters();
        for(Unit unit : list.getUnits()) {
           for(CompactedUnit cu: unit.getAllUnits()) {
               PrintableUnit pu = new PrintableUnit(filters, cu);
               units.add(pu);
           }
        }
        makeList();
    }

    public void addTTSModels(final ModelSet modelSet) {
        for( Map.Entry<Integer, Model> entry : modelSet.getModels().entrySet() ) {
            List<PrintableUnit> targets = unitList.stream().filter(u->u.getUnit_idx()==entry.getKey()).collect(Collectors.toList());
            Map<Integer, Map<Integer, Map<Integer, List<TTSModel>>>> items = entry.getValue().getItems();
            for(Map.Entry<Integer, Map<Integer, Map<Integer, List<TTSModel>>>> groupEntry : items.entrySet()) {
                List<PrintableUnit> groupTargets = targets.stream().filter(u->u.getGroup_idx()==groupEntry.getKey()).collect(Collectors.toList());
                for(Map.Entry<Integer, Map<Integer, List<TTSModel>>> profileEntry : groupEntry.getValue().entrySet()) {
                    List<PrintableUnit> profileTargets = groupTargets.stream().filter(u->u.getProfile_idx() == profileEntry.getKey()).collect(Collectors.toList());
                    for( Map.Entry<Integer, List<TTSModel>> optionEntry : profileEntry.getValue().entrySet()) {
                        List<PrintableUnit> optionUnits = profileTargets.stream().filter(u->u.getOption_idx() == optionEntry.getKey()).collect(Collectors.toList());
                        List<TTSModel> models = optionEntry.getValue();
                        optionUnits.stream().forEach(x->x.addTTSModels(models));
                    }
                }
            }
        }
        // Now let's search for sideways copyable meshes. This is basically looking for meshes for the LTs
        List<PrintableUnit> blanks = unitList.stream().filter(u->u.getModels().size()==0).collect(Collectors.toList());
        for( PrintableUnit blank : blanks ) {
            // This is going to be pricey!
            Optional<PrintableUnit> maybeDonor = unitList.stream()
                    .filter(d->d.getModels().size()>0)
                    .filter(d->d.isEquivalent(blank))
                    .findFirst();
            if( maybeDonor.isPresent() ) {
                // TODO:: PROPERLOGGING!
                System.out.println(String.format("Found donor %s in %s",blank, maybeDonor.get()));
                blank.addTTSModels(maybeDonor.get().getModels());
            }
        }
    }


    public void toCSV(String filename) throws IOException {
        FileWriter fh = new FileWriter(filename);
        String[] headers = {"unit,", "group", "profile", "option","name","weapons","equip","skills",
                "decals1", "meshes1",
                "decals2", "meshes2",
                "decals3", "meshes3",
                "decals4", "meshes4",
                "decals5", "meshes5",
        };
        try(CSVPrinter out = new CSVPrinter(fh, CSVFormat.EXCEL.withHeader(headers))) {
            for(PrintableUnit u : unitList) {
                out.printRecord(u.getUnit_idx(),
                        u.getGroup_idx(),
                        u.getProfile_idx(),
                        u.getOption_idx(),
                        u.getName(),
                        String.join(",", u.getWeapons()),
                        String.join(",", u.getEquip()),
                        String.join(",", u.getSkills()),
                        u.getTTSMesh(0),
                        u.getTTSDecal(0),
                        u.getTTSMesh(1),
                        u.getTTSDecal(1),
                        u.getTTSMesh(2),
                        u.getTTSDecal(2),
                        u.getTTSMesh(3),
                        u.getTTSDecal(3),
                        u.getTTSMesh(4),
                        u.getTTSDecal(4),
                        u.getTTSMesh(5),
                        u.getTTSDecal(5));
                }
            }
        }


    public List<PrintableUnit> getUnitList() {
        return unitList;
    }
}
