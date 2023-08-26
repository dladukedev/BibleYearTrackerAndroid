package com.dladukedev.bibleyeartracker.common

import java.time.LocalDate
import javax.inject.Inject

interface GetCurrentDate {
    operator fun invoke(): LocalDate
}

class GetCurrentDateUseCase @Inject constructor(): GetCurrentDate {
    override fun invoke(): LocalDate {
        return LocalDate.now()
    }
}