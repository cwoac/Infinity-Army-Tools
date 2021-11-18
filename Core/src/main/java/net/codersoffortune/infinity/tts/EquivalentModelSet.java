package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.UnitID;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EquivalentModelSet extends ModelSet {

    transient final Set<EquivalenceMapping> equivalenceMappings;

    public EquivalentModelSet(Set<EquivalenceMapping> mappings) {
        super();
        equivalenceMappings = mappings;
    }

    public Set<EquivalenceMapping> getEquivalenceMappings() {
        return equivalenceMappings;
    }


    @Override
    public Set<TTSModel> getModels(final UnitID unitID) {
        // Do we have this model?
        if (hasUnit(unitID)) {
            return models.get(unitID);
        }
        // OK, do we know some equivalents for this model?
        Optional<EquivalenceMapping> maybeMapping = equivalenceMappings.stream().filter(m -> m.contains(unitID)).findFirst();
        if (maybeMapping.isEmpty()) {
            return new HashSet<>();
        }
        EquivalenceMapping mapping = maybeMapping.get();

        // OK, what about the equivalents?
        Optional<UnitID> equivalent = mapping.getAllUnits().stream()
                .map(PrintableUnit::getUnitID)
                .filter(this::hasUnit)
                .findAny();
        if (equivalent.isEmpty()) {
            return new HashSet<>();
        }

        return models.get(equivalent.get());
    }


    /**
     * expand the equivalences into a full model set.
     * @return a new modelset which is equivalent to this EquivalentModelSet
     */
    public ModelSet expand() {
        ModelSet ms = new ModelSet();
        // First add the explicit models.
        models.forEach(ms::addModels);
        for(EquivalenceMapping mapping : equivalenceMappings) {
            // Do we have a model for this one?
            Optional<UnitID> maybeUnitID = mapping.getAllUnits().stream()
                    .map(PrintableUnit::getUnitID)
                    .filter(models::containsKey)
                    .findAny();
            if (maybeUnitID.isEmpty()) {
                System.out.printf("No model for equivalence based on %s", mapping.getBaseUnit());
                continue;
            }
            // OK, look up that Unit's models
            UnitID mappingUnitID = maybeUnitID.get();
            Set<TTSModel> mappingModels = models.get(mappingUnitID);

            // Give them a nice name
            mappingModels.forEach(m->m.setName(mapping.getBaseUnit().getTTSName()));

            // Now add them all to ms
            mapping.getAllUnits().stream()
                    .map(PrintableUnit::getUnitID)
                    .filter(u->!ms.hasUnit(u))
                    .forEach(u->ms.addModels(u, mappingModels));
        }
        return ms;
    }


}
