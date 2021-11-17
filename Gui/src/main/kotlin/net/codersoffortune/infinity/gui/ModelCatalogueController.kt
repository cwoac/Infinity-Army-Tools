package net.codersoffortune.infinity.gui

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ListView
import net.codersoffortune.infinity.FACTION
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.metadata.unit.Unit

class ModelCatalogueController {
    private val database: Database = Database.getInstance()
    private lateinit var currentFaction: FACTION
    private lateinit var factionList: SectoralList
    private var currentUnit: Int = -1

    @FXML
    private lateinit var factionChoiceBox: ChoiceBox<FACTION>

    @FXML
    private lateinit var missingCheckBox: CheckBox

    @FXML
    private lateinit var unitListView: ListView<Unit>

    @FXML
    private lateinit var returnButton: Button

    @FXML
    fun initialize() {
        factionChoiceBox.items.addAll(FACTION.armyFactions)
        factionChoiceBox.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            changeFaction(factionChoiceBox.items[newValue as Int])
        }

        // go back to main menu
        returnButton.setOnAction {
            InfinityToolsGui.switchWindow("Main")
        }
    }


    private fun changeFaction(faction: FACTION)
    {
        currentFaction = faction
        factionList = database.sectorals[currentFaction.armySectoral.id]!!
        unitListView.items.clear()
        unitListView.items.addAll(factionList.units)
        currentUnit = -1
    }
}