package com.dladukedev.bibleyeartracker.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dladukedev.bibleyeartracker.bibleReading.display.BibleReadingScreen
import com.dladukedev.bibleyeartracker.settings.display.SettingsScreen

object Routes {
    const val Home = "home"
    const val Settings = "setting"
}

@Composable
fun Router(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(route = Routes.Home) {
           BibleReadingScreen(
               goToSettingScreen = { navController.navigate(Routes.Settings)},
           )
        }
        composable(route = Routes.Settings) {
           SettingsScreen(
               goBack = { navController.popBackStack() }
           )
        }
    }
}