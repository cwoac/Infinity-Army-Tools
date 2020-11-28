package net.codersoffortune.infinity.metadata;

public class FireteamUnit {
    // {
    //                    "unit": 30,
    //                    "min": 1,
    //                    "profile": 1,
    //                    "option": 5
    //                }
    private int unit;
    private int min;
    private int max;
    private int profile;
    private int option;

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getProfile() {
        return profile;
    }

    public void setProfile(int profile) {
        this.profile = profile;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
