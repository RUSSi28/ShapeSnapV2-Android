package com.orukunnn.shapesnapapp.data.model.preset

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object PresetsFactory {
    @OptIn(ExperimentalTime::class)
    fun createPreset(
        presetId: String = "id_1",
        characterTagId: String = "tag_1",
        createdAt: Instant = Instant.fromEpochMilliseconds(1000L),
        imageUrl: String = "",
        likedUserIds: List<String> = emptyList(),
        savedUserIds: List<String> = emptyList(),
        blendShapeWeight: Map<String, Double> = mapOf("eye_open" to 0.5, "mouth_smile" to 0.8),
        displayName: String = ""
    ): Preset {
        return Preset(
            presetId = presetId,
            characterTagId = characterTagId,
            createdAt = createdAt,
            imageUrl = imageUrl,
            likedUserIds = likedUserIds,
            savedUserIds = savedUserIds,
            blendShapeWeights = blendShapeWeight,
            displayName = displayName
        )
    }

    @OptIn(ExperimentalTime::class)
    fun createPresetList(): ImmutableList<Preset> {
        return buildList {
            repeat(10) { index ->
                add(createPreset(presetId = "id_$index"))
            }
        }.toPersistentList()
    }
}
