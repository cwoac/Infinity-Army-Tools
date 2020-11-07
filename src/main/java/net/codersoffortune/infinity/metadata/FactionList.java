package net.codersoffortune.infinity.metadata;

import net.codersoffortune.infinity.metadata.unit.Unit;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FactionList {
    private Map<Integer, SectorialList> sectorials = new HashMap<>();

    public Optional<Unit> getUnit(int id) {
        return sectorials.values().stream().
                map(x -> x.getUnit(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }

    public void addSectorial(int faction_idx, SectorialList sectorialList) {
        sectorials.put(faction_idx, sectorialList);
    }

    public Map<Integer, SectorialList> getSectorials() {
        return sectorials;
    }

    public void setSectorials(Map<Integer, SectorialList> sectorials) {
        this.sectorials = sectorials;
    }
}
