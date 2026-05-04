package net.codersoffortune.infinity.gui

import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.*
import net.codersoffortune.infinity.armylist.Armylist
import net.codersoffortune.infinity.db.Database
import net.codersoffortune.infinity.metadata.SectoralList
import net.codersoffortune.infinity.tts.Catalogue
import net.codersoffortune.infinity.tts.EquivalentModelSet
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class MainController {
    private var db = Database.getInstance()

    @FXML
    private lateinit var versionLabel: Label

    @FXML
    private lateinit var updateDBIndicator: ProgressIndicator

    @FXML
    private lateinit var armyCodeTF: TextField

    @FXML
    private lateinit var enableAddons: CheckBox

    @FXML
    private fun initialize() {
        versionLabel.text = db.version
        updateDBIndicator.isVisible = false
        enableAddons.isSelected = false
    }

    @FXML
    fun updateClicked(e: Event) {
        val curVersion = db.version
        updateDBIndicator.isVisible = true
        db = Database.updateAll()
        val newVersion = db.version
        versionLabel.text = newVersion
        updateDBIndicator.isVisible = false

        Alert(Alert.AlertType.INFORMATION, "DB updated from $curVersion to $newVersion").show()
    }

    @FXML
    fun writeBoxes(e: Event) {
        val skipped = db.writeJson(File("output"), enableAddons.isSelected)
        Alert(Alert.AlertType.INFORMATION, "Files written to output/").show()
        if (skipped.isNotEmpty()) {
            val summary = skipped.entries.joinToString("\n") { (key, value) ->
                "$key: $value unit(s) skipped"
            }
            showLongAlert(Alert.AlertType.WARNING, "Units without models were skipped:\n$summary")
        }
    }

    @FXML
    fun writeMissing(e: Event) {
        db.writeMissing(File("missing"))
    }

    @FXML
    fun readMissing(e: Event) {
        db.readMissing(File("missing"))
    }

    @FXML
    fun writeDuplicates(e: Event) {
        db.writeDuplicates(File("duplicates"))
    }

    @FXML
    fun generateArmy(e: Event) {
        val al: Armylist
        try {
            al = Armylist.fromArmyCode(armyCodeTF.text)
        } catch (exception: IllegalArgumentException) {
            showLongAlert(Alert.AlertType.ERROR, "Failed to decode armylist:\n${exception.message}")
            return
        }

        val sl: SectoralList = db.sectorals[al.sectoral.id]!!
        val c = Catalogue()
        c.addUnits(sl, al.sectoral, false)
        val ems = EquivalentModelSet(c.mappings)
        ems.addModelSet(Database.getModelSet())

        val json: String
        try {
            json = al.asJson(ems, enableAddons.isSelected)
        } catch (exception: IllegalArgumentException) {
            showLongAlert(Alert.AlertType.ERROR, "Failed to decode army list:\n${exception.message}")
            return
        }

        Files.createDirectories(Paths.get("army lists"))
        File("army lists/AL ${al.sectoralName} ${al.army_name}.json").bufferedWriter(StandardCharsets.UTF_8).use {
            it.append(json)
        }
        armyCodeTF.text = "Done"
    }

    fun ttsCatalogEdit(actionEvent: ActionEvent) {
        InfinityToolsGui.switchWindow("ModelCatalogue")
    }

    fun physicalCatalogEdit(actionEvent: ActionEvent) {
        InfinityToolsGui.switchWindow("PhysicalCatalogue")
    }

    companion object {
        fun showLongAlert(type: Alert.AlertType, message: String) {
            val label = Label(message)
            label.isWrapText = true
            val alert = Alert(type)
            alert.dialogPane.content = label
            alert.show()
        }
    }
}
