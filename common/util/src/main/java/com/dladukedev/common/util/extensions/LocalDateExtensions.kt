package com.dladukedev.common.util.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

fun LocalDate.toMilliseconds(): Long {
    return LocalDateTime.of(this, LocalTime.NOON)
        .toInstant(ZoneOffset.UTC)
        .toEpochMilli()
}

fun Long.asLocalDate(zoneId: String = "UTC"): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.of(zoneId))
        .toLocalDate()
}