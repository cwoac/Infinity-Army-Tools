package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.stream.Collectors;

public class TransmutedCompactedUnit extends CompactedUnit {

    private final List<CompactedUnit> CompactedUnits;

    public TransmutedCompactedUnit(Unit unit, ProfileGroup group, List<Profile> profiles, ProfileOption option) {
        super(unit, group, profiles.get(0), option);
        this.CompactedUnits = profiles.stream().map(p->new CompactedUnit(unit, group, p,option)).collect(Collectors.toList());
    }


    /**
     * Factory function to generate a printable version of this unit.
     * @param filters to translate with
     * @param sectoral to mark this as belonging to
     * @return a PrintableUnit interpretation of this CompactedUnit
     * @throws InvalidObjectException on failure
     */
    public PrintableUnit getPrintableUnit(final MappedFactionFilters filters, SECTORAL sectoral) throws InvalidObjectException {
        return new TransmutedPrintableUnit(filters, this, sectoral);
    }

    public List<CompactedUnit> getCompactedUnits() {
        return CompactedUnits;
    }
}
