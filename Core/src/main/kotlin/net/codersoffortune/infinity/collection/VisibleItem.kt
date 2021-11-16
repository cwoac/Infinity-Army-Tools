package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.codersoffortune.infinity.metadata.FilterType
import net.codersoffortune.infinity.metadata.MappedFactionFilters
import net.codersoffortune.infinity.metadata.unit.ProfileItem

@Serializable
class VisibleItem(val type: FilterType, @Transient var mappedFactionFilters: MappedFactionFilters? = null) {
    var id : Int = 0

    constructor(profileItem: ProfileItem, type: FilterType) : this(type){
        id = profileItem.id
    }
    constructor(unitId: Int, type: FilterType) : this(type) {
        id = unitId
    }

    override fun toString() : String {
        return MappedFactionFilters.getFromAll(type, id).name
        //return String.format(CatalogueController.filters.getItem(type,id).name)
//        mappedFactionFilters?.let {
//            return String.format(mappedFactionFilters!!.getItem(type,id).name)
//        }
//        //result.append(filters.getItem(type, getId()).getName());
//        // Fallback in case we haven't set the filters.
//        return String.format("%s %u", type, id)
    }
}