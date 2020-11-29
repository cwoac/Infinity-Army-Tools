package net.codersoffortune.infinity.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class InfinityToolsGui extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InfinityToolsGui.class.getResource("/Main.fxml"));
        VBox anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setTitle("Infinity Tools Gui");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}