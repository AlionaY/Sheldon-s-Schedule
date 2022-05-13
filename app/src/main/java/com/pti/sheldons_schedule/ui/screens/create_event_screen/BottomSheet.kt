package com.pti.sheldons_schedule.ui.screens.create_event_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.BottomSheetType.*
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.ui.theme.LightSky

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    state: CreateEventScreenState,
    modifier: Modifier = Modifier,
    content: @Composable (state: ModalBottomSheetState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        sheetContent = {
            Box(
                modifier = modifier
                    .navigationBarsPadding()
                    .background(LightSky)
            ) {
                state.optionsList?.let {
                    when (state.bottomSheetType) {
                        Repeat -> BottomSheetContent(
                            items = it, header = stringResource(id = R.string.repeat)
                        )
                        Priority -> BottomSheetContent(
                            items = it, header = stringResource(id = R.string.priority)
                        )
                        Reminder -> BottomSheetContent(
                            items = it, header = stringResource(id = R.string.remind)
                        )
                        else -> None
                    }
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