package net.codersoffortune.infinity.metadata;

import net.codersoffortune.infinity.FACTION;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FactionList {
    private Map<SECTORAL, SectoralList> sectorals = new HashMap<>();

    public Optional<Unit> getUnit(int id) {
        return sectorals.values().stream().
                map(x -> x.getUnit(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    public FactionList(FACTION faction, Map<Integer, SectoralList> sectorals) {
        faction.getSectorals().stream().forEach(s-> addSectoral(s, sectorals.get(s.getId())));
    }

    private void addSectoral(SECTORAL sectoral, SectoralList sectoralList) {
        sectorals.put(sectoral, sectoralList);
    }
}
