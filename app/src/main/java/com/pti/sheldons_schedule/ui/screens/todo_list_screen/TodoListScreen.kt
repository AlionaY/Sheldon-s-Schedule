package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import com.pti.sheldons_schedule.ui.common.IconedText
import androidx.compose.foundation.clickable
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
    val screenState by viewModel.screenState.collectAsState(initial = null)
    val addTextField by viewModel.addTextField.collectAsState(initial = false)

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
            title = screenState?.title.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(start = 15.dp)
        )

//        todo: make functionality of adding todo items
        Column(modifier = Modifier.fillMaxSize()) {
            if (screenState?.toDoList.isNullOrEmpty()) {
                IconedText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FIELD_HEIGHT.dp)
                        .padding(start = 30.dp)
                        .clickable { viewModel.onAddItemClicked() }
                )
            }

            if (addTextField) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(FIELD_HEIGHT.dp)
                        .padding(start = 15.dp, top = 15.dp)
                ) {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.wrapContentSize()
                    )
                    BasicTextField(
                        value = "",
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