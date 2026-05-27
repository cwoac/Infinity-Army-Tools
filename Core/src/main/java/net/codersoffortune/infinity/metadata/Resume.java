package net.codersoffortune.infinity.metadata;

import java.util.List;

public class Resume {
    // {"id":161,"isc":"Yáozăo","idArmy":38,"name":"YÁOZĂO","slug":"yaozao","logo":"https://assets.corvusbelli.net/army/img/logo/units/yaozao-1-1.svg","type":5,"category":8},
    private int id;
    private String isc;
    private int idArmy;
    private String name;
    private String slug;
    private String logo;
    private int type;
    private int category;
    private List<Integer> chars;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<Integer> getChars() {
        return chars;
    }

    public void setChars(List<Integer> chars) {
        this.chars = chars;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdArmy() {
        return idArmy;
    }

    public void setIdArmy(int idArmy) {
        this.idArmy = idArmy;
    }

    public String getIsc() {
        return isc;
    }

    public void setIsc(String isc) {
        this.isc = isc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
