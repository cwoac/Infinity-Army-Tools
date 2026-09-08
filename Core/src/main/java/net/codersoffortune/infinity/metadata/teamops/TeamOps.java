package net.codersoffortune.infinity.metadata.teamops;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class TeamOps {
    private List<JsonNode> units;

    public List<JsonNode> getUnits() {
        return units;
    }

    public void setUnits(List<JsonNode> units) {
        this.units = units;
    }
}
