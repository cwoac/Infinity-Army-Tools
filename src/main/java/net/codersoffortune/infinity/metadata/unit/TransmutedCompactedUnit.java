package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.stream.Collectors;

public class TransmutedCompactedUnit extends CompactedUnit {
    private final List<Profile> profiles;
    private final List<CompactedUnit> CompactedUnits;

    public TransmutedCompactedUnit(int unit_idx, ProfileGroup group, List<Profile> profiles, ProfileOption option) {
        super(unit_idx, group, profiles.get(0), option);
        this.profiles = profiles;
        this.CompactedUnits = profiles.stream().map(p->new CompactedUnit(unit_idx, group, p,option)).collect(Collectors.toList());
    }


    /**
     * Factory function to generate a printable version of this unit.
     * @param filters to translate with
     * @param sectoral to mark this as belonging to
     * @return a PrintableUnit interpretation of this CompactedUnit
     * @throws InvalidObjectException on failure
     */
    @Override
    public PrintableUnit getPrintableUnit(final MappedFactionFilters filters, SECTORAL sectoral) throws InvalidObjectException {
        return new TransmutedPrintableUnit(filters, this, sectoral);
    }

    public List<CompactedUnit> getCompactedUnits() {
        return CompactedUnits;
    }
}
