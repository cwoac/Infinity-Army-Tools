package net.codersoffortune.infinity.metadata.unit;

import com.codepoetics.protonpack.StreamUtils;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.armylist.CombatGroup;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.FilterType;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Like a @{link RegularCompactedUnit}, but with all the strings expanded.
 */
public class PrintableUnit implements Comparable<PrintableUnit> {
    private final List<String> weapons;
    private final List<String> skills;
    private final List<String> equip;
    private final List<String> peripheral;
    private final Set<String> visible_weapons = new HashSet<>();
    private final Set<String> visible_equipment = new HashSet<>();
    private final Set<String> visible_skills = new HashSet<>();
    private final UnitFlags flags;
    protected final SECTORAL sectoral;
    private final int unit_idx;
    private final int group_idx;
    private final int option_idx;
    private final int profile_idx;
    private final String name;
    private final String profile_name;

    private final int arm;
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

    /* Most primary weapons are visible, so a list of the ones which tend not to be
        61, 199 AP mines
        78 AP+DA CC
        72 AP+Shock CC
        6 AP CC
        5 CC
        147 Chest Mines
        20 CrazyKoala
        174 CyberMines
        17 D-Charges
        11 DA CC
        96 Dropbears
        7 EM CC
        47 EM Grenades
        197 EM Mines
        25 EMitter
        46 eclipse grenades
        8 exp cc
        72 flash pulse
        44 grenades
        150 madtraps
        176 mine dispenser
        9 mono CC
        62 mono mines
        65 nanopulser
        71 para cc
        203-205 pheroware
        154 pitcher
        73 sepsitor
        114 sepsitor plus
        10 shock cc
        196 shock mines
        45 smoke grenades
        153 t2 cc
        13 viral cc
        63 viral mines
        149 vorpal cc
        182 wild parrot
        175 zapper
     */

    private static final List<Integer> invisibleWeapons = Arrays.asList(61, 199, 78, 72, 6, 5, 147, 20, 174, 17, 11, 96, 7, 47, 197, 25, 46, 8, 72, 44, 150, 176, 9, 62, 65, 71, 203, 204, 205, 154, 73, 114, 10, 196, 45, 153, 13, 63, 149, 182, 175);

    /**
     * A visible weapon is one which would be on the actual model and distinguishable from another weapon.
     *
     * @param w the weapon to check visibility of
     * @return true if the weapon would be distinctively on the model.
     */
    private static boolean isVisibleWeapon(int w) {
        return !invisibleWeapons.contains(w);
    }

    /* Visible equipment on the other hand, is much rarer.
       100 hacking device
       101 hd+
       106 medkit
       107 motorcycle
       145 KHD
       182 Evo HD
       205 AI motorcycle
       247 gizmokit
     */
    private static final List<Integer> visibleEquipment = Arrays.asList(100, 101, 106, 107, 145, 182, 205, 247);


    /**
     * A visible piece of equipment is one which might be put on the model.
     *
     * @param e the equipment to check
     * @return true if it might be visible on the model (reasonably).
     */
    private static boolean isVisibleEquipment(int e) {
        return visibleEquipment.contains(e);
    }

    @Override
    public String toString() {
        return String.format("PrintableUnit{%d:%d:%d %s}",
                unit_idx, group_idx, option_idx, name);
    }

    /* Very few skills are 'visible'. The main use is to enforce different models when weapons + kit would
     * suggest they are the same (i.e. hacking related stuff mostly).
     *    255 - remdriver
     */
    private static final List<Integer> visibleSkills = Arrays.asList(255);

    private static boolean isVisibleSkill(int s) { return visibleSkills.contains(s); }

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

    public boolean isEquivalent(PrintableUnit that) {
        return name.equals(that.name) &&
                visible_equipment.equals(that.visible_equipment) &&
                visible_weapons.equals(that.visible_weapons) &&
                visible_skills.equals(that.visible_skills);
    }

    public UnitID getUnitID() {
        return new UnitID(sectoral.getId(), unit_idx, group_idx, profile_idx, option_idx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weapons, skills, equip, peripheral, unit_idx, group_idx, option_idx, profile_idx, name, arm, bs, bts, cc, chars, move, ph, s, str, type, w, wip, notes, distinguisher);
    }

    /**
     * Look for items which would be in the options title chunk.
     *
     * @param src     Compacted Unit to examine
     * @param filters to look up types in.
     * @return A list of items.
     */
    private List<String> getInterestingStuff(CompactedUnit src, MappedFactionFilters filters) {
        List<String> results = new ArrayList<>();
        src.getOption().getSkills().stream()
                .filter(s -> CompactedUnit.skipSkills(s.getId()))
                .forEach(s -> results.add(s.toString(filters, FilterType.skills)));

        src.getOption().getEquip().stream()
                .filter(e -> filters.getItem(FilterType.equip, e).getType().equalsIgnoreCase("PERSON"))
                .map(e -> e.toString(filters, FilterType.equip))
                .forEach(results::add);
        return results;
    }

    // TODO:: Make a seperate module for special case lists so they are easy to find
    private final static List<Integer> nameEdgeCases = Arrays.asList(
            20, // Joan
            42, // Joan
            613, // achilles
            749, // achilles
            53, // Sheskiin
            1503, // Sheskiin
            143, // TZE
            165 // TZE
    );

    public PrintableUnit(MappedFactionFilters filters, CompactedUnit src, SECTORAL sectoral) throws InvalidObjectException {
        weapons = src.getWeapons().stream().map(x -> x.toString(filters, FilterType.weapons)).collect(Collectors.toList());
        visible_weapons.addAll(src.getWeapons().stream()
                .filter(x -> isVisibleWeapon(x.getId()))
                .map(x -> x.toString(filters, FilterType.weapons))
                .collect(Collectors.toList()));
        skills = src.getPublicSkills().stream().map(x -> x.toString(filters, FilterType.skills)).collect(Collectors.toList());
        visible_skills.addAll(src.getPublicSkills().stream()
                .filter(x -> isVisibleSkill(x.getId()))
                .map(x -> x.toString(filters, FilterType.skills))
                .collect(Collectors.toList()));
        equip = src.getEquipment().stream().map(x -> x.toString(filters, FilterType.equip)).collect(Collectors.toList());
        visible_equipment.addAll(src.getEquipment().stream()
                .filter(x -> isVisibleEquipment(x.getId()))
                .map(x -> x.toString(filters, FilterType.equip))
                .collect(Collectors.toList()));
        peripheral = src.getPeripheral().stream().map(x -> x.toString(filters, FilterType.peripheral)).collect(Collectors.toList());
        // Name. Name _should_ be easy. For almost everyone it is the option name
        // However, for a few characters who exist as multiple units (in different armours), this is not usefully the case.
        profile_name = src.getProfile().getName();
        if (nameEdgeCases.contains(src.getUnit_idx())) {
            name = profile_name;
        } else {
            name = src.getName();
        }

        this.sectoral = sectoral;
        unit_idx = src.getUnit_idx();
        group_idx = src.getGroup_idx();
        option_idx = src.getOption_idx();
        profile_idx = src.getProfile_idx();
        arm = src.getProfile().getArm();
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


        // Is the weapon interesting?
        Optional<ProfileItem> interestingWeaponMaybe = src.getInterestingWeaponMaybe(filters);
        List<String> interestingStuff = getInterestingStuff(src, filters);

        if (interestingWeaponMaybe.isPresent()) {
            interestingStuff.add(interestingWeaponMaybe.get().toString(filters, FilterType.weapons));
        }
        distinguisher = String.join(", ", interestingStuff);

    }

    public void printCSVRecord(CSVPrinter out, final ModelSet ms) throws IOException {
        List<String> unfolded = new ArrayList<>();

        for( TTSModel model : ms.getModels(getUnitID())) {
            unfolded.add(model.getName());
            unfolded.add(model.getMeshes());
            unfolded.add(model.getDecals());
        }
        while( unfolded.size() < 15 ) unfolded.add("");


        out.printRecord(
                sectoral.getId(),
                unit_idx,
                group_idx,
                profile_idx,
                option_idx,
                name,
                String.join(",", visible_weapons),
                String.join(",", visible_equipment),
                unfolded.get(0),
                unfolded.get(1),
                unfolded.get(2),
                unfolded.get(3),
                unfolded.get(4),
                unfolded.get(5),
                unfolded.get(6),
                unfolded.get(7),
                unfolded.get(8),
                unfolded.get(9),
                unfolded.get(10),
                unfolded.get(11),
                unfolded.get(12),
                unfolded.get(13),
                unfolded.get(14));
    }


    protected String getTTSDescription() {
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

        result.append(getUnitID().encode());
        return result.toString();


    }

    protected static String embedState(final String state, long index) {
        return String.format("\"%d\" : %s", index, state);
    }

    protected List<String> getTTSSilhouettes() {
        String template = Database.getSilhouetteTemplates().get(s);
        String diffuse = "http://cloud-3.steamusercontent.com/ugc/859478426278214079/BFA0CAEAE34C30E5A87F6FB2595C59417DCFFE27/";
        // TODO:: Different tint for different camo types?

        String tint = sectoral.getTint();

        String silhouette;
        List<String> result = new ArrayList<>();

        if (flags.isCamo()) {
            if (flags.getMimetism() > 0) {
                silhouette = String.format("Camouflage (%d) S%d", flags.getMimetism(), s);
            } else {
                silhouette = String.format("Camouflage S%d", s);
            }
            // edge case of single use camo
            if (flags.isSingleCamo())
                try {
                    result.add(String.format(template, silhouette, tint, diffuse));
                    result.add(String.format(template, String.format("Silhouette %d", s), tint, ""));
                } catch (MissingFormatArgumentException e) {
                    e.printStackTrace();
                }
                return result;

        } else {
            diffuse = "";
            silhouette = String.format("Silhouette %d", s);
        }

        result.add(String.format(template, silhouette, tint, diffuse));
        return result;
    }

    private String getTTSNameInner(String n) {
        String result = String.format("[%s]%s[-]", sectoral.getFontTint(), n);
        if (!distinguisher.isEmpty()) {
            result += " " + distinguisher;
        }
        return StringEscapeUtils.escapeJson(result);
    }

    protected String getTTSName() {
        return getTTSNameInner(name);
    }

    protected String getEmbeddedTTSName() {
        return getTTSNameInner(profile_name);
    }




    public String asFactionJSON(final ModelSet ms) {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final List<String> ttsSilhouettes = getTTSSilhouettes();
        final String states = StreamUtils.zipWithIndex(ttsSilhouettes.stream())
            .map(x -> embedState(x.getValue(), x.getIndex()+2))
            .collect(Collectors.joining("\n,"));

        //final String states = String.join(",\n", ttsSilhouettes);
        final String ttsColour = sectoral.getTint();
        List<String> ttsModels = ms.getModels(getUnitID()).stream().map(m -> String.format(Database.getUnitTemplate(),
                ttsName,
                ttsDescription,
                ttsColour,
                m.getMeshes(),
                m.getDecals(),
                states)).collect(Collectors.toList());
        return String.join(",\n", ttsModels);
    }

    public String asEmbeddedJSON(final ModelSet ms) {
        String ttsName = getEmbeddedTTSName();
        String ttsDescription = getTTSDescription();
        String ttsColour = sectoral.getTint();
        Set<TTSModel> models = ms.getModels(getUnitID());
        List<String> ttsModels = models.stream().map(m -> String.format(Database.getTransmutedUnitTemplate(),
                ttsName,
                ttsDescription,
                ttsColour,
                m.getMeshes(),
                m.getDecals())).collect(Collectors.toList());
        return String.join(",\n", ttsModels);
    }

    public String asArmyJSON(int combatGroup_idx, final ModelSet ms) throws IllegalArgumentException {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final List<String> ttsSilhouettes = getTTSSilhouettes();
        final String states = String.join(",\n", ttsSilhouettes);
        final String ttsColour = CombatGroup.getTint(combatGroup_idx);
        // TODO:: Better picking of model
        final TTSModel model =  ms.getModels(getUnitID()).stream().findAny().orElseThrow(IllegalArgumentException::new);
        return String.format(Database.getUnitTemplate(),
                ttsName,
                ttsDescription,
                ttsColour,
                model.getMeshes(),
                model.getDecals(),
                states);

    }

    public String getDistinguisher() {
        return distinguisher;
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

}
