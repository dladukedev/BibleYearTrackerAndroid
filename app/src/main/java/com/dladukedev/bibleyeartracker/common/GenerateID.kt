package com.dladukedev.bibleyeartracker.common

import java.util.UUID
import javax.inject.Inject

interface GenerateID {
    operator fun invoke(): String
}

class GenerateUUIDUseCase @Inject constructor(): GenerateID {
    override fun invoke(): String {
        return UUID.randomUUID().toString()
    }
}