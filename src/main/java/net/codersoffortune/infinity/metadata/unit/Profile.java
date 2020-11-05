package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.metadata.Peripheral;

import java.util.List;

public class Profile {
    // {"id":1,"arm":0,"ava":2,"bs":8,"bts":3,"cc":11,"chars":[2,27,5,21],"equip":[],"logo":"https://assets.corvusbelli.net/army/img/logo/units/yaozao-1-1.svg",
    // "weapons":[],"includes":[],"move":[15,10],"ph":10,"s":1,"str":true,"type":5,"w":1,"wip":13,"name":"YÁOZĂO","notes":null,
    // "skills":[{"extra":[41],"id":243,"order":1},{"id":84,"order":2},{"extra":[6],"id":28,"order":3},{"extra":[30],"id":162,"order":4}],"peripheral":[]}
    private int id;
    private int arm;
    private int ava;
    private int bs;
    private int bts;
    private int cc;
    private List<Integer> chars;
    private List<ProfileItem> equip; // I THINK
    private String logo;
    private List<ProfileItem> weapons;
    private List<ProfileInclude> includes;
    private List<Integer> move;
    private int ph;
    private int s;
    private boolean str;
    private int type;
    private int w;
    private int wip;
    private String name;
    private String notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWip() {
        return wip;
    }

    public void setWip(int wip) {
        this.wip = wip;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isStr() {
        return str;
    }

    public void setStr(boolean str) {
        this.str = str;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getPh() {
        return ph;
    }

    public void setPh(int ph) {
        this.ph = ph;
    }

    public List<Integer> getMove() {
        return move;
    }

    public void setMove(List<Integer> move) {
        this.move = move;
    }

    public List<ProfileInclude> getIncludes() {
        return includes;
    }

    public void setIncludes(List<ProfileInclude> includes) {
        this.includes = includes;
    }

    public List<ProfileItem> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<ProfileItem> weapons) {
        this.weapons = weapons;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<ProfileItem> getEquip() {
        return equip;
    }

    public void setEquip(List<ProfileItem> equip) {
        this.equip = equip;
    }

    public List<Integer> getChars() {
        return chars;
    }

    public void setChars(List<Integer> chars) {
        this.chars = chars;
    }

    public int getCc() {
        return cc;
    }

    public void setCc(int cc) {
        this.cc = cc;
    }

    public int getBts() {
        return bts;
    }

    public void setBts(int bts) {
        this.bts = bts;
    }

    public int getBs() {
        return bs;
    }

    public void setBs(int bs) {
        this.bs = bs;
    }

    public int getAva() {
        return ava;
    }

    public void setAva(int ava) {
        this.ava = ava;
    }

    public int getArm() {
        return arm;
    }

    public void setArm(int arm) {
        this.arm = arm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
