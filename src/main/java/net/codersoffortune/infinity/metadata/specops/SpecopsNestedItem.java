package net.codersoffortune.infinity.metadata.specops;

public class SpecopsNestedItem {
    // for some reason, nested items are of the form {"id":XX} rather than just XX
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
