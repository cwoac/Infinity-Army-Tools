package net.codersoffortune.infinity.gui


import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import net.codersoffortune.infinity.DecalBlock
import net.codersoffortune.infinity.FACTION
import net.codersoffortune.infinity.collection.GuiModel
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.metadata.unit.Unit
import net.codersoffortune.infinity.tts.Catalogue
import net.codersoffortune.infinity.tts.DecalBlockModel
import net.codersoffortune.infinity.tts.ModelSet
import net.codersoffortune.infinity.tts.TTSModel


class ModelCatalogueController {
    private val database: Database = Database.getInstance()
    // TODO: should be a static somewhere. Or in Database instance?
    private val modelSetFileName = "resources/model catalogue.json"
    private lateinit var currentFaction: FACTION
    private lateinit var factionList: SectoralList
    private var currentUnit: Int = -1
    private var currentProfile: Int = -1
    private var currentModel: Int = -1

    private var modelSet = ModelSet(modelSetFileName)

    @FXML
    private lateinit var factionChoiceBox: ChoiceBox<FACTION>

    @FXML
    private lateinit var missingCheckBox: CheckBox

    @FXML
    private lateinit var unitListView: ListView<Unit>

    @FXML
    private lateinit var profileListView: ListView<GuiModel>

    @FXML
    private lateinit var ttsModelListView: ListView<TTSModel>

    @FXML
    private lateinit var returnButton: Button

    @FXML
    private lateinit var decalField: TextArea

    @FXML
    private lateinit var baseImageField: TextArea

    @FXML
    private lateinit var frontImage: ImageView

    @FXML
    private lateinit var rearImage: ImageView

    @FXML
    private lateinit var addModelButton: Button

    @FXML
    private lateinit var removeModelButton: Button

    @FXML
    private lateinit var saveModelButton: Button

    @FXML
    fun initialize() {
        factionChoiceBox.items.addAll(FACTION.armyFactions)
        factionChoiceBox.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            changeFaction(factionChoiceBox.items[newValue as Int])
        }

        unitListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentUnit = newValue as Int
            // Handle the -1 call when we switch factions
            if (currentUnit >= 0)
                populateProfileList(unitListView.items[newValue])
        }

        profileListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentProfile = newValue as Int
            // Handle the -1 call when we switch factions
            if (currentProfile >= 0)
                populateTTSList(profileListView.items[newValue])
        }

        ttsModelListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentModel = newValue as Int
            if (currentModel >= 0)
                populateFields(ttsModelListView.items[currentModel])
        }

        missingCheckBox.selectedProperty().addListener { _ ->
            // force reload of the pane.
            changeFaction(currentFaction)
        }

        decalField.textProperty().addListener { _, _, _ ->
            updateDecalImages()
        }

        // go back to main menu
        returnButton.setOnAction {
            InfinityToolsGui.switchWindow("Main")
        }

        removeModelButton.setOnAction {
            removeModel()
        }

        addModelButton.setOnAction {
            addModel()
        }

        saveModelButton.setOnAction {
            saveModels()
        }
    }


    private fun changeFaction(faction: FACTION) {
        currentFaction = faction
        factionList = database.sectorals[currentFaction.armySectoral.id]!!

        currentUnit = -1
        clearUnitPane()

        if (missingCheckBox.isSelected) {
            val catalogue = Catalogue()
            catalogue.addUnits(currentFaction, false)
            unitListView.items.addAll(
                catalogue.modellessList.map { it.compactedUnit.unit }.distinct()
            )
        } else {
            unitListView.items.addAll(factionList.units)
        }

    }

    private fun populateProfileList(unit: Unit) {
        clearProfilePane()

        profileListView.items.addAll(unit.allDistinctUnits.map { GuiModel(it, currentFaction.armySectoral) })

    }

    private fun populateTTSList(guiUnit: GuiModel) {
        clearTTSPane()

        ttsModelListView.items.addAll(modelSet.getModels(guiUnit.printableUnit.unitID))
    }

    private fun populateFields(ttsModel: TTSModel) {
        clearDecalPanes()

        decalField.text = ttsModel.decals
        baseImageField.text = ttsModel.baseImage
        updateDecalImages()
    }

    private fun updateDecalImages() {
        val decals = DecalBlock(decalField.text).getDecals()
        val decalImages = decals.map { Image(it.getImageStream(), 100.0, 250.0, true, false) }.toList()

        if (decalImages.isNotEmpty()) {
            frontImage.image = decalImages[0]
            if (decalImages.size > 1) {
                rearImage.image = decalImages[1]
            }
        }
    }

    private fun addModel() {
        // make sure we have a profile to add to
        if (currentProfile < 0 ) return
        modelSet.addModel(profileListView.items[currentProfile].printableUnit.unitID,
                          DecalBlockModel(profileListView.items[currentProfile].toString(),"",""))

        val newIdx = ttsModelListView.items.size
        Platform.runLater {
            ttsModelListView.scrollTo(newIdx)
            ttsModelListView.selectionModel.select(newIdx)
            populateTTSList(profileListView.items[currentProfile])
        }
    }

    private fun removeModel() {
        // Can't remove anything if nothing is selected
        if( ttsModelListView.selectionModel.isEmpty ) return
        modelSet.removeModel(profileListView.items[currentProfile].printableUnit.unitID, ttsModelListView.selectionModel.selectedItem)
        // now reload this model
        populateTTSList(profileListView.items[currentProfile])
    }

    private fun saveModels() {
        modelSet.writeFile(modelSetFileName)
    }


    private fun clearUnitPane() {
        currentProfile = -1
        unitListView.items.clear()
        clearProfilePane()
    }

    private fun clearProfilePane() {
        currentModel = -1
        profileListView.items.clear()
        clearTTSPane()
    }

    private fun clearTTSPane() {
        ttsModelListView.items.clear()
        clearDecalPanes()
    }

    private fun clearDecalPanes() {
        frontImage.image = null
        rearImage.image = null
        decalField.text = ""
        baseImageField.text = ""
    }
}