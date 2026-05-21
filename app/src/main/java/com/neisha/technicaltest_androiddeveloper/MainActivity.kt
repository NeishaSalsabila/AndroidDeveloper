package com.neisha.technicaltest_androiddeveloper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neisha.technicaltest_androiddeveloper.ui.navigation.Screen
import com.neisha.technicaltest_androiddeveloper.ui.screen.AddUserScreen
import com.neisha.technicaltest_androiddeveloper.ui.screen.UserListScreen
import com.neisha.technicaltest_androiddeveloper.ui.theme.TechnicalTestAndroidDeveloperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechnicalTestAndroidDeveloperTheme(darkTheme = isSystemInDarkTheme()) {
                val navController = rememberNavController()
                val windowSizeClass = calculateWindowSizeClass(this)

                NavHost(
                    navController = navController,
                    startDestination = Screen.UserList.route
                ) {
                    composable(Screen.UserList.route) {
                        UserListScreen(
                            windowSizeClass = windowSizeClass,
                            onNavigateToAddUser = {
                                navController.navigate(Screen.AddUser.route)
                            }
                        )
                    }
                    composable(Screen.AddUser.route) {
                        AddUserScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}