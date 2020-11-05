package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.metadata.Order;
import net.codersoffortune.infinity.metadata.Peripheral;

import java.util.List;

public class ProfileOption {
    // "id":1,"chars":[],"disabled":false,"equip":[],"minis":0,"orders":[],"includes":[],"points":3,"swc":"0","weapons":[{"extra":[6],"id":71,"order":1}],"name":"YÁOZĂO","skills":[],"peripheral":[]}
    // "id":1,"chars":[],"compatible":null,"disabled":false,"equip":[],"habilities":[],"minis":2,
    //   "orders":[{"type":"REGULAR","list":2,"total":2},{"type":"IRREGULAR","list":0,"total":1}],
    //   "includes":[{"q":1,"group":1,"option":1},{"q":1,"group":3,"option":1}],"points":85,"swc":"1.5","weapons":[],"name":"SCARFACE Loadout Alpha & CORDELIA TURNER","skills":[],"peripheral":[]},
    private int id;
    private List<Integer> chars; // characteristics
    private String compatible; // TODO:: ARE THESE STRINGS?
    private List<String> habilities;
    private boolean disabled; // used with mercs afaict
    private List<ProfileItem> equip;
    private int minis;
    private List<Order> orders;
    private List<ProfileInclude> includes;
    private int points;
    private String swc;
    private List<ProfileItem> weapons;
    private String name;
    private List<ProfileItem> skills;
    private List<Peripheral> peripheral;

    public List<Peripheral> getPeripheral() {
        return peripheral;
    }

    public void setPeripheral(List<Peripheral> peripheral) {
        this.peripheral = peripheral;
    }

    public List<ProfileItem> getSkills() {
        return skills;
    }

    public void setSkills(List<ProfileItem> skills) {
        this.skills = skills;
    }

    public List<ProfileItem> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<ProfileItem> weapons) {
        this.weapons = weapons;
    }

    public String getSwc() {
        return swc;
    }

    public void setSwc(String swc) {
        this.swc = swc;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getMinis() {
        return minis;
    }

    public void setMinis(int minis) {
        this.minis = minis;
    }

    public List<ProfileItem> getEquip() {
        return equip;
    }

    public void setEquip(List<ProfileItem> equip) {
        this.equip = equip;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<Integer> getChars() {
        return chars;
    }

    public void setChars(List<Integer> chars) {
        this.chars = chars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProfileInclude> getIncludes() {
        return includes;
    }

    public void setIncludes(List<ProfileInclude> includes) {
        this.includes = includes;
    }

    public String getCompatible() {
        return compatible;
    }

    public void setCompatible(String compatible) {
        this.compatible = compatible;
    }

    public List<String> getHabilities() {
        return habilities;
    }

    public void setHabilities(List<String> habilities) {
        this.habilities = habilities;
    }
}
