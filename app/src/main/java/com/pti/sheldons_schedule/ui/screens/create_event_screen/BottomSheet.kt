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
import com.pti.sheldons_schedule.data.*
import com.pti.sheldons_schedule.data.BottomSheetType.*
import com.pti.sheldons_schedule.ui.theme.LightSky

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    state: CreateEventScreenState,
    modifier: Modifier = Modifier,
    content: @Composable (state: ModalBottomSheetState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val repeatOptionsList = mutableListOf<String>()
    val priorityOptionsList = mutableListOf<String>()
    val remindOptionsList = mutableListOf<String>()

    RepeatOptions.values().forEach {
        repeatOptionsList += stringResource(id = it.stringRes)
    }

    PriorityOptions.values().forEach {
        priorityOptionsList += stringResource(id = it.stringRes)
    }

    RemindOptions.values().forEach {
        remindOptionsList += stringResource(id = it.stringRes)
    }

    ModalBottomSheetLayout(
        sheetContent = {
            Box(
                modifier = modifier
                    .navigationBarsPadding()
                    .background(LightSky)
            ) {
                when (state.bottomSheetType) {
                    Repeat -> BottomSheetContent(
                        items = repeatOptionsList, header = stringResource(id = R.string.repeat)
                    )
                    Priority -> BottomSheetContent(
                        items = priorityOptionsList, header = stringResource(id = R.string.priority)
                    )
                    Reminder -> BottomSheetContent(
                        items = remindOptionsList, header = stringResource(id = R.string.remind)
                    )
                    else -> None
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