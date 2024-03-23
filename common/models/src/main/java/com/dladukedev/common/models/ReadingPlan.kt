package com.dladukedev.common.models

data class ReadingPlan(
    val key: ReadingPlanKey,
    val readingSets: List<ReadingSet>
)