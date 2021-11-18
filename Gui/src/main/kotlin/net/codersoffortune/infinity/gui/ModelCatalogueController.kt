package net.codersoffortune.infinity.gui

import javafx.fxml.FXML
import javafx.scene.control.*
import net.codersoffortune.infinity.FACTION
import net.codersoffortune.infinity.collection.GuiModel
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.metadata.unit.CompactedUnit
import net.codersoffortune.infinity.metadata.unit.Unit
import net.codersoffortune.infinity.tts.Catalogue
import net.codersoffortune.infinity.tts.EquivalentModelSet
import net.codersoffortune.infinity.tts.ModelSet
import net.codersoffortune.infinity.tts.TTSModel

class ModelCatalogueController {
    private val database: Database = Database.getInstance()
    private lateinit var currentFaction: FACTION
    private lateinit var factionList: SectoralList
    private var currentUnit: Int = -1
    private var currentProfile: Int = -1
    private var currentModel: Int = -1

    private var factionCatalogues: MutableMap<FACTION, Catalogue> = mutableMapOf()
    private var factionModelSets: MutableMap<FACTION, EquivalentModelSet> = mutableMapOf()
    init {
        FACTION.armyFactions.forEach {
            var result = Catalogue()
            result.addUnits(database.sectorals,it, false)
            factionCatalogues[it] = result
            factionModelSets[it] = EquivalentModelSet(result.mappings)
        }
    }
    private var modelSet = ModelSet("resources/model catalogue.json")

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
            changeFaction(currentFaction)
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
        profileListView.items.clear()
        currentUnit = -1
        /*
        Catalogue c = new Catalogue();
            c.addUnits(sectorals, faction, false);
            EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
            ems.addModelSet(modelSet);
            logger.info("Writing JSON");
            String factionJson = c.asJson(faction, ems, doAddons);
            ...
            List<String> units = allUnits.stream().map(u->u.asFactionJSON(ms, doAddons)).collect(Collectors.toList());
         */
        if( missingCheckBox.isSelected ) {
            val catalogue = Catalogue()
            catalogue.addUnits(currentFaction, false)
            unitListView.items.addAll(
                catalogue.modellessList.map { it.compactedUnit.unit }.distinct()
            )
        } else {
            unitListView.items.addAll(factionList.units)
        }

    }

    private fun populateProfileList(unit: Unit)
    {
        profileListView.items.clear()
        profileListView.items.addAll(unit.allDistinctUnits.map { GuiModel(it, currentFaction.armySectoral) })
    }

    private fun populateTTSList(guiUnit: GuiModel)
    {
        ttsModelListView.items.clear()
        ttsModelListView.items.addAll(modelSet.getModels(guiUnit.printableUnit.unitID))
    }

    private fun populateFields(ttsModel: TTSModel)
    {
        decalField.text = ttsModel.decals
        baseImageField.text = ttsModel.baseImage
    }
}