package net.codersoffortune.infinity.metadata.fireteamChart;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FireteamChartSpec {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String CORE;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String HARIS;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private String DUO;

    public FireteamChartSpec() {
    }

    public String getCORE() {
        return CORE;
    }

    public void setCORE(String CORE) {
        this.CORE = CORE;
    }

    public String getHARIS() {
        return HARIS;
    }

    public void setHARIS(String HARIS) {
        this.HARIS = HARIS;
    }

    public String getDUO() {
        return DUO;
    }

    public void setDUO(String DUO) {
        this.DUO = DUO;
    }
}
