package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.data.Options
import com.pti.sheldons_schedule.ui.theme.LightSky
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    state: CreateEventScreenState,
    onSelected: (Options, String) -> Unit,
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
                    items = state.options.string.map { stringResource(id = it) },
                    header = stringResource(id = R.string.repeat),
                    onClick = {
                        onSelected(state.options, it)
                        scope.launch { sheetState.hide() }
                    }
                )
            }
        },
        sheetState = sheetState,
        sheetBackgroundColor = LightSky,
        sheetShape = RoundedCornerShape(3)
    ) {
        content(sheetState)
    }
}