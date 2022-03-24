package net.codersoffortune.infinity.metadata.fireteamChart;

import java.util.List;

public class FireteamChartTeam {
    private String name;
    private String obs; // who knows what this is?
    private List<String> type;
    private List<FireteamChartUnit> units;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<FireteamChartUnit> getUnits() {
        return units;
    }

    public void setUnits(List<FireteamChartUnit> units) {
        this.units = units;
    }
}
