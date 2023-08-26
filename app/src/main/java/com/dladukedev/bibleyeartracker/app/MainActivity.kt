package com.dladukedev.bibleyeartracker.app

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dladukedev.bibleyeartracker.app.theme.BibleYearTrackerTheme
import com.dladukedev.bibleyeartracker.settings.domain.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val initialTheme = runBlocking { settingRepository.getTheme() }

        setContent {
            val theme by settingRepository.observeTheme().collectAsState(initial = initialTheme)
            BibleYearTrackerTheme(theme = theme) {
                Router()
            }
        }
    }
}
