package com.dladukedev.bibleyeartracker.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingScreen
import com.dladukedev.feature.settings.settingscreen.SettingsScreen
import com.dladukedev.feature.statistics.StatisticsScreen
import kotlinx.coroutines.launch

object Routes {
    const val Home = "home"
    const val Stats = "stats"
    const val Settings = "setting"
}

@Composable
fun Router(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(route = Routes.Home) {
           BibleReadingScreen(
               goToSettingScreen = { navController.navigate(Routes.Settings)},
               goToStatisticsScreen = { navController.navigate(Routes.Stats)},
           )
        }
        composable(route = Routes.Stats) {
            StatisticsScreen(
                goBack = { navController.navigateUp() }
            )
        }
        composable(route = Routes.Settings) {
            SettingsScreen(
                goBack = { navController.navigateUp() }
            )
        }
    }
}
