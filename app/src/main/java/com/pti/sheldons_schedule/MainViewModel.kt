package com.pti.sheldons_schedule

import android.content.res.Configuration
import androidx.compose.ui.unit.Density
import androidx.lifecycle.ViewModel
import com.pti.sheldons_schedule.data.AnimationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.hypot

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    val animationState = MutableStateFlow(AnimationState())

    fun toggleClicked() {
        animationState.update { it.copy(isClicked = !it.isClicked) }
    }

    fun resetIsClicked() {
        animationState.update { it.copy(isClicked = false) }
    }

    fun updateAnimationRadius(radius: Float) {
        animationState.update { it.copy(radius = radius) }
    }

    fun getMaxRadiusPx(density: Density, config: Configuration) {
        val width = screenParamToPx(config.screenWidthDp, density)
        val height = screenParamToPx(config.screenHeightDp, density)
        val maxRadiusPx = hypot(width, height)

        animationState.update { it.copy(maxRadiusPx = maxRadiusPx) }
    }

    private fun screenParamToPx(param: Int, density: Density) = param.toFloat() * density.density
}