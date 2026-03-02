package com.orukunnn.shapesnapapp.data.model

import com.google.firebase.Timestamp

data class PresetEntity(
    val presetId: String = "",
    val characterTagId: String = "",
    val createdAt: Timestamp? = null,
    val imageUrl: String = "",
    val likes: Int = 0,
    val blendShapeWeights: Map<String, Double> = emptyMap(),
    val displayName: String = "",
)
