package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.metadata.unit.PrintableUnit;
import net.codersoffortune.infinity.metadata.unit.UnitID;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EquivalentModelSet extends ModelSet {

    transient final Set<Catalogue.EquivalenceMapping> equivalenceMappings;

    public EquivalentModelSet(Set<Catalogue.EquivalenceMapping> mappings) {
        super();
        equivalenceMappings = mappings;
    }

    public Set<Catalogue.EquivalenceMapping> getEquivalenceMappings() {
        return equivalenceMappings;
    }


    @Override
    public Set<TTSModel> getModels(final UnitID unitID) {
        // Do we have this model?
        if (hasUnit(unitID)) {
            return models.get(unitID);
        }
        // OK, do we know some equivalents for this model?
        Optional<Catalogue.EquivalenceMapping> maybeMapping = equivalenceMappings.stream().filter(m -> m.contains(unitID)).findFirst();
        if (!maybeMapping.isPresent()) {
            return new HashSet<>();
        }
        Catalogue.EquivalenceMapping mapping = maybeMapping.get();

        // OK, what about the equivalents?
        Optional<UnitID> equivalent = mapping.getAllUnits().stream()
                .map(PrintableUnit::getUnitID)
                .filter(this::hasUnit)
                .findAny();
        if (!equivalent.isPresent()) {
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
        models.entrySet().stream().forEach(e->ms.addModels(e.getKey(), e.getValue()));
        for(Catalogue.EquivalenceMapping mapping : equivalenceMappings) {
            // Do we have a model for this one?
            Optional<UnitID> maybeUnitID = mapping.getAllUnits().stream()
                    .map(PrintableUnit::getUnitID)
                    .filter(models::containsKey)
                    .findAny();
            if (!maybeUnitID.isPresent()) {
                System.out.printf("No model for equivalence based on %s", mapping.getRepresentative());
                continue;
            }
            // OK, look up that Unit's models
            UnitID mappingUnitID = maybeUnitID.get();
            Set<TTSModel> mappingModels = models.get(mappingUnitID);

            // Now add them all to ms
            mapping.getAllUnits().stream()
                    .map(PrintableUnit::getUnitID)
                    .filter(u->!ms.hasUnit(u))
                    .forEach(u->ms.addModels(u, mappingModels));
        }
        return ms;
    }


}
