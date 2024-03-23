package com.dladukedev.core.preferences

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PreferencesCoreModule {
    @Binds abstract fun bindPreferenceRepository(repo: SettingLocalRepository): SettingsRepository

    @Binds abstract fun bindGetTheme(useCase: GetThemeUseCase): GetTheme
    @Binds abstract fun bindSetTheme(useCase: SetThemeUseCase): SetTheme
    @Binds abstract fun bindSubscribeTheme(useCase: SubscribeToThemeUseCase): SubscribeToTheme
}