package net.codersoffortune.infinity.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.codersoffortune.infinity.db.Database;

import java.io.IOException;

public class InfinityToolsGui extends Application {
    private Database db;

    @Override
    public void start(Stage stage) throws IOException {
        db = Database.getInstance();

//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Scene scene = new Scene(new StackPane(l), 640, 480);
//        stage.setScene(scene);
//        stage.setTitle("Infinity Tools Gui");
//        stage.show();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InfinityToolsGui.class.getResource("/Main.fxml"));
        VBox anchorPane = loader.<VBox>load();
        Scene scene = new Scene(anchorPane);
        stage.setTitle("Infinity Tools Gui");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}