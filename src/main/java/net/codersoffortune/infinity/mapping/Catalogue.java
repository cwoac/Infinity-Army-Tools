package net.codersoffortune.infinity.mapping;

import net.codersoffortune.infinity.metadata.SectorialList;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.util.HashMap;
import java.util.Map;

public class Catalogue {
    private Map<Integer, UnitMapping> units = new HashMap<>();

    /**
     * Iterate over a sectiorial list loaded from ArmyBuilder
     *
     * @param sectoralList the sectoral to add
     */
    public void addUnits(int idx, final SectorialList sectorialList) {
        for (Unit unit : sectorialList.getUnits()) {
            if (!units.containsKey(unit.getID())) {
                units.put(unit.getID(), new UnitMapping());
            }
            units.get(unit.getID()).add(idx, unit);
        }

    }

}
