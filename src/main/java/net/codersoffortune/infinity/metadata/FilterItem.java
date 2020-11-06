package net.codersoffortune.infinity.metadata;

public class FilterItem {
    private int id;
    private String name;
    private boolean mercs;
    private String wiki;
    private String type;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FilterItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
