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
    private final EnumMap<FilterType, Map<Integer, FilterItem>> data = new EnumMap<>(FilterType.class);

    public MappedFactionFilters() {
        // initialise the maps
        Stream.of(FilterType.values()).forEach(x -> data.put(x, new HashMap<>()));
    }

    public MappedFactionFilters(final FactionFilters filters) {
        super();
        addFromFilters(filters);
    }

    private void addOneType(final FilterType type, final Map<Integer, FilterItem> values) {
        Map<Integer, FilterItem> target = data.get(type);
        values.keySet().stream().forEach(it->target.putIfAbsent(it, values.get(it)));
    }

    public void addFromMappedFilters(final MappedFactionFilters filters) {
        Stream.of(FilterType.values()).forEach(x -> addOneType(x, filters.data.get(x)));
    }

    public void addFromFilters(final FactionFilters filters) {
        filters.getAmmunition().forEach(x -> data.get(FilterType.ammunition).put(x.getId(), x));
        filters.getWeapons().forEach(x -> data.get(FilterType.weapons).put(x.getId(), x));
        filters.getType().forEach(x -> data.get(FilterType.type).put(x.getId(), x));
        filters.getCategory().forEach(x -> data.get(FilterType.category).put(x.getId(), x));
        filters.getChars().forEach(x -> data.get(FilterType.chars).put(x.getId(), x));
        filters.getEquip().forEach(x -> data.get(FilterType.equip).put(x.getId(), x));
        filters.getExtras().forEach(x -> data.get(FilterType.extras).put(x.getId(), x));
        filters.getPeripheral().forEach(x -> data.get(FilterType.peripheral).put(x.getId(), x));
        filters.getSkills().forEach(x -> data.get(FilterType.skills).put(x.getId(), x));
    }

    public FilterItem getItem(final FilterType type, final ProfileItem item) throws IndexOutOfBoundsException {
        return getItem(type, item.getId());
    }

    public FilterItem getItem(final FilterType type, final int idx) throws IndexOutOfBoundsException {
        return data.get(type).get(idx);
    }
}
