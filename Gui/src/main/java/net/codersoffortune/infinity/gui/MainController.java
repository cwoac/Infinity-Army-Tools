package net.codersoffortune.infinity.gui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import net.codersoffortune.infinity.db.Database;

import java.io.File;
import java.io.IOException;

public class MainController {
    private Database db = Database.getInstance();

    @FXML
    private Label versionLabel;

    @FXML
    private ProgressIndicator updateDBIndicator;

    public MainController() throws IOException {
    }

    @FXML
    private void initialize() {
        versionLabel.setText(db.getVersion());
        updateDBIndicator.setVisible(false);
    }

    @FXML
    public void updateClicked(Event e) throws IOException {
        versionLabel.setText("Updating");
        updateDBIndicator.setVisible(true);
        db = Database.updateAll();
        versionLabel.setText("Updated");
        updateDBIndicator.setVisible(false);
    }

    @FXML
    public void writeBoxes(Event e) throws IOException {
        db.writeJson(new File("output"));
    }
}
