package net.codersoffortune.infinity.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.metadata.specops.Specops;

import java.io.IOException;
import java.util.List;

public class FactionList {
    private String version;
    private List<Unit> units;
    private FactionFilters filters;
    private List<Resume> resume;
    private List<String> fireteams; // TODO:: Is this ever used?
    private List<String> relations; // TODO:: Is this ever used?
    private Specops specops;

    public static FactionList loadFaction(String url) throws IOException {
        ObjectMapper om = new ObjectMapper();
        //data = om.readValue(new java.net.URL(url), Map.class);
        return om.readValue(FactionList.class.getResource("/" + url), FactionList.class);
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

    public void setFilters(FactionFilters filters) {
        this.filters = filters;
    }

    public List<Resume> getResume() {
        return resume;
    }

    public void setResume(List<Resume> resume) {
        this.resume = resume;
    }

    public List<String> getRelations() {
        return relations;
    }

    public void setRelations(List<String> relations) {
        this.relations = relations;
    }

    public List<String> getFireteams() {
        return fireteams;
    }

    public void setFireteams(List<String> fireteams) {
        this.fireteams = fireteams;
    }

    public Specops getSpecops() {
        return specops;
    }

    public void setSpecops(Specops specops) {
        this.specops = specops;
    }
}
