package net.codersoffortune.infinity.metadata;

public class Equipment {
    private int ID;
    private String name;
    private String wiki;

    public Equipment() {
    }

    public Equipment(int ID, String name, String wiki) {
        setID(ID);
        setName(name);
        setWiki(wiki);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWiki() {
        return wiki;
    }

    public void setWiki(String wiki) {
        this.wiki = wiki;
    }
}
