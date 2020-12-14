package net.codersoffortune.infinity.metadata;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.codersoffortune.infinity.GAME;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Metadata {
    private List<Faction> factions;
    private List<Ammunition> ammunitions;
    private List<Weapon> weapons;
    private List<Skill> skills;
    private List<Equipment> equips;
    private List<Program> hack;
    private List<MartialArt> martialArts;
    private List<TableValue> metachemistry;
    private List<TableValue> booty;
    //private final String url = "https://api.corvusbelli.com/army/infinity/en/metadata";private
    private Map<String, Object> data;

    public Metadata() {
    }

    public Metadata(List<Faction> factions,
                    List<Ammunition> ammunitions,
                    List<Weapon> weapons,
                    List<Skill> skills,
                    List<Equipment> equips,
                    List<Program> hack,
                    List<MartialArt> martialArts,
                    List<TableValue> metaChemistry,
                    List<TableValue> booty) {
        setFactions(factions);
        setAmmunitions(ammunitions);
        setWeapons(weapons);
        setSkills(skills);
        setEquips(equips);
        setHack(hack);
        setMartialArts(martialArts);
        setMetachemistry(metaChemistry);
        setBooty(booty);
    }

    public static Map<GAME, Metadata> loadMetadata() throws IOException {
        Map<GAME, Metadata> result = new HashMap<>();
        ObjectMapper om = new ObjectMapper();
        for ( GAME game : GAME.values()) {
            result.put(game, om.readValue(new File(game.getMetadataFile()), Metadata.class));
        }
        return result;

    }


    public List<Faction> getFactions() {
        return factions;
    }

    public void setFactions(List<Faction> factions) {
        this.factions = factions;
    }

    public List<Ammunition> getAmmunitions() {
        return ammunitions;
    }

    public void setAmmunitions(List<Ammunition> ammunitions) {
        this.ammunitions = ammunitions;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Equipment> getEquips() {
        return equips;
    }

    public void setEquips(List<Equipment> equips) {
        this.equips = equips;
    }

    public List<Program> getHack() {
        return hack;
    }

    public void setHack(List<Program> hack) {
        this.hack = hack;
    }

    public List<MartialArt> getMartialArts() {
        return martialArts;
    }

    public void setMartialArts(List<MartialArt> martialArts) {
        this.martialArts = martialArts;
    }

    public List<TableValue> getMetachemistry() {
        return metachemistry;
    }

    public void setMetachemistry(List<TableValue> metachemistry) {
        this.metachemistry = metachemistry;
    }

    public List<TableValue> getBooty() {
        return booty;
    }

    public void setBooty(List<TableValue> booty) {
        this.booty = booty;
    }
}
