package net.codersoffortune.infinity.metadata;

public class MartialArt {
    //{"opponent":"-3","damage":"+1","attack":"0","name":"1","burst":"0"}
    private String opponent;
    private String damage;
    private String attack;
    private String name;
    private String burst;

    public MartialArt() {
    }

    public MartialArt(String opponent, String damage, String attack, String name, String burst) {
        setOpponent(opponent);
        setDamage(damage);
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

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }
}
