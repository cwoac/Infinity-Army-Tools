package net.codersoffortune.infinity.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.codersoffortune.infinity.metadata.specops.Specops;
import net.codersoffortune.infinity.metadata.specops.SpecopsNestedItem;
import net.codersoffortune.infinity.metadata.specops.SpecopsNestedItemDeserializer;
import net.codersoffortune.infinity.metadata.unit.Unit;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SectoralList {
    private String version;
    private List<Unit> units;
    private FactionFilters filters;
    private List<Resume> resume;
    private List<Fireteam> fireteams; // TODO:: Is this ever used?
    private List<Relation> relations; // TODO:: Is this ever used?
    private Specops specops;

    public static SectoralList load(String url) throws IOException {
        ObjectMapper om = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
        sm.addDeserializer(SpecopsNestedItem.class, new SpecopsNestedItemDeserializer());
        om.registerModule(sm);
        return om.readValue(new File(String.format("resources/%s.json",url)), SectoralList.class);
    }

    public Optional<Unit> getUnit(int id) {
        return units.stream().filter(x -> x.getID() == id).findFirst();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public FactionFilters getFilters() {
        return filters;
    }

    public MappedFactionFilters getMappedFilters() {
        return new MappedFactionFilters(filters);
    }

    public void setFilters(FactionFilters filters) {
        this.filters = filters;
    }

    public List<Resume> getResume() {
        return resume;
    }

    public void setResume(List<Resume> resume) {
        this.resume = resume;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<Fireteam> getFireteams() {
        return fireteams;
    }

    public void setFireteams(List<Fireteam> fireteams) {
        this.fireteams = fireteams;
    }

    public Specops getSpecops() {
        return specops;
    }

    public void setSpecops(Specops specops) {
        this.specops = specops;
    }
}
