package net.codersoffortune.infinity.metadata.specops;

public class SpecopsNestedItem {
    // for some reason, nested items are of the form {"id":XX} rather than just XX.. usually.
    private int id;

    public SpecopsNestedItem() {}

    public SpecopsNestedItem(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
