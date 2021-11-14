package net.codersoffortune.infinity.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class InfinityToolsGui extends Application {
    private static Stage stage = null;

    @Override
    public void start(Stage stage) throws IOException {
        Pane anchorPane = FXMLLoader.load(InfinityToolsGui.class.getResource("/Main.fxml"));
        Scene scene = new Scene(anchorPane);
        stage.setTitle("Infinity Tools Gui");
        stage.setScene(scene);
        stage.show();
        InfinityToolsGui.stage = stage;
    }

    public static void switchWindow(String name) throws IOException {
        String fileName = String.format("/%s.fxml", name);
        Pane pane = FXMLLoader.load(InfinityToolsGui.class.getResource(fileName));
        stage.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }

}