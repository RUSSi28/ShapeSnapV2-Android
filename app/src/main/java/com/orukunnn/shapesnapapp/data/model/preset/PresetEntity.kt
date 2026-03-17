package com.orukunnn.shapesnapapp.data.model.preset

import com.google.firebase.Timestamp

data class PresetEntity(
    val presetId: String = "",
    val characterTagId: String = "",
    val createdAt: Timestamp? = null,
    val imageUrl: String = "",
    val likedUserIds: List<String> = emptyList(),
    val savedUserIds: List<String> = emptyList(),
    val blendShapeWeights: Map<String, Double> = emptyMap(),
    val displayName: String = "",
)
