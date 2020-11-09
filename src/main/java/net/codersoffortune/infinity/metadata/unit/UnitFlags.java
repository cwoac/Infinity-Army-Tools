package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.metadata.FilterType;

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
    private final boolean singleCamo;
    private final int mimetism;

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

        Optional<ProfileItem> maybeCamo = unit.getSkills().stream().filter(x -> x.getId() == 29).findFirst();
        camo = maybeCamo.isPresent();

        singleCamo = maybeCamo.isPresent()
                && maybeCamo.get().getExtra() != null
                && maybeCamo.get().getExtra().contains(43);

        isLT = unit.getSkills().stream().anyMatch(x -> x.getId() == 119);
    }

    public boolean isLT() {
        return isLT;
    }

    public boolean isCamo() {
        return camo;
    }

    public boolean isSingleCamo() {
        return singleCamo;
    }

    public int getMimetism() {
        return mimetism;
    }
}
