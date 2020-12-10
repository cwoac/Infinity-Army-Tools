package net.codersoffortune.infinity.metadata.unit;

import com.codepoetics.protonpack.StreamUtils;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.SIZE;
import net.codersoffortune.infinity.armylist.CombatGroup;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransmutedPrintableUnit extends PrintableUnit {
    private final List<PrintableUnit> printableUnits = new ArrayList<>();

    public TransmutedPrintableUnit(MappedFactionFilters filters, TransmutedCompactedUnit src, SECTORAL sectoral) throws InvalidObjectException {
        super(filters, src, sectoral);
        boolean skipped = false;
        for( CompactedUnit cu : src.getCompactedUnits()) {
            // The first one gets folded into this.
            if( !skipped ) {
                skipped = true;
                continue;
            }
            printableUnits.add(new PrintableUnit(filters, cu, sectoral));
        }
    }

    @Override
    public void printCSVRecord(CSVPrinter out, final ModelSet ms) throws IOException {
        super.printCSVRecord(out, ms);
        for( PrintableUnit pu : printableUnits ) {
            pu.printCSVRecord(out, ms);
        }
    }




    private String asJSONInner(final TTSModel model, final String embeddedModel, final List<String> embeddedSilhouettes, boolean doAddons, final List<String> silhouettes, final String colour) {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final String addon = doAddons?Database.getAddonTemplate(s):"";
        List<String> states = new ArrayList<>();
        // There are two types here - if the silhouette size changes, we want active -> active sil -> inactive -> inactive sil
        // but if it doesn't, then we want active -> inactive -> sil
        boolean silChange = !silhouettes.containsAll(embeddedSilhouettes);
        if (silChange) {
            states.addAll(silhouettes);
            states.add(embeddedModel);
            states.addAll(embeddedSilhouettes);
        } else {
            states.add(embeddedModel);
            states.addAll(silhouettes);
        }
        String embed = StreamUtils.zipWithIndex(states.stream().filter(s->!s.isEmpty()))
                .map(x -> embedState(x.getValue(), x.getIndex()+2))
                .collect(Collectors.joining("\n,"));
        return String.format(Database.getUnitTemplate(),
                ttsName,
                ttsDescription,
                colour,
                SIZE.get(s).getModelCustomMesh(model.getBaseImage()),
                addon,
                model.getDecals(),
                embed);
    }

    private String asJSON(final ModelSet ms, boolean doAddons, String colour) {
        assert(printableUnits.size()==1);
        final PrintableUnit pu = printableUnits.get(0);

        final List<String> silhouettes = getTTSSilhouettes(doAddons);
        List<String> results = new ArrayList<>();

        List<String> puEmbeds = pu.asEmbeddedJSON(ms, doAddons);
        List<String> puSilhouettes = pu.getTTSSilhouettes(doAddons);

        Set<TTSModel> models = ms.getModels(getUnitID());
        int curEmbed = 0;

        for( TTSModel model : models) {
            try {
                results.add(asJSONInner(model, puEmbeds.get(curEmbed), puSilhouettes, doAddons, silhouettes, colour));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                throw e;
            }
            curEmbed = (curEmbed+1) % puEmbeds.size();
        }

        return String.join(",\n", results);

    }

    @Override
    public String asFactionJSON(final ModelSet ms, boolean doAddons) {
        return asJSON(ms, doAddons, sectoral.getTint());
    }

    @Override
    public String asArmyJSON(int combatGroup_idx, final EquivalentModelSet ms, final boolean doAddons) throws IllegalArgumentException {
        return asJSON(ms, doAddons, CombatGroup.getTint(combatGroup_idx));
    }
}
