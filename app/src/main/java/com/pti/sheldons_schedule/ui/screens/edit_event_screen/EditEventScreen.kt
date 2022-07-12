package com.pti.sheldons_schedule.ui.screens.edit_event_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.ui.common.CreateOrEditEventViewModel
import com.pti.sheldons_schedule.ui.common.ScreenContent
import com.pti.sheldons_schedule.ui.screens.create_event_screen.ModalBottomSheet
import com.pti.sheldons_schedule.util.Constants.FIELD_COUNT
import com.pti.sheldons_schedule.util.Constants.PADDING_WIDTH_SUM
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditEventScreen(
    navController: NavController,
    viewModel: CreateOrEditEventViewModel = hiltViewModel()
) {

    val screenState by viewModel.editEventScreenState.collectAsState()
    val focusManager = LocalFocusManager.current

    ModalBottomSheet(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        data = screenState?.options.orEmpty(),
        header = screenState?.options?.first()?.title?.let { stringResource(it) }.orEmpty(),
        nameGetter = { stringResource(id = it.nameId) },
        onClick = {
            focusManager.clearFocus()
        }
    ) { sheetState ->

        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(key1 = screenState?.options) {
            if (!screenState?.options.isNullOrEmpty()) {
                scope.launch {
                    sheetState.show()
                }
            }
        }


        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val halfFieldWidth = (this.maxWidth.value.toInt() - PADDING_WIDTH_SUM) / FIELD_COUNT

            Column(modifier = Modifier.fillMaxSize()) {
                TopToolbar(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .height(58.dp)
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                )

                ScreenContent(
                    state = screenState,
                    fieldWidth = halfFieldWidth.dp,
                    focusRequester = focusRequester,
                    onTitleEdited = {},
                    onFocusChanged = {},
                    onDescriptionEdited = {},
                    onStartDatePicked = {},
                    onEndDatePicked = {},
                    onTimeStartPicked = { hour, minutes -> },
                    onTimeEndPicked = { hour, minutes -> },
                    onRepeatFieldClicked = { },
                    onPriorityFieldClicked = { },
                    onRemindFieldClicked = {}
                )
            }

            BottomToolbar(
                onSaveEventClicked = { },
                onDeleteEventClicked = { },
                modifier = Modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}