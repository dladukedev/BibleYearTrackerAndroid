package com.dladukedev.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dladukedev.common.models.Theme
import com.dladukedev.core.preferences.GetTheme
import com.dladukedev.core.preferences.SubscribeToTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@Composable
fun rememberTheme(): Theme {
    return rememberThemeInternal()
}

@HiltViewModel
internal class RememberThemeViewModel @Inject constructor(
    val subscribeToTheme: SubscribeToTheme,
    val getTheme: GetTheme,
) : ViewModel()

@Composable
private fun rememberThemeInternal(
    viewModel: RememberThemeViewModel = hiltViewModel()
): Theme {
    val initialTheme = remember(Unit) {
        runBlocking { viewModel.getTheme() }
    }

    val theme by viewModel.subscribeToTheme.values.collectAsStateWithLifecycle(initialValue = initialTheme)

    return theme
}
