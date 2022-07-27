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
import com.pti.sheldons_schedule.util.Constants.FIELD_HEIGHT


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
    focusRequester: FocusRequester,
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
            textSize = 15.sp,
            onClick = { onAddTodoItemClicked() },
            modifier = Modifier
                .padding(start = 30.dp)
                .fillMaxWidth()
                .height(FIELD_HEIGHT.dp)
        )
    }
}