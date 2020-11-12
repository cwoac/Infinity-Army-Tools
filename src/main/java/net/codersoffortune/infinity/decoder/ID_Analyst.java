package net.codersoffortune.infinity.decoder;

import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.metadata.unit.ProfileOption;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ID_Analyst {

    public static void main(String[] args) throws IOException {
        Database db = Database.getInstance();

        Map<Integer, List<Unit>> unitMap = new HashMap<>();

        for (SectoralList sl : db.getSectorals().values()) {
            for (Unit unit : sl.getUnits()) {
                if (!unitMap.containsKey(unit.getID())) {
                    unitMap.put(unit.getID(), new ArrayList<>());
                }
                unitMap.get(unit.getID()).add(unit);
            }
        }
        List<List<Unit>> interesting = unitMap.values().stream()
                .filter(x->x.size()>1).collect(Collectors.toList());
        for( List<Unit> unitList : interesting ){
            if( unitList.stream().map(Unit::getName).distinct().count() > 1) {
                System.out.println("Different names");
            }
            Map<Integer,List<ProfileOption>> comparisons = new HashMap<>();
            for( Unit unit : unitList) {
                for( ProfileOption po : unit.getOptions()) {
                    if (!comparisons.containsKey(po.getId())) {
                        comparisons.put(po.getId(), new ArrayList<>());
                    }
                    comparisons.get(po.getId()).add(po);
                }
            }
            List<List<ProfileOption>> interesting_options = comparisons.values().stream()
                    .filter(x->x.size()>1).collect(Collectors.toList());
            for( List<ProfileOption> interesting_option: interesting_options) {
                List<ProfileOption> distinct = interesting_option.stream().distinct().collect(Collectors.toList());
                if( distinct.size() >1 ) {
                    System.out.print("moo");
                }
            }

        }
        System.out.println("moo");
    }
}
