package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ScreenState
import com.pti.sheldons_schedule.data.ToDo
import com.pti.sheldons_schedule.ui.common.DefaultCheckboxRow
import com.pti.sheldons_schedule.ui.common.HeightSpacer
import com.pti.sheldons_schedule.ui.common.IconedText
import com.pti.sheldons_schedule.util.Constants
import com.pti.sheldons_schedule.util.Constants.FIELD_HEIGHT


@Composable
fun CheckboxContent(
    state: ScreenState?,
    onTodoItemChanged: (String, Int) -> Unit,
    onCheckedChange: (Boolean, Int) -> Unit,
    onAddTodoItemClicked: () -> Unit
) {
    HeightSpacer(5.dp)

    state?.toDoList?.let {
        (0 until state.toDoList.size).forEachIndexed { index, todo ->
            DefaultCheckboxRow(
                text = state.toDoList[index].title,
                checked = state.toDoList[index].completed,
                onValueChanged = { onTodoItemChanged(it, index) },
                onCheckedChange = { onCheckedChange(it, index) },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
                    .height(FIELD_HEIGHT.dp),
                textFieldModifier = Modifier
                    .padding(horizontal = 10.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
            )
            HeightSpacer(5.dp)
        }
    }

    IconedText(
        text = stringResource(id = R.string.add_todo_item),
        onClick = { onAddTodoItemClicked() },
        modifier = Modifier
            .padding(start = 30.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    )
    HeightSpacer(5.dp)
}

@Composable
fun TopToolbar(
    onBackClicked: () -> Unit,
    onEditEventClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIos,
            contentDescription = "back",
            modifier = Modifier.clickable { onBackClicked() },
            tint = MaterialTheme.colors.onBackground
        )

        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "edit",
            modifier = Modifier.clickable { onEditEventClicked() },
            tint = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun EventTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ToDoList(
    todoList: List<ToDo>,
    checked: Boolean = false,
    onValueChanged: (String, Int) -> Unit,
    onAddTodoListClicked: () -> Unit,
    onAddTodoItemClicked: () -> Unit
) {
    var isAddToDoListClicked by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = isAddToDoListClicked) {
        if (isAddToDoListClicked) {
            focusManager.clearFocus()
            focusRequester.requestFocus()
        }
    }

    if (!isAddToDoListClicked) {
        IconedText(
            text = stringResource(id = R.string.add_to_do_list),
            onClick = {
                onAddTodoListClicked()
                isAddToDoListClicked = true
            },
            modifier = Modifier
                .padding(start = 15.dp)
                .fillMaxWidth()
                .height(50.dp)
        )
    } else {
        (todoList).forEachIndexed { index, _ ->
            HeightSpacer(5.dp)
            DefaultCheckboxRow(
                text = todoList[index].title,
                checked = checked,
                onValueChanged = { onValueChanged(it, index) },
                onCheckedChange = { },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .height(FIELD_HEIGHT.dp),
                textFieldModifier = Modifier
                    .padding(horizontal = 10.dp)
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }

        IconedText(
            text = stringResource(id = R.string.add_todo_item),
            onClick = { onAddTodoItemClicked() },
            modifier = Modifier
                .padding(start = 30.dp)
                .fillMaxWidth()
                .height(FIELD_HEIGHT.dp)
        )
    }
}