package net.codersoffortune.infinity.metadata.unit;

import net.codersoffortune.infinity.armylist.Armylist;
import net.codersoffortune.infinity.armylist.CombatGroup;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.TTSModel;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;
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
    private final int sectoral_idx;
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

    // Properties below here are used for description only.
    // They should not be added to equals / hashcode!



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
     *
     * @param that the unit to check
     * @return true iff the equipment, skills, name and weapons match
     */
    public boolean isEquivalent(PrintableUnit that) {
        return name.equals(that.name) &&
                Objects.equals(new HashSet<>(weapons), new HashSet<>(that.weapons)) &&
                Objects.equals(new HashSet<>(equip), new HashSet<>(that.equip));
    }

    public UnitID getUnitID() {
        return new UnitID(sectoral_idx, unit_idx, group_idx, profile_idx, option_idx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weapons, skills, equip, peripheral, unit_idx, group_idx, option_idx, profile_idx, name, arm, bs, bts, cc, chars, move, ph, s, str, type, w, wip, notes, distinguisher);
    }

    public PrintableUnit(MappedFactionFilters filters, CompactedUnit src, int sectoral) throws InvalidObjectException {
        weapons = src.getWeapons().stream().map(x -> x.toString(filters, FilterType.weapons)).collect(Collectors.toList());
        skills = src.getPublicSkills().stream().map(x -> x.toString(filters, FilterType.skills)).collect(Collectors.toList());
        equip = src.getEquipment().stream().map(x -> x.toString(filters, FilterType.equip)).collect(Collectors.toList());
        peripheral = src.getPeripheral().stream().map(x -> x.toString(filters, FilterType.peripheral)).collect(Collectors.toList());
        name = src.getName();
        sectoral_idx = sectoral;
        unit_idx = src.getUnit_idx();
        group_idx = src.getGroup_idx();
        option_idx = src.getOption_idx();
        profile_idx = src.getProfile_idx();
        arm = src.getProfile().getArm();
        ava = src.getProfile().getAva();
        bs = src.getProfile().getBs();
        bts = src.getProfile().getBts();
        cc = src.getProfile().getCc();
        chars = src.getPublicChars().stream().map(x -> filters.getItem(FilterType.chars, x).getName()).collect(Collectors.toList());
        move = src.getProfile().getMove().stream().map(String::valueOf).collect(Collectors.joining("-"));
        ph = src.getProfile().getPh();
        s = src.getProfile().getS();
        str = src.getProfile().isStr();
        type = filters.getItem(FilterType.type, src.getProfile().getType()).getName();
        w = src.getProfile().getW();
        wip = src.getProfile().getWip();
        notes = src.getProfile().getNotes();
        flags = new UnitFlags(src);
        if (src.getGroup().getOptions().size() == 1) {
            // Don't need to distinguish when only one option
            distinguisher = "";
        } else {
            List<ProfileItem> pSkills = src.getOption().getSkills().stream().filter(c -> CompactedUnit.skipSkills(c.getId())).collect(Collectors.toList());
            if (pSkills.isEmpty()) {
                // No special skills, take the first weapon, I guess?
                distinguisher = weapons.get(0);
            } else {
                distinguisher = pSkills.get(0).toString(filters, FilterType.skills);
            }
        }

    }

    public String getTTSMesh(int idx) {
        if (models.size() < idx + 1) return "";
        return models.get(idx).getMeshes();
    }


    public String getTTSDecal(int idx) {
        if (models.size() < idx + 1) return "";
        return models.get(idx).getDecals();
    }

    public String getTTSName(int idx) {
        if (models.size() < idx + 1) return "";
        return models.get(idx).getName();
    }


    public void addTTSModel(TTSModel model) {
        if( !this.models.contains(model))
            this.models.add(model);
    }

    public void addTTSModels(Collection<TTSModel> models) {
        // Use this rather than addAll so we can apply filtering.
        models.stream().forEach(m->addTTSModel(m));
    }

    private String getTTSDescription() {
        //Example
        //[b]REM[/b] ● [AAFFAA]Regular[-] ● Hackable \n
        //[sub]---------Attributes-------\n
        //[/sub]\n
        //[b]MOV[/b]: 6-6\n
        //[b]CC[/b]: 13\n
        //[b]BS[/b]: 8\n
        //[b]PH[/b]: 11\n
        //[b]WIP[/b]: 13\n
        //[b]ARM[/b]: 0\n
        //[b]BTS[/b]: 3\n
        //[B]STR[/B]: 1\n
        //[b]S[/b]: 3\n
        //[ffdddd][sub]----------Weapons---------\n
        //[/sub]\n
        //Flash Pulse ● PARA CC Weapon (-3) [-]\n
        //[ddffdd][sub]---------Equipment--------\n
        //[/sub]\n
        //Repeater [-]\n
        //[ddddff][sub]----------Skills----------\n
        //[/sub]\n
        //Remote Presence ● Courage ● Mimetism (-3) ● Immunity (Shock) [-]\n
        //[000013][-]

        StringBuilder result = new StringBuilder();
        result.append(String.format("[b]%s[/b] ● [AAFFAA]%s[-]\\n", type, String.join(" ● ", chars)));
        result.append("[sub]---------Attributes-------\\n[/sub]\\n");
        // TODO:: Allow selecting inches.
        result.append(String.format("[b]MOV[/b]: %s\\n", move));
        result.append(String.format("[b]CC[/b]: %d\\n", cc));
        result.append(String.format("[b]BS[/b]: %d\\n", bs));
        result.append(String.format("[b]PH[/b]: %d\\n", ph));
        result.append(String.format("[b]WIP[/b]: %d\\n", wip));
        result.append(String.format("[b]ARM[/b]: %d\\n", arm));
        result.append(String.format("[b]BTS[/b]: %d\\n", bts));
        if (str) {
            result.append(String.format("[B]STR[/B]: %d\\n", w));
        } else {
            result.append(String.format("[B]W[/B]: %d\\n", w));
        }
        result.append(String.format("[B]S[/B]: %d\\n", s));
        result.append("[ffdddd][sub]----------Weapons---------\\n[/sub]\\n");
        result.append(String.format("%s[-]\\n", String.join(" ● ", weapons)));
        result.append("[ddffdd][sub]---------Equipment--------\\n[/sub]\\n");
        result.append(String.format("%s[-]\\n", String.join(" ● ", equip)));
        result.append("[ddddff][sub]----------Skills ---------\\n[/sub]\\n");
        result.append(String.format("%s[-]\\n", String.join(" ● ", skills)));

        // TODO:: implement reference code
        return result.toString();


    }

    private String getTTSSilhouette(String[] templates) {
        String template = templates[s-1];
        String diffuse = "http://cloud-3.steamusercontent.com/ugc/859478426278214079/BFA0CAEAE34C30E5A87F6FB2595C59417DCFFE27/";

        String stype;

        if (flags.isCamo()) {
            if (flags.getMimetism() > 0) {
                stype = String.format("Camouflage (%d) S%d", flags.getMimetism(), s);
            } else {
                stype = String.format("Camouflage S%d", s);
            }
            // edge case of single use camo
            if (flags.isSingleCamo())
                return String.format("%s,\n%s",
                        String.format(template, 2, stype, diffuse),
                        String.format(template, 3, String.format("Silhouette %d", s), ""));

        } else {
            diffuse = "";
            stype = String.format("Silhouette %d", s);
        }

        return String.format(template, 2, stype, diffuse);
    }

    private String getTTSName() {
        String result = String.format("[FFFFFF]%s[-]", name);
        if (!distinguisher.isEmpty()) {
            result += " " + distinguisher;
        }
        return result;
    }

    /**
     * Get the JSON representation of this model, as is suitible for putting in a TTS bag.
     * Of course, there is a minor issue. We might well have multiple valid models for this unit.
     * @param model_template the loaded template file (to save having to load it repeatedly)
     * @param silhouette_templates The loaded templates for the different sized silhouettes
     * @return the filled in template.
     */
    public String asJson(final String model_template, final String[] silhouette_templates) {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final String ttsSilhouette = getTTSSilhouette(silhouette_templates);
        final String ttsColour = CombatGroup.getTint(1); // TODO:: maybe faction specific colours.
        List<String> ttsModels = models.stream().map(m->String.format(model_template, ttsName, ttsDescription, ttsColour, m.getMeshes(), m.getDecals(), ttsSilhouette)).collect(Collectors.toList());
        return String.join(",\n", ttsModels);
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
        if (printableUnit.unit_idx != unit_idx)
            return unit_idx - printableUnit.unit_idx;
        if (printableUnit.group_idx != group_idx)
            return group_idx - printableUnit.group_idx;
        if (printableUnit.profile_idx != profile_idx)
            return profile_idx - printableUnit.profile_idx;
        return option_idx - printableUnit.option_idx;
    }

    public List<TTSModel> getModels() {
        return models;
    }

    public int getSectoral_idx() {
        return sectoral_idx;
    }
}
