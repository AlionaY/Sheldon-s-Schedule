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
import com.pti.sheldons_schedule.data.BottomSheetType.*
import com.pti.sheldons_schedule.data.CreateEventScreenState
import com.pti.sheldons_schedule.ui.theme.LightSky
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheet(
    state: CreateEventScreenState,
    onRepeatSelected: (String) -> Unit,
    onPrioritySelected: (String) -> Unit,
    onRemindSelected: (String) -> Unit,
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
                state.optionsList?.let { options ->
                    when (state.bottomSheetType) {
                        Repeat -> BottomSheetContent(
                            items = options,
                            header = stringResource(id = R.string.repeat),
                            onClick = {
                                onRepeatSelected(it)
                                scope.launch { sheetState.hide() }
                            }
                        )
                        Priority -> BottomSheetContent(
                            items = options,
                            header = stringResource(id = R.string.priority),
                            onClick = {
                                onPrioritySelected(it)
                                scope.launch { sheetState.hide() }
                            }
                        )
                        Reminder -> BottomSheetContent(
                            items = options,
                            header = stringResource(id = R.string.remind),
                            onClick = {
                                onRemindSelected(it)
                                scope.launch { sheetState.hide() }
                            }
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