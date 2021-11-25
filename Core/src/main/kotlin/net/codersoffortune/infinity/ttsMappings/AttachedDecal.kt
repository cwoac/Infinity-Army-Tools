package net.codersoffortune.infinity.ttsMappings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachedDecal(
    @SerialName("Transform")
    val transform: Transform,
    @SerialName("CustomDecal")
    val customDecal: CustomDecal
)