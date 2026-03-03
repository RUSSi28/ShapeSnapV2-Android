package com.orukunnn.shapesnapapp.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Instant.convertShapeSnapDateFormat(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.JST)
    println(localDateTime.toString())
    val format = LocalDateTime.Format {
        year(); char('-'); monthNumber(); char('-'); dayOfMonth()
        char(' '); hour(); char(':'); minute()
    }
    return localDateTime.format(format)
}

val TimeZone.Companion.JST: TimeZone
    get() = TimeZone.of("Asia/Tokyo")