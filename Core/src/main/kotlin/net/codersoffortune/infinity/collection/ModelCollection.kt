package net.codersoffortune.infinity.collection
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.codersoffortune.infinity.FACTION
import java.io.File

@Serializable
// TODO: replace first element of map with Faction.
data class ModelCollection(val modelMap: MutableMap<Int,MutableMap<Int, MutableList<PhysicalModel>>>) {

    fun addSku(faction: FACTION, sku: Sku) {
        sku.models.forEach{addModel(faction, it)}
    }

    fun addModel(faction: FACTION, physicalModel: PhysicalModel) {
        val unitId : Int = physicalModel.id
        val factionId : Int = faction.id
        modelMap.getOrPut(factionId) { mutableMapOf()}.getOrPut(unitId) { mutableListOf()}.add(physicalModel)
    }

    fun getModels(faction:FACTION, unitIdx: Int) : MutableList<PhysicalModel>? {
        return modelMap[faction.id]?.get(unitIdx)
    }

    fun save() {
        val repr = Json.encodeToString(this)
        File("resources/physical_models.json").writeText(repr)
    }

    companion object {
        fun load(jsonData : String) : ModelCollection {
            return Json.decodeFromString(jsonData)
        }
    }

}
