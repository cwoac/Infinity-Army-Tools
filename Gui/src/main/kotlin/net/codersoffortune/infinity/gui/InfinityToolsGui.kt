package net.codersoffortune.infinity.gui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.Pane
import javafx.stage.Stage
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator

class InfinityToolsGui : Application() {
    override fun start(stage: Stage) {
        val pane = FXMLLoader.load<Pane>(InfinityToolsGui::class.java.getResource("/Main.fxml"))
        stage.scene = Scene(pane)
        stage.title = "Infinity Tools Gui"
        stage.show()
        Companion.stage = stage
    }

    companion object {
        private var stage: Stage? = null

        @JvmStatic
        fun main(args: Array<String>) {
            Configurator.setRootLevel(Level.DEBUG)
            launch(InfinityToolsGui::class.java)
        }

        fun switchWindow(name: String) {
            val pane = FXMLLoader.load<Pane>(InfinityToolsGui::class.java.getResource("/$name.fxml"))
            stage!!.scene.root = pane
        }
    }
}
