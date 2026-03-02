package com.orukunnn.shapesnapapp.data.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object PresetsFactory {
    @OptIn(ExperimentalTime::class)
    fun createPreset(
        presetId: String = "id_1",
        characterTagId: String = "tag_1",
        createdAt: Instant = Instant.fromEpochMilliseconds(1000L),
        imageUrl: String = "",
        likes: Int = 0,
        blendShapeWeight: Map<String, Double> = mapOf("eye_open" to 0.5, "mouth_smile" to 0.8),
        displayName: String = ""
    ): Preset {
        return Preset(
            presetId = presetId,
            characterTagId = characterTagId,
            createdAt = createdAt,
            imageUrl = imageUrl,
            likes = likes,
            blendShapeWeights = blendShapeWeight,
            displayName = displayName
        )
    }

    @OptIn(ExperimentalTime::class)
    fun createPresetList(): List<Preset> {
        val presetList = mutableListOf<Preset>()
        repeat(10) {
            presetList.add(createPreset())
        }

        return presetList
    }
}