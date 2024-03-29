package net.codersoffortune.infinity.metadata;

import java.util.Objects;

public class Order {
    // "type":"REGULAR","list":1,"total":1
    private String type;
    private int list;
    private int total;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return list == order.list &&
                total == order.total &&
                type.equals(order.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, list, total);
    }

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
