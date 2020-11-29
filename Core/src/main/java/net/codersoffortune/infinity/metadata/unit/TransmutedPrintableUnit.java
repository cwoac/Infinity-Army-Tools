package net.codersoffortune.infinity.metadata.unit;

import com.codepoetics.protonpack.StreamUtils;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import net.codersoffortune.infinity.tts.ModelSet;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashSet;
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



    @Override
    public String asFactionJSON(final ModelSet ms, boolean doAddons) {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final String addon = doAddons?Database.getAddonTemplate(s):"";
        final Set<String> states = new HashSet<>(getTTSSilhouettes(doAddons));
        for( PrintableUnit pu : printableUnits ) {
            states.add(pu.asEmbeddedJSON(ms, doAddons));
            states.addAll(pu.getTTSSilhouettes(doAddons));
        }
        final String stateString = StreamUtils.zipWithIndex(states.stream().filter(s->!s.isEmpty()))
                .map(x -> embedState(x.getValue(), x.getIndex()+2))
                .collect(Collectors.joining("\n,"));
        //final String states = String.join(",\n", ttsSilhouettes);
        final String ttsColour = sectoral.getTint();
        List<String> ttsModels =  ms.getModels(getUnitID()).stream().map(m -> String.format(Database.getUnitTemplate(),
                ttsName,
                ttsDescription,
                ttsColour,
                m.getMeshes(),
                addon,
                m.getDecals(),
                stateString)).collect(Collectors.toList());
        return String.join(",\n", ttsModels);
    }
}
