package com.orukunnn.shapesnapapp.data.model.preset

import androidx.compose.runtime.Stable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Stable
data class Preset @OptIn(ExperimentalTime::class) constructor(
    val presetId: String,
    val characterTagId: String,
    val createdAt: Instant,
    val imageUrl: String,
    val likedUserIds: List<String>,
    val savedUserIds: List<String>,
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
        likedUserIds = remote.likedUserIds,
        savedUserIds = remote.savedUserIds,
        blendShapeWeights = remote.blendShapeWeights,
        displayName = remote.displayName,
    )
}
