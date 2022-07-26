package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.ui.common.DefaultCheckboxRow
import com.pti.sheldons_schedule.ui.navigation.NavDestination
import com.pti.sheldons_schedule.ui.navigation.navigate
import com.pti.sheldons_schedule.util.Constants.FIELD_HEIGHT

@Composable
fun ToDoListScreen(
    eventId: Long,
    navController: NavController,
    viewModel: ToDoListViewModel = hiltViewModel()
) {
    val event by viewModel.event.collectAsState(initial = null)
    val scrollState = rememberScrollState()

    viewModel.getEvent(eventId)

    Column(modifier = Modifier.fillMaxSize()) {
        TopToolbar(
            onBackClicked = { navController.popBackStack() },
            onEditEventClicked = {
                navController.navigate(
                    NavDestination.EditEventScreen,
                    event?.event?.creationDate.toString()
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 15.dp)
        )

        EventTitle(
            title = event?.event?.title.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 15.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            event?.toDoList?.forEachIndexed { index, _ ->
                DefaultCheckboxRow(
                    checked = event?.toDoList?.get(index)?.completed ?: false,
                    text = event?.toDoList?.get(index)?.title.orEmpty(),
                    onValueChanged = { },
                    onCheckedChange = { viewModel.onCheckedChange(it, index) },
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .fillMaxWidth()
                        .height(FIELD_HEIGHT.dp),
                    textFieldModifier = Modifier
                        .padding(horizontal = 10.dp)
                        .wrapContentSize()
                        .clickable {
                            //todo: show dialog
                        },
                    readOnly = true
                )
            }
        }
    }
}