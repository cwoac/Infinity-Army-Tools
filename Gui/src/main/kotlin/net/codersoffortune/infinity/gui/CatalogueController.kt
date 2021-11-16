package net.codersoffortune.infinity.gui

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import net.codersoffortune.infinity.SECTORAL
import net.codersoffortune.infinity.collection.ModelCollection
import net.codersoffortune.infinity.collection.PhysicalModel
import net.codersoffortune.infinity.collection.UniversalFilters
import net.codersoffortune.infinity.collection.VisibleItem
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.MappedFactionFilters
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.metadata.unit.Unit


class CatalogueController {
    private val database: Database = Database.getInstance()
    private val modelCollection: ModelCollection = Database.getPhysicalModels()
    private var currentSectoral: SECTORAL = SECTORAL.Aleph
    private var sectoralList: SectoralList = database.sectorals[currentSectoral.id]!!
    private var currentUnit: Int = 0
    private var currentModels: MutableList<PhysicalModel> = mutableListOf()


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
    private lateinit var clearButton: Button

    @FXML
    private lateinit var saveButton: Button

    @FXML
    private lateinit var writeButton: Button

    @FXML
    private lateinit var returnButton: Button

    @FXML
    fun initialize() {

        sectoralChoiceBox.items.addAll(SECTORAL.values())
        sectoralChoiceBox.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentSectoral = sectoralChoiceBox.items[newValue as Int]
            changeSectoral()
        }

        unitListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentUnit = newValue as Int
            changeUnit()
        }

        // Can't quite figure out how to set this from the fxml
        weaponOptionsListView.selectionModel.selectionMode = SelectionMode.MULTIPLE
        skillOptionsListView.selectionModel.selectionMode = SelectionMode.MULTIPLE

        // add new model button
        addButton.setOnAction {
            addPhysicalModel()
        }

        // abort changes
        clearButton.setOnAction {
            currentModels = mutableListOf()
            populateModelList()
        }

        // save changes to (in memory) database
        saveButton.setOnAction {
            saveNewModels()
        }

        // write saved changes to disk
        writeButton.setOnAction {
            modelCollection.save()
        }

        // go back to main menu
        // TODO:: add confirm if we have unwritten changes
        returnButton.setOnAction {
            InfinityToolsGui.switchWindow("Main")
        }
    }


    private fun addPhysicalModel() {
        currentModels.add(
            PhysicalModel(
                sectoralList.units[currentUnit].isc,
                currentUnit,
                weaponOptionsListView.selectionModel.selectedItems,
                skillOptionsListView.selectionModel.selectedItems
            )
        )
        weaponOptionsListView.selectionModel.clearSelection()
        skillOptionsListView.selectionModel.clearSelection()
        populateModelList()
    }

    private fun saveNewModels() {
        currentModels.forEach { modelCollection.addModel(currentSectoral.parent, it) }
        changeUnit()
    }

    private fun changeSectoral() {
        sectoralList = database.sectorals[currentSectoral.id]!!
        unitListView.items.clear()
        unitListView.items.addAll(sectoralList.units)
        currentUnit = 0
        changeUnit()
    }

    private fun changeUnit() {
        currentModels = mutableListOf()
        populateOptionLists()
        populateModelList()
    }

    private fun populateModelList() {
        existingModelsListView.items.clear()
        modelCollection.getModels(currentSectoral.parent,currentUnit)?.let {
            existingModelsListView.items.addAll(
                it
            )
        }
        existingModelsListView.items.addAll(currentModels)
    }

    private fun populateOptionLists() {
        weaponOptionsListView.items.clear()
        val visibleWeapons = unitListView.items[currentUnit].visibleWeapons
        weaponOptionsListView.items.addAll(visibleWeapons)

        skillOptionsListView.items.clear()
        val visibleSkills = unitListView.items[currentUnit].visibleSkills
        skillOptionsListView.items.addAll(visibleSkills)


    }

}