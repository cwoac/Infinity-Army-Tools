package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.TTSModel;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Like a @CompactedUnit, but with all the strings expanded.
 */
public class PrintableUnit implements Comparable<PrintableUnit> {
    private final List<String> weapons;
    private final List<String> skills;
    private final List<String> equip;
    private final List<String> peripheral;
    private final UnitFlags flags;
    private final int unit_idx;
    private final int group_idx;
    private final int option_idx;
    private final int profile_idx;
    private final String name;

    private final int arm;
    private final int ava;
    private final int bs;
    private final int bts;
    private final int cc;
    private final List<String> chars;
    private final String move;
    private final int ph;
    private final int s;
    private final boolean str;
    private final String type;
    private final int w;
    private final int wip;
    private final String notes;
    private final String distinguisher;
    private final List<TTSModel> models = new ArrayList<>();

    //private final List<String> orders; // TODO:: DO WE NEED THIS?


    @Override
    public String toString() {
        return String.format("PrintableUnit{%d:%d:%d %s}",
            unit_idx, group_idx, option_idx, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintableUnit that = (PrintableUnit) o;
        return unit_idx == that.unit_idx &&
                group_idx == that.group_idx &&
                option_idx == that.option_idx &&
                profile_idx == that.profile_idx &&
                arm == that.arm &&
                bs == that.bs &&
                bts == that.bts &&
                cc == that.cc &&
                ph == that.ph &&
                s == that.s &&
                str == that.str &&
                w == that.w &&
                wip == that.wip &&
                Objects.equals(weapons, that.weapons) &&
                Objects.equals(skills, that.skills) &&
                Objects.equals(equip, that.equip) &&
                Objects.equals(peripheral, that.peripheral) &&
                name.equals(that.name) &&
                chars.equals(that.chars) &&
                move.equals(that.move) &&
                type.equals(that.type) &&
                Objects.equals(notes, that.notes) &&
                Objects.equals(distinguisher, that.distinguisher);
    }

    /**
     * Checks whether the other unit is close enough in equipment that we can still use it's TTS model
     * @param that the unit to check
     * @return true iff the equipment, skills, name and weapons match
     */
    public boolean isEquivalent(PrintableUnit that) {
        return name.equals(that.name) &&
                Objects.equals(weapons, that.weapons) &&
                Objects.equals(skills, that.skills) &&
                Objects.equals(equip, that.equip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weapons, skills, equip, peripheral, unit_idx, group_idx, option_idx, profile_idx, name, arm, bs, bts, cc, chars, move, ph, s, str, type, w, wip, notes, distinguisher);
    }

    public PrintableUnit(MappedFactionFilters filters, CompactedUnit src) throws InvalidObjectException {
        weapons = src.getWeapons().stream().map(x -> x.toString(filters, FilterType.weapons)).collect(Collectors.toList());
        skills = src.getPublicSkills().stream().map(x -> x.toString(filters, FilterType.skills)).collect(Collectors.toList());
        equip = src.getEquipment().stream().map(x -> x.toString(filters, FilterType.equip)).collect(Collectors.toList());
        peripheral = src.getPeripheral().stream().map(x -> x.toString(filters, FilterType.peripheral)).collect(Collectors.toList());
        name = src.getName();
        unit_idx = src.getUnit_idx();
        group_idx = src.getGroup_idx();
        option_idx = src.getOption_idx();
        profile_idx = src.getProfile_idx();
        arm = src.getProfile().getArm();
        ava = src.getProfile().getAva();
        bs = src.getProfile().getBs();
        bts = src.getProfile().getBts();
        cc = src.getProfile().getCc();
        chars = src.getPublicChars().stream().map(x->filters.getItem(FilterType.chars, x).getName()).collect(Collectors.toList());
        move = src.getProfile().getMove().stream().map(String::valueOf).collect(Collectors.joining("-"));
        ph = src.getProfile().getPh();
        s = src.getProfile().getS();
        str = src.getProfile().isStr();
        type = filters.getItem(FilterType.type, src.getProfile().getType()).getName();
        w = src.getProfile().getW();
        wip = src.getProfile().getWip();
        notes = src.getProfile().getNotes();
        flags = new UnitFlags(src);
        if( src.getGroup().getOptions().size() == 1) {
            // Don't need to distinguish when only one option
            distinguisher = "";
        } else {
            List<ProfileItem> pSkills = src.getOption().getSkills().stream().filter(c -> CompactedUnit.skipSkills(c.getId())).collect(Collectors.toList());
            if( pSkills.isEmpty() ) {
                // No special skills, take the first weapon, I guess?
                distinguisher=weapons.get(0);
            } else {
                distinguisher=pSkills.get(0).toString(filters, FilterType.skills);
            }
        }
    }

    public String getTTSMesh(int idx) {
        if(models.size() < idx+1 ) return " ";
        return models.get(idx).getMeshes();
    }


    public String getTTSDecal(int idx) {
        if(models.size() < idx+1 ) return " ";
        return models.get(idx).getDecals();
    }


    public void addTTSModel(TTSModel model) {
        this.models.add(model);
    }

    public void addTTSModels(Collection<TTSModel> models) {
        this.models.addAll(models);
    }

    public String getTTSSilhouette() throws IOException {
        // Important numbers:
        // skill 29 - Camouflage
        // skill 28 - Mimitism
        // extra 6,7 -3, -6

        String template = Armylist.getResourceFileAsString(String.format("templates/S%d.json", s));
        String diffuse = "http://cloud-3.steamusercontent.com/ugc/859478426278214079/BFA0CAEAE34C30E5A87F6FB2595C59417DCFFE27/";

        String stype;

        if( flags.isCamo()) {
            if( flags.getMimetism() > 0) {
                stype = String.format("Camouflage (%d) S%d", flags.getMimetism(), s);
            } else {
                stype = String.format("Camouflage S%d", s);
            }
            // edge case of single use camo
            if( flags.isSingleCamo())
                return String.format("%s,\n%s",
                        String.format(template, 2, stype, diffuse),
                        String.format(template, 3, String.format("Silhouette %d", s), ""));

        } else {
            diffuse = "";
            stype = String.format("Silhouette %d", s);
        }

        return String.format(template, 2, stype, diffuse);
    }

    public String getTTSNickName(final MappedFactionFilters filters) {
        //TODO:: implement a filter to select the 'interesting' options
        String result = String.format("[FFFFFF]%s[-]", name);
        if( !distinguisher.isEmpty()) {
            result += " " + distinguisher;
        }
        return  result;
    }


    public List<String> getWeapons() {
        return weapons;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getEquip() {
        return equip;
    }

    public int getUnit_idx() {
        return unit_idx;
    }

    public int getGroup_idx() {
        return group_idx;
    }

    public int getOption_idx() {
        return option_idx;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(PrintableUnit printableUnit) {
        if( printableUnit.unit_idx != unit_idx)
            return unit_idx - printableUnit.unit_idx;
        if( printableUnit.group_idx != group_idx)
            return group_idx - printableUnit.group_idx;
        if( printableUnit.profile_idx != profile_idx)
            return profile_idx - printableUnit.profile_idx;
        return option_idx - printableUnit.option_idx;
    }

    public List<TTSModel> getModels() {
        return models;
    }
}
