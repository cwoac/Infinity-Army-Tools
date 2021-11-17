package net.codersoffortune.infinity.metadata;

/**
 * Looks like a normal MappedFactionFilter object, but actually uses the global registry of them.
 */
public class GlobalMappedFactionFilters extends MappedFactionFilters {
    public GlobalMappedFactionFilters() {}

    @Override
    public FilterItem getItem(final FilterType type, final int idx) throws IndexOutOfBoundsException {
        return MappedFactionFilters.getFromAll(type, idx);
    }
}
