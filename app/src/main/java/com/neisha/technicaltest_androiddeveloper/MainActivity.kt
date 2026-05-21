package com.neisha.technicaltest_androiddeveloper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
                    composable(
                        route = Screen.UserList.route,
                        enterTransition = { fadeIn(animationSpec = tween(300)) },
                        exitTransition = { fadeOut(animationSpec = tween(300)) },
                        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                        popExitTransition = { fadeOut(animationSpec = tween(300)) }
                    ) {
                        UserListScreen(
                            windowSizeClass = windowSizeClass,
                            onNavigateToAddUser = {
                                navController.navigate(Screen.AddUser.route)
                            }
                        )
                    }
                    composable(
                        route = Screen.AddUser.route,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { -it / 3 },
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { -it / 3 },
                                animationSpec = tween(300)
                            ) + fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(300)
                            ) + fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        AddUserScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}