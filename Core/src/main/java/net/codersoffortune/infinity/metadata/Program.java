package net.codersoffortune.infinity.metadata;


import java.util.List;

public class Program {
    //{"opponent":"-","special":"Target gains Marksmanship.","skillType":["entire order"],"extra":18,"damage":"-","devices":[182],"target":["REM"],"attack":"-","name":"Assisted Fire","burst":"-"},
    private String opponent;
    private String special;
    private List<String> skillType;
    private int extra;
    private String damage;
    private List<Integer> devices;
    private List<String> target;
    private String attack;
    private String name;
    private String burst;

    public Program() {
    }

    public Program(String opponent, String special, List<String> skillType, int extra, String damage, List<Integer> devices, List<String> target, String attack, String name, String burst) {
        setOpponent(opponent);
        setSpecial(special);
        setSkillType(skillType);
        setExtra(extra);
        setDamage(damage);
        setDevices(devices);
        setTarget(target);
        setAttack(attack);
        setName(name);
        setBurst(burst);
    }

    public String getBurst() {
        return burst;
    }

    public void setBurst(String burst) {
        this.burst = burst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public List<String> getTarget() {
        return target;
    }

    public void setTarget(List<String> target) {
        this.target = target;
    }

    public List<Integer> getDevices() {
        return devices;
    }

    public void setDevices(List<Integer> devices) {
        this.devices = devices;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public List<String> getSkillType() {
        return skillType;
    }

    public void setSkillType(List<String> skillType) {
        this.skillType = skillType;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
}
