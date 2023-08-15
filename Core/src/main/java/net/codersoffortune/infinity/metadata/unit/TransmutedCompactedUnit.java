package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.SECTORAL;

import java.io.InvalidObjectException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TransmutedCompactedUnit extends CompactedUnit {

    private final List<CompactedUnit> compactedUnits;

    public TransmutedCompactedUnit(Unit unit, ProfileGroup group, List<Profile> profiles, ProfileOption option) {
        super(unit, group, profiles.get(0), option);
        this.compactedUnits = profiles.stream().map(p->new CompactedUnit(unit, group, p,option)).collect(Collectors.toList());
    }


    /**
     * Factory function to generate a printable version of this unit.
     * @param sectoral to mark this as belonging to
     * @return a PrintableUnit interpretation of this CompactedUnit
     * @throws InvalidObjectException on failure
     */
    public PrintableUnit getPrintableUnit( SECTORAL sectoral) throws InvalidObjectException {
        return new TransmutedPrintableUnit(this, sectoral);
    }

    @Override
    public Collection<PrintableUnit> getPrintableUnits(SECTORAL sectoral) throws  InvalidObjectException {
        HashSet<PrintableUnit> result = new HashSet<>();
        result.add(getPrintableUnit(sectoral));
        for (CompactedUnit compactedUnit: compactedUnits) {
            result.add(compactedUnit.getPrintableUnit(sectoral));
        }
        return result;
    }

    public List<CompactedUnit> getCompactedUnits() {
        return compactedUnits;
    }
}
