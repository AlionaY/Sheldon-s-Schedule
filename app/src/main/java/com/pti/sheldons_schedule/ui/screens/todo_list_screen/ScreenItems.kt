package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.data.ToDo
import com.pti.sheldons_schedule.ui.common.DefaultCheckboxRow
import com.pti.sheldons_schedule.ui.common.HeightSpacer
import com.pti.sheldons_schedule.ui.common.IconedText

private const val FIELD_HEIGHT = 40

@Composable
fun EventTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
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

    if (!isAddToDoListClicked) {
        IconedText(
            text = stringResource(id = R.string.add_to_do_list),
            textSize = 15.sp,
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
                isClicked = isAddToDoListClicked,
                onValueChanged = { onValueChanged(it, index) },
                onCheckedChange = { },
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
                    .height(FIELD_HEIGHT.dp)
            )
        }

        IconedText(
            text = stringResource(id = R.string.add_todo_item),
            textSize = 15.sp,
            onClick = { onAddTodoItemClicked() },
            modifier = Modifier
                .padding(start = 30.dp)
                .fillMaxWidth()
                .height(FIELD_HEIGHT.dp)
        )
    }
}