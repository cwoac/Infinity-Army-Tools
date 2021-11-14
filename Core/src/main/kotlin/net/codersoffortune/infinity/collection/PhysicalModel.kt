package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import net.codersoffortune.infinity.metadata.unit.CompactedUnit

@Serializable
data class PhysicalModel(val name: String, val id: Int, val items: List<VisibleItem>) {
    constructor(cu: CompactedUnit) : this(cu.name, cu.unit_idx, cu.visibleItems)
}
