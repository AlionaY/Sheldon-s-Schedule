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
import com.pti.sheldons_schedule.data.BottomSheetType
import com.pti.sheldons_schedule.data.BottomSheetType.*
import com.pti.sheldons_schedule.ui.theme.LightSky

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    bottomSheetType: BottomSheetType,
    modifier: Modifier = Modifier,
    content: @Composable (state: ModalBottomSheetState) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val repeatStringList = listOf(
        stringResource(id = R.string.repeat_daily),
        stringResource(id = R.string.repeat_week_day),
        stringResource(id = R.string.repeat_weekly),
        stringResource(id = R.string.repeat_monthly),
        stringResource(id = R.string.repeat_annually),
        stringResource(id = R.string.repeat_custom)
    )
    val remindStringList = listOf(
        stringResource(id = R.string.remind_10_minutes),
        stringResource(id = R.string.remind_1_hour_before),
        stringResource(id = R.string.remind_1_day_before)
    )
    val priorityStringList = listOf(
        stringResource(id = R.string.priority_high),
        stringResource(id = R.string.priority_medium),
        stringResource(id = R.string.priority_low)
    )

    ModalBottomSheetLayout(
        sheetContent = {
            Box(
                modifier = modifier
                    .navigationBarsPadding()
                    .background(LightSky)
            ) {
                when (bottomSheetType) {
                    Repeat -> BottomSheetContent(
                        items = repeatStringList, header = stringResource(id = R.string.repeat)
                    )
                    Priority -> BottomSheetContent(
                        items = priorityStringList, header = stringResource(id = R.string.priority)
                    )
                    Reminder -> BottomSheetContent(
                        items = remindStringList, header = stringResource(id = R.string.remind)
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