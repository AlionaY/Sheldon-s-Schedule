package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.pti.sheldons_schedule.ui.theme.LightSky
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
    data: List<Int>?,
    nameGetter: (Int) -> String,
    onClick: (index: Int) -> Unit,
    header: String,
    modifier: Modifier = Modifier,
    content: @Composable (state: ModalBottomSheetState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetContent = {
            Box(
                modifier = modifier
                    .navigationBarsPadding()
                    .background(LightSky)
            ) {
                BottomSheetContent(
                    data = data,
                    nameGetter = nameGetter,
                    header = header
                ) {
                    onClick(it)
                    scope.launch { sheetState.hide() }
                }
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = LightSky,
        sheetShape = RoundedCornerShape(3)
    ) {
        content(sheetState)
    }
}