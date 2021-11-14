package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable


@Serializable
data class ModelCollection(val models: MutableList<PhysicalModel>) {

    fun addSku(sku: Sku) {
        sku.models.forEach{models.add(it)}
    }

    fun addModel(physicalModel: PhysicalModel) {
        models.add(physicalModel)
    }

}
