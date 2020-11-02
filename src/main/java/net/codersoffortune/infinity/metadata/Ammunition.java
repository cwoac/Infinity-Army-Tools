package net.codersoffortune.infinity.metadata;

public class Ammunition {
    private int ID;
    private String name;
    private String wiki;

    public Ammunition() {
    }

    ;

    public Ammunition(int ID, String name) {
        setID(ID);
        setName(name);
    }

    public Ammunition(int ID, String name, String wiki) {
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
