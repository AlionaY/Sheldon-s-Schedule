package com.pti.sheldons_schedule.ui.screens.todo_list_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pti.sheldons_schedule.R
import com.pti.sheldons_schedule.ui.common.DefaultCheckboxColumn
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
    itemsCount: Int,
    text: String = "",
    isAddToDoListClicked: Boolean,
    checked: Boolean = false,
    focusRequester: FocusRequester,
    onValueChanged: (String) -> Unit,
    onClick: () -> Unit
) {
    val contentHeight = (itemsCount * FIELD_HEIGHT).dp

    if (!isAddToDoListClicked) {
        IconedText(
            text = stringResource(id = R.string.add_to_do_list),
            textSize = 15.sp,
            onClick = { onClick() },
            modifier = Modifier
                .padding(start = 15.dp)
                .fillMaxWidth()
                .height(50.dp)
        )
    } else {
        DefaultCheckboxColumn(
            todoItemsCount = 1,
            focusRequester = focusRequester,
            text = text,
            checked = checked,
            onValueChanged = { onValueChanged(it) },
            onCheckedChange = { },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 15.dp)
                .fillMaxWidth()
                .height(contentHeight)
        )
    }
}