package net.codersoffortune.infinity.metadata.unit;

import com.codepoetics.protonpack.StreamUtils;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.SIZE;
import net.codersoffortune.infinity.armylist.CombatGroup;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.DecalBlockModel;
import net.codersoffortune.infinity.tts.EquivalentModelSet;
import net.codersoffortune.infinity.tts.ModelSet;
import net.codersoffortune.infinity.tts.TTSModel;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.*;
import java.util.stream.Collectors;

public class TransmutedPrintableUnit extends PrintableUnit {
    private static final Logger logger = LogManager.getLogger(TransmutedPrintableUnit.class);
    private final List<PrintableUnit> printableUnits = new ArrayList<>();

    public List<PrintableUnit> getPrintableUnits() {
        return printableUnits;
    }

    public TransmutedPrintableUnit(TransmutedCompactedUnit src, SECTORAL sectoral) throws InvalidObjectException {
        super(src, sectoral);
        boolean skipped = false;
        for( CompactedUnit cu : src.getCompactedUnits()) {
            // The first one gets folded into this.
            if( !skipped ) {
                skipped = true;
                continue;
            }
            printableUnits.add(new PrintableUnit(cu, sectoral));
        }
    }

    @Override
    public void printCSVRecord(CSVPrinter out, final ModelSet ms) throws IOException {
        super.printCSVRecord(out, ms);
        for( PrintableUnit pu : printableUnits ) {
            pu.printCSVRecord(out, ms);
        }
    }




    private String asJSONInner(final TTSModel model, final Collection<String> embeddedModels, final Collection<String> embeddedSilhouettes, boolean doAddons, final List<String> silhouettes, final String colour) {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final String addon = doAddons?Database.getAddonTemplate(s):"";
        List<String> states = new ArrayList<>();
        // There are two types here - if the silhouette size changes, we want active -> active sil -> inactive -> inactive sil
        // but if it doesn't, then we want active -> inactive -> sil
        boolean silChange = !silhouettes.containsAll(embeddedSilhouettes);
        if (silChange) {
            states.addAll(silhouettes);
            states.addAll(embeddedModels);
            // don't include a silhouette we have already.
            states.addAll(embeddedSilhouettes.stream().filter(e->!silhouettes.contains(e)).collect(Collectors.toList()));
        } else {
            states.addAll(embeddedModels);
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

    private Optional<String> asJSON(final ModelSet ms, boolean doAddons, String colour) {
        final List<String> silhouettes = getTTSSilhouettes(doAddons);
        List<String> results = new ArrayList<>();
        Collection<String> puSilhouettes = new HashSet<>();
        printableUnits.forEach(pu -> puSilhouettes.addAll(pu.getTTSSilhouettes(doAddons)));

        Set<TTSModel> models = ms.getModels(getUnitID());
        if (models.isEmpty()) {
            logger.debug("No models found for {}, skipping", this);
            return Optional.empty();
        }

        // Pre-fetch embedded model lists (may be empty for same-model profiles like Cyberplug remotes)
        List<List<String>> perPuEmbeds = printableUnits.stream()
                .map(pu -> pu.asEmbeddedJSON(ms, doAddons))
                .collect(Collectors.toList());

        int curModel = 0;
        for (TTSModel model : models) {
            List<String> embeds = new ArrayList<>();
            for (int i = 0; i < printableUnits.size(); i++) {
                List<String> puEmbed = perPuEmbeds.get(i);
                if (puEmbed.isEmpty()) {
                    // No separate catalogue entry for this profile — reuse parent's model
                    embeds.add(printableUnits.get(i).asEmbeddedJSONWithModel(model, doAddons));
                } else {
                    embeds.add(puEmbed.get(curModel));
                }
            }
            results.add(asJSONInner(model, embeds, puSilhouettes, doAddons, silhouettes, colour));
            curModel++;
        }
        return Optional.of(String.join(",\n", results));

    }

    @Override
    public Optional<String> asFactionJSON(final ModelSet ms, boolean doAddons) {
        return asJSON(ms, doAddons, sectoral.getTint());
    }

    @Override
    public String asArmyJSON(CombatGroup combatGroup, final EquivalentModelSet ms, final boolean doAddons) throws IllegalArgumentException {
        return asJSON(ms, doAddons, combatGroup.getTint()).orElse("");
    }
}
