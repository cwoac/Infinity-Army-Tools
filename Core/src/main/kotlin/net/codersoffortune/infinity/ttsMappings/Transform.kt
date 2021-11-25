package net.codersoffortune.infinity.ttsMappings

import kotlinx.serialization.Serializable

@Serializable
data class Transform(
    val posX: Float,
    val posY: Float,
    val posZ: Float,
    val rotX: Float,
    val rotY: Float,
    val rotZ: Float,
    val scaleX: Float,
    val scaleY: Float,
    val scaleZ: Float
)