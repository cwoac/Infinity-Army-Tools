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

    private var currentUnitIdx: Int = -1
        set(value) {
            currentUnit = if (value>0) unitListView.items[value] else null
            field = value
        }
    private var currentUnit: Unit? = null

    private var currentProfileIdx: Int = -1
        set(value) {
            currentProfile = if (value>0) profileListView.items[value] else null
            field = value
        }
    private var currentProfile: GuiModel? = null

    private var currentModelIdx: Int = -1
        set(value) {
            currentModel = if (value>0) ttsModelListView.items[value] else null
            field = value
        }
    private var currentModel: TTSModel? = null

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
            currentUnitIdx = newValue as Int
            // Handle the -1 call when we switch factions
            if (currentUnitIdx >= 0)
                populateProfileList()
        }

        profileListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentProfileIdx = newValue as Int
            // Handle the -1 call when we switch factions
            if (currentProfileIdx >= 0)
                populateTTSList()
        }

        ttsModelListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentModelIdx = newValue as Int
            if (currentModelIdx >= 0)
                populateFields()
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

        currentUnitIdx = -1
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

    private fun populateProfileList() {
        clearProfilePane()

        currentUnit?.allDistinctUnits?.let { profileListView.items.addAll(it.map { GuiModel(it, currentFaction.armySectoral) }) }
    }

    private fun populateTTSList() {
        clearTTSPane()
        currentProfile?.let {
            ttsModelListView.items.addAll(modelSet.getModels(currentProfile?.printableUnit!!.unitID))
        }
    }

    private fun populateFields() {
        clearDecalPanes()

        decalField.text = currentModel?.decals
        baseImageField.text = currentModel?.baseImage
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
        currentProfile?.let {
            modelSet.addModel(
                currentProfile!!.printableUnit.unitID,
                DecalBlockModel(currentProfile!!.toString(), "", "")
            )

            currentProfileIdx = ttsModelListView.items.size

            Platform.runLater {
                ttsModelListView.scrollTo(currentProfileIdx)
                ttsModelListView.selectionModel.select(currentProfileIdx)

                populateTTSList()
            }
        }
    }

    private fun removeModel() {
        // Can't remove anything if nothing is selected
        if( ttsModelListView.selectionModel.isEmpty ) return
        currentProfile?.let {
            modelSet.removeModel(
                currentProfile!!.printableUnit.unitID,
                ttsModelListView.selectionModel.selectedItem
            )
            // now reload this model
            populateTTSList()
        }
    }

    private fun saveModels() {
        modelSet.writeFile(modelSetFileName)
    }


    private fun clearUnitPane() {
        currentProfileIdx = -1
        unitListView.items.clear()
        clearProfilePane()
    }

    private fun clearProfilePane() {
        currentModelIdx = -1
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