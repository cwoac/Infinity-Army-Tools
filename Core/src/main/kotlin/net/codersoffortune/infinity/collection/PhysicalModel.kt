package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import net.codersoffortune.infinity.metadata.unit.CompactedUnit

@Serializable
/**
 * An actual physical model.
 */
data class PhysicalModel(val name: String, val id: Int, val visibleItems: List<VisibleItem>) {

    constructor(name: String, id: Int, weapons: List<VisibleItem>, skills: List<VisibleItem>) :
            this(name, id, skills+weapons)


    override fun toString(): String {
        return String.format("%s (%s)",name,visibleItems.joinToString())
    }
}
