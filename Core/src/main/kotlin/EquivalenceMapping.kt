package net.codersoffortune.infinity.tts;

import net.codersoffortune.infinity.metadata.unit.PrintableUnit
import net.codersoffortune.infinity.metadata.unit.UnitID

data class EquivalenceMapping(val baseUnit : PrintableUnit) {
    val equivalentUnits: MutableSet<PrintableUnit> = mutableSetOf()
    val allUnits: MutableSet<PrintableUnit> = mutableSetOf(baseUnit)

    /**
     * Check to see if the passed unit is visibly equivalent to the ones in this mapping. If so, claim it.
     *
     * @param unit to consider
     * @return true iff the unit is claimed.
     */
    fun addUnitMaybe(unit: PrintableUnit): Boolean {
        if (!baseUnit.isEquivalent(unit)) {
            return false
        }

        if (baseUnit.functionallyEquals(unit) ||
            equivalentUnits.stream().anyMatch { pu: PrintableUnit ->
                pu.functionallyEquals(
                    unit
                )
            }
        ) {
            // Don't bother adding it if it is the base object
            return true
        }
        equivalentUnits.add(unit)
        allUnits.add(unit)
        return true
    }

    operator fun contains(unitID: UnitID): Boolean {
        return allUnits.stream().anyMatch { u: PrintableUnit -> u.unitID == unitID }
    }


}