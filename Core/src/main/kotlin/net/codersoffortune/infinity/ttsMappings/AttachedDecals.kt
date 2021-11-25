package net.codersoffortune.infinity.ttsMappings

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AttachedDecals(
    @SerialName("AttachedDecals")
    val attachedDecals: List<AttachedDecal>
)