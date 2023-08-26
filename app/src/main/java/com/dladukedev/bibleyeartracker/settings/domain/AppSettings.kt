package com.dladukedev.bibleyeartracker.settings.domain

import java.time.LocalDate

enum class Theme { SYSTEM, DARK, LIGHT, }

data class AppSettings(
    val readingGoalStartDate: LocalDate,
    val theme: Theme,
)
