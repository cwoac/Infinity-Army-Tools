package net.codersoffortune.infinity.gui

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ChoiceBox
import javafx.stage.Stage
import net.codersoffortune.infinity.FACTION
import net.codersoffortune.infinity.db.Database


class CatalogueController {
    private val database : Database = Database.getInstance()

    @FXML
    private lateinit var factionChoiceBox: ChoiceBox<String>

    @FXML
    fun initialize() {
        enumValues<FACTION>().forEach { factionChoiceBox.items.add(it.name) }
    }


}