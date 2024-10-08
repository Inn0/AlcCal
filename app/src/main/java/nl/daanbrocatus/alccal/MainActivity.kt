package nl.daanbrocatus.alccal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import nl.daanbrocatus.alccal.composables.common.BottomNavigationBar
import nl.daanbrocatus.alccal.common.BottomNavigationItem
import nl.daanbrocatus.alccal.database.AppDatabase
import nl.daanbrocatus.alccal.database.DateTimeRepository
import nl.daanbrocatus.alccal.screens.home.HomeScreen
import nl.daanbrocatus.alccal.screens.SettingsScreen
import nl.daanbrocatus.alccal.screens.home.HomeScreenViewModel
import nl.daanbrocatus.alccal.screens.stats.MonthlyScreen
import nl.daanbrocatus.alccal.screens.stats.MonthlyScreenViewModel
import nl.daanbrocatus.alccal.ui.theme.AlcCalTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase
    private lateinit var dateTimeRepository: DateTimeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        val dateTimeDao = database.dateTimeDao()
        dateTimeRepository = DateTimeRepository(dateTimeDao)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AlcCalTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavigationItem.items[1].title,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(BottomNavigationItem.items[0].title) { MonthlyScreen(viewModel = MonthlyScreenViewModel(dateTimeRepository)) }
                        composable(BottomNavigationItem.items[1].title) { HomeScreen(viewModel = HomeScreenViewModel(dateTimeRepository)) }
                        composable(BottomNavigationItem.items[2].title) { SettingsScreen() }
                    }
                }
            }
        }
    }
}