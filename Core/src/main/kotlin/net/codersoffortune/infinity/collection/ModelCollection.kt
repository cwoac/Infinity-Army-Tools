package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ModelCollection(val modelMap: MutableMap<Int, MutableList<PhysicalModel>>) {

    fun addSku(sku: Sku) {
        sku.models.forEach{addModel(it)}
    }

    fun addModel(physicalModel: PhysicalModel) {
        val unitId : Int = physicalModel.id
        if(!modelMap.containsKey(unitId)) {
            modelMap[unitId] = mutableListOf()
            modelMap[unitId]?.add(physicalModel)
        }

    }

    companion object {
        fun load(jsonData : String) : ModelCollection {
            return Json.decodeFromString(jsonData)
        }
    }

}
