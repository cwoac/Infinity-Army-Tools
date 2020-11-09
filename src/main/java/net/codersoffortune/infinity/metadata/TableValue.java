package net.codersoffortune.infinity.metadata;

public class TableValue {
    private String name;
    private String id;
    private String value;

    public TableValue() {
    }

    public TableValue(String name, String id, String value) {
        setName(name);
        setId(id);
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
