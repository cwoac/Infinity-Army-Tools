package net.codersoffortune.infinity.gui


import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException
import net.codersoffortune.infinity.DecalBlock
import net.codersoffortune.infinity.FACTION
import net.codersoffortune.infinity.collection.GuiModel
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.metadata.unit.PrintableUnit
import net.codersoffortune.infinity.metadata.unit.Unit
import net.codersoffortune.infinity.tts.Catalogue
import net.codersoffortune.infinity.tts.DecalBlockModel
import net.codersoffortune.infinity.tts.ModelSet
import net.codersoffortune.infinity.tts.TTSModel
import kotlin.streams.toList


class ModelCatalogueController {
    private val database: Database = Database.getInstance()

    // TODO: should be a static somewhere. Or in Database instance?
    private val modelSetFileName = "resources/model catalogue.json"
    private lateinit var currentFaction: FACTION
    private lateinit var factionList: SectoralList

    private var currentUnitIdx: Int = -1
        set(value) {
            // Do we need to do anything?
            if (value == currentUnitIdx) return

            // put any changes into the modelset before removing them.
            // Have to call updateDecalText as this listener will fire _before_ the field loses focus
            if (currentModelIdx > -1) {
                updateDecalText()
                saveChanges()
            }
            currentUnit = if (value >= 0) unitListView.items[value] else null
            field = value
            if (value >= 0)
                populateProfileList()
        }
    private var currentUnit: Unit? = null

    private var currentProfileIdx: Int = -1
        set(value) {
            // Do we need to do anything?
            if (value == currentProfileIdx) return

            // put any changes into the modelset before removing them.
            // Have to call updateDecalText as this listener will fire _before_ the field loses focus
            if (currentModelIdx > -1) {
                updateDecalText()
                saveChanges()
            }
            currentProfile = if (value >= 0) profileListView.items[value] else null
            field = value
            if (currentProfileIdx >= 0)
                populateFormList()
        }
    private var currentProfile: GuiModel? = null

    private var currentFormIdx: Int = -1
        set(value) {
            currentForm = if (value >= 0) formListView.items[value] else null
            field = value
            if (currentFormIdx >= 0)
                populateTTSList()
        }
    private var currentForm: PrintableUnit? = null

    private var currentModelIdx: Int = -1
        set(value) {
            currentModel = if (value >= 0) ttsModelListView.items[value] else null
            field = value
            if (currentModelIdx >= 0)
                populateFields()
        }
    private var currentModel: TTSModel? = null

    private var modelSet = ModelSet(modelSetFileName)

    @FXML
    private lateinit var factionChoiceBox: ChoiceBox<FACTION>

    @FXML
    private lateinit var missingCheckBox: CheckBox

    @FXML
    private lateinit var mercCheckBox: CheckBox

    @FXML
    private lateinit var unitListView: ListView<Unit>

    @FXML
    private lateinit var profileListView: ListView<GuiModel>

    @FXML
    private lateinit var formListView: ListView<PrintableUnit>

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
        }

        profileListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentProfileIdx = newValue as Int
        }

        formListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentFormIdx = newValue as Int
        }

        ttsModelListView.selectionModel.selectedIndexProperty().addListener { _, _, newValue ->
            currentModelIdx = newValue as Int
        }

        missingCheckBox.selectedProperty().addListener { _ ->
            // force reload of the pane.
            changeFaction(currentFaction)
        }

        mercCheckBox.selectedProperty().addListener { _ ->
            // force reload of the pane.
            changeFaction(currentFaction)
        }

        decalField.focusedProperty().addListener { _, _, newValue ->
            // only fire on de-focus
            if (!newValue)
                updateDecalText()
        }

        baseImageField.focusedProperty().addListener { _, _, newValue ->
            // only fire on de-focus
            if (!newValue)
                updateDecalText()
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
            writeModelSetToDisk()
        }
    }


    private fun changeFaction(faction: FACTION) {
        clearUnitPane()
        currentUnitIdx = -1

        currentFaction = faction
        factionList = database.sectorals[currentFaction.armySectoral.id]!!

        if (missingCheckBox.isSelected) {
            val catalogue = Catalogue()
            catalogue.addUnits(currentFaction, mercCheckBox.isSelected)
            unitListView.items.addAll(
                catalogue.modellessList.map { it.compactedUnit.unit }.distinct()
            )
        } else {
            if (mercCheckBox.isSelected) {
                unitListView.items.addAll(factionList.units)
            } else {
                // exclude units over 10000 as they are mercs
                unitListView.items.addAll(
                    factionList.units.stream().filter { it.id < 10000 }.toList()
                )
            }
        }
        if (unitListView.items.isNotEmpty()) {
            unitListView.items.sortBy { it.id }
            unitListView.selectionModel.select(0)
            currentUnitIdx = 0
        }

    }

    private fun populateProfileList() {
        clearProfilePane()
        currentUnit?.allDistinctUnits?.let {
            profileListView.items.addAll(it.map { jt ->
                GuiModel(
                    jt,
                    currentFaction.armySectoral
                )
            })
        }
        if (profileListView.items.isNotEmpty()) {
            profileListView.selectionModel.select(0)
            currentProfileIdx = 0
        }
    }

    /**
     * When we change profile, get a list of all the associated printable units.
     * Only really relevant for transmutable models tbh.
     */
    private fun populateFormList() {
        clearFormPane()
        currentProfile?.let {
            formListView.items.addAll(currentProfile!!.printableUnits)
        }

        if (formListView.items.isNotEmpty()) {
            formListView.selectionModel.select(0)
            currentFormIdx = 0
        }
    }

    private fun populateTTSList() {
        clearTTSPane()
        currentForm?.let {
            ttsModelListView.items.addAll(modelSet.getModels(currentForm!!.unitID))
        }

        if (ttsModelListView.items.isNotEmpty()) {
            ttsModelListView.selectionModel.select(0)
            currentModelIdx = 0
        }
    }

    private fun populateFields() {

        clearDecalPanes()

        decalField.text = currentModel?.decals
        baseImageField.text = currentModel?.baseImage
        updateDecalImages()
    }

    /**
     * Update the modelset with a (potentially) changed set of TTSModels
     */
    private fun saveChanges() {
        currentProfile?.let {
            modelSet.setModels(
                currentProfile!!.printableUnit.unitID,
                ttsModelListView.items
            )
        }
    }

    // Called when decal text field has been changed
    private fun updateDecalText() {
        currentModel?.let {
            try {
                currentModel!!.decals = Json.parseToJsonElement(decalField.text).toString()
                decalField.text = currentModel!!.decals
            } catch (_: SerializationException) {
                // TODO: Kotlin logging.
                currentModel!!.decals = decalField.text
            }
            try {
                currentModel!!.baseImage = Json.parseToJsonElement(baseImageField.text).toString()
                baseImageField.text = currentModel!!.baseImage
            } catch (_: SerializationException) {
                currentModel!!.baseImage = baseImageField.text
            }

            // TODO:: Some form of validation here?
            updateDecalImages()
        }
    }

    // called when we may need to display new decal images
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
        currentForm?.let {
            modelSet.addModel(currentForm!!.unitID, DecalBlockModel(currentForm!!.toString(), "", ""))
        }

        Platform.runLater {
            populateTTSList()
            currentModelIdx = ttsModelListView.items.size - 1
            ttsModelListView.scrollTo(currentProfileIdx)
            ttsModelListView.selectionModel.select(currentProfileIdx)
        }
    }


    private fun removeModel() {
        // Can't remove anything if nothing is selected
        if (ttsModelListView.selectionModel.isEmpty) return
        if (currentProfile == null) return

        // remove selected model
        ttsModelListView.items.remove(
            ttsModelListView.selectionModel.selectedItem
        )

        // update the database with the new set of models.
        saveChanges()

        currentModelIdx = -1

        // now reload this model. Should be a no-op
        populateTTSList()
    }

    private fun writeModelSetToDisk() {
        modelSet.writeFile(modelSetFileName)
        Alert(Alert.AlertType.INFORMATION, "Catalogue updated.").show()
    }


    private fun clearUnitPane() {
        clearProfilePane()

        currentProfileIdx = -1
        unitListView.items.clear()
    }

    private fun clearProfilePane() {
        clearFormPane()

        currentFormIdx = -1
        profileListView.items.clear()
    }

    private fun clearFormPane() {
        clearTTSPane()

        currentModelIdx = -1
        formListView.items.clear()
    }

    private fun clearTTSPane() {
        clearDecalPanes()
        ttsModelListView.items.clear()
    }

    private fun clearDecalPanes() {
        frontImage.image = null
        rearImage.image = null
        decalField.text = ""
        baseImageField.text = ""
    }
}