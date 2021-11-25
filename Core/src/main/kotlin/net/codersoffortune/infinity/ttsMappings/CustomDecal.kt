package net.codersoffortune.infinity.ttsMappings

import kotlinx.serialization.Serializable

@Serializable
data class CustomDecal(
    val Name: String,
    val ImageUrl: String,
    val Size: Float
)