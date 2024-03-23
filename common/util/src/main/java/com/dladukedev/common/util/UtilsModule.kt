package com.dladukedev.common.util

import com.dladukedev.common.util.datetime.FormatDate
import com.dladukedev.common.util.datetime.FormatDateUseCase
import com.dladukedev.common.util.datetime.GetCurrentDate
import com.dladukedev.common.util.datetime.GetCurrentDateUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {
    @Binds abstract fun bindFormatDate(useCase: FormatDateUseCase): FormatDate
    @Binds abstract fun getCurrentDate(useCase: GetCurrentDateUseCase): GetCurrentDate

}