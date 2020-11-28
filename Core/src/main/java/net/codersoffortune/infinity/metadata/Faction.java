package net.codersoffortune.infinity.metadata;

public class Faction {
    private int ID;
    private int parent;
    private String name;
    private String slug;
    private String logo;

    public Faction() {
    }

    public Faction(int ID, int parent, String name, String slug, String logo) {
        this.ID = ID;
        this.parent = parent;
        this.name = name;
        this.slug = slug;
        this.logo = logo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
