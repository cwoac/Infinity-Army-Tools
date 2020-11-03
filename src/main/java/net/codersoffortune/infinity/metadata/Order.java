package net.codersoffortune.infinity.metadata;

public class Order {
    // "type":"REGULAR","list":1,"total":1
    private String type;
    private int list;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getList() {
        return list;
    }

    public void setList(int list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
