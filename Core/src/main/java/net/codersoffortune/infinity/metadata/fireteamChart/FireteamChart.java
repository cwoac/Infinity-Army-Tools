package net.codersoffortune.infinity.metadata.fireteamChart;

import java.util.List;

public class FireteamChart {
    private FireteamChartSpec spec;
    private String desc;
    private List<FireteamChartTeam> teams;

    public FireteamChartSpec getSpec() {
        return spec;
    }

    public void setSpec(FireteamChartSpec spec) {
        this.spec = spec;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<FireteamChartTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<FireteamChartTeam> teams) {
        this.teams = teams;
    }
}
