package com.orukunnn.shapesnapapp.data.model.preset

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Preset @OptIn(ExperimentalTime::class) constructor(
    val presetId: String,
    val characterTagId: String,
    val createdAt: Instant,
    val imageUrl: String,
    val likes: Int,
    val blendShapeWeights: Map<String, Double>,
    val displayName: String,
) {
    @OptIn(ExperimentalTime::class)
    constructor(remote: PresetEntity) : this(
        presetId = remote.presetId,
        characterTagId = remote.characterTagId,
        createdAt = remote.createdAt?.let {
            Instant.fromEpochSeconds(it.seconds, it.nanoseconds)
        } ?: Instant.DISTANT_PAST,
        imageUrl = remote.imageUrl,
        likes = remote.likes,
        blendShapeWeights = remote.blendShapeWeights,
        displayName = remote.displayName,
    )
}