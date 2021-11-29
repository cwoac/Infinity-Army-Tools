package net.codersoffortune.infinity.collection

import net.codersoffortune.infinity.SECTORAL
import net.codersoffortune.infinity.metadata.unit.CompactedUnit
import net.codersoffortune.infinity.metadata.unit.PrintableUnit
import net.codersoffortune.infinity.metadata.unit.TransmutedCompactedUnit
import java.awt.print.Printable
import kotlin.streams.toList

/**
 * A GuiModel is basically a compacted unit, but presents itself for GUI consumption
 */
data class GuiModel(val unit: CompactedUnit, val sectoral: SECTORAL) {
    val printableUnit: PrintableUnit by lazy {
        unit.getPrintableUnit(sectoral)
    }

    val printableUnits: Collection<PrintableUnit> by lazy {
        if (unit is TransmutedCompactedUnit ) {
            unit.compactedUnits.stream().map {it.getPrintableUnit(sectoral)}.toList()
        } else {
            listOf(printableUnit)
        }
    }

    override fun toString(): String {
        return String.format("%s (%s)",printableUnit.name, unit.visibleItems.joinToString())
    }
}