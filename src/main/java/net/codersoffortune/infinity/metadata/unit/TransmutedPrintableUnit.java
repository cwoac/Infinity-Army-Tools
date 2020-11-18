package net.codersoffortune.infinity.metadata.unit;

import com.codepoetics.protonpack.StreamUtils;
import net.codersoffortune.infinity.SECTORAL;
import net.codersoffortune.infinity.db.Database;
import net.codersoffortune.infinity.metadata.MappedFactionFilters;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
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
    public void printCSVRecord(CSVPrinter out) throws IOException {
        super.printCSVRecord(out);
        for( PrintableUnit pu : printableUnits ) {
            pu.printCSVRecord(out);
        }
    }



    @Override
    public String asFactionJSON() {
        final String ttsName = getTTSName();
        final String ttsDescription = getTTSDescription();
        final List<String> states = getTTSSilhouettes();
        for( PrintableUnit pu : printableUnits ) {
            states.add(pu.asEmbeddedJSON());
            // TODO:: filter duplicate Ss
            states.addAll(pu.getTTSSilhouettes());
        }
        final String stateString = StreamUtils.zipWithIndex(states.stream())
                .map(x -> embedState(x.getValue(), x.getIndex()+2))
                .collect(Collectors.joining("\n,"));
        //final String states = String.join(",\n", ttsSilhouettes);
        final String ttsColour = sectoral.getTint();
        List<String> ttsModels = models.stream().map(m -> String.format(Database.getUnitTemplate(),
                ttsName,
                ttsDescription,
                ttsColour,
                m.getMeshes(),
                m.getDecals(),
                states)).collect(Collectors.toList());
        return String.join(",\n", ttsModels);
    }
}
