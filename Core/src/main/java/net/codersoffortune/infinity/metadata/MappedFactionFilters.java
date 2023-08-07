package net.codersoffortune.infinity.metadata;

import net.codersoffortune.infinity.metadata.unit.ProfileItem;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Like FactionFilters, but has maps!
 */
public class MappedFactionFilters {
    private final EnumMap<FilterType, Map<String, FilterItem>> data = new EnumMap<>(FilterType.class);
    private final static EnumMap<FilterType, Map<String, FilterItem>> allData = new EnumMap<>(FilterType.class);

    static {
        // initialise the maps
        Stream.of(FilterType.values()).forEach(x -> allData.put(x, new HashMap<>()));
    }

    public MappedFactionFilters() {
    }

    public MappedFactionFilters(final FactionFilters filters) {
        super();
        addFromFilters(filters);
    }

    private void addOneType(final FilterType type, final Map<String, FilterItem> values) {
        Map<String, FilterItem> target = data.get(type);
        values.keySet().forEach(it->target.putIfAbsent(it, values.get(it)));

    }

    public void addFromMappedFilters(final MappedFactionFilters filters) {
        Stream.of(FilterType.values()).forEach(x -> addOneType(x, filters.data.get(x)));
    }

    private void addFromFilters(final FactionFilters filters) {
        Stream.of(FilterType.values()).forEach(ft -> {
            filters.getByType(ft).forEach(it -> {
                data.put(ft, new HashMap<>());
                data.get(ft).put(it.getId(), it);
                allData.get(ft).putIfAbsent(it.getId(), it);
            });
        });
    }


    public FilterItem getItem(final FilterType type, final ProfileItem item) throws IndexOutOfBoundsException {
        return getItem(type, item.getId());
    }

    public FilterItem getItem(final FilterType type, final int idx) throws IndexOutOfBoundsException {
        return data.get(type).get(idx);
    }

    public static FilterItem getFromAll(final FilterType type, final int idx) throws IndexOutOfBoundsException {
        return allData.get(type).get(idx);
    }
}
