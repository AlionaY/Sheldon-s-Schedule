package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pti.sheldons_schedule.ui.screens.edit_event_screen.TopToolbar

private const val FIELD_HEIGHT = 25

@Composable
fun ToDoListScreen(
    eventId: Long,
    navController: NavController,
    viewModel: ToDoListViewModel = hiltViewModel()
) {
    val event by viewModel.event.collectAsState(initial = null)

    viewModel.getEvent(eventId)

    Column(modifier = Modifier.fillMaxSize()) {
        TopToolbar(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(start = 15.dp)
        )

        EventTitle(
            title = event?.event?.title.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(start = 15.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            event?.toDoList?.forEach { todo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FIELD_HEIGHT.dp)
                        .padding(start = 15.dp, top = 15.dp)
                ) {
                    Checkbox(
                        checked = todo.completed,
                        onCheckedChange = { viewModel.onCheckedChange(it, todo) },
                        modifier = Modifier.wrapContentSize()
                    )
                    BasicTextField(
                        value = todo.title,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                    )
                }
            }
        }
    }
}