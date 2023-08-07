package net.codersoffortune.infinity.metadata;

public class SWCFilterItem {
    private String id;
    private String name;
    private boolean mercs;
    private String wiki;
    private String type;
    private boolean specops;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }

    public boolean isMercs() {
        return mercs;
    }

    public void setMercs(boolean mercs) {
        this.mercs = mercs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FilterItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public boolean isSpecops() {
        return specops;
    }

    public void setSpecops(boolean specops) {
        this.specops = specops;
    }
}
