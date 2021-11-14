package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable

@Serializable
data class Sku(val id: String, val oop: Boolean, val models: Array<PhysicalModel>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sku

        // only the SKU id matters.
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
