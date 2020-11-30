package net.codersoffortune.infinity.gui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.metadata.SectoralList;
import net.codersoffortune.infinity.tts.Catalogue;
import net.codersoffortune.infinity.tts.EquivalentModelSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainController {
    private Database db = Database.getInstance();

    @FXML
    private Label versionLabel;

    @FXML
    private ProgressIndicator updateDBIndicator;

    @FXML
    private TextField armyCodeTF;

    @FXML
    private CheckBox enableAddons;

    public MainController() throws IOException {
    }

    @FXML
    private void initialize() {
        versionLabel.setText(db.getVersion());
        updateDBIndicator.setVisible(false);
        enableAddons.setSelected(false);
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
        db.writeJson(new File("output"), enableAddons.isSelected());
    }

    @FXML
    public void writeMissing(Event e) throws IOException {
        // TODO:: report whether there are any missing.
        boolean anyMissing = db.writeMissing(new File("missing"));

    }

    @FXML
    public void readMissing(Event e) throws IOException {
        // TODO:: report outcome.
        db.readMissing(new File("missing"));
    }

    @FXML
    public void writeDuplicates(Event e) throws IOException {
        db.writeDuplicates(new File("duplicates"));
    }

    @FXML
    public void generateArmy(Event e) throws IOException {
        Armylist al = Armylist.fromArmyCode(armyCodeTF.getText());
        SectoralList sl = db.getSectorals().get(al.getSectoral().getId());
        MappedFactionFilters filters = new MappedFactionFilters(sl.getFilters());
        Catalogue c = new Catalogue();
        c.addUnits(sl, al.getSectoral(), false);
        EquivalentModelSet ems = new EquivalentModelSet(c.getMappings());
        ems.addModelSet(Database.getModelSet());
        String json = al.asJson(filters, ems, enableAddons.isSelected());
        BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("output/AL %s %s.json", al.getSectoralName(), al.getArmy_name())));
        writer.append(json);
        writer.close();
        armyCodeTF.setText("Done");
    }
}
