package net.codersoffortune.infinity.metadata;

public class RangeBand {
    private int max;
    private String mod;


    public RangeBand() {
    }

    public RangeBand(int max, String mod) {
        setMax(max);
        setMod(mod);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }
}
