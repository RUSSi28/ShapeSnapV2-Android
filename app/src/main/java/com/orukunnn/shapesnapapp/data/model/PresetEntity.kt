package com.orukunnn.shapesnapapp.data.model

data class PresetEntity(
    val presetId: String = "",
    val characterTagId: String = "",
    val uploadedBy: String = "",
    val imageUrl: String = "",
    val likes: Int = 0,
    val blendShapeWeights: Map<String, Double> = emptyMap(),
    val displayName: String = "",
)
