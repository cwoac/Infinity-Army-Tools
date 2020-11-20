package net.codersoffortune.infinity.metadata.unit;

import java.io.InvalidObjectException;
import java.util.List;
import java.util.Optional;

/**
 * Dead simple POJO to hold flags about the unit that would be a pain to
 * calculate inside PrintableUnit
 */
public class UnitFlags {
    private final boolean isLT;
    private final boolean camo;
    private final int mimetism;
    private final int impersonisation;

    public UnitFlags(CompactedUnit unit) throws InvalidObjectException {
        Optional<ProfileItem> maybeMimetism = unit.getSkills().stream().filter(x -> x.getId() == 28).findFirst();
        if (maybeMimetism.isPresent()) {
            List<Integer> extras = maybeMimetism.get().getExtra();
            if (extras != null && !extras.isEmpty()) {
                switch (extras.get(0)) {
                    case 6:
                        mimetism = -3;
                        break;
                    case 7:
                        mimetism = -6;
                        break;
                    default:
                        throw new InvalidObjectException(String.format("Unknown mimetism type %s found", extras.get(0)));
                }
            } else {
                // Probably Laxmee, or another profile they haven't updated yet.
                mimetism = -3;
            }
        } else {
            mimetism = 0;
        }

        camo = unit.getSkills().stream().anyMatch(x -> x.getId() == 29);

        isLT = unit.getSkills().stream().anyMatch(x -> x.getId() == 119);

        // Are they an impersonator?
        boolean hasIMP = unit.getSkills().stream().anyMatch(x->x.getId() == 249);
        if( !hasIMP ) {
            // No? Maybe they have access to cybermask though (and aren't impetuous / have camo)?
            if (unit.getEquipment().stream().anyMatch(x->(x.getId()==145)||x.getId()==101) &&
                    !unit.getPublicChars().contains(6) &&
                    !camo) {
                impersonisation = 1;
            }
            else
            {
                impersonisation = 0;
            }
        } else {
            impersonisation = 2;
        }
    }

    public boolean isLT() {
        return isLT;
    }

    public boolean isCamo() {
        return camo;
    }

    public int getMimetism() {
        return mimetism;
    }

    public int getImpersonisation() {
        return impersonisation;
    }
}
