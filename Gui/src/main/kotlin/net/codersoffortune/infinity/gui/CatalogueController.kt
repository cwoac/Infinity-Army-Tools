package net.codersoffortune.infinity.gui

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import net.codersoffortune.infinity.SECTORAL
import net.codersoffortune.infinity.collection.ModelCollection
import net.codersoffortune.infinity.collection.PhysicalModel
import net.codersoffortune.infinity.collection.VisibleItem
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.metadata.unit.Unit


class CatalogueController {
    private val database : Database = Database.getInstance()
    private val modelCollection : ModelCollection = Database.getPhysicalModels()
    private var sectoralList : SectoralList = database.sectorals[SECTORAL.Aleph.id]!!
    private var currentUnit : Int = 0

    @FXML
    private lateinit var sectoralChoiceBox: ChoiceBox<SECTORAL>

    @FXML
    private lateinit var unitListView: ListView<Unit>

    @FXML
    private lateinit var existingModelsListView: ListView<PhysicalModel>

    @FXML
    private lateinit var weaponOptionsListView: ListView<VisibleItem>

    @FXML
    private lateinit var skillOptionsListView: ListView<VisibleItem>

    @FXML
    private lateinit var addButton: Button

    @FXML
    fun initialize() {
        sectoralChoiceBox.items.addAll(SECTORAL.values())
        sectoralChoiceBox.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            sectoralList = database.sectorals[sectoralChoiceBox.items[newValue as Int].id]!!
            populateUnitList()
        }
        unitListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentUnit = newValue as Int
            populateOptionLists()
        }

        // add new model button
        addButton.setOnAction { _ ->
            addPhysicalModel()
        }
    }


    private fun addPhysicalModel() {

    }

    private fun populateUnitList() {
        unitListView.items.clear()
        unitListView.items.addAll(sectoralList.units)
    }

    private fun populateOptionLists() {
        weaponOptionsListView.items.clear()
        val filters = sectoralList.mappedFilters
        var visibleWeapons = unitListView.items[currentUnit].getVisibleWeapons(filters)
        weaponOptionsListView.items.addAll(visibleWeapons)
        //modelCollection.modelMap[currentUnit]?.let { profileListView.items.addAll(it) }

    }

}