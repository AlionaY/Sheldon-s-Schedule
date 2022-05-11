package com.pti.sheldons_schedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pti.sheldons_schedule.ui.navigation.Navigation
import com.pti.sheldons_schedule.ui.screens.CreateEventScreen
import com.pti.sheldons_schedule.ui.theme.Sheldons_ScheduleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sheldons_ScheduleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    val navController = rememberAnimatedNavController()
//                    Navigation(navHostController = navController)
                    CreateEventScreen()
                }
            }
        }
    }
}