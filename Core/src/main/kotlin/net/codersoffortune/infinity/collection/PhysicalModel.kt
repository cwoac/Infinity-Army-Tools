package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import net.codersoffortune.infinity.metadata.unit.CompactedUnit

@Serializable
data class PhysicalModel(val name: String, val items: List<Item>)
