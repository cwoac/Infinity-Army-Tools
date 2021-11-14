package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import net.codersoffortune.infinity.metadata.FilterType
import net.codersoffortune.infinity.metadata.unit.ProfileItem

@Serializable
class VisibleItem(val type: FilterType) {
    var id : Int = 0
    constructor(profileItem: ProfileItem, type: FilterType) : this(type){
        id = profileItem.id
    }
}