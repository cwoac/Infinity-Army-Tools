package net.codersoffortune.infinity.collection

import net.codersoffortune.infinity.SECTORAL
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.FilterItem
import net.codersoffortune.infinity.metadata.FilterType
import net.codersoffortune.infinity.metadata.MappedFactionFilters

object UniversalFilters {
    var data : MappedFactionFilters = MappedFactionFilters()
    init {

        Database.getInstance().sectorals.values.stream()
            .map { it.mappedFilters }
            .forEach { data.addFromMappedFilters(it)
            }
    }
}