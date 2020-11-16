package net.codersoffortune.infinity.metadata.unit;

import java.util.Comparator;
import java.util.List;

public class ProfileGroup {
    // "id":1,"category":8,"isc":"Yáozăo","notes":null,
    //  "options":[{"id":1,"chars":[],"disabled":false,"equip":[],"minis":0,"orders":[],"includes":[],"points":3,"swc":"0","weapons":[{"extra":[6],"id":71,"order":1}],"name":"YÁOZĂO","skills":[],"peripheral":[]}]}
    private int id;
    private int category;
    private String isc;
    private String notes;
    private List<Profile> profiles;
    private List<ProfileOption> options;

    public List<Profile> getProfiles() {
        return profiles;
    }

    public ProfileOption getOptionOne() { return options.stream()
            .filter(x->x.getId()==1)
            .findFirst()
            .orElse(options.get(0));
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIsc() {
        return isc;
    }

    public void setIsc(String isc) {
        this.isc = isc;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ProfileOption> getOptions() {
        return options;
    }

    public void setOptions(List<ProfileOption> options) {
        this.options = options;
    }
}
