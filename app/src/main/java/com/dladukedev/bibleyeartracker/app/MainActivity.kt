package com.dladukedev.bibleyeartracker.app

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.common.models.Theme
import com.dladukedev.feature.settings.rememberTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            val theme = rememberTheme()

            setWindowFromTheme(theme = theme)

            BibleYearTrackerTheme(theme = theme) {
                Router()
            }
        }
    }

    @Composable
    fun setWindowFromTheme(theme: Theme) {
        val isDarkTheme = isSystemInDarkTheme()
        val activity = (LocalContext.current as Activity)
        LaunchedEffect(theme) {
            val windowColor = when (theme) {
                Theme.SYSTEM -> if (isDarkTheme) R.color.black else R.color.white
                Theme.DARK -> R.color.black
                Theme.LIGHT -> R.color.white
            }
            activity.window.setBackgroundDrawable(ColorDrawable(windowColor))
        }
    }
}
